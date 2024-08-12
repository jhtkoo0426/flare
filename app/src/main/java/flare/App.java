package flare;


import flare.ibkr.IBKRConnectionManager;
import io.github.cdimascio.dotenv.Dotenv;


public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dotenv dotenv = Dotenv.load();

        // Prepare required modules for GenericBroker
        IPersistentStorage persistentStorage = new PersistentStorage(dotenv);
        Analyst analyst = new Analyst();

        // Create a single IBKRConnectionManager that will manage multiple clients
        IBKRConnectionManager connectionManager = new IBKRConnectionManager(analyst, persistentStorage);
        connectionManager.initializeClients(4);
        connectionManager.startClients();
        connectionManager.connectClients();

        // Assign connections. TODO: Convert to strategy in the future.
        connectionManager.getIBKRClient(1).subscribeEquityData("CRWD");
    }

}
