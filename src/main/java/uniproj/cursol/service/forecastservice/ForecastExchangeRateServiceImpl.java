package uniproj.cursol.service.forecastservice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import uniproj.cursol.dao.ForecastRepo;
import uniproj.cursol.entity.ForecastExchangeRate;


@Service
public class ForecastExchangeRateServiceImpl implements ForecastExchangeRateService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ForecastRepo forecastRepo;

    private final String ForecastUrl = "http://localhost:8000/predict";

    @Override
    public void fetchAndStoreForecast(String currency, LocalDate targetDate, Double rate) {

        System.out.println("dates   ===  " + targetDate);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("currency", currency);
        requestBody.put("target_date", targetDate);

        String apiUrlWithParams = buildApiUrl(currency, targetDate);

        String response = webClient.post()
                .uri(apiUrlWithParams)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);

            String currency1 = root.get("currency").asText();
            String targetDateStr = root.get("target_date").asText();
            double predictedValue = root.get("predicted_value").asDouble();
            JsonNode confidenceInterval = root.get("confidence_interval");
            double confidenceIntervalMin = confidenceInterval.get(0).asDouble();
            double confidenceIntervalMax = confidenceInterval.get(1).asDouble();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate targetDate1 = LocalDate.parse(targetDateStr, formatter);

            ForecastExchangeRate forecast = new ForecastExchangeRate();
            forecast.setCurrency(currency1);
            forecast.setTargetDate(targetDate1);
            forecast.setConfidenceIntervalMin(confidenceIntervalMin);
            forecast.setConfidenceIntervalMax(confidenceIntervalMax);


            double predValue = rate;
            double conIntMax = forecast.getConfidenceIntervalMax();
            double conIntMin = forecast.getConfidenceIntervalMin();
            
            
            if ( conIntMax < predValue ) {
                double dummy = predValue - conIntMax;
                forecast.setPredictedValue(rate-dummy - 0.5);
            }else if(conIntMin > predValue){
                double dummy = conIntMin - predValue;
                forecast.setPredictedValue(rate+dummy + 0.5);
            }else{
                forecast.setPredictedValue(rate);
            }

            

            forecastRepo.save(forecast);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse forecast data", e);
        }
    }

    private String buildApiUrl(String currency, LocalDate targetDate) {
        return UriComponentsBuilder.fromHttpUrl(ForecastUrl)
                .queryParam("currency", currency)
                .queryParam("targetDate", targetDate)
                .toUriString();
    }

}
