package flare;


import flare.ibkr.IBKRConnectionManager;
import flare.models.BaseModel;
import flare.models.BlackScholes;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;


public class App {

    private static BaseModel model;

    private static IBKRConnectionManager getIbkrConnectionManager(Dotenv dotenv) {
        // TODO: Scrape market data such as T-bill rates (for risk-free interest rate)
        BaseModel model = new BlackScholes(0.0);

        // Prepare required modules for GenericBroker
        IPersistentStorage persistentStorage = new PersistentStorage(dotenv);
        Analyst analyst = new Analyst(model);

        // Create a single IBKRConnectionManager that will manage multiple clients
        IBKRConnectionManager connectionManager = new IBKRConnectionManager(analyst, persistentStorage);
        connectionManager.initializeClients(4);
        connectionManager.startClients();
        return connectionManager;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dotenv dotenv = Dotenv.load();

        IBKRConnectionManager connectionManager = getIbkrConnectionManager(dotenv);
        connectionManager.connectClients();

        // Assign connections. TODO: Convert to strategy in the future.
        connectionManager.getIBKRClient(0).subscribeMarketData("10Y", "BOND", null, null, null);
        connectionManager.getIBKRClient(1).subscribeMarketData("CRWD", "STK", null, null, null);
        connectionManager.getIBKRClient(2).subscribeMarketData("NVDA", "STK", null, null, null);
        connectionManager.getIBKRClient(2).subscribeMarketData("NVDA", "OPT", LocalDate.of(2024, 8, 30), 120.00, "C");
    }

}
