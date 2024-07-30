package flare;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Singleton class that provides a thread-safe mechanism for managing the orderId variable.
 * This class ensures that the orderId is unique and incremented atomically across the application.
 */
public class SharedDataStorage {
    private static SharedDataStorage instance;
    private Lock orderIdLock;
    private int orderId;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the orderId lock.
     */
    private SharedDataStorage() {
        orderIdLock = new ReentrantLock();
        orderId = -1; // Indicates that orderId should be initialized
    }

    /**
     * Returns the single instance of the SharedDataStorage class.
     * This method ensures that only one instance of the class exists.
     *
     * @return the single instance of SharedDataStorage
     */
    public static synchronized SharedDataStorage getInstance() {
        if (instance == null) {
            instance = new SharedDataStorage();
        }
        return instance;
    }

    /**
     * Returns the next unique orderId. This method uses a lock to ensure that
     * the orderId is incremented atomically, preventing duplicate order IDs.
     *
     * @return the next unique orderId
     */
    public int getNextOrderId() {
        System.out.println("SDS.getNextOrderId called. Now locking orderId...");
        orderIdLock.lock();
        try {
            if (orderId == -1) {
                throw new IllegalStateException("OrderId has not been initialized.");
            }
            System.out.println("SDS.getNextOrderId - orderId has been incremented.");
            return ++orderId;
        } finally {
            orderIdLock.unlock();
            System.out.println("SDS.getNextOrderId ended. Now unlocking orderId...");
        }
    }

    /**
     * Returns the current orderId without incrementing it.
     *
     * @return the current orderId
     */
    public int getCurrentOrderId() {
        orderIdLock.lock();
        try {
            return orderId;
        } finally {
            orderIdLock.unlock();
        }
    }

    /**
     * Initializes the orderId to the last used orderId + 1. This should be called
     * on application start to ensure the orderId continues from where it left off.
     *
     * @param lastOrderId the last used orderId from previous sessions
     */
    public void initializeOrderId(int lastOrderId) {
        orderIdLock.lock();
        try {
            this.orderId = lastOrderId;
        } finally {
            orderIdLock.unlock();
        }
    }
}
