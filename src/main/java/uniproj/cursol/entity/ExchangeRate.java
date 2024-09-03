package uniproj.cursol.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_rate_id")
    private Long exchangeRateId;

    @Column(name = "platform_id")
    private Integer platformId;

    @Column(name = "source_currency_code")
    private String fromCurrency;

    @Column(name = "target_currency_code")
    private String toCurrency;

    @Column(name = "rate")
    private double rate;

    @Column(name = "delivery_fee")
    private double deliveryFee;

    @Column(name = "estimated_delivery_time")
    private String estimatedDeliveryTime;

    @Column(name = "last_updated")
    private Date lastUpdated;

    // Getters and Setters
    public Long getExchangeRateId() {
        return exchangeRateId;
    }

    public void setExchangeRateId(Long exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrencyId) {
        this.fromCurrency = fromCurrencyId;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Integer getPlatform() {
        return platformId;
    }

    public void setPlatform(Integer platformId) {
        this.platformId = platformId;
    }

}
