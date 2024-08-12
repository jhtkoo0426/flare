package flare.ibkr;


import com.ib.client.*;
import flare.Analyst;
import flare.IPersistentStorage;

import java.util.ArrayList;
import java.util.List;


/**
 * Establishes and manages all concurrent connections to the Interactive Broker (IBKR) Trader Workstation (TWS)
 */
public class IBKRConnectionManager {

    private final List<EClientSocket> brokerClients;
    private final List<EReaderSignal> readerSignals;
    private final List<IBKRClient> clients;
    private final Analyst analyst;
    private final IPersistentStorage persistentStorage;

    public IBKRConnectionManager(Analyst analyst, IPersistentStorage persistentStorage) {
        this.brokerClients = new ArrayList<>();
        this.readerSignals = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.analyst = analyst;
        this.persistentStorage = persistentStorage;
    }

    public void initializeClients(int numberOfClients) {
        for (int i = 0; i < numberOfClients; i++) {
            EJavaSignal signal = new EJavaSignal();
            readerSignals.add(signal);

            IBKRWrapper wrapper = new IBKRWrapper(analyst, i);
            EClientSocket socket = new EClientSocket(wrapper, signal);
            brokerClients.add(socket);

            IBKRClient client = new IBKRClient(persistentStorage, analyst, socket, i);
            clients.add(client);
        }
    }

    public void startClients() {
        for (IBKRClient client : clients) {
            new Thread(client).start();
        }
    }

    /**
     * Connects to a running instance of IBKR TWS.
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

    public boolean isConnected(EClientSocket brokerClient) {
        return brokerClient.isConnected();
    }

    public IBKRClient getIBKRClient(int id) {
        return clients.get(id);
    }

    public List<EClientSocket> getBrokerClients() {
        return brokerClients;
    }

    public List<EReaderSignal> getReaderSignals() {
        return readerSignals;
    }
}
