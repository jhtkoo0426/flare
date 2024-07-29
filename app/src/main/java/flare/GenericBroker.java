package flare;


/**
 * Superclass for wrapping various broker APIs. All broker interactions should be done via
 * this class.
 */
public abstract class GenericBroker implements Runnable {

    public abstract void onOrderMade();
    public abstract void run();

    public abstract void onOrderExecuted(int orderId, double executionPrice, long quantity);
    public abstract void onOrderCancelled(int orderId);

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
