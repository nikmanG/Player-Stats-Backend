package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.League;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.services.ExternalApiService;
import io.github.nikmang.playerinfo.services.TeamService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin(
        allowCredentials = "true",
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
@RequestMapping("/team")
public class TeamController {

    private TeamService teamService;
    private ExternalApiService externalApiService;

    public TeamController(TeamService teamService, ExternalApiService externalApiService) {
        this.teamService = teamService;
        this.externalApiService = externalApiService;
    }

    @PostMapping("private/add")
    public ResponseEntity<Team> addTeam(@RequestBody TeamWrapper teamWrapper) {
        return ResponseEntity
                .ok()
                .body(teamService.addTeam(teamWrapper.name, teamWrapper.teamType));
    }

    @PostMapping("private/add_league")
    public ResponseEntity<League> addLeague(@RequestBody TeamWrapper leagueWrapper) {
        return ResponseEntity
                .ok()
                .body(teamService.addLeague(leagueWrapper.name, leagueWrapper.teamType));
    }

    @PostMapping("private/join_team_league")
    public ResponseEntity<League> addTeamsToLeague(@RequestBody LeagueTeamWrapper leagueTeamWrapper) {
        League league = teamService.addTeamToLeague(leagueTeamWrapper.playerIds, leagueTeamWrapper.leagueId);

        if(league == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity
                .ok()
                .body(league);
    }

    @GetMapping("public/get_leagues")
    public ResponseEntity<List<League>> getLeaguesForType(@RequestParam TeamType type) {
        return ResponseEntity
        .ok()
        .body(teamService.getLeaguesForLeagueType(type));
    }

    @GetMapping("public/find/{id}")
    public Team getIndividualTeam(@PathVariable long id) {
        Team team = teamService.getTeamById(id);

        externalApiService.getPlayerNames(team.getPlayers());

        return team;
    }

    @GetMapping("public/player/{playerId}")
    public Map<TeamType, Team> getTeamsForPlayer(@PathVariable long playerId) {
        return teamService.getTeamsByPlayerId(playerId);
    }

    @Data
    static class LeagueTeamWrapper {
        long leagueId;
        List<Long> playerIds;
    }

    @Data
    static class TeamWrapper {
        String name;
        TeamType teamType;
    }
}
