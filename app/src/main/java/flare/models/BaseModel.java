package flare.models;


import java.time.LocalDate;


public abstract class BaseModel {

    public abstract void setRiskFree(double riskFreeRate);
    public abstract Double call(double spot, double strike, double vol, LocalDate expiryDate);
    public abstract Double put(double spot, double strike, double vol, LocalDate expiryDate);

}
