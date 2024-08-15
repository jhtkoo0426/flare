package flare;

import com.ib.client.Decimal;
import flare.models.BaseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AnalystTest {

    private Analyst analyst;
    private BaseModel mockModel;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        analyst = new Analyst();
        mockModel = Mockito.mock(BaseModel.class);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testListenBar() {
        RequestStruct mockRequest = mock(RequestStruct.class);
        when(mockRequest.getSymbol()).thenReturn("AAPL");
        when(mockRequest.getSecType()).thenReturn("STK");

        analyst.listenBar(mockRequest, 1234567890L, 150.00, 155.00, 149.00, 154.00, Decimal.get(1000));

        assertEquals(154.00, analyst.lastPrices.get("AAPLSTK"));
        String expectedOutput = "Request AAPL @ 1234567890 | o=150.00 h=155.00 l=149.00 c=154.00 volume=1000\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void testAnalyseOptionCall() {
        RequestStruct mockRequest = mock(RequestStruct.class);
        when(mockRequest.getSymbol()).thenReturn("AAPL");
        when(mockRequest.getSecType()).thenReturn("STK");

        when(mockModel.call(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(10.50);
        analyst.loadModel(mockModel);
        analyst.listenBar(mockRequest, System.currentTimeMillis(), 100.00, 110.00, 95.00, 105.00, Decimal.get(5000));

        // Clear the captured output before calling analyseOption
        outputStreamCaptor.reset();
        LocalDate expiryDate = LocalDate.of(2024, 8, 15);
        analyst.analyseOption("AAPL", "STK", 105.00, 105.00, 0.20, expiryDate, "C", 10.00);

        String expectedOutput = "AAPL240815C00105000 | Actual: 10.00 | Predicted: 10.50\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    @Test
    void testAnalyseOptionPut() {
        RequestStruct mockRequest = mock(RequestStruct.class);
        when(mockRequest.getSymbol()).thenReturn("AAPL");
        when(mockRequest.getSecType()).thenReturn("STK");

        when(mockModel.put(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(8.75);
        analyst.loadModel(mockModel);
        analyst.listenBar(mockRequest, System.currentTimeMillis(), 100.00, 110.00, 95.00, 105.00, Decimal.get(5000));

        // Clear the captured output after listenBar
        outputStreamCaptor.reset();
        LocalDate expiryDate = LocalDate.of(2024, 8, 15);
        analyst.analyseOption("AAPL", "STK", 105.00, 95.00, 0.25, expiryDate, "P", 8.00);

        String expectedOutput = "AAPL240815P00095000 | Actual: 8.00 | Predicted: 8.75\n";
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }


    @Test
    void testLoadModel() {
        analyst.loadModel(mockModel);
        // Verify that the model is set correctly
        assertNotNull(analyst.getModel());
    }

    @Test
    void testModelNullException() {
        analyst.loadModel(null);
        LocalDate expiryDate = LocalDate.of(2024, 8, 15);
        // Test for NullPointerException or handle the case if model is not loaded
        assertThrows(NullPointerException.class, () -> {
            analyst.analyseOption("AAPL", "STK", 100.00, 105.00, 0.20, expiryDate, "C", 10.00);
        });
    }
}
