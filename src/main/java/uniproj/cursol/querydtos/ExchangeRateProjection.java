package uniproj.cursol.querydtos;

public interface ExchangeRateProjection {
    String getFromCurrency();   // Matches e.fromCurrency
    String getToCurrency();     // Matches e.toCurrency
    Double getRate();           // Matches e.rate
    Double getDeliveryFee();    // Matches e.deliveryFee
    String getEstimatedDeliveryTime(); // Matches e.estimatedDeliveryTime
    String getLastUpdated();    // Matches e.lastUpdated
    String getPlatformName();   // Matches p.platformName
    String getPlatformImg();    // Matches p.platformImg
    String getPlatformSiteUrl(); // Matches p.platformSiteUrl
}

