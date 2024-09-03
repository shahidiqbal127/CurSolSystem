package uniproj.cursol.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uniproj.cursol.entity.ExchangeRate;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHold;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHoldNative;

@Repository
public interface ExchangeRateRepo extends JpaRepository<ExchangeRate, Long> {

        @Query("SELECT c FROM ExchangeRate c WHERE c.fromCurrency = :sourceCurrency AND c.toCurrency = :targetCurrency")
        List<ExchangeRate> getExRateBySourceAndTarget(String sourceCurrency, String targetCurrency);

        @Query("SELECT new uniproj.cursol.querydtos.ExchangeRateQueryResultHold(e.fromCurrency, e.toCurrency, e.rate, e.deliveryFee, e.estimatedDeliveryTime, e.lastUpdated, p.platformName, p.platformImg, p.platformSiteUrl) "
                        +
                        "FROM ExchangeRate e " +
                        "JOIN Platform p ON e.platformId = p.platformId " +
                        "WHERE e.exchangeRateId > :lastMaxId " +
                        "AND e.fromCurrency = :sourceCurrency " +
                        "AND e.toCurrency = :targetCurrency")
        List<ExchangeRateQueryResultHold> findLatestExchangeRates(
                        @Param("lastMaxId") Long lastMaxId,
                        @Param("sourceCurrency") String sourceCurrency,
                        @Param("targetCurrency") String targetCurrency);

        @Query("SELECT MAX(e.exchangeRateId) FROM ExchangeRate e")
        Long findExRateMaxId();

        // @Query("SELECT new uniproj.cursol.querydtos.ExchangeRateQueryResultHold(" +
        // "e.fromCurrency, e.toCurrency, e.rate, e.deliveryFee,
        // e.estimatedDeliveryTime, e.lastUpdated, p.platformName, p.platformImg,
        // p.platformSiteUrl) "
        // +
        // "FROM ExchangeRate e JOIN Platform p ON e.platformId = p.platformId " +
        // "WHERE e.lastUpdated::text LIKE %:datePattern%")
        // List<ExchangeRateQueryResultHold>
        // findHistoricalExchangeRates(@Param("datePattern") String datePattern);

        @Query(value = "SELECT e.source_currency_code AS fromCurrency, e.target_currency_code AS toCurrency, e.rate, e.delivery_fee AS deliveryFee, "
                        +
                        "e.estimated_delivery_time AS estimatedDeliveryTime, e.last_updated AS lastUpdated, " +
                        "p.platform_name AS platformName, p.platform_img_url AS platformImg, p.platform_site_url AS platformSiteUrl "
                        +
                        "FROM exchange_rate e " +
                        "JOIN platform p ON e.platform_id = p.platform_id " +
                        "WHERE e.last_updated::DATE = CAST(:datePattern AS DATE) " +
                        "AND e.source_currency_code = :sourceCurrency " +
                        "AND e.target_currency_code = :targetCurrency", nativeQuery = true)
        List<ExchangeRateQueryResultHoldNative> findHistoricalExchangeRates(
                        @Param("datePattern") String datePattern,
                        @Param("sourceCurrency") String sourceCurrency,
                        @Param("targetCurrency") String targetCurrency);

}
