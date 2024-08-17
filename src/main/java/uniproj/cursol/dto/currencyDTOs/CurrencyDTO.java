package uniproj.cursol.dto.currencyDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyDTO {

    @JsonProperty("code")
    private String currencyCode;

    private String name;


    public CurrencyDTO(String currencyCode, String name) {
        this.currencyCode = currencyCode;
        this.name = name;
    }

    public CurrencyDTO() {
    }

    // Getters and setters
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
}
