package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerRepository playerRepository;

    private final TeamRepository teamRepository;

    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
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
        return playerRepository
                .findById(playerId)
                .map(p -> ResponseEntity.accepted().body(p))
                .orElse(ResponseEntity.notFound().build());
    }
}
