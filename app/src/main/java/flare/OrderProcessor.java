package flare;

import java.util.ArrayList;
import java.util.List;


public class OrderProcessor {

    private final List<OrderListener> orderListeners = new ArrayList<>();

    public void addOrderListener(OrderListener listener) {
        orderListeners.add(listener);
    }

    public void removeOrderListener(OrderListener listener) {
        orderListeners.remove(listener);
    }

    public List<OrderListener> getListeners() {
        return orderListeners;
    }

    public void makeOrder(double price) {
        System.out.println("Making order...");
        OrderEvent event = new OrderEvent();
        event.setOrderPrice(price);
        notifyListeners(event);
    }

    private void notifyListeners(OrderEvent event) {
        for (OrderListener listener : orderListeners) {
            listener.onOrderMade(event);
        }
    }
}
