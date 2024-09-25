package uniproj.cursol.service.scrapservice;

import java.util.List;

public interface ScrapService {

    List<Double> forecastRates(String sourceCur, String targetCur);

}
