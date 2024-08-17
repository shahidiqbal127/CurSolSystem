package uniproj.cursol.api.exchangerateentities;

import java.util.Date;

public class Quote {
    public double rate;
    public double fee;
    public Date dateCollected;
    public String sourceCountry;
    public String targetCountry;
    public double markup;
    public double receivedAmount;
    public DeliveryEstimation deliveryEstimation;
}
