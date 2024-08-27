package uniproj.cursol.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import uniproj.cursol.entity.ForecastExchangeRate;

public interface ForecastRepo extends JpaRepository<ForecastExchangeRate, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ForecastExchangeRate f")
    void deleteOldForecasts();

    @Query("Select f FROM ForecastExchangeRate f where f.currency = :currencyPair")
    List<ForecastExchangeRate> gettingAllForecastData(@Param("currencyPair") String currencyPair);

}
