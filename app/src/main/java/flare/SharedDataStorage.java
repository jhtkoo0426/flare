package flare;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton class that provides a thread-safe mechanism for managing various unique IDs.
 * This class ensures that each ID is unique and incremented atomically across the application.
 */
public class SharedDataStorage {
    private static SharedDataStorage instance;

    // Map to store locks and IDs for different keys
    private ConcurrentHashMap<String, Lock> locks;
    private ConcurrentHashMap<String, Integer> ids;

    /**
     * Private constructor to prevent instantiation from other classes.
     * Initializes the locks and IDs maps.
     */
    private SharedDataStorage() {
        locks = new ConcurrentHashMap<>();
        ids = new ConcurrentHashMap<>();
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
     * Initializes a specific ID to the provided value. This should be called
     * on application start to ensure the ID continues from where it left off.
     *
     * @param key the identifier for the ID (e.g., "orderId", "requestId")
     * @param initialValue the initial value for this ID
     */
    public void initializeId(String key, int initialValue) {
        locks.putIfAbsent(key, new ReentrantLock());
        ids.put(key, initialValue);
    }

    /**
     * Returns the next unique ID for the specified key. This method uses a lock
     * to ensure that the ID is incremented atomically, preventing duplicates.
     *
     * @param key the identifier for the ID (e.g., "orderId", "requestId")
     * @return the next unique ID
     */
    public int getNextId(String key) {
        Lock lock = locks.get(key);
        if (lock == null) {
            throw new IllegalArgumentException("ID not initialized for key: " + key);
        }

        lock.lock();
        try {
            if (!ids.containsKey(key)) {
                throw new IllegalStateException(key + " has not been initialized.");
            }
            return ids.merge(key, 1, Integer::sum);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current ID for the specified key without incrementing it.
     *
     * @param key the identifier for the ID (e.g., "orderId", "requestId")
     * @return the current ID
     */
    public int getCurrentId(String key) {
        Lock lock = locks.get(key);
        if (lock == null) {
            throw new IllegalArgumentException("ID not initialized for key: " + key);
        }

        lock.lock();
        try {
            return ids.getOrDefault(key, -1);
        } finally {
            lock.unlock();
        }
    }
}
