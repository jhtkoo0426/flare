package flare;

public interface IPersistentStorage {
    int readLastOrderId();
    void writeLastOrderId(int orderId);
}
