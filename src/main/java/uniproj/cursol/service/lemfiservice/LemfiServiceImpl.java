package uniproj.cursol.service.lemfiservice;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import uniproj.cursol.dao.ExchangeRateRepo;
import uniproj.cursol.entity.ExchangeRate;
import org.openqa.selenium.WebDriver;

@Service
public class LemfiServiceImpl implements LemfiService {

    public static Map<String, String> countryCurrencyMap = new HashMap<>();

    @Autowired
    private ExchangeRateRepo exchangeRateRepo;

    private static final Logger logger = LoggerFactory.getLogger(LemfiServiceImpl.class);

    @Override
    @Transactional
    public void storingLemfiData() {

        System.out.println("Lemfi Scraping Service Started");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1280x800");
        options.addArguments("--disable-software-rasterizer");
        options.addArguments("--disable-extensions"); // Disable any extensions
        options.addArguments("--blink-settings=imagesEnabled=false"); // Disable images
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(35));

        driver.get("https://lemfi.com/gb/international-money-transfer");

        acceptCookies(driver, wait);

        List<WebElement> optionsList = gettingSupportedCurrencies(driver);

        List<String> currencyCodes = new ArrayList<>();

        try {

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

                    System.out.println("the County Map is " + entryS);

                    String xpath = String.format(
                            "//li[@class=\"base-dropdown-item\" and @role=\"option\"]//div[contains(@class, \"money-box__selector-option--list\") and .//p[text()='%s']]",
                            entryS.getKey());

                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

                    element.click();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    WebElement detailsElement = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(By.className("molecule-conversion-box__details")));

                    // Extract the exchange rate
                    WebElement exchangeRateElement = detailsElement
                            .findElement(
                                    By.xpath(".//span[contains(text(), 'Exchange rate')]/following-sibling::span"));
                    String exRate = exchangeRateElement.getText().trim();

                    String[] parts = exRate.split(" ");

                    ExchangeRate exchangeRate = new ExchangeRate();

                    String numberWithoutComma = parts[3].replaceAll(",", "");
                    double parsedDouble = Double.parseDouble(numberWithoutComma);

                    exchangeRate.setRate(parsedDouble);
                    exchangeRate.setPlatform(48);
                    exchangeRate.setDeliveryFee(0.00);
                    exchangeRate
                            .setEstimatedDeliveryTime(
                                    "2024-06-25T23:59:59.999999999Z - 2024-06-26T00:04:59.999999999Z");
                    exchangeRate.setFromCurrency("GBP");
                    exchangeRate.setToCurrency(entryS.getValue());
                    exchangeRate.setLastUpdated(new Date());

                    exchangeRateRepo.save(exchangeRate);

                    WebElement dropdownContainerRe = driver
                            .findElement(By.xpath("//div[@class='money-box__selector cursor-pointer']//span[text()='"
                                    + entryS.getValue() + "']"));

                    dropdownContainerRe.click();

                } catch (ElementClickInterceptedException e) {

                    // Capture screenshot on error
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    File destinationFile = new File(System.getProperty("user.home") + "/Desktop/screenshot.png"); // Path
                                                                                                                  // to
                                                                                                                  // Desktop

                    try {
                        FileUtils.copyFile(screenshot, destinationFile);
                        System.out.println("Screenshot saved to Desktop as 'screenshot.png'");
                    } catch (IOException ioe) {
                        System.out.println("Failed to save screenshot: " + ioe.getMessage());
                    }

                }
            }
        } finally {

            driver.quit();

        }

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
