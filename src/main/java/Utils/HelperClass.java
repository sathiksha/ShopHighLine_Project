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

    private static HelperClass instance;
    private static WebDriver driver;
    public static Properties properties;
    public static int TIMEOUT = 30;
    public static int PollingTime = 100;

    private HelperClass() throws AWTException {
        try {
            loadProperties();
            initializeDriver();
        } catch (Exception e) {
            handleException(e);
        }
    }

    public static HelperClass getInstance() throws AWTException {
        if (instance == null) {
            instance = new HelperClass();
        }
        return instance;
    }

    private void loadProperties() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("config.properties");
        properties = new Properties();
        properties.load(fileInputStream);
    }

    @SuppressWarnings("deprecation")
	private void initializeDriver() throws Exception {
        String environment = properties.getProperty("environment", "local");
        String url = properties.getProperty("URL");

        if (environment.equalsIgnoreCase("browserstack")) {
            System.out.println("Running on BrowserStack cloud...");

            String username = properties.getProperty("username");
            String accessKey = properties.getProperty("access_key");

            MutableCapabilities caps = new MutableCapabilities();
            caps.setCapability("browserName", properties.getProperty("browser", "Chrome"));
            caps.setCapability("browserVersion", properties.getProperty("browser_version", "latest"));

            MutableCapabilities bstackOptions = new MutableCapabilities();
            bstackOptions.setCapability("os", properties.getProperty("os", "Windows"));
            bstackOptions.setCapability("osVersion", properties.getProperty("os_version", "11"));
            bstackOptions.setCapability("resolution", properties.getProperty("resolution", "1920x1080"));
            bstackOptions.setCapability("sessionName", "Shop HighLine Execution");
            bstackOptions.setCapability("buildName", "ShopHighLine Automation Build");
            bstackOptions.setCapability("local", "false");
            bstackOptions.setCapability("seleniumVersion", "4.17.0");

            caps.setCapability("bstack:options", bstackOptions);

            driver = new RemoteWebDriver(
                new URL("https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"),
                caps
            );

        } else {
            System.out.println("Running locally...");

            String browser = properties.getProperty("Chromebrowser", "chrome");

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driver = new EdgeDriver(edgeOptions);
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.get(url);
    }


    public static WebDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public WebDriverWait createWebDriverWait(Duration timeoutSeconds) {
        return new WebDriverWait(driver, timeoutSeconds);
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
