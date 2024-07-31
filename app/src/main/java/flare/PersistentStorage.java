package flare;


import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileWriter;
import java.io.IOException;


/**
 * This class provides methods to persist and retrieve the last order ID using environment
 * variables stored in a .env file.
 */
public class PersistentStorage implements IPersistentStorage {
    private static final String ENV_FILE = ".env";
    private Dotenv dotenv;

    /**
     * Constructs a new {@code PersistentStorage} instance with the specified {@code Dotenv} object.
     *
     * @param dotenv the {@code Dotenv} object used to read environment variables.
     */
    public PersistentStorage(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    /**
     * Reads the last order ID from the environment variables.
     *
     * @return the last order ID if it exists; {@code 0} otherwise.
     */
    @Override
    public int readLastOrderId() {
        String lastOrderId = dotenv.get("LAST_ORDER_ID");
        if (lastOrderId != null) {
            return Integer.parseInt(lastOrderId);
        }
        return 0;   // No record found
    }

    /**
     * Writes the last order ID to the environment variables.
     *
     * @param orderId the order ID to be written.
     */
    @Override
    public void writeLastOrderId(int orderId) {
        try (FileWriter writer = new FileWriter(ENV_FILE)) {
            writer.write("LAST_ORDER_ID=" + orderId + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
