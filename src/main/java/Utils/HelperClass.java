package Utils;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HelperClass {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties properties;
    public static int TIMEOUT = 30;
    public static int PollingTime = 100;

    public static void initialize() throws AWTException {
        try {
            loadProperties();
            initializeDriver();
        } catch (Exception e) {
            handleException(e);
        }
    }

    private static void loadProperties() throws IOException {
        if (properties == null) {
            FileInputStream fileInputStream = new FileInputStream("config.properties");
            properties = new Properties();
            properties.load(fileInputStream);
        }
    }

    @SuppressWarnings("deprecation")
    private static void initializeDriver() throws Exception {
          if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }

        String environment = properties.getProperty("environment", "local");
        String url = properties.getProperty("URL");

        if (environment.equalsIgnoreCase("browserstack")) {
            System.out.println("Running on BrowserStack cloud...");

            String username = properties.getProperty("username");
            String accessKey = properties.getProperty("access_key");

            if (username == null || accessKey == null || username.isEmpty() || accessKey.isEmpty()) {
                throw new IllegalStateException("BrowserStack username or access key is missing in config.properties");
            }

            MutableCapabilities caps = new MutableCapabilities();
            String browser = System.getProperty("browser", properties.getProperty("browser", "Chrome"));
            String resolution = System.getProperty("resolution", properties.getProperty("resolution", "1920x1080"));

            System.out.println("Browser: " + browser + ", Resolution: " + resolution);

            caps.setCapability("browserName", browser);
            caps.setCapability("browserVersion", properties.getProperty("browser_version", "latest"));

            MutableCapabilities bstackOptions = new MutableCapabilities();
            bstackOptions.setCapability("os", properties.getProperty("os", "Windows"));
            bstackOptions.setCapability("osVersion", properties.getProperty("os_version", "10"));
            bstackOptions.setCapability("resolution", resolution);
            bstackOptions.setCapability("sessionName", "Shop HighLine Execution - " + browser + " " + resolution);
            bstackOptions.setCapability("buildName", "ShopHighLine Automation Build");
            bstackOptions.setCapability("local", "false");
            bstackOptions.setCapability("seleniumVersion", "4.17.0");

            caps.setCapability("bstack:options", bstackOptions);

            driver.set(new RemoteWebDriver(
                new URL("https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),
                caps
            ));

        } else {
            System.out.println("Running locally...");
            String browser = properties.getProperty("browser", "chrome");

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    driver.set(new ChromeDriver(chromeOptions));
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driver.set(new EdgeDriver(edgeOptions));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }

        WebDriver currentDriver = driver.get();
        currentDriver.manage().window().maximize();
        currentDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        currentDriver.get(url);
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            try {
                initialize();
            } catch (AWTException e) {
                handleException(e);
            }
        }
        return driver.get();
    }

    public static void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove();
        }
    }

    public static WebDriverWait createWebDriverWait(Duration timeoutSeconds) {
        return new WebDriverWait(driver.get(), timeoutSeconds);
    }

    public static void handleException(Exception e) {
        System.err.println("Exception occurred: " + e.getMessage());
        e.printStackTrace();
        Assert.fail(e.getMessage());
    }

    public static void CommonWaitTime() throws InterruptedException {
        Thread.sleep(4000);
    }
}