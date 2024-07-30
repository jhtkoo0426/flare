package flare.ibkr;


import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
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
        boolean testOrder = false;

        while (true) {
            if (!connectionManager.getBrokerClient().isConnected()) {
                initialized = false;
                connectionManager.connectToBroker();
                sleep(5000);
            } else {
                if (!initialized) {
                    connectionManager.setupTWSReader();
                    initialized = true;
                    sleep(1000);
                } else {
                    if (!testOrder) {
                        makeOrder("NVDA", "STK", "MKT", 113.00, 1);
                    }
                    testOrder = true;
                }
            }
        }
    }

    @Override
    public void makeOrder(String symbol, String secType, String orderType, double price, double quantity) {
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType(secType);
        contract.exchange("SMART");
        contract.currency("USD");

        String action = (quantity > 0) ? "BUY" : "SELL";
        Order order = new Order();
        order.action(action);
        order.orderType(orderType);
        order.totalQuantity(Decimal.get(quantity));
        order.lmtPrice(price);

        int orderId = orderManager.registerOrderData(symbol, secType, quantity);
        connectionManager.getBrokerClient().placeOrder(orderId, contract, order);
    }

    @Override
    public void onOrderPlaced(int orderId, double executionPrice, double quantity) {
        System.out.printf("Order ID %d placed: Execution price @ %.2f and quantity %.2f.\n", orderId, executionPrice, quantity);
    }

    @Override
    public void onOrderFilled(int orderId, double avgFillPrice, double quantityFilled) {
        System.out.printf("Order ID %d filled with average price @ %.2f and quantity %.2f.\n", orderId, avgFillPrice, quantityFilled);
    }

    @Override
    public void onOrderCancelled(int orderId) {
        System.out.printf("Order ID %d cancelled.\n", orderId);
    }
}