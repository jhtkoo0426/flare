package flare;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OrderEventTest {

    private final OrderEvent event = new OrderEvent();

    @Test
    public void shouldSetPriceOnInvoke() {
        event.setOrderPrice(100.00);
        assertEquals(event.getOrderPrice(), 100.00);
    }

    @Test
    public void testEventPrice() {
        assertNull(event.getOrderPrice());
    }

}
