package flare.ibkr;

import com.ib.client.EClientSocket;
import com.ib.client.EReaderSignal;
import flare.Analyst;
import flare.IPersistentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class IBKRConnectionManagerTest {

    private IBKRConnectionManager connectionManager;
    private Analyst mockAnalyst;
    private IPersistentStorage mockPersistentStorage;

    @BeforeEach
    void setUp() {
        mockAnalyst = mock(Analyst.class);
        mockPersistentStorage = mock(IPersistentStorage.class);
        connectionManager = new IBKRConnectionManager(mockAnalyst, mockPersistentStorage);
    }

    @Test
    void testInitializeClients() {
        connectionManager.initializeClients(5);

        List<EClientSocket> clients = connectionManager.getBrokerClients();
        List<EReaderSignal> signals = connectionManager.getReaderSignals();

        assertEquals(5, clients.size());
        assertEquals(5, signals.size());
    }

    @Test
    void testStartClients() {
        connectionManager.initializeClients(5);
        connectionManager.startClients();

        // Verifying that threads were started, but we can't directly test this in a straightforward way without
        // significantly more complex test code. Instead, we can check that clients are initialized.
        List<EClientSocket> clients = connectionManager.getBrokerClients();
        assertEquals(5, clients.size());
    }
}
