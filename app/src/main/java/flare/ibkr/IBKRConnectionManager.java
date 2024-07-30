package flare.ibkr;


import com.ib.client.*;


/**
 * Establishes and manages all concurrent connections to the Interactive Broker (IBKR) Trader Workstation (TWS)
 */
public class IBKRConnectionManager {

    private final EClientSocket brokerClient;
    private final EReaderSignal readerSignal;

    public IBKRConnectionManager(EWrapper wrapper) {
        this.readerSignal = new EJavaSignal();
        this.brokerClient = new EClientSocket(wrapper, readerSignal);
    }

    public EClientSocket getBrokerClient() {
        return brokerClient;
    }

    /**
     * Connects to a running instance of IBKR TWS.
     */
    public void connectToBroker() {
        System.out.println("IBKRClient: Attempting to connect to a running TWS instance.");
        brokerClient.eConnect("127.0.0.1", 7497, 2);
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
    public void setupTWSReader() {
        final EReader reader = new EReader(brokerClient, readerSignal);
        reader.start();
        new Thread(() -> {
            while (brokerClient.isConnected()) {
                readerSignal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        }).start();
    }
}
