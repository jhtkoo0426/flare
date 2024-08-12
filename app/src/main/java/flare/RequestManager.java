package flare;


public class RequestManager extends KeyManager {

    public RequestManager() {
        super("REQUEST_ID", null);
    }

    public void registerRequestData(int requestId, String requestData) {
        registerData(requestId, requestData);
    }
}