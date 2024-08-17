package uniproj.cursol.service.lemfiservice;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uniproj.cursol.dao.ExchangeRateRepo;
import uniproj.cursol.entity.ExchangeRate;

import org.openqa.selenium.WebDriver;

@Service
public class LemfiServiceImpl implements LemfiService {

    public static Map<String, String> countryCurrencyMap = new HashMap<>();

    @Autowired
    private ExchangeRateRepo exchangeRateRepo;

    @Override
    public void storingLemfiData() {

        WebDriver driver = new ChromeDriver();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://lemfi.com/gb/international-money-transfer");

        acceptCookies(driver, wait);

        List<WebElement> optionsList = gettingSupportedCurrencies(driver);

        List<String> currencyCodes = new ArrayList<>();

        for (WebElement option : optionsList) {
            WebElement countryElement = option.findElement(By.cssSelector("p.base-text--size-small--bold"));
            WebElement currencyElement = option.findElement(By.cssSelector("p.base-text--size-x-small"));

            String country = countryElement.getText();
            String currency = currencyElement.getText();

            String currencyCode = currency.split(" ")[0];
            if (!countryCurrencyMap.containsValue("EUR")) {
                countryCurrencyMap.put(country, currencyCode);
            }

            addUniqueEntry(countryCurrencyMap, country, currencyCode);

            if (!currencyCodes.contains(currencyCode)) {
                currencyCodes.add(currencyCode);
            }

        }

        for (Map.Entry<String, String> entryS : countryCurrencyMap.entrySet()) {

            try {

                String xpath = String.format("//p[text()='%s']", entryS.getKey());

                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
                element.click();

                Thread.sleep(3000);
                WebElement detailsElement = wait.until(ExpectedConditions
                        .visibilityOfElementLocated(By.className("molecule-conversion-box__details")));

                // Extract the exchange rate
                WebElement exchangeRateElement = detailsElement
                        .findElement(By.xpath(".//span[contains(text(), 'Exchange rate')]/following-sibling::span"));
                String exRate = exchangeRateElement.getText().trim();

                String[] parts = exRate.split(" ");

                ExchangeRate exchangeRate = new ExchangeRate();

                // NumberFormat format = NumberFormat.getInstance(Locale.GERMANY); // Germany
                // uses comma as decimal separator
                // Number number = format.parse(parts[3]);
                // double parsedDouble = number.doubleValue();

                String numberWithoutComma = parts[3].replaceAll(",", "");
                double parsedDouble = Double.parseDouble(numberWithoutComma);

                exchangeRate.setRate(parsedDouble);
                exchangeRate.setPlatform(48);
                exchangeRate.setDeliveryFee(0.00);
                exchangeRate
                        .setEstimatedDeliveryTime("2024-06-25T23:59:59.999999999Z - 2024-06-26T00:04:59.999999999Z");
                exchangeRate.setFromCurrency("GBP");
                exchangeRate.setToCurrency(entryS.getValue());
                exchangeRate.setLastUpdated(new Date());

                exchangeRateRepo.save(exchangeRate);

                WebElement dropdownContainerRe = driver.findElement(By.xpath(
                        "//span[contains(@class, 'base-text') and contains(text(), '" + entryS.getValue() + "')]"));

                dropdownContainerRe.click();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        driver.close();

    }

    private List<WebElement> gettingSupportedCurrencies(WebDriver driver) {
        WebElement dropdownContainer = driver
                .findElement(By.xpath("//span[contains(@class, 'base-text') and contains(text(), 'EUR')]"));

        dropdownContainer.click();

        WebElement dropdown = driver.findElement(By.cssSelector("ul.base-dropdown-box"));

        List<WebElement> optionsList = dropdown.findElements(By.cssSelector("li.base-dropdown-item"));

        return optionsList;
    }

    private void acceptCookies(WebDriver driver, WebDriverWait wait) {
        try {
            // Wait until the cookie consent popup appears and locate the accept button
            WebElement acceptCookiesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(@class, 'base-button') and contains(., 'Accept all cookies')]")));

            // Click the accept button
            acceptCookiesButton.click();
        } catch (Exception e) {
            System.out.println("Cookie consent popup not found or already accepted.");
        }
    }

    private static void addUniqueEntry(Map<String, String> map, String key, String value) {
        if (!map.containsValue(value)) {
            map.put(key, value);
        }
    }

    public static Double extractExchangeRate(String input) {
        // Initialize the exchange rate
        Double exchangeRateValue = 0.00;

        // Split the string by spaces
        String[] parts = input.split(" ");
        if (parts.length >= 4) {
            // Remove any commas from the exchange rate part
            String exchangeRate = parts[3].replace(",", "");
            try {
                // Parse the exchange rate to a double
                exchangeRateValue = Double.parseDouble(exchangeRate);
                System.out.println("Parsed exchange rate: " + exchangeRateValue); // Debug statement
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for input: " + input);
            }
        } else {
            System.out.println("Invalid input format: " + input);
        }

        return exchangeRateValue;
    }

}
