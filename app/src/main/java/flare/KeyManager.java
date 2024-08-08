package flare;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class KeyManager {
    protected final SharedDataStorage storage;
    protected final String idKey;
    protected final String logFilePath;

    public KeyManager(String idKey, String logFilePath) {
        this.storage = SharedDataStorage.getInstance();
        this.idKey = idKey;
        this.logFilePath = logFilePath;
    }

    public void initializeId(int lastIdValue) {
        storage.initializeId(idKey, lastIdValue);
    }

    public int getNextId() {
        return storage.getNextId(idKey);
    }

    public int getCurrentId() {
        return storage.getCurrentId(idKey);
    }

    public void registerData(int id, String... dataFields) {
        System.out.printf("ID %d registered with data: %s\n", id, String.join(", ", dataFields));

        if (logFilePath != null && !logFilePath.isEmpty()) {
            String line = id + "," + String.join(",", dataFields) + "\n";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                writer.write(line);
            } catch (IOException e) {
                System.err.println("[IdManager] Error saving data: " + e.getMessage());
            }
        }
    }
}
