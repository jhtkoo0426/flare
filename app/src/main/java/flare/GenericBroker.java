package flare;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Superclass for wrapping various broker APIs. All broker interactions should be done via
 * this class.
 */
public abstract class GenericBroker implements Runnable {

    // Order management variables
    private AtomicInteger orderId;
    private final Map<Integer, String> orderData = new HashMap<>();
    private static final String ORDER_FILE = "src/main/java/flare/logs/order_logs.csv";
    private static final String LAST_ORDER_ID_FILE = "src/main/java/flare/logs/last_order_id.txt";

    public GenericBroker() {
        this.orderId = new AtomicInteger(loadLastOrderId());
    }

    // Abstract methods
    public abstract void run();

    public abstract void makeOrder(String symbol, String secType, String orderType, double price, double quantity);
    public abstract void onOrderPlaced(int orderId, double executionPrice, double quantity);
    public abstract void onOrderFilled(int orderId, double avgFillPrice, double quantityFilled);
    public abstract void onOrderCancelled(int orderId);

    // Re-usable methods
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Order management methods
    public synchronized int registerOrderData(String symbol, String secType, double price) {
        long systemTime = System.currentTimeMillis();
        int id = orderId.incrementAndGet(); // Increment and get the next order ID
        String order = String.format("%d,%s,%s,%d,%.2f", id, symbol, secType, systemTime, price);
        if (!orderData.containsKey(id)) {
            orderData.put(id, order);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE, true))) {
                writer.write(order + "\n");
            } catch (IOException e) {
                System.err.println("[OrderManager] Error saving order data: " + e.getMessage());
            }
            saveLastOrderId(id); // Save the last used order ID
        }
        return id;
    }

    private int loadLastOrderId() {
        int lastOrderId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(LAST_ORDER_ID_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                lastOrderId = Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("[OrderManager] Error loading last order ID: " + e.getMessage());
        }
        return lastOrderId;
    }

    private void saveLastOrderId(int lastOrderId) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_ORDER_ID_FILE))) {
            writer.write(String.valueOf(lastOrderId));
        } catch (IOException e) {
            System.err.println("[OrderManager] Error saving last order ID: " + e.getMessage());
        }
    }
}
