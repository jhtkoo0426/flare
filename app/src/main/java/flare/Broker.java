package flare;


public class Broker implements OrderListener {

    @Override
    public void onOrderMade(OrderEvent event) {
        double price = event.getOrderPrice();
        System.out.printf("Order made at price %.2f", price);
    }

}