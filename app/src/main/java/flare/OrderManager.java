package flare;


public class OrderManager extends KeyManager {

    public OrderManager() {
        super("ORDER_ID", "src/main/java/flare/logs/order_logs.csv");
    }

    public void registerOrderData(int orderId, String symbol, String secType, double quantity) {
        registerData(orderId, symbol, secType, String.valueOf(quantity));
    }

}
