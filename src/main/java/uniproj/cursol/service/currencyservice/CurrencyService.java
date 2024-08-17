package uniproj.cursol.service.currencyservice;

import java.util.List;

import uniproj.cursol.dto.CurrencyCodeAndFlagsDTO;

public interface CurrencyService {

    int fetchAndStoreCurrencyData();

    List<CurrencyCodeAndFlagsDTO> getCurrencyCodesAndCountryFlags();

}
