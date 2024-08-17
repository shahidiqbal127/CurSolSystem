package uniproj.cursol.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHold;

@Service
public class ExchangeRateReopEM {

    @Autowired
    private EntityManager entityManager;

    public List<ExchangeRateQueryResultHold> findHistoricalExchangeRates(LocalDate date, String source, String target) {

        String jpql = "SELECT DISTINCT new uniproj.cursol.querydtos.ExchangeRateQueryResultHold(" +
                "e.fromCurrency, e.toCurrency, e.rate, e.deliveryFee, e.estimatedDeliveryTime, e.lastUpdated, " +
                "p.platformName, p.platformImg, p.platformSiteUrl) " +
                "FROM ExchangeRate e JOIN Platform p ON e.platformId = p.platformId " +
                "WHERE FUNCTION('DATE', e.lastUpdated) = :date " +
                "AND e.fromCurrency = :sourceCurrency " +
                "AND e.toCurrency = :targetCurrency";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("date", date);
        query.setParameter("sourceCurrency", source);
        query.setParameter("targetCurrency", target);

        List<ExchangeRateQueryResultHold> results = query.getResultList();

        Map<String, ExchangeRateQueryResultHold> distinctResults = results.stream()
                .collect(Collectors.toMap(
                        ExchangeRateQueryResultHold::getPlatformName, // Assuming there's a method to get platform ID
                        Function.identity(),
                        (existing, replacement) -> existing // Keep the first occurrence
                ));

        return new ArrayList<>(distinctResults.values());

    }

}
