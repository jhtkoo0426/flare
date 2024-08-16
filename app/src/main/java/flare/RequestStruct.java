package flare;


import java.time.LocalDate;


public class RequestStruct {
    private String symbol;
    private String secType;
    private Double strike;
    private String right;
    private LocalDate lastTradeDate;
    private Double quantity;

    public RequestStruct(String symbol, String secType, Double strike, String right, LocalDate expiryDate, Double quantity) {
        this.symbol = symbol;
        this.secType = secType;
        this.strike = strike;
        this.right = right;
        this.lastTradeDate = expiryDate;
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getStrike() {
        return strike;
    }

    public LocalDate getLastTradeDate() {
        return lastTradeDate;
    }

    public String getRight() {
        return right;
    }

    public String getSecType() {
        return secType;
    }

    public Double getQuantity() {
        return quantity;
    }
}
