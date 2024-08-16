package flare.ibkr;


import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import flare.*;

import java.time.LocalDate;


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
    public RequestStruct getRequestData(int id) {
        return requestManager.getRequestData(id);
    }

    private Contract createContract(String symbol, String secType, String exchange, String currency) {
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType(secType);
        contract.exchange(exchange);
        contract.currency(currency);
        return contract;
    }

    private Contract createOptionContract(String symbol, LocalDate lastTradeDate, double strike, String right) {
        Contract contract = createContract(symbol, "OPT", "SMART", "USD");
        contract.lastTradeDateOrContractMonth(OCCFormatter.formatDate(lastTradeDate, "yyyyMMdd"));
        contract.strike(strike);
        contract.right(right);
        contract.multiplier("100");
        return contract;
    }

    @Override
    public void makeOrder(String symbol, String secType, String orderType, double price, double quantity) {
        Contract contract = createContract(symbol, secType, "SMART", "USD");

        Order order = new Order();
        order.action(quantity > 0 ? "BUY" : "SELL");
        order.orderType(orderType);
        order.totalQuantity(Decimal.get(quantity));
        order.lmtPrice(price);

        int orderId = orderManager.getNextId();
        orderManager.registerOrderData(orderId, symbol, secType, quantity);
        brokerClient.placeOrder(orderId, contract, order);

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
    public void subscribeMarketData(String symbol, String secType, LocalDate expiryDate, Double strike, String right) {
        int requestId = requestManager.getNextId();
        RequestStruct requestData = new RequestStruct(symbol, secType, strike, right, expiryDate, null);
        requestManager.registerRequestData(requestId, requestData);

        Contract contract;
        if (secType.equals("OPT")) {
            contract = createOptionContract(symbol, expiryDate, strike, right);
            brokerClient.reqRealTimeBars(requestId, contract, 5, "MIDPOINT", true, null);
            sleep(1000);

            // Request market data for the option contract, e.g., greeks, implied volatility, option price.
            brokerClient.reqMktData(requestId, contract, "", false, false, null);
        } else if (secType.equals("STK")) {
            contract = createContract(symbol, secType, "SMART", "USD");
            brokerClient.reqRealTimeBars(requestId, contract, 5, "MIDPOINT", true, null);
        } else if (secType.equals("BOND")) {
            contract = createContract(symbol, secType, "SMART", "USD");
            brokerClient.reqMktData(requestId, contract, "", false, false, null);
        }
    }

}