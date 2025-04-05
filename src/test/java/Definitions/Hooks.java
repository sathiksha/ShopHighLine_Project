package Definitions;

import Utils.HelperClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import java.awt.AWTException;

public class Hooks {
	public static WebDriver driver;

	@BeforeTest(alwaysRun = true)
	public void setUpTest(ITestContext context) throws AWTException {
		String testName = context.getCurrentXmlTest().getName();
		String browser = context.getCurrentXmlTest().getParameter("browser");
		String resolution = context.getCurrentXmlTest().getParameter("resolution");

		System.out.println("Starting Test: " + testName + " | Browser=" + browser + ", Resolution=" + resolution);
		System.clearProperty("browser");
		System.clearProperty("resolution");
		System.setProperty("browser", browser);
		System.setProperty("resolution", resolution);

		HelperClass.quitDriver();
		HelperClass.initialize();
		driver = HelperClass.getDriver();
	}

	@AfterTest(alwaysRun = true)
	public void tearDownTest(ITestContext context) {
		System.out.println("Completed Test: " + context.getCurrentXmlTest().getName());
		HelperClass.quitDriver();
	}

	@Before
	public void beforeScenario() {
		driver = HelperClass.getDriver();
		System.out.println("Running scenario with Browser=" + System.getProperty("browser") + ", Resolution="
				+ System.getProperty("resolution"));
	}

	@After
	public void afterScenario() {
		System.out.println("Closing the browser after scenario");
		HelperClass.quitDriver();
	}
}