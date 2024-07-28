package flare;


import flare.ibkr.IBKRClient;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        GenericBroker broker = new IBKRClient();
        Thread thread = new Thread(broker);
        thread.start();
    }

}
