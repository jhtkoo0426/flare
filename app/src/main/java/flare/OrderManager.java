package flare;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class OrderManager {

    private AtomicInteger orderId;
    private final Map<Integer, String> orderData = new HashMap<>();
    private static final String ORDER_FILE = "src/main/java/flare/logs/order_logs.csv";
    private static final String LAST_ORDER_ID_FILE = "src/main/java/flare/logs/last_order_id.txt";

    public OrderManager() {
        orderId = new AtomicInteger(loadLastOrderId());
    }

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

    // Retrieve the last recorded order ID from the current session or previous sessions.
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
