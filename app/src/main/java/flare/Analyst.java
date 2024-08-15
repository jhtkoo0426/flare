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
    private ConcurrentHashMap<String, Double> lastPrices = new ConcurrentHashMap<>();

    public Analyst() {

    }

    public void listenBar(RequestStruct reqData, long time, double open, double high, double low, double close, Decimal volume) {
        System.out.printf("Request %s @ %d | o=%.2f h=%.2f l=%.2f c=%.2f volume=%d\n", reqData.getSymbol(), time, open, high, low, close, volume.longValue());
        lastPrices.put(reqData.getSymbol() + reqData.getSecType(), close);
    }

    public void analyseOption(String symbol, String underlyingSecType, double underlyingSpot, double optionStrike, double optionImpliedVol, LocalDate expiryDate, String right, double actualOptionPrice) {
        String secType = "STK";
        double spot = lastPrices.get(symbol + secType);
        String key = OCCFormatter.formatOCC(symbol, expiryDate, optionStrike, right);

        if (right.equals("C")) {
            double call = model.call(spot, optionStrike, optionImpliedVol, expiryDate);
            System.out.printf("%s | Actual: %.2f | Predicted: %.2f\n", key, actualOptionPrice, call);
        } else {
            double put = model.put(spot, optionStrike, optionImpliedVol, expiryDate);
            System.out.printf("\"%s | Actual: %.2f | Predicted: %.2f\n", key, actualOptionPrice, put);
        }
    }

    public void loadModel(BaseModel model) {
        this.model = model;
    }

}
