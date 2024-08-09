package flare;


import flare.ibkr.IBKRClient;
import io.github.cdimascio.dotenv.Dotenv;

public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dotenv dotenv = Dotenv.load();
        IPersistentStorage persistentStorage = new PersistentStorage(dotenv);

        Analyst analyst = new Analyst();
        GenericBroker broker = new IBKRClient(persistentStorage, analyst);
        Thread thread = new Thread(broker);
        thread.start();

    }

}
