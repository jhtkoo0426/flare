package flare;


/**
 * Manages the registration and tracking of order data.
 * This class extends {@link KeyManager} and is responsible for storing order-related information,
 * including order ID, symbol, security type, and quantity. The order data is typically logged
 * to a CSV file specified at initialization.
 */
public class OrderManager extends KeyManager {

    /**
     * Constructs an OrderManager with a specified log file path.
     *
     * @param logFilePath the path to the log file where order data will be recorded
     */
    public OrderManager(String logFilePath) {
        super("ORDER_ID", logFilePath);
    }

    /**
     * Constructs an OrderManager with a default log file path.
     * The default path is "src/main/java/flare/logs/order_logs.csv".
     */
    public OrderManager() {
        this("src/main/java/flare/logs/order_logs.csv");
    }

    /**
     * Registers order data, including the order ID, symbol, security type, and quantity.
     * This method records the order details in the log file specified during initialization.
     *
     * @param orderId  the unique identifier for the order
     * @param symbol   the trading symbol of the security
     * @param secType  the type of security (e.g., stock, option, etc.)
     * @param quantity the quantity of the order
     */
    public void registerOrderData(int orderId, String symbol, String secType, double quantity) {
        RequestStruct requestData = new RequestStruct(symbol, secType, null, null, null, quantity);
        registerData(orderId, requestData);
    }
}