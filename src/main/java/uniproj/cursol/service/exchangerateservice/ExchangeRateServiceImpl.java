package uniproj.cursol.service.exchangerateservice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import uniproj.cursol.api.exchangerateentities.Root;
import uniproj.cursol.dao.CurrencyRepo;
import uniproj.cursol.dao.ExchangeRateRepo;
import uniproj.cursol.dao.MaxIdRepository;
import uniproj.cursol.dao.PlatformRepo;
import uniproj.cursol.dto.exchangerateDTOs.DeliveryDateDTO;
import uniproj.cursol.dto.exchangerateDTOs.DeliveryEstimationDTO;
import uniproj.cursol.dto.exchangerateDTOs.LogosDTO;
import uniproj.cursol.dto.exchangerateDTOs.ProviderDTO;
import uniproj.cursol.dto.exchangerateDTOs.QuoteDTO;
import uniproj.cursol.dto.exchangerateDTOs.RootDTO;
import uniproj.cursol.entity.ExchangeRate;
import uniproj.cursol.entity.Platform;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHold;
import uniproj.cursol.querydtos.ExchangeRateQueryResultHoldNative;
import uniproj.cursol.service.platformservice.PlatformService;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ExchangeRateRepo exchangeRateRepo;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PlatformRepo platformRepo;

    @Autowired
    private CurrencyRepo currencyRepo;

    @Autowired
    private MaxIdRepository maxIdRepository;

    private final String comparisonApiUrl = "https://api.transferwise.com/v3/comparisons/";

    public static Map<String, Integer> platformMap = new HashMap<>();

    @Override
    public void fetchAndStoreExchangeRates() {

        String[] sourceCur = { "GBP", "EUR" };
        List<String> targetCur = currencyRepo.findAllCurrencyCodes();
        // String[] targetCur = { "BDT", "PKR", "AED" };

        for (String src : sourceCur) {

            for (String trgt : targetCur) {

                fetchDataAndSave(src, trgt, 500);

            }
        }

    }

    private void fetchDataAndSave(String sourceCurrency, String targetCurrency, double sendAmount) {

        String apiUrlWithParams = buildApiUrl(sourceCurrency, targetCurrency, sendAmount);

        String response = webClient.get()
                .uri(apiUrlWithParams)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        Root root;
        try {
            root = objectMapper.readValue(response, Root.class);
        } catch (Exception e) {
            // Handle JSON parsing error
            e.printStackTrace();
            return;
        }

        RootDTO rootDTO = mapToDTO(root);

        platformService.storingPlatformData(rootDTO);

        List<Platform> platforms = platformRepo.findAll();

        for (Platform platform : platforms) {
            platformMap.put(platform.getPlatformName(), platform.getPlatformId());
        }

        saveToDatabase(rootDTO);

    }

    @Transactional
    private void saveToDatabase(RootDTO rootDTO) {

        for (ProviderDTO providerDTO : rootDTO.providers) {
            for (QuoteDTO quoteDTO : providerDTO.quotes) {
                // if (!dataExists(rootDTO.sourceCurrency, rootDTO.targetCurrency,
                // quoteDTO.rate, quoteDTO.fee)) {
                ExchangeRate exchangeRate = new ExchangeRate();
                // Map data from providerDTO to exchangeRate
                exchangeRate.setPlatform(platformMap.get(providerDTO.name));// putting the platform id in exchange rate
                                                                            // table
                System.out.println(providerDTO.name);
                exchangeRate.setFromCurrency(rootDTO.sourceCurrency);
                exchangeRate.setToCurrency(rootDTO.targetCurrency);
                exchangeRate.setRate(quoteDTO.rate);
                exchangeRate.setDeliveryFee(quoteDTO.fee);
                exchangeRate.setEstimatedDeliveryTime(quoteDTO.deliveryEstimation != null
                        && quoteDTO.deliveryEstimation.duration != null
                                ? quoteDTO.deliveryEstimation.duration.min + " - "
                                        + quoteDTO.deliveryEstimation.duration.max // 2
                                : null);
                exchangeRate.setLastUpdated(new Date());

                exchangeRateRepo.save(exchangeRate);
            }
        }
        // }
    }

    private String buildApiUrl(String sourceCurrency, String targetCurrency, double sendAmount) {
        return UriComponentsBuilder.fromHttpUrl(comparisonApiUrl)
                .queryParam("sourceCurrency", sourceCurrency)
                .queryParam("targetCurrency", targetCurrency)
                .queryParam("sendAmount", sendAmount)
                .toUriString(); 
    }

    private RootDTO mapToDTO(Root root) {
        RootDTO rootDTO = new RootDTO();
        rootDTO.sourceCurrency = root.sourceCurrency;
        rootDTO.targetCurrency = root.targetCurrency;
        rootDTO.providers = root.providers.stream().map(provider -> {
            ProviderDTO providerDTO = new ProviderDTO();

            providerDTO.name = provider.name;

            LogosDTO logosDTO = new LogosDTO();
            logosDTO.svgUrl = provider.logos != null && provider.logos.normal != null ? provider.logos.normal.svgUrl
                    : null;
            providerDTO.logos = logosDTO;

            providerDTO.quotes = provider.quotes.stream().map(quote -> {
                QuoteDTO quoteDTO = new QuoteDTO();
                quoteDTO.rate = quote.rate;
                quoteDTO.fee = quote.fee;

                DeliveryEstimationDTO deliveryEstimationDTO = new DeliveryEstimationDTO();
                DeliveryDateDTO deliveryDateDTO = new DeliveryDateDTO();
                if (quote.deliveryEstimation != null && quote.deliveryEstimation.duration != null) { // 4
                    deliveryDateDTO.min = quote.deliveryEstimation.duration.min;
                    deliveryDateDTO.max = quote.deliveryEstimation.duration.max;
                }
                deliveryEstimationDTO.duration = deliveryDateDTO; // 3
                quoteDTO.deliveryEstimation = deliveryEstimationDTO;

                return quoteDTO;
            }).collect(Collectors.toList());

            return providerDTO;
        }).collect(Collectors.toList());

        return rootDTO;
    }

    @Override
    public List<ExchangeRate> getExRateBySourceAndTarget(String sourceCurrency, String targetCurrency) {
        // TODO Auto-generated method stub
        return exchangeRateRepo.getExRateBySourceAndTarget(sourceCurrency, targetCurrency);
    }

    @Override
    public List<ExchangeRateQueryResultHold> getLatestExchangeRates(String sourceCurrency, String targetCurrency) {
        Long lastMaxId = maxIdRepository.getExRateMaxId();

        // Query the latest exchange rates using the stored max ID
        return exchangeRateRepo.findLatestExchangeRates(lastMaxId, sourceCurrency, targetCurrency);
    }

    @Override
    public List<ExchangeRateQueryResultHoldNative> findHistoricalExchangeRates(String datePattern,
            String sourceCurrency,
            String targetCurrency) {
        // TODO Auto-generated method stub

        return exchangeRateRepo.findHistoricalExchangeRates(datePattern, sourceCurrency, targetCurrency);
    }

    // @Override
    // public List<ExchangeRateQueryResultHold>
    // getExchangeRatesBySourceAndTarget(String sourceCurrency,
    // String targetCurrency) {

    // return exchangeRateRepo.findBySourceAndTargetCurrency(sourceCurrency,
    // targetCurrency);

    // }

}
