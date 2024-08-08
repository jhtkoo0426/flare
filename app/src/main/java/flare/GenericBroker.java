package flare;


/**
 * Superclass for wrapping various broker APIs. All broker interactions should be done via
 * this class.
 */
public abstract class GenericBroker implements Runnable {

    public GenericBroker() {

    }

    // Abstract methods
    public abstract void run();

    public abstract void makeOrder(String symbol, String secType, String orderType, double price, double quantity);
    public abstract void onOrderPlaced(int orderId, double executionPrice, double quantity);
    public abstract void onOrderFilled(int orderId, double avgFillPrice, double quantityFilled);
    public abstract void onOrderCancelled(int orderId);

    public abstract void subscribeMarketData(String symbol, String secType);
    public abstract void connectDataStream(String name);

    // Re-usable methods
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
