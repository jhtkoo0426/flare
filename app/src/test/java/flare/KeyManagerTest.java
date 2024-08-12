package flare;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    void testRegisterDataWithFileLogging() throws IOException {
        // Create a temporary file for logging
        Path tempFile = Files.createTempFile("order_logs", ".csv");
        tempFile.toFile().deleteOnExit(); // Ensure the temp file is deleted on exit

        // Create an instance of OrderManager with the temporary file path
        OrderManager orderManager = new OrderManager(tempFile.toString());

        // Initialize the ID and register some order data
        orderManager.initializeId(1);
        orderManager.registerOrderData(1, "AAPL", "STOCK", 50.5);

        // Read the contents of the temp file and verify it contains the expected data
        String content = Files.readString(tempFile);
        assertTrue(content.contains("1,AAPL,STOCK,50.5"));

        // Cleanup the temp file
        Files.delete(tempFile);
    }



    @Test
    void testRegisterDataWithoutFileLogging() {
        RequestManager requestManager = new RequestManager();

        requestManager.initializeId(1);
        requestManager.registerRequestData(1, "AAPL");

        // No assertion needed here as no file writing occurs, but the test should pass
    }
}
