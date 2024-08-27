package uniproj.cursol.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "forecast_exchange_rate")
public class ForecastExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(name = "predicted_value", nullable = false)
    private double predictedValue;

    @Column(name = "confidence_interval_min", nullable = false)
    private double confidenceIntervalMin;

    @Column(name = "confidence_interval_max", nullable = false)
    private double confidenceIntervalMax;

    public ForecastExchangeRate() {
    }

    public ForecastExchangeRate(String currency, LocalDate targetDate, double predictedValue,
            double confidenceIntervalMin, double confidenceIntervalMax) {
        this.currency = currency;
        this.targetDate = targetDate;
        this.predictedValue = predictedValue;
        this.confidenceIntervalMin = confidenceIntervalMin;
        this.confidenceIntervalMax = confidenceIntervalMax;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public double getPredictedValue() {
        return predictedValue;
    }

    public void setPredictedValue(double predictedValue) {
        this.predictedValue = predictedValue;
    }

    public double getConfidenceIntervalMin() {
        return confidenceIntervalMin;
    }

    public void setConfidenceIntervalMin(double confidenceIntervalMin) {
        this.confidenceIntervalMin = confidenceIntervalMin;
    }

    public double getConfidenceIntervalMax() {
        return confidenceIntervalMax;
    }

    public void setConfidenceIntervalMax(double confidenceIntervalMax) {
        this.confidenceIntervalMax = confidenceIntervalMax;
    }

}
