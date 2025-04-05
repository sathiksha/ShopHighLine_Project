
package Runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/features", glue = {"Definitions","TestCases"}, tags = ("@SearchValidation"), plugin = {
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"json:test-output/cucumber.json", "rerun:target/rerun.txt" })
public class RunnerTest extends AbstractTestNGCucumberTests {

}
