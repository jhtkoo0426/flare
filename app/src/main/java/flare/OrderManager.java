package flare;


import java.io.*;


public class OrderManager {

    private SharedDataStorage storage;

    public OrderManager() {
        storage = SharedDataStorage.getInstance();
    }

    public void initializeOrderId(int lastOrderId) {
        storage.initializeOrderId(lastOrderId);
    }

    public int getNextOrderId() {
        return storage.getNextOrderId();
    }

    public int getCurrentOrderId() {
        return storage.getCurrentOrderId();
    }

    /**
     * Registers order data by printing it to the console and writing it to a CSV log file.
     *
     * @param orderId  the unique identifier for the order
     * @param symbol   the symbol of the security being ordered
     * @param secType  the type of security (e.g., stock, bond)
     * @param quantity the quantity of the security being ordered
     */
    public void registerOrderData(int orderId, String symbol, String secType, double quantity) {
        System.out.printf("Order ID %d registered for order - %s (%s) @ %.2f\n", orderId, symbol, secType, quantity);
        String line = String.format("%d,%s,%s,%f\n", orderId, symbol, secType, quantity);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/flare/logs/order_logs.csv"))) {
            writer.write(line);
        } catch (IOException e) {
            System.err.println("[OrderManager] Error saving last order ID: " + e.getMessage());
        }
    }

}
