package flare.models;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.apache.commons.math3.distribution.NormalDistribution;


/**
 * Black-Scholes model for option pricing.
 * <ul>
 *      <li>Volatility: Should be calculated or queried from data sources. Use implied
 *      volatility instead of historical to reflect current market conditions and expectations.</li>
 *      <li>Risk-free interest rate: Should be queried from external sources.</li>
 * </ul>
 */
public class BlackScholes extends BaseModel {

    private double riskFree;

    public BlackScholes(double riskFreeRate) {
        this.riskFree = riskFreeRate;
    }

    public void setRiskFree(double riskFreeRate) {
        this.riskFree = riskFreeRate;
    }

    private static double daysToMaturity(LocalDate expiryDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate) / 365.0;
    }

    private static double d1(double spot, double strike, double riskFree, double vol, double years) {
        return (Math.log(spot / strike) + (riskFree + 0.5 * vol * vol) * years) / (vol * Math.sqrt(years));
    }

    private static double optionPrice(double spot, double strike, double riskFree, double vol, LocalDate expiryDate, boolean isCall) {
        double years = daysToMaturity(expiryDate);
        double d1 = d1(spot, strike, riskFree, vol, years);
        double d2 = d1 - vol * Math.sqrt(years);
        NormalDistribution normDist = new NormalDistribution(0, 1);
        double callPrice = spot * normDist.cumulativeProbability(d1) - strike * Math.exp(-riskFree * years) * normDist.cumulativeProbability(d2);
        return isCall ? callPrice : callPrice - spot + strike * Math.exp(-riskFree * years);
    }

    public Double call(double spot, double strike, double vol, LocalDate expiryDate) {
        return optionPrice(spot, strike, riskFree, vol, expiryDate, true);
    }

    public Double put(double spot, double strike, double vol, LocalDate expiryDate) {
        return optionPrice(spot, strike, riskFree, vol, expiryDate, false);
    }
}
