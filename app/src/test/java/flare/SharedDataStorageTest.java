package flare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SharedDataStorageTest {

    @BeforeEach
    void resetSingleton() {
        // Resetting the singleton instance before each test
        SharedDataStorage.getInstance();
    }

    @Test
    void testSingletonBehavior() {
        SharedDataStorage instance1 = SharedDataStorage.getInstance();
        SharedDataStorage instance2 = SharedDataStorage.getInstance();

        assertSame(instance1, instance2);
    }

    @Test
    void testInitializeAndGetId() {
        SharedDataStorage storage = SharedDataStorage.getInstance();

        storage.initializeId("ORDER_ID", 100);
        assertEquals(100, storage.getCurrentId("ORDER_ID"));
    }

    @Test
    void testGetNextId() {
        SharedDataStorage storage = SharedDataStorage.getInstance();

        storage.initializeId("ORDER_ID", 100);
        int nextId = storage.getNextId("ORDER_ID");

        assertEquals(101, nextId);
        assertEquals(101, storage.getCurrentId("ORDER_ID"));
    }

    @Test
    void testGetCurrentIdWithoutIncrementing() {
        SharedDataStorage storage = SharedDataStorage.getInstance();

        storage.initializeId("ORDER_ID", 100);
        assertEquals(100, storage.getCurrentId("ORDER_ID"));
    }

    @Test
    void testIdNotInitializedThrowsException() {
        SharedDataStorage storage = SharedDataStorage.getInstance();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storage.getNextId("UNINITIALIZED_KEY");
        });

        assertTrue(exception.getMessage().contains("ID not initialized for key: UNINITIALIZED_KEY"));
    }

    @Test
    void testInvalidKeyThrowsException() {
        SharedDataStorage storage = SharedDataStorage.getInstance();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storage.getNextId("INVALID_KEY");
        });

        assertTrue(exception.getMessage().contains("ID not initialized for key"));
    }
}