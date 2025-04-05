package Utils;

import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
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
        } catch (IOException e) {
            handleException(e); 
        } catch (InterruptedException e) {
					e.printStackTrace();
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

   
	private void initializeDriver() throws AWTException, InterruptedException {
        String browser = properties.getProperty("Chromebrowser", "chrome");
        String url = properties.getProperty("URL");

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL); 
                driver = new ChromeDriver(chromeOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("–remote-allow-origins=*");
                driver = new EdgeDriver(edgeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                handleException(new IllegalArgumentException("Unsupported browser: " + browser)); 
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
        String customMessage = "";

        if (e instanceof NoSuchElementException) {
            customMessage = e.getClass().getSimpleName()
                    + " - When the WebDriver is unable to discover an element on a web page using the provided selector or location, Selenium throws an exception known as a NoSuchElementException";
        } else if (e instanceof TimeoutException) {
            customMessage = e.getClass().getSimpleName()
                    + " - When the WebDriver cannot execute a command or action within a predetermined amount of time, a timeout exception is thrown in Selenium";
        } else if (e instanceof ElementNotInteractableException) {
            customMessage = e.getClass().getSimpleName()
                    + " - An exception called ElementNotInteractableException is raised whenever a web page detects an element that is not able to be interacted with using the designated action";
        } else if (e instanceof ElementClickInterceptedException) {
            customMessage = e.getClass().getSimpleName()
                    + " - When a web element is present but the WebDriver is unable to click on it because another element is blocking or intercepting the click action, Selenium can throw the ElementClickInterceptedException exception";
        } else if (e instanceof NoSuchWindowException) {
            customMessage = e.getClass().getSimpleName()
                    + " - When the WebDriver is unable to switch to a window or frame that does not exist or that's no longer available, the NoSuchWindowException exception is thrown";
        } else if (e instanceof NoAlertPresentException) {
            customMessage = e.getClass().getSimpleName()
                    + " - When the WebDriver is unable to switch to a window or frame that does not exist or is no longer available, the NoSuchWindowException exception is thrown";
        } else if (e instanceof InvalidElementStateException) {
            customMessage = e.getClass().getSimpleName()
                    + " - Whenever the state of an element is not what is expected or is unable to allow the requested action to be done, an exception called InvalidElementStateException is raised";
        } else if (e instanceof WebDriverException) {
            customMessage = e.getClass().getSimpleName()
                    + " - Many unique circumstances, such as when the WebDriver is unable to connect to the browser, when there is a network issue, or when the browser unexpectedly crashes, can result in a WebDriverException";
        } else if (e instanceof StaleElementReferenceException) {
            customMessage = e.getClass().getSimpleName()
                    + " - When a reference to an element is no longer valid or \"stale,\" StaleElementReferenceException is the exception that is raised. This may occur if an element is removed from the DOM, deleted, or modified in some other manner";
        } else if (e instanceof IllegalArgumentException && e.getMessage().contains("setPageLoadStrategy")) {
            customMessage = "IllegalArgumentException occurred: Unable to set page load strategy. Ensure correct parameter type.";
        } else {
            customMessage = "An exception occurred: " + e.getClass().getSimpleName();
        }

        Assert.fail(customMessage);
        System.err.println(customMessage);
        e.printStackTrace();
        
    }
    
    public static void CommonWaitTime() throws InterruptedException {
        Thread.sleep(4000);
    }
}
