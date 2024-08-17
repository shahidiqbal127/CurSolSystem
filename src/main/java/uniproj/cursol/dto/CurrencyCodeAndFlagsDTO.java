package uniproj.cursol.dto;

public class CurrencyCodeAndFlagsDTO {
    private String currencyCode;
    private String countryFlagUrl;

    // Constructor
    public CurrencyCodeAndFlagsDTO(String currencyCode, String countryFlagUrl) {
        this.currencyCode = currencyCode;
        this.countryFlagUrl = countryFlagUrl;
    }

    // Getters and Setters
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCountryFlagUrl() {
        return countryFlagUrl;
    }

    public void setCountryFlagUrl(String countryFlagUrl) {
        this.countryFlagUrl = countryFlagUrl;
    }
}
