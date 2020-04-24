package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.services.ExternalApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final ExternalApiService externalApiService;

    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository, ExternalApiService externalApiService) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.externalApiService = externalApiService;
    }

    @PostMapping("add")
    public ResponseEntity<Player> addPlayer(@Valid @RequestBody Player player) {
        return ResponseEntity
                .ok()
                .body(playerRepository.save(player));
    }

    @PostMapping("add_team")
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        return ResponseEntity
                .ok()
                .body(teamRepository.save(team));
    }

    @GetMapping("join")
    public void joinPlayerAndTeam(@RequestParam long playerId, @RequestParam long teamId) {
        Team t = teamRepository.getOne(teamId);
        Player p = playerRepository.getOne(playerId);

        p.getTeams().add(t);
        playerRepository.save(p);
    }

    @GetMapping("find/{playerId}")
    public ResponseEntity<Player> getPlayerById(@PathVariable long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if(player != null) {
            externalApiService.getPlayerName(player.getUuid());
        }

        return ResponseEntity
                .ok()
                .body(player);
    }
}
