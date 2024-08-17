package uniproj.cursol.dto.currencyDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyCountryDTO {

    @JsonProperty("iso3")
    private String iso3;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("name")
    private String countryName;

    public CurrencyCountryDTO() {
    }

    public CurrencyCountryDTO(String iso3, String currency, String countryName) {
        this.iso3 = iso3;
        this.currency = currency;
        this.countryName = countryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso2) {
        this.iso3 = iso2;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
