package integration.setup;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/features",
        glue = { "classpath:integration.stepdefinitions" })
@ActiveProfiles("test")
public class CucumberTests extends SpringIntegrationTests {
}
