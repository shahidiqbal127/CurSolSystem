package uniproj.cursol.api.exchangerateentities;

import java.util.ArrayList;

public class Root {
    public String sourceCurrency;
    public String targetCurrency;
    public Object sourceCountry;
    public Object targetCountry;
    public Object providerCountry;
    public ArrayList<String> providerTypes;
    public double sendAmount;
    public Object providerType;
    public ArrayList<Provider> providers;
}
