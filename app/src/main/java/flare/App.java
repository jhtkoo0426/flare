package flare;


import flare.ibkr.IBKRConnectionManager;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;


public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dotenv dotenv = Dotenv.load();

        // Prepare required modules for GenericBroker
        IPersistentStorage persistentStorage = new PersistentStorage(dotenv);
        Analyst analyst = new Analyst();

        // Create a single IBKRConnectionManager that will manage multiple clients
        IBKRConnectionManager connectionManager = new IBKRConnectionManager(analyst, persistentStorage);
        connectionManager.initializeClients(8);
        connectionManager.startClients();
        connectionManager.connectClients();

        // Assign connections. TODO: Convert to strategy in the future.
        connectionManager.getIBKRClient(1).subscribeMarketData("NVDA", "STK", "SMART", null, null, null);
        connectionManager.getIBKRClient(2).subscribeMarketData("NVDA", "OPT", "SMART",LocalDate.of(2024, 8, 30), 120.00, "C");
        connectionManager.getIBKRClient(3).subscribeMarketData("NVDA", "OPT", "SMART",LocalDate.of(2024, 8, 30), 121.00, "C");
        connectionManager.getIBKRClient(4).subscribeMarketData("NVDA", "OPT", "SMART",LocalDate.of(2024, 8, 30), 122.00, "C");
    }

}
