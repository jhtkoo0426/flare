package flare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class KeyManagerTest {

    private SharedDataStorage storage;

    @BeforeEach
    void setup() {
        // Resetting the singleton instance before each test
        storage = SharedDataStorage.getInstance();
        storage.initializeId("ORDER_ID", 0);
        storage.initializeId("REQUEST_ID", 0);
    }

    @Test
    void testInitializeId() {
        KeyManager keyManager = new KeyManager("TEST_ID", null);
        keyManager.initializeId(100);

        assertEquals(100, storage.getCurrentId("TEST_ID"));
    }

    @Test
    void testGetNextId() {
        KeyManager keyManager = new KeyManager("ORDER_ID", null);

        keyManager.initializeId(10);
        int nextId = keyManager.getNextId();

        assertEquals(11, nextId);
        assertEquals(11, storage.getCurrentId("ORDER_ID"));
    }

    @Test
    void testGetCurrentId() {
        KeyManager keyManager = new KeyManager("ORDER_ID", null);

        keyManager.initializeId(10);
        int currentId = keyManager.getCurrentId();

        assertEquals(10, currentId);
    }

    @Test
    void testRegisterDataWithoutFileLogging() {
        RequestManager requestManager = new RequestManager();

        requestManager.initializeId(1);

        // Create sample RequestStruct data
        RequestStruct requestData = new RequestStruct(
                "AAPL",
                "STOCK",
                150.0,
                "CALL",
                LocalDate.of(2024, 12, 31),
                100.0
        );

        requestManager.registerRequestData(1, requestData);

        // Retrieve the data and verify its correctness
        RequestStruct registeredData = requestManager.getRequestData(1);
        assertNotNull(registeredData, "Registered data should not be null");
        assertEquals("AAPL", registeredData.getSymbol());
        assertEquals("STOCK", registeredData.getSecType());
        assertEquals(150.0, registeredData.getStrike());
        assertEquals("CALL", registeredData.getRight());
        assertEquals(LocalDate.of(2024, 12, 31), registeredData.getLastTradeDate());
        assertEquals(100.0, registeredData.getQuantity());
    }
}
