package flare;


public class RequestManager {

    private final SharedDataStorage storage;
    private final String ID_KEY = "REQUEST_ID";

    public RequestManager() {
        storage = SharedDataStorage.getInstance();
    }

    public void initializeId(int lastIdValue) {
        storage.initializeId(ID_KEY, lastIdValue);
    }

    public int getNextId() {
        return storage.getNextId(ID_KEY);
    }

    public int getCurrentId() {
        return storage.getCurrentId(ID_KEY);
    }

    public void registerRequestData(int requestId, String symbol, String requestName) {
        System.out.printf("Request ID %d registered for symbol %s. Request name = %s", requestId, symbol, requestName);
    }
}
