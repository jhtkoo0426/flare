package flare;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileWriter;
import java.io.IOException;


public class PersistentStorage implements IPersistentStorage {
    private static final String ENV_FILE = ".env";
    private Dotenv dotenv;

    public PersistentStorage(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @Override
    public int readLastOrderId() {
        String lastOrderId = dotenv.get("LAST_ORDER_ID");
        if (lastOrderId != null) {
            return Integer.parseInt(lastOrderId);
        }
        return 0;   // No record found
    }

    @Override
    public void writeLastOrderId(int orderId) {
        try (FileWriter writer = new FileWriter(ENV_FILE)) {
            writer.write("LAST_ORDER_ID=" + orderId + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
