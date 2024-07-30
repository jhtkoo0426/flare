package flare;


import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileWriter;
import java.io.IOException;


public class PersistentStorage {
    private static final String ENV_FILE = ".env";
    private static Dotenv dotenv = Dotenv.load();

    /**
     * Reads the last used orderId from the .env file.
     *
     * @return the last used orderId or 0 if no record is found
     */
    public static int readLastOrderId() {
        String lastOrderId = dotenv.get("LAST_ORDER_ID");
        if (lastOrderId != null) {
            return Integer.parseInt(lastOrderId);
        }
        return 0; // Default if no record is found
    }

    /**
     * Writes the current orderId to the .env file.
     *
     * @param orderId the current orderId to be recorded
     */
    public static void writeLastOrderId(int orderId) {
        try (FileWriter writer = new FileWriter(ENV_FILE)) {
            writer.write("LAST_ORDER_ID=" + orderId + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
