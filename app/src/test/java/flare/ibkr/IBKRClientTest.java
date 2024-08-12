package flare.ibkr;

import com.ib.client.EClientSocket;
import flare.Analyst;
import flare.IPersistentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


public class IBKRClientTest {

    private IPersistentStorage mockPersistentStorage;
    private IBKRClient ibkrClient;
    private EClientSocket mockBrokerClient;
    private Analyst analyst;

    @BeforeEach
    public void setUp() {
        // Mock dependencies
        mockPersistentStorage = mock(IPersistentStorage.class);
        mockBrokerClient = mock(EClientSocket.class);
        analyst = mock(Analyst.class);

        // Inject mocks into IBKRClient
        ibkrClient = new IBKRClient(mockPersistentStorage, analyst, mockBrokerClient, 1);
    }

    @Test
    void testIBKRClientInstantiation() {
        assertNotNull(ibkrClient);
    }

    @Test
    void testOnOrderExecuted() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ibkrClient.onOrderPlaced(123, 456.78, 100);
        String actualOutput = outputStream.toString().trim();
        assertEquals(actualOutput, "Order ID 123 placed: Execution price @ 456.78 and quantity 100.00.");
    }

    @Test
    void testOnOrderCancelled() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ibkrClient.onOrderCancelled(123);
        String actualOutput = outputStream.toString().trim();
        assertEquals(actualOutput, "Order ID 123 cancelled.");
    }
}
