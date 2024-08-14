package flare;


import java.time.LocalDate;


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

    // Data subscription methods
    // public abstract void subscribeEquityData(String symbol);
    // public abstract void subscribeOptionData(String symbol, LocalDate lastTradeDate, double strike, String right);
    public abstract void subscribeMarketData(String symbol, String secType, String exchange, LocalDate lastTradeDate, Double strike, String right);

    public abstract String getRequestData(int id);

    // Re-usable methods
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
