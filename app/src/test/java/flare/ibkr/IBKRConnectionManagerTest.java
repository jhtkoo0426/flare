package flare.ibkr;

import com.ib.client.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class IBKRConnectionManagerTest {

    private EWrapper mockWrapper;
    private EClientSocket mockClientSocket;
    private EReaderSignal mockReaderSignal;
    private IBKRConnectionManager connectionManager;

    @BeforeEach
    void setUp() {
        mockWrapper = mock(EWrapper.class);
        mockClientSocket = mock(EClientSocket.class);
        mockReaderSignal = mock(EReaderSignal.class);
        connectionManager = new IBKRConnectionManager(mockWrapper);

        // Injecting mocks into the connection manager using reflection (since it's not designed for dependency injection)
        try {
            var brokerClientField = IBKRConnectionManager.class.getDeclaredField("brokerClient");
            brokerClientField.setAccessible(true);
            brokerClientField.set(connectionManager, mockClientSocket);

            var readerSignalField = IBKRConnectionManager.class.getDeclaredField("readerSignal");
            readerSignalField.setAccessible(true);
            readerSignalField.set(connectionManager, mockReaderSignal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testConstructorInitializesFields() {
        IBKRConnectionManager manager = new IBKRConnectionManager(mockWrapper);
        assertNotNull(manager.getBrokerClient());
    }

    @Test
    void testGetBrokerClient() {
        assertEquals(mockClientSocket, connectionManager.getBrokerClient());
    }

    @Test
    void testConnectToBroker() {
        connectionManager.connectToBroker();
        verify(mockClientSocket).eConnect("127.0.0.1", 7497, 2);
    }
}
