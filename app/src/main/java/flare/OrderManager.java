package flare;

public class OrderManager extends KeyManager {

    public OrderManager(String logFilePath) {
        super("ORDER_ID", logFilePath);
    }

    public OrderManager() {
        this("src/main/java/flare/logs/order_logs.csv");
    }

    public void registerOrderData(int orderId, String symbol, String secType, double quantity) {
        registerData(orderId, symbol, secType, String.valueOf(quantity));
    }
}
