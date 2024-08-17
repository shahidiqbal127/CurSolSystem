package uniproj.cursol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "currency")
public class DummyCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Integer currencyId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "country_code")
    private String countryCodeIso3;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_flag")
    private String countryFlag;

    


    // Getters and setters

    public DummyCurrency() {
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }


    public String getCountryCodeIso3() {
        return countryCodeIso3;
    }

    public void setCountryCodeIso3(String countryCodeIso3) {
        this.countryCodeIso3 = countryCodeIso3;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }

    
}
