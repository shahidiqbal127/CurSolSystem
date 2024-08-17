package uniproj.cursol.service.currencyservice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import uniproj.cursol.api.currencyentities.CurrencyCountryApiResponse;
import uniproj.cursol.api.currencyentities.CurrencyFlagApiResponse;
import uniproj.cursol.dao.CurrencyRepo;
import uniproj.cursol.dto.CurrencyCodeAndFlagsDTO;
import uniproj.cursol.dto.currencyDTOs.CurrencyCountryDTO;
import uniproj.cursol.dto.currencyDTOs.CurrencyDTO;
import uniproj.cursol.dto.currencyDTOs.CurrencyFlagDTO;
import uniproj.cursol.entity.DummyCurrency;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyRepo currencyRepo;

    @Autowired
    private WebClient webClient;

    private static final String CURRENCY_API_URL = "https://api.sandbox.transferwise.tech/v1/currencies";
    private static final String COUNTRY_API_URL = "https://countriesnow.space/api/v0.1/countries/currency";
    private static final String FLAG_API_URL = "https://countriesnow.space/api/v0.1/countries/flag/images";

    @Override
    public int fetchAndStoreCurrencyData() {

        // Fetch data from Currency API
        CurrencyDTO[] currencyDTOs = webClient.get()
                .uri(CURRENCY_API_URL)
                .retrieve()
                .bodyToMono(CurrencyDTO[].class)
                .block();

        List<String> existingCurrencyCodes = currencyRepo.findAllCurrencyCodes();

        if (currencyDTOs != null) {
            // Extract currency codes
            List<String> currencyCodes = Arrays.stream(currencyDTOs)
                    .map(CurrencyDTO::getCurrencyCode)
                    .collect(Collectors.toList());

            System.out.println("lemght **************" + currencyDTOs.length);

            // Fetch data from Country API

            CurrencyCountryApiResponse countryApiResponse = webClient.get()
                    .uri(COUNTRY_API_URL)
                    .retrieve()
                    .bodyToMono(CurrencyCountryApiResponse.class)
                    .block();

            CurrencyFlagApiResponse flagApiResponse = webClient.get()
                    .uri(FLAG_API_URL)
                    .retrieve()
                    .bodyToMono(CurrencyFlagApiResponse.class)
                    .block();

            if (countryApiResponse != null && countryApiResponse.getData() != null && currencyDTOs != null
                    && flagApiResponse != null && flagApiResponse.getData() != null) {
                // Convert CountryDTO to a Map for quick lookup

                Map<String, CurrencyCountryDTO> countryMap = countryApiResponse.getData().stream()
                        .filter(country -> currencyCodes.contains(country.getCurrency()))
                        .collect(Collectors.toMap(
                                CurrencyCountryDTO::getCurrency,
                                country -> country,
                                (existing, replacement) -> existing)); // keep the first encountered value

                List<String> iso3Codes = countryApiResponse.getData().stream()
                        .map(CurrencyCountryDTO::getIso3)
                        .collect(Collectors.toList());

                if (flagApiResponse != null && flagApiResponse.getData() != null) {

                    Map<String, CurrencyFlagDTO> dummyFlagMap = flagApiResponse.getData().stream()
                            .filter(flag -> iso3Codes.contains(flag.getIso3()))
                            .collect(Collectors.toMap(
                                    CurrencyFlagDTO::getIso3, // or .getIso3 if that is the common field
                                    flag -> flag,
                                    (existing, replacement) -> existing));

                    List<DummyCurrency> currencies = Arrays.stream(currencyDTOs)
                            .map(currencyDTO -> {

                                CurrencyCountryDTO countryDTO = countryMap.get(currencyDTO.getCurrencyCode());

                                if (countryDTO != null) {

                                    CurrencyFlagDTO dummyFlagDTO = dummyFlagMap.get(countryDTO.getIso3());

                                    if (dummyFlagDTO != null) {
                                        DummyCurrency currency = new DummyCurrency();
                                        currency.setCurrencyCode(currencyDTO.getCurrencyCode());
                                        currency.setCurrencyName(currencyDTO.getName());
                                        currency.setCountryCodeIso3(countryDTO.getIso3());
                                        currency.setCountryName(countryDTO.getCountryName());
                                        currency.setCountryFlag(dummyFlagDTO.getCountryFlag());

                                        if (!existingCurrencyCodes.contains(currency.getCurrencyCode())) {
                                            return currency;
                                        }

                                    }

                                }

                                return null;
                            })
                            .filter(currency -> currency != null) // filter out null entries
                            .collect(Collectors.toList());

                    if (!currencies.isEmpty()) {
                        currencyRepo.saveAll(currencies);
                    }
                }
            }
        }

        return currencyDTOs.length;

    }

    @Override
    public List<CurrencyCodeAndFlagsDTO> getCurrencyCodesAndCountryFlags() {
        return currencyRepo.findAll()
                .stream()
                .map(currency -> new CurrencyCodeAndFlagsDTO(currency.getCurrencyCode(), currency.getCountryFlag()))
                .collect(Collectors.toList());
    }

}
