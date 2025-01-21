package uniproj.cursol.service.exchangerateservice;

import java.util.List;
import uniproj.cursol.entity.ExchangeRate;
import uniproj.cursol.querydtos.ExchangeRateProjection;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHold;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHoldNative;

public interface ExchangeRateService {

    void fetchAndStoreExchangeRates();

    List<ExchangeRate> getExRateBySourceAndTarget(String sourceCurrency, String targetCurrency);

    List<ExchangeRateProjection> getLatestExchangeRates(String sourceCurrency, String targetCurrency);

    List<ExchangeRateQueryResultHoldNative> findHistoricalExchangeRates(String datePattern, String sourceCurrency,
            String targetCurrency);

    // List<ExchangeRateQueryResultHold> getExchangeRatesBySourceAndTarget(String
    // sourceCurrency, String targetCurrency);

}
