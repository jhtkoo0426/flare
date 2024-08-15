package flare;


/**
 * Manages the registration and tracking of request data.
 * This class extends {@link KeyManager} and is responsible for storing request-related information,
 * including request ID and the associated request data.
 */
public class RequestManager extends KeyManager {

    /**
     * Constructs a RequestManager with a default configuration.
     * The request data is not logged to a file, as the log file path is set to null.
     */
    public RequestManager() {
        super("REQUEST_ID", null);
    }

    /**
     * Registers request data, including the request ID and the associated data.
     * This method stores the request details in memory, as no log file is specified.
     *
     * @param requestId   the unique identifier for the request
     * @param requestData the data associated with the request
     */
    public void registerRequestData(int requestId, RequestStruct requestData) {
        registerData(requestId, requestData);
    }
}