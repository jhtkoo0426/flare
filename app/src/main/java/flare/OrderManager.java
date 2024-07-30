package flare;


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

    public void registerOrderData(int orderId, String symbol, String secType, double quantity) {
        System.out.printf("Order ID %d registered for order - %s (%s) @ %.2f", orderId, symbol, secType, quantity);
    }

}
