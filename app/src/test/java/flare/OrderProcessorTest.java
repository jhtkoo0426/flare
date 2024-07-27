package flare;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;



class OrderProcessorTest {

    private OrderProcessor orderProcessor;

    @BeforeEach
    void setUp() {
        orderProcessor = new OrderProcessor();
    }

    @Test
    void addOrderListener_ShouldAddListenerToList() {
        OrderListener mockListener = mock(OrderListener.class);
        orderProcessor.addOrderListener(mockListener);

        List<OrderListener> listeners = orderProcessor.getListeners();
        assertTrue(listeners.contains(mockListener));
    }

    @Test
    void removeOrderListener_ShouldRemoveListenerFromList() {
        OrderListener mockListener = mock(OrderListener.class);
        orderProcessor.addOrderListener(mockListener);
        orderProcessor.removeOrderListener(mockListener);

        List<OrderListener> listeners = orderProcessor.getListeners();
        assertFalse(listeners.contains(mockListener));
    }

    @Test
    void makeOrder_ShouldNotifyAllListeners() {
        OrderListener mockListener1 = mock(OrderListener.class);
        OrderListener mockListener2 = mock(OrderListener.class);
        orderProcessor.addOrderListener(mockListener1);
        orderProcessor.addOrderListener(mockListener2);

        double price = 99.99;
        orderProcessor.makeOrder(price);

        ArgumentCaptor<OrderEvent> eventCaptor = ArgumentCaptor.forClass(OrderEvent.class);

        verify(mockListener1).onOrderMade(eventCaptor.capture());
        verify(mockListener2).onOrderMade(eventCaptor.capture());

        List<OrderEvent> capturedEvents = eventCaptor.getAllValues();
        assertEquals(2, capturedEvents.size());
        assertEquals(price, capturedEvents.get(0).getOrderPrice());
        assertEquals(price, capturedEvents.get(1).getOrderPrice());
    }
}
