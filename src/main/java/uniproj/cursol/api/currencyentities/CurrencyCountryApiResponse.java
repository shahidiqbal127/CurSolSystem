package uniproj.cursol.api.currencyentities;

import java.util.List;

import uniproj.cursol.dto.currencyDTOs.CurrencyCountryDTO;

public class CurrencyCountryApiResponse {
    private boolean error;
    private String msg;
    private List<CurrencyCountryDTO> data;

    public CurrencyCountryApiResponse() {
    }

    public CurrencyCountryApiResponse(boolean error, String msg, List<CurrencyCountryDTO> data) {
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CurrencyCountryDTO> getData() {
        return data;
    }

    public void setData(List<CurrencyCountryDTO> data) {
        this.data = data;
    }

    // getters and setters
}
