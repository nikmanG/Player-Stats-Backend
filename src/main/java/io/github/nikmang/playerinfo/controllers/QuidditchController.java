package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.services.QuidditchService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(
        allowCredentials = "true",
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@Controller
@RequestMapping("/quidditch")
public class QuidditchController {

    private final QuidditchService quidditchService;

    public QuidditchController(QuidditchService quidditchService) {
        this.quidditchService = quidditchService;
    }

    @PostMapping("private/record")
    public ResponseEntity<QuidditchMatch> addMatch(@RequestBody MatchRecord matchRecord) {
        return ResponseEntity
                .ok()
                .body(quidditchService.recordMatch(
                        matchRecord.winner,
                        matchRecord.loser,
                        matchRecord.winnerScore,
                        matchRecord.loserScore,
                        matchRecord.catcherUuid));
    }

    @GetMapping("public/all")
    public ResponseEntity<List<QuidditchTeam>> getTeams() {
        return ResponseEntity
                .ok()
                .body(quidditchService.getTeams());
    }

    @GetMapping("public/find/{teamId}")
    public ResponseEntity<QuidditchTeam> getTeamById(@PathVariable long teamId) {
        return ResponseEntity
                .ok()
                .body(quidditchService.getByTeamId(teamId));
    }

    @GetMapping("public/history")
    public ResponseEntity<List<QuidditchMatch>> getMatchHistory(@RequestParam long id) {
        return ResponseEntity
                .ok()
                .body(quidditchService.retrieveAllMatchesForTeam(id));
    }

    static class MatchRecord {
        String winner;
        String loser;
        String catcherUuid;
        long winnerScore;
        long loserScore;
    }
}
