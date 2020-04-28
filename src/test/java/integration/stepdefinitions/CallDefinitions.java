package integration.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import integration.setup.SpringIntegrationTests;
import org.springframework.http.HttpStatus;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallDefinitions extends SpringIntegrationTests {
    @When("^the client pings the server$")
    public void the_client_issues_POST_hello() throws Throwable {
        executePost("http://localhost:8080");
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
    }

    @And("^the client receives the message '(.+)'$")
    public void the_client_receives_a_message(String message) throws Throwable {
        assertThat(latestResponse.getBody(), is(message));
    }

}
