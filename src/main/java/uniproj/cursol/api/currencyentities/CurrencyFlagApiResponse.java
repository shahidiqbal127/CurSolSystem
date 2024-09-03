package uniproj.cursol.api.currencyentities;

import java.util.List;
import uniproj.cursol.dto.currencyDTOs.CurrencyFlagDTO;

public class CurrencyFlagApiResponse {
    private boolean error;
    private String msg;
    private List<CurrencyFlagDTO> data;

    public CurrencyFlagApiResponse() {
    }

    public CurrencyFlagApiResponse(boolean error, String msg, List<CurrencyFlagDTO> data) {
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

    public List<CurrencyFlagDTO> getData() {
        return data;
    }

    public void setData(List<CurrencyFlagDTO> data) {
        this.data = data;
    }

    // getters and setters
}
