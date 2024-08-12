package flare;

import com.ib.client.Decimal;

/**
 * Receives market data and performs real-time analysis.
 */
public class Analyst {

    public Analyst() {

    }

    public void listenBar(String reqData, long time, double open, double high, double low, double close, Decimal volume) {
        System.out.printf("Request %s @ %d | o=%.2f h=%.2f l=%.2f c=%.2f volume=%d\n", reqData, time, open, high, low, close, volume.longValue());
    }

}
