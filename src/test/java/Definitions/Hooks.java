package Definitions;

import Utils.HelperClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.awt.AWTException;

import org.openqa.selenium.WebDriver;


public class Hooks {
    public static WebDriver driver;

    @Before
    public void setup() throws AWTException {
        driver = HelperClass.getInstance().getDriver();
    }
    
    @After
    public void tearDown() throws AWTException {
        System.out.println("Closing the browser");
        HelperClass.getInstance().quitDriver();
    }
}
