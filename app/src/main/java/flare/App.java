package flare;


import flare.ibkr.IBKRConnectionManager;
import flare.models.BaseModel;
import flare.models.BlackScholes;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDate;


public class App {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Dotenv dotenv = Dotenv.load();

        BaseModel model = new BlackScholes(0.03);

        // Prepare required modules for GenericBroker
        IPersistentStorage persistentStorage = new PersistentStorage(dotenv);
        Analyst analyst = new Analyst();
        analyst.loadModel(model);

        // Create a single IBKRConnectionManager that will manage multiple clients
        IBKRConnectionManager connectionManager = new IBKRConnectionManager(analyst, persistentStorage);
        connectionManager.initializeClients(4);
        connectionManager.startClients();
        connectionManager.connectClients();

        // Assign connections. TODO: Convert to strategy in the future.
        connectionManager.getIBKRClient(1).subscribeEquityData("CRWD");
        connectionManager.getIBKRClient(2).subscribeEquityData("NVDA");
        connectionManager.getIBKRClient(2).subscribeOptionData("NVDA", LocalDate.of(2024, 8, 30), 120, "C");
    }

}
