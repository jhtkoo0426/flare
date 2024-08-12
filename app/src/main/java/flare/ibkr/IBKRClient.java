package flare.ibkr;


import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import flare.*;


/**
 * Broker implementation for the Interactive Brokers (IBKR) Trader WorkStation (TWS).
 */
public class IBKRClient extends GenericBroker {

    private final EClientSocket brokerClient;
    private final OrderManager orderManager;
    private final RequestManager requestManager;
    private final IPersistentStorage persistentStorage;
    private final Analyst analyst;
    private final int clientID;

    public IBKRClient(IPersistentStorage persistentStorage, Analyst analyst, EClientSocket brokerClient, int instanceId) {
        this.analyst = analyst;
        this.persistentStorage = persistentStorage;
        this.brokerClient = brokerClient;
        this.clientID = instanceId;
        orderManager = new OrderManager();
        requestManager = new RequestManager();

        // Initialize the orderId from persistent storage
        int lastOrderId = persistentStorage.readLastOrderId();
        orderManager.initializeId(lastOrderId);
        requestManager.initializeId(0);
    }

    @Override
    public void run() { }

    @Override
    public String getRequestData(int id) {
        return requestManager.getRequestData(id);
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

        int orderId = orderManager.getNextId();
        orderManager.registerOrderData(orderId, symbol, secType, quantity);
        brokerClient.placeOrder(orderId, contract, order);

        // Update the last orderId in persistent storage
        persistentStorage.writeLastOrderId(orderManager.getCurrentId());
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

    @Override
    public void subscribeEquityData(String symbol) {
        int requestId = requestManager.getNextId();
        String requestData = "DATASUB_" + symbol;
        requestManager.registerRequestData(requestId, requestData);
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType("STK");
        contract.currency("USD");
        contract.exchange("SMART");
        brokerClient.reqRealTimeBars(requestId, contract, 5, "MIDPOINT", true, null);
    }
}