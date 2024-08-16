package flare;

import com.ib.client.Decimal;
import flare.models.BaseModel;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Receives market data and performs real-time analysis.
 */
public class Analyst {

    private BaseModel model;
    public ConcurrentHashMap<String, Double> lastPrices = new ConcurrentHashMap<>();

    public Analyst(BaseModel model) {
        this.model = model;
    }

    public void listenBar(RequestStruct reqData, long time, double open, double high, double low, double close, Decimal volume) {
        System.out.printf("Request %s @ %d | o=%.2f h=%.2f l=%.2f c=%.2f volume=%d\n", reqData.getSymbol(), time, open, high, low, close, volume.longValue());
        lastPrices.put(reqData.getSymbol() + reqData.getSecType(), close);
    }

    public void analyseOption(String symbol, String underlyingSecType, double underlyingSpot, double optionStrike, double optionImpliedVol, LocalDate expiryDate, String right, double actualOptionPrice) {
        String key = OCCFormatter.formatOCC(symbol, expiryDate, optionStrike, right);

        double predictedPrice = right.equals("C")
                ? model.call(underlyingSpot, optionStrike, optionImpliedVol, expiryDate)
                : model.put(underlyingSpot, optionStrike, optionImpliedVol, expiryDate);

        System.out.printf("%s | Actual: %.2f | Predicted: %.2f\n", key, actualOptionPrice, predictedPrice);
    }

    public BaseModel getModel() {
        return model;
    }

}
