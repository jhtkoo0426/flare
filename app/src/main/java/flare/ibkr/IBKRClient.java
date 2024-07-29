package flare.ibkr;

import flare.GenericBroker;


/**
 * Broker implementation for the Interactive Brokers (IBKR) Trader WorkStation (TWS).
 */
public class IBKRClient extends GenericBroker {

    private final IBKRConnectionManager connectionManager;

    public IBKRClient() {
        connectionManager = new IBKRConnectionManager(new IBKRWrapper(this));
    }

    @Override
    public void run() {
        boolean initialized = false;

        while (true) {
            if (!connectionManager.getBrokerClient().isConnected()) {   // No TWS connection
                initialized = false;
                connectionManager.connectToBroker();
                sleep(10000);
            } else {
                if (!initialized) {     // TWS connected but FLARE is not connected
                    connectionManager.setupTWSReader();
                    initialized = true;
                }
                sleep(5000);
            }
        }
    }

    @Override
    public void onOrderMade() {

    }

    @Override
    public void onOrderExecuted(int orderId, double executionPrice, long quantity) {
        System.out.println("Order Executed: Order ID " + orderId + ", Execution Price: " + executionPrice + ", Quantity: " + quantity);
    }

    @Override
    public void onOrderCancelled(int orderId) {
        System.out.println("Order Cancelled: Order ID " + orderId);
    }

}
