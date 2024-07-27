package flare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AppTest {

    private final App app = new App();

    @Test
    public void testDummy() {
        assertEquals(app.dummyMethod(), "haha");
    }

}