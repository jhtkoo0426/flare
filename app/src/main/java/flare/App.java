package flare;


public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Broker broker = new Broker();
        OrderProcessor processor = new OrderProcessor();
        processor.addOrderListener(broker);
        processor.makeOrder(100.00);
    }

}
