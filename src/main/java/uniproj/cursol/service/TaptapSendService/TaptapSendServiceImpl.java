package uniproj.cursol.service.TaptapSendService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uniproj.cursol.dao.ExchangeRateRepo;
import uniproj.cursol.entity.ExchangeRate;

@Service
public class TaptapSendServiceImpl implements TaptapSendService {

    
     public static Map<String, String> sourceCurMap = new HashMap<>();
     public static Map<String, String> targetCurMap = new HashMap<>();

    @Autowired
    private ExchangeRateRepo exchangeRateRepo;  

    @Override
    public void storingTaptapSendData() {
       
        
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.taptapsend.com/");

        sourceCurMap.put("GBP", "United Kingdom");
        sourceCurMap.put("EUR", "Belgium");

        WebElement sourceCur = driver.findElement(By.id("origin-currency"));

        WebElement targetCurr = driver.findElement(By.id("destination-currency"));

        String[] targetCurSplt = targetCurr.getText().split("\n");

        Pattern currencyPattern = Pattern.compile("\\((.*?)\\)");
        Pattern countryPattern = Pattern.compile("^(.*?) \\(");

        // putting the countryName and currencyCode in the map as key and value to lookup
        for (String entry : targetCurSplt) {
            Matcher countryMatcher = countryPattern.matcher(entry);
            Matcher currencyMatcher = currencyPattern.matcher(entry);
            
            if (countryMatcher.find() && currencyMatcher.find()) {
                String country = countryMatcher.group(1);
                String currencyCode = currencyMatcher.group(1);
                targetCurMap.put(currencyCode, country);
            }
        }

        targetCurMap.remove("FCFA");

        WebElement inp = driver.findElement(By.id("origin-amount"));
        inp.clear();

         for (Map.Entry<String, String> entryS : sourceCurMap.entrySet()) {
            for (Map.Entry<String, String> entryT : targetCurMap.entrySet()) {
                
                sourceCur.sendKeys(entryS.getValue());
                targetCurr.sendKeys(entryT.getValue());
                inp.sendKeys("1");

                inp.clear();


                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setRate(Double.parseDouble(driver.findElement(By.id("destination-amount")).getAttribute("value")));
                exchangeRate.setFromCurrency(entryS.getKey());
                exchangeRate.setToCurrency(entryT.getKey());
                exchangeRate.setDeliveryFee(0);
                exchangeRate.setPlatform(39);
                exchangeRate.setEstimatedDeliveryTime("PT0S");
                exchangeRate.setLastUpdated(new Date());

                exchangeRateRepo.save(exchangeRate);


            }
            
        }


        driver.close();


    }

    

}