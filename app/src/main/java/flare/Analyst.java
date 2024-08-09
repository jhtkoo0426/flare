package flare;

/**
 * Receives market data and performs real-time analysis.
 */
public class Analyst {

    public Analyst() {

    }

    public void listenBar(int reqId, long time, double open, double high, double low, double close, double volume) {
        System.out.printf("Request %d @ %d | o=%.2f h=%.2f l=%.2f c=%.2f volume=%d", reqId, time, open, high, low, close, volume);
    }

}
