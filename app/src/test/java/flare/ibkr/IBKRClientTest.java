package flare.ibkr;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.ib.client.Order;
import flare.IPersistentStorage;
import flare.OrderManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class IBKRClientTest {

    private IPersistentStorage mockPersistentStorage;
    private IBKRClient ibkrClient;
    private OrderManager mockOrderManager;
    private IBKRConnectionManager mockConnectionManager;
    private EClientSocket mockBrokerClient;

    @BeforeEach
    public void setUp() {
        // Mock dependencies
        mockPersistentStorage = mock(IPersistentStorage.class);
        mockOrderManager = mock(OrderManager.class);
        mockConnectionManager = mock(IBKRConnectionManager.class);
        mockBrokerClient = mock(EClientSocket.class);

        when(mockConnectionManager.getBrokerClient()).thenReturn(mockBrokerClient);

        // Configure mocks
        when(mockPersistentStorage.readLastOrderId()).thenReturn(123);
        when(mockOrderManager.getNextOrderId()).thenReturn(1);

        // Inject mocks into IBKRClient
        ibkrClient = new IBKRClient(mockPersistentStorage);
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
        //FIXME Change assertion when the method for handling order execution output is changed.
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
