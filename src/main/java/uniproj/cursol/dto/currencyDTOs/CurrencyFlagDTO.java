package uniproj.cursol.dto.currencyDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyFlagDTO {

    @JsonProperty("iso3")
    private String iso3;

    @JsonProperty("flag")
    private String countryFlag;


    public CurrencyFlagDTO() {
    }

    public CurrencyFlagDTO(String iso3, String currency) {
        this.iso3 = iso3;
        this.countryFlag = currency;
    }


    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso2) {
        this.iso3 = iso2;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    
}
