package integration.setup;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/features",
        glue = { "classpath:integration.stepdefinitions" })
public class CucumberTests extends SpringIntegrationTests {
}
