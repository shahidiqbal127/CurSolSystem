package uniproj.cursol.service.scrapservice;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.regex.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uniproj.cursol.dao.ForecastRepo;
import uniproj.cursol.entity.ForecastExchangeRate;

@Service
public class ScrapServiceImpl implements ScrapService {

    @Autowired
    private ForecastRepo forecastRepo;

    private String baseUrl = "https://30rates.com/%s-%s";

    @Override
    public void forecastRates() {

        forecastRepo.deleteOldForecasts();

        List<String> currencyPairs = List.of(
            "GBP to PKR",
            "GBP to INR",
            "GBP to NGN",
            "EUR to PKR",
            "EUR to NGN",
            "EUR to INR");

        String fromCur = null;
        String toCur = null;

        for (String currencyPair : currencyPairs) {

            if (currencyPair.equals("EUR to INR")) {

                String[] parts = currencyPair.split(" to ");

                // Get the "from" currency and truncate it to 3 letters if needed
                String fromCurrency = parts[0].length() > 3 ? parts[0].substring(0, 3) : parts[0];

                // Construct the final "from-to" string
                fromCur = fromCurrency + "-to";

                // Store the "to" currency
                toCur = parts[1];

            } else {

                String[] parts = currencyPair.split(" to ");

                fromCur = parts[0].trim();

                toCur = parts[1].trim();
            }

            List<Double> rates = scrapingRates(fromCur, toCur);
        double minRate = Collections.min(rates);
        double maxRate = Collections.max(rates);

        IntStream.range(1, 16).forEach(i -> {
            LocalDate targetDate = LocalDate.now().plusDays(i);
            storingForecasting(minRate - 1.8, maxRate + 1.8 , currencyPair, targetDate, rates.get(i - 1));
        });


        }

        

       

        
    }

    private List<Double> scrapingRates(String sourceCur, String targetCur){

        List<Double> rates = new ArrayList<>();

        WebDriver driver = new ChromeDriver();

        try {

            String url = String.format(baseUrl, sourceCur.toLowerCase(), targetCur.toLowerCase());

            // Navigate to the webpage containing the table
            driver.get(url); // Replace with the actual URL

            // Use WebDriverWait to wait for the table to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.tbh")));

            // Locate the table and extract all rows
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            // Define regex pattern for extracting numbers
            Pattern ratePattern = Pattern.compile("\\d+");

            // Iterate through each row and extract the rate
            for (int i = 1; i < 16; i++) { // Skip header row if present
                WebElement row = rows.get(i);
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() >= 5) {
                    WebElement strongElement = cells.get(4).findElement(By.cssSelector("strong"));

                    // Use regex to extract the number from the HTML
                    String strongElementHtml = strongElement.getAttribute("outerHTML");
                    Matcher matcher = ratePattern.matcher(strongElementHtml);

                    if (matcher.find()) {
                        String rateText = matcher.group();
                        try {
                            // Convert rateText to Double and add to the list
                            double rate = Double.parseDouble(rateText);
                            rates.add(rate);
                        } catch (NumberFormatException e) {
                            // Handle parsing error if needed
                            System.err.println("Failed to parse rate: " + rateText);
                        }
                    }
                }
            }
        } finally {
            // Close the browser
            driver.quit();
        }

        return rates;

    }

    private void storingForecasting(Double minRate, Double maxRate, String currency, LocalDate targetDate, Double rate){

        ForecastExchangeRate forecast = new ForecastExchangeRate();
            forecast.setCurrency(currency);
            forecast.setTargetDate(targetDate);
            forecast.setConfidenceIntervalMin(minRate);
            forecast.setConfidenceIntervalMax(maxRate);
            forecast.setPredictedValue(rate);

            forecastRepo.save(forecast);

    }

}
