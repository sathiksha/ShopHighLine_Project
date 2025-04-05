package Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/Features",
    glue = {"StepDefinitions", "TestCases", "Definitions"},
    plugin = {
        "pretty",
        "html:target/cucumber-html-report",
        "json:target/cucumber.json",
        "rerun:target/failed_scenarios.txt"
    },
    monochrome = true
)
public class RunnerTest extends AbstractTestNGCucumberTests {
}
