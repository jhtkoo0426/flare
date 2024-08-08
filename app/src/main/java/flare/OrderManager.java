package flare;

import java.io.*;


public class OrderManager {

    private final SharedDataStorage storage;
    private final String ID_KEY = "ORDER_ID";

    public OrderManager() {
        storage = SharedDataStorage.getInstance();
    }

    /**
     * Initializes a specific ID to the provided value.
     * This should be called on application start to ensure the ID continues from where it left off.
     *
     * @param lastIdValue the last used ID value from previous sessions
     */
    public void initializeId(int lastIdValue) {
        storage.initializeId(ID_KEY, lastIdValue);
    }

    /**
     * Retrieves the next unique ID for the specified key.
     *
     * @return the next unique ID
     */
    public int getNextId() {
        return storage.getNextId(ID_KEY);
    }

    /**
     * Retrieves the current ID for the specified key without incrementing it.
     *
     * @return the current ID
     */
    public int getCurrentId() {
        return storage.getCurrentId(ID_KEY);
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/flare/logs/order_logs.csv", true))) {
            writer.write(line);
        } catch (IOException e) {
            System.err.println("[OrderManager] Error saving last order ID: " + e.getMessage());
        }
    }
}
