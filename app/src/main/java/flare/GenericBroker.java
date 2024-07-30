package flare;


import com.ib.client.Order;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Superclass for wrapping various broker APIs. All broker interactions should be done via
 * this class.
 */
public abstract class GenericBroker implements Runnable {

    public OrderManager orderManager;

    public GenericBroker() {
        this.orderManager = new OrderManager();
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

}
