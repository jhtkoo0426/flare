package flare;


import flare.ibkr.IBKRClient;
import io.github.cdimascio.dotenv.Dotenv;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dotenv dotenv = Dotenv.load();
        IPersistentStorage persistentStorage = new PersistentStorage(dotenv);

        GenericBroker broker = new IBKRClient(persistentStorage);
        Thread thread = new Thread(broker);
        thread.start();
    }

}
