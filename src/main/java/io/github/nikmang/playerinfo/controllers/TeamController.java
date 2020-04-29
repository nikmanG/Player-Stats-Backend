package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.services.ExternalApiService;
import io.github.nikmang.playerinfo.services.TeamService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
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
                .body(teamService.addTeam(teamWrapper.name, TeamType.valueOf(teamWrapper.teamType.toUpperCase())));
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
    static class TeamWrapper {
        String name;
        String teamType;
    }
}
