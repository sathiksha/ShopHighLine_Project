package Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"Definitions", "TestCases"},
    tags = "@SearchValidation",
    plugin = {"pretty", "html:target/cucumber-reports.html"}
)
public class RunnerTest extends AbstractTestNGCucumberTests {
	
}

//local