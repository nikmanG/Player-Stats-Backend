package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import io.github.nikmang.playerinfo.services.DuelService;
import io.github.nikmang.playerinfo.services.ExternalApiService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Controller
@RequestMapping("/duel")
public class DuelController {

    private DuelService duelService;
    private ExternalApiService externalApiService;

    public DuelController(DuelService duelService, ExternalApiService externalApiService) {
        this.duelService = duelService;
        this.externalApiService = externalApiService;
    }

    @PostMapping("record")
    public ResponseEntity<DuelMatch> addMatch(@RequestBody MatchRecord matchRecord) {
        return ResponseEntity
                .ok()
                .body(duelService.recordMatch(matchRecord.winner, matchRecord.loser));
    }

    @GetMapping("all")
    public ResponseEntity<List<DuelPlayer>> getPlayers() {
        List<DuelPlayer> getPlayers = duelService.getAllPlayers();

        Map<String, String> mappings = externalApiService.getPlayerNames(getPlayers
                .stream()
                .map(DuelPlayer::getPlayer)
                .collect(Collectors.toList()));

        for(DuelPlayer pl : getPlayers) {
            pl.getPlayer().setName(mappings.get(pl.getPlayer().getUuid()));
        }

        return ResponseEntity
                .ok()
                .body(getPlayers);
    }

    @GetMapping("all_teams")
    public ResponseEntity<List<Team>> getTeams() {
        return ResponseEntity
                .ok()
                .body(duelService.getTeams());
    }

    @GetMapping("profile")
    public ResponseEntity<DuelPlayer> getPlayerData(@RequestParam String uuid) {
        DuelPlayer pl = duelService.getPlayerProfile(uuid);
        pl.getPlayer().setName(externalApiService.getPlayerName(uuid));

        return ResponseEntity
                .ok()
                .body(pl);
    }

    @GetMapping("find/{playerId}")
    public ResponseEntity<DuelPlayer> getPlayerById(@PathVariable long playerId) {
        DuelPlayer duelPlayer = duelService.getPlayerProfile(playerId);

        if(duelPlayer == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        return ResponseEntity
                .ok()
                .body(duelPlayer);
    }

    @GetMapping("history")
    public ResponseEntity<List<DuelMatch>> getMatchHistory(@RequestParam long id, @RequestParam boolean winsOnly) {
        if(winsOnly) {
            return ResponseEntity
                    .ok()
                    .body(duelService.retrieveAllWonMatchesForPlayer(id));
        }

        return ResponseEntity
                .ok()
                .body(duelService.retrieveAllMatchesForPlayer(id));
    }

    @Data
    static class MatchRecord {
        String winner;
        String loser;
    }

}