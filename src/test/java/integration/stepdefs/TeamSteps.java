package integration.stepdefs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import integration.setup.HttpHelper;
import integration.setup.ResponseResults;
import integration.setup.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.League;
import io.github.nikmang.playerinfo.models.Team;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamSteps extends HttpHelper {

    private ObjectMapper objectMapper = new ObjectMapper();
    private TypeFactory typeFactory = objectMapper.getTypeFactory();

    @Given("^the following teams exist$")
    public void the_following_teams_exist(DataTable dataTable) throws IOException {
        for(int i = 1; i < dataTable.height(); i++) {
            this.call(
                    HttpMethod.POST,
                    "team/private/add",
                    Map.of("Authorization", TestContext.CONTEXT.getAuth()),
                    Map.of("name", dataTable.cell(i, 0), "teamType", dataTable.cell(i, 1)));
        }
    }

    @Given("^the following leagues exist$")
    public void the_following_leagus_exist(DataTable dataTable) throws IOException {
        for(int i = 1; i < dataTable.height(); i++) {
            this.call(
                    HttpMethod.POST,
                    "team/private/add_league",
                    Map.of("Authorization", TestContext.CONTEXT.getAuth()),
                    Map.of("name", dataTable.cell(i, 0), "teamType", dataTable.cell(i, 1)));
        }
    }

    @When("^getting all available leagues for type '(.+)'$")
    public void when_getting_leagues_of_type(String type) throws IOException {
        ResponseResults results = this.call(
                HttpMethod.GET,
                "team/public/get_leagues",
                Collections.emptyMap(),
                Map.of("type", type));

        TestContext.CONTEXT.addResult("leagues", results);
    }

    @Then("^the following leagues are returned$")
    public void the_following_leagues_are_returned(DataTable dataTable) {
        ResponseResults responseResults = TestContext.CONTEXT.getResult("leagues");

        LeagueFixture[] leagues = new LeagueFixture[0];
        try {
            leagues = objectMapper.readValue(responseResults.getBody(), LeagueFixture[].class);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        Set<String> teamsInTable = new HashSet<>();

        for(int i = 1; i < dataTable.height(); i++) {
            teamsInTable.add(dataTable.cell(i, 0));
        }

        assertEquals(teamsInTable.size(), leagues.length);

        assertTrue(teamsInTable.containsAll(Arrays.stream(leagues).map(LeagueFixture::getName).collect(Collectors.toList())));
    }

    @Data
    private static class LeagueFixture {
        private String name;
        private TeamType leagueType;
        private long id;
    }
}
