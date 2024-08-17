package uniproj.cursol.querydtos;

import java.time.LocalDateTime;

public class ExchangeRateQueryResultHoldNative {

    private String fromCurrency;
    private String toCurrency;
    private double rate;
    private double deliveryFee;
    private String estimatedDeliveryTime;
    private LocalDateTime lastUpdated;
    private String platformName;
    private String platformImg;
    private String platformSiteUrl;

    public ExchangeRateQueryResultHoldNative(String fromCurrency, String toCurrency, double rate, double deliveryFee,
            String estimatedDeliveryTime, LocalDateTime lastUpdated,
            String platformName, String platformImg, String platformSiteUrl) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.deliveryFee = deliveryFee;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.lastUpdated = lastUpdated;
        this.platformName = platformName;
        this.platformImg = platformImg;
        this.platformSiteUrl = platformSiteUrl;
    }

    // Getters and Setters
    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformImg() {
        return platformImg;
    }

    public void setPlatformImg(String platformImg) {
        this.platformImg = platformImg;
    }

    public String getPlatformSiteUrl() {
        return platformSiteUrl;
    }

    public void setPlatformSiteUrl(String platformSiteUrl) {
        this.platformSiteUrl = platformSiteUrl;
    }
}
