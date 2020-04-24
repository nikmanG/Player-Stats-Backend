package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.services.ExternalApiService;
import io.github.nikmang.playerinfo.services.TeamService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("find/{id}")
    public Team getIndividualTeam(@PathVariable long id) {
        Team team = teamService.getTeamById(id);

        externalApiService.getPlayerNames(team.getPlayers());

        return team;
    }
}
