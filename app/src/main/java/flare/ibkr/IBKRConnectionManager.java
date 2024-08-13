package flare.ibkr;


import com.ib.client.*;
import flare.Analyst;
import flare.IPersistentStorage;

import java.util.ArrayList;
import java.util.List;


/**
 * Manages multiple concurrent connections to the Interactive Brokers (IBKR) Trader Workstation (TWS).
 * This class handles the initialization, connection, and management of clients that interface with the
 * IBKR TWS API, ensuring smooth operation of trading activities across multiple clients.
 */
public class IBKRConnectionManager {

    private final List<EClientSocket> brokerClients;
    private final List<EReaderSignal> readerSignals;
    private final List<IBKRClient> clients;
    private final Analyst analyst;
    private final IPersistentStorage persistentStorage;

    /**
     * Constructs an IBKRConnectionManager with the specified Analyst and persistent storage.
     *
     * @param analyst           the Analyst instance used for handling trading logic and analysis
     * @param persistentStorage the persistent storage interface for saving and retrieving data
     */
    public IBKRConnectionManager(Analyst analyst, IPersistentStorage persistentStorage) {
        this.brokerClients = new ArrayList<>();
        this.readerSignals = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.analyst = analyst;
        this.persistentStorage = persistentStorage;
    }

    /**
     * Initializes the specified number of IBKR clients.
     * This method sets up the necessary EClientSocket and EReaderSignal for each client and associates
     * them with a corresponding IBKRWrapper for handling IBKR API interactions.
     *
     * @param numberOfClients the number of IBKR clients to initialize
     */
    public void initializeClients(int numberOfClients) {
        for (int i = 0; i < numberOfClients; i++) {
            EJavaSignal signal = new EJavaSignal();
            readerSignals.add(signal);

            IBKRWrapper wrapper = new IBKRWrapper(analyst, i);
            EClientSocket socket = new EClientSocket(wrapper, signal);
            brokerClients.add(socket);

            IBKRClient client = new IBKRClient(persistentStorage, analyst, socket, i);
            clients.add(client);

            // Ensure that the IBKRWrapper has the correct reference to the broker
            wrapper.setBroker(client);
        }
    }

    /**
     * Starts all initialized IBKR clients in separate threads.
     * This method ensures that each client operates concurrently, allowing multiple simultaneous
     * connections to the IBKR TWS.
     */
    public void startClients() {
        for (IBKRClient client : clients) {
            new Thread(client).start();
        }
    }

    /**
     * Connects each initialized client to the IBKR TWS.
     * This method establishes a connection to the TWS instance running on the local machine and sets
     * up the necessary readers to handle incoming messages from TWS.
     */
    public void connectClients() {
        for (int i = 0; i < brokerClients.size(); i++) {
            EClientSocket brokerClient = brokerClients.get(i);
            brokerClient.eConnect("127.0.0.1", 7497, i);
            setupTWSReader(brokerClient, i);
        }
    }

    /**
     * Sets up the Interactive Brokers (IBKR) Trader Workstation (TWS) reader for handling incoming messages.
     * This method initializes an EReader instance and starts it in a separate thread to process incoming messages
     * from the IBKR TWS API. The reader continuously waits for signals and processes messages received from TWS.
     * This method is typically called during the initialization phase of a TWS client connection to ensure
     * proper handling of incoming messages.
     *
     * <p><strong>Note:</strong> This method is intended for internal use and should not be called directly by
     * client code.</p>
     *
     * @param brokerClient the EClientSocket instance representing the client connection
     * @param id           the unique identifier of the client
     */
    private void setupTWSReader(EClientSocket brokerClient, int id) {
        EReaderSignal signal = readerSignals.get(id);
        EReader reader = new EReader(brokerClient, signal);
        reader.start();

        new Thread(() -> {
            while (brokerClient.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Checks whether the specified client is connected to the IBKR TWS.
     *
     * @param brokerClient the EClientSocket instance representing the client connection
     * @return true if the client is connected; false otherwise
     */
    public boolean isConnected(EClientSocket brokerClient) {
        return brokerClient.isConnected();
    }

    /**
     * Retrieves the IBKRClient instance associated with the specified client ID.
     *
     * @param id the unique identifier of the client
     * @return the IBKRClient instance corresponding to the given ID
     */
    public IBKRClient getIBKRClient(int id) {
        return clients.get(id);
    }

    /**
     * Retrieves the list of EClientSocket instances representing the broker clients.
     *
     * @return the list of EClientSocket instances
     */
    public List<EClientSocket> getBrokerClients() {
        return brokerClients;
    }

    /**
     * Retrieves the list of EReaderSignal instances used for client connections.
     *
     * @return the list of EReaderSignal instances
     */
    public List<EReaderSignal> getReaderSignals() {
        return readerSignals;
    }
}
