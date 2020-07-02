package integration.stepdefs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import integration.setup.HttpHelper;
import integration.setup.ResponseResults;
import integration.setup.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CallSteps extends HttpHelper {

    @Given("^the user logs in with username '(.+)' and password '(.+)'$")
    public void the_client_logs_in_with_username_password(String username, String password) throws IOException {
        Map<String, String> map = new HashMap<>();

        map.put("username", username);
        map.put("password", password);

        ResponseResults responseResults = this.call(
                HttpMethod.GET,
                "authenticate",
                Collections.emptyMap(),
                map);

        TestContext.CONTEXT.addAuthorization(Objects.requireNonNull(responseResults.getHttpHeaders().get("Authorization")).get(0));
    }

    @When("^the client pings the server$")
    public void the_client_issues_POST_hello() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", TestContext.CONTEXT.getAuth());

        ResponseResults responseResults = this.call(HttpMethod.GET, "", map, Collections.emptyMap());

        TestContext.CONTEXT.addResult("Hello", responseResults);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) {
        ResponseResults responseResults = TestContext.CONTEXT.getResult("Hello");

        int currentStatusCode = responseResults.getCode();
        assertEquals("status code is incorrect : " + responseResults.getBody(), statusCode, currentStatusCode);
    }

    @And("^the client receives the message '(.+)'$")
    public void the_client_receives_a_message(String message) throws Throwable {
        ResponseResults responseResults = TestContext.CONTEXT.getResult("Hello");

        assertThat(responseResults.getBody(), is(message));
    }
}
