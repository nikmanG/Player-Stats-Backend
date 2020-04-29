package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.services.ExternalApiService;
import io.github.nikmang.playerinfo.services.PlayerService;
import io.github.nikmang.playerinfo.services.TeamService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/player")
public class PlayerController {

    private final ExternalApiService externalApiService;
    private final PlayerService playerService;
    private final TeamService teamService;

    public PlayerController(
            ExternalApiService externalApiService,
            PlayerService playerService,
            TeamService teamService) {
        this.externalApiService = externalApiService;
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @PostMapping("private/add")
    public ResponseEntity<Player> addPlayer(@RequestBody UuidWrapper wrapper) {
        return ResponseEntity
                .ok()
                .body(playerService.addPlayer(wrapper.uuid));
    }

    @GetMapping("private/join")
    public ResponseEntity<Team> joinPlayerAndTeam(@RequestParam long playerId, @RequestParam long teamId) {
        Team t = teamService.getTeamById(teamId);
        Player p = playerService.getPlayer(playerId);

        teamService.addPlayerToTeam(t, p);

        return ResponseEntity.ok().build();
    }

    @GetMapping("public/find/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable long playerId) {
        Player player = playerService.getPlayer(playerId);

        if(player != null) {
            externalApiService.getPlayerName(player.getUuid());
        }

        return ResponseEntity
                .ok()
                .body(player);
    }

    @Data
    static class UuidWrapper {
        String uuid;
    }
}
