package uniproj.cursol.service.scrapservice;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

@Service
public class ScrapServiceImpl implements ScrapService {

    private String baseUrl = "https://30rates.com/%s-%s";

    @Override
    public List<Double> forecastRates(String sourceCur, String targetCur) {

        List<Double> rates = new ArrayList<>();

        WebDriver driver = new ChromeDriver();

        try {

            String url = String.format(baseUrl, sourceCur.toLowerCase(), targetCur.toLowerCase());

            // Navigate to the webpage containing the table
            driver.get(url); // Replace with the actual URL

            // Use WebDriverWait to wait for the table to be present
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.tbh")));

            // Locate the table and extract all rows
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            // Define regex pattern for extracting numbers
            Pattern ratePattern = Pattern.compile("\\d+");

            // Iterate through each row and extract the rate
            for (int i = 1; i < 16; i++) { // Skip header row if present
                WebElement row = rows.get(i);
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() >= 5) {
                    WebElement strongElement = cells.get(4).findElement(By.cssSelector("strong"));

                    // Use regex to extract the number from the HTML
                    String strongElementHtml = strongElement.getAttribute("outerHTML");
                    Matcher matcher = ratePattern.matcher(strongElementHtml);

                    if (matcher.find()) {
                        String rateText = matcher.group();
                        try {
                            // Convert rateText to Double and add to the list
                            double rate = Double.parseDouble(rateText);
                            rates.add(rate);
                        } catch (NumberFormatException e) {
                            // Handle parsing error if needed
                            System.err.println("Failed to parse rate: " + rateText);
                        }
                    }
                }
            }
        } finally {
            // Close the browser
            driver.quit();
        }

        return rates;

    }

}
