package uniproj.cursol.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uniproj.cursol.dao.ExchangeRateRepo;
import uniproj.cursol.dao.ForecastRepo;
import uniproj.cursol.dao.ExchangeRateReopEM;
import uniproj.cursol.dao.MaxIdRepository;
import uniproj.cursol.dto.CurrencyCodeAndFlagsDTO;
import uniproj.cursol.entity.ContactUs;
import uniproj.cursol.entity.ExRateMaxId;
import uniproj.cursol.entity.ForecastExchangeRate;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHold;
import uniproj.cursol.service.TaptapSendService.TaptapSendService;
import uniproj.cursol.service.contactusservice.ContactUsService;
import uniproj.cursol.service.currencyservice.CurrencyService;
import uniproj.cursol.service.exchangerateservice.ExchangeRateService;
import uniproj.cursol.service.forecastservice.ForecastExchangeRateService;
import uniproj.cursol.service.lemfiservice.LemfiService;
import uniproj.cursol.service.scrapservice.ScrapService;

@RestController
@RequestMapping("/api")
public class CurSolController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private MaxIdRepository maxIdRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private TaptapSendService taptapSendService;

    @Autowired
    private LemfiService lemfiService;

    @Autowired
    private ForecastExchangeRateService forecastExchangeRateService;

    @Autowired
    private ExchangeRateRepo exchangeRateRepo;

    @Autowired
    private ExchangeRateReopEM exchangeRateRepoEM;

    @Autowired
    private ContactUsService contactUsService;

    @Autowired
    private ScrapService scrapService;

    @Autowired
    private ForecastRepo forecastRepo;

    @PostMapping("/ContactUs")
    public ResponseEntity<String> submitContactForm(@RequestBody ContactUs contactUs) {
        try {
            contactUsService.saveContactForm(contactUs);
            return new ResponseEntity<>("Thank you for contacting us. We will get back to you shortly.",
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("There was an error processing your request. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/CurrencyCodeAndFlags")
    public List<CurrencyCodeAndFlagsDTO> getCurrencyCodesAndCountryFlags() {
        return currencyService.getCurrencyCodesAndCountryFlags();
    }

    @GetMapping("/fetch-currencies")
    public String FetchAndStoreCurrencyData() {
        int kgf = currencyService.fetchAndStoreCurrencyData();
        return "Currencies fetched and stored successfully " + kgf;
    }

    @GetMapping("/FetchExchangeRates")
    // @Scheduled(cron = "0 0 * * * ?")
    public String fetchAndStoreCurrency() {

        Long maxId = exchangeRateRepo.findExRateMaxId();

        ExRateMaxId maxIdEntity = new ExRateMaxId();
        maxIdEntity.setMaxId(maxId);
        maxIdRepository.save(maxIdEntity);
        try {

            Thread.sleep(2000);
            // Call the first service method
            taptapSendService.storingTaptapSendData();

            // Add a delay of 2 seconds (2000 milliseconds)
            Thread.sleep(2000);

            // Call the second service method
            lemfiService.storingLemfiData();

            // Add a delay of 2 seconds
            Thread.sleep(2000);

            // Call the third service method
            exchangeRateService.fetchAndStoreExchangeRates();

        } catch (InterruptedException e) {
            // Handle interrupted exception
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.out.println("Sleep was interrupted.");
        }

        return "Exchange Rate Table and Platform table are updated successfully on railway";
    }

    // @GetMapping("/bySourceAndTarget")
    // public List<ExchangeRate> getCurrenciesBySourceAndTarget(@RequestParam String
    // source, @RequestParam String target) {
    // return exchangeRateService.getExRateBySourceAndTarget(source, target);
    // }

    @GetMapping("/ExchangeRateConversion")
    public List<ExchangeRateQueryResultHold> getExchangeRatesBySourceAndTarget(@RequestParam String source,
            @RequestParam String target) {
        return exchangeRateService.getLatestExchangeRates(source, target);
    }

    @GetMapping("/HistoricalRates")
    public List<ExchangeRateQueryResultHold> historicalData(@RequestParam LocalDate datePattern,
            @RequestParam String source,
            @RequestParam String target) {
        return exchangeRateRepoEM.findHistoricalExchangeRates(datePattern, source, target);

    }

    // @GetMapping("/scrap_rates")
    // public List<Double> scrapData() {
    // return scrapService.forecastRates("EUR-to","INR");

    // }

    @GetMapping("/fetching-forecast-data")
    public List<ForecastExchangeRate> fetchingForecastData(@RequestParam String currencyPair) {
        List<ForecastExchangeRate> forecastList = forecastRepo.gettingAllForecastData(currencyPair);

        return forecastList;
    }

    @GetMapping("/storing-forecast_data")
    // @Scheduled(cron = "0 0 * * * ?")
    public String fetchForecast() {

        forecastRepo.deleteOldForecasts();

        List<String> currencyPairs = List.of(
                "GBP to PKR",
                "GBP to INR",
                "GBP to NGN",
                "EUR to PKR",
                "EUR to NGN",
                "EUR to INR");

        String fromCur;
        String toCur;

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

            List<Double> rates = scrapService.forecastRates(fromCur, toCur);

            IntStream.range(1, 16).forEach(i -> {
                LocalDate targetDate = LocalDate.now().plusDays(i);
                forecastExchangeRateService.fetchAndStoreForecast(currencyPair, targetDate, rates.get(i - 1));
            });

            // Call the service method to fetch and store forecast data

        }
        // Return a success message
        return "Forecast data fetched and saved successfully!";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runTaskOnStartup() {
        // fetchAndStoreCurrency();
    }

    // @GetMapping("/fetch-taptapsendrates")
    // public String fetchTaptapSendRates() {
    // taptapSendService.storingTaptapSendData();
    // return "Exchange Rate Table and Platform table are updated successfully";
    // }

    // @GetMapping("/fetch-lamfi")
    // public String lemfiData() {
    // lemfiService.storingLemfiData();
    // return "Lemfi done";
    // }

}
