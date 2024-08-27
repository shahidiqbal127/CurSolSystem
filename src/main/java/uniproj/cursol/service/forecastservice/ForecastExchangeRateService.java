package uniproj.cursol.service.forecastservice;

import java.time.LocalDate;

public interface ForecastExchangeRateService {

    void fetchAndStoreForecast(String currency, LocalDate targetDate);

}
