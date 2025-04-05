package Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "@target/rerun.txt", glue = { "" }, plugin = {
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "json:test-output/cucumber.json", })
public class ReRunnerTest extends AbstractTestNGCucumberTests {

}