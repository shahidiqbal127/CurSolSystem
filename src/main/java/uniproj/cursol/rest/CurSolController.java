package uniproj.cursol.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import org.checkerframework.checker.units.qual.t;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uniproj.cursol.dao.ExchangeRateRepo;
import uniproj.cursol.dao.ExchangeRateReopEM;
import uniproj.cursol.dao.MaxIdRepository;
import uniproj.cursol.dto.CurrencyCodeAndFlagsDTO;
import uniproj.cursol.entity.ContactUs;
import uniproj.cursol.entity.ExRateMaxId;
import uniproj.cursol.entity.ExchangeRate;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHold;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHoldNative;
import uniproj.cursol.service.TaptapSendService.TaptapSendService;
import uniproj.cursol.service.contactusservice.ContactUsService;
import uniproj.cursol.service.currencyservice.CurrencyService;
import uniproj.cursol.service.exchangerateservice.ExchangeRateService;
import uniproj.cursol.service.lemfiservice.LemfiService;

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
    private ExchangeRateRepo exchangeRateRepo;

    @Autowired
    private ExchangeRateReopEM exchangeRateRepoEM;

    @Autowired
    private ContactUsService contactUsService;

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
    @Scheduled(cron = "0 0 * * * ?")
    public String fetchAndStoreCurrency() {

        Long maxId = exchangeRateRepo.findExRateMaxId();

        ExRateMaxId maxIdEntity = new ExRateMaxId();
        maxIdEntity.setMaxId(maxId);
        maxIdRepository.save(maxIdEntity);

        exchangeRateService.fetchAndStoreExchangeRates();
        taptapSendService.storingTaptapSendData();
        lemfiService.storingLemfiData();
        // Get the maximum ID after insertion

        return "Exchange Rate Table and Platform table are updated successfully";
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