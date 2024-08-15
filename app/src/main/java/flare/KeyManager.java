package flare;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Manages the registration and tracking of data associated with unique IDs.
 * The {@code KeyManager} class handles the generation of unique IDs, storing of data,
 * and optional logging of data to a specified file. It serves as a base class for
 * other managers that need to track data keyed by unique IDs.
 */
public class KeyManager {
    protected final SharedDataStorage storage;
    protected final String idKey;
    protected final String logFilePath;
    protected final Map<Integer, RequestStruct> requestMap;

    /**
     * Constructs a KeyManager with a specified ID key and log file path.
     *
     * @param idKey       the key used to generate and manage unique IDs
     * @param logFilePath the path to the log file where data will be recorded, or null if logging is not required
     */
    public KeyManager(String idKey, String logFilePath) {
        this.storage = SharedDataStorage.getInstance();
        this.idKey = idKey;
        this.logFilePath = logFilePath;
        this.requestMap = new HashMap<>();
    }

    /**
     * Initializes the ID generation with the specified last ID value.
     * This method sets the starting point for ID generation, allowing continuation from a specific ID.
     *
     * @param lastIdValue the last used ID value from which the next ID will be generated
     */
    public void initializeId(int lastIdValue) {
        storage.initializeId(idKey, lastIdValue);
    }

    /**
     * Retrieves the next available unique ID.
     * This method increments the internal ID counter and returns the new ID.
     *
     * @return the next unique ID
     */
    public int getNextId() {
        return storage.getNextId(idKey);
    }

    /**
     * Retrieves the current ID without incrementing it.
     * This method is useful for checking the last generated ID.
     *
     * @return the current ID
     */
    public int getCurrentId() {
        return storage.getCurrentId(idKey);
    }

    /**
     * Registers data associated with a specific ID.
     * The data is stored in the internal map and optionally logged to a file if a log file path is specified.
     *
     * @param id         the unique identifier for the data
     * @param requestData the data fields to be associated with the ID
     */
    public void registerData(int id, RequestStruct requestData) {
        requestMap.put(id, requestData);
    }

    /**
     * Retrieves the data associated with a specific ID.
     * If no data is found for the given ID, an empty string is returned.
     *
     * @param id the unique identifier for the data
     * @return the data associated with the ID, or an empty string if no data is found
     */
    public RequestStruct getRequestData(int id) {
        return requestMap.getOrDefault(id, null);
    }
}
