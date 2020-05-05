package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.models.Match;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.events.EventGroup;
import io.github.nikmang.playerinfo.models.events.MatchEvent;
import io.github.nikmang.playerinfo.models.events.PlayerMatchEvent;
import io.github.nikmang.playerinfo.models.events.TeamMatchEvent;
import io.github.nikmang.playerinfo.models.odds.BetSlip;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.services.*;
import io.github.nikmang.playerinfo.utils.BettingCalculator;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/odds")
public class OddsController {

    EventService eventService;
    DuelService duelService;
    QuidditchService quidditchService;

    public OddsController(EventService eventService, DuelService duelService, QuidditchService quidditchService) {
        this.eventService = eventService;
        this.duelService = duelService;
        this.quidditchService = quidditchService;
    }

    @PostMapping("public/generate_slip")
    public ResponseEntity<List<BetResult>> generateBettingSlip(@RequestBody BetSlip betSlip) {
        List<AnonymousBetClass> events = betSlip
                .getBets()
                .stream()
                .map(b -> {
                    MatchEvent matchEvent = eventService.getMatchById(b.getMatchId(), b.getEventType());

                    if(matchEvent != null) {
                        return new AnonymousBetClass(matchEvent, b.getChosenId(), b.getGalleons());
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //TODO: bet tally
        List<BetResult> betResults = events
                .stream()
                .map(e -> {
                    double odds = 0;

                    //This is a bit hacky TODO: change
                    if(e.matchEvent instanceof PlayerMatchEvent) {
                        List<DuelMatch> playerMatches1 =  duelService
                                .retrieveAllMatchesForPlayer(((PlayerMatchEvent) e.matchEvent).getPlayer1().getId());
                        List<DuelMatch> playerMatches2 =  duelService
                                .retrieveAllMatchesForPlayer(((PlayerMatchEvent) e.matchEvent).getPlayer2().getId());

                        double[] mappings = BettingCalculator.getOdds(
                                ((PlayerMatchEvent) e.matchEvent).getPlayer1(),
                                ((PlayerMatchEvent) e.matchEvent).getPlayer2(),
                                playerMatches1,
                                playerMatches2);

                        if(e.chosenId == null)
                            odds = mappings[1];
                        else if(e.chosenId == ((PlayerMatchEvent) e.matchEvent).getPlayer1().getId())
                            odds = mappings[0];
                        else
                            odds = mappings[2];

                    } else {
                        //TODO: when team duel matches come then add that
                        List<QuidditchMatch> quidditchMatches1 = quidditchService
                                .retrieveAllMatchesForTeam(((TeamMatchEvent) e.matchEvent).getTeam1().getId());
                        List<QuidditchMatch> quidditchMatches2 = quidditchService
                                .retrieveAllMatchesForTeam(((TeamMatchEvent) e.matchEvent).getTeam2().getId());

                        double[] mappings = BettingCalculator.getOdds(
                                ((TeamMatchEvent) e.matchEvent).getTeam1(),
                                ((TeamMatchEvent) e.matchEvent).getTeam2(),
                                quidditchMatches1,
                                quidditchMatches2);

                        if(e.chosenId == null)
                            odds = mappings[1];
                        else if(e.chosenId == ((TeamMatchEvent) e.matchEvent).getTeam1().getId())
                            odds = mappings[0];
                        else
                            odds = mappings[2];
                    }

                    return new BetResult(e.matchEvent.getId(), odds, Math.round(100*odds*e.galleons)/100.);
                })
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(betResults);
    }

    @GetMapping("public/get_odds")
    public ResponseEntity<OddsCalculation> getOddsForMatch(@RequestParam long matchId, @RequestParam EventType eventType) {
        double[] odds;
        MatchEvent matchEvent = eventService.getMatchById(matchId, eventType);
        List<? extends Match> matchesCompetitor1;
        List<? extends Match> matchesCompetitor2;

        if(matchEvent instanceof PlayerMatchEvent) {
            PlayerMatchEvent playerMatchEvent = (PlayerMatchEvent) matchEvent;
            matchesCompetitor1 =  duelService.retrieveAllMatchesForPlayer(playerMatchEvent.getPlayer1().getId());
            matchesCompetitor2 =  duelService.retrieveAllMatchesForPlayer(playerMatchEvent.getPlayer2().getId());
        } else {
            TeamMatchEvent playerMatchEvent = (TeamMatchEvent) matchEvent;
            matchesCompetitor1 =  duelService.retrieveAllMatchesForPlayer(playerMatchEvent.getTeam1().getId());
            matchesCompetitor2 =  duelService.retrieveAllMatchesForPlayer(playerMatchEvent.getTeam2().getId());
        }

        odds = BettingCalculator.getOdds(
                matchEvent.getCompetitor1(),
                matchEvent.getCompetitor2(),
                matchesCompetitor1,
                matchesCompetitor2);

        return ResponseEntity
                .ok()
                .body(new OddsCalculation(odds));
    }

    @Data
    static class BetResult {
         long matchId;
         double odds;
         double galleonsWon;

        BetResult(long matchId, double odds, double galleonsWon) {
            this.matchId = matchId;
            this.odds = odds;
            this.galleonsWon = galleonsWon;
        }
    }

    @Data
    static class OddsCalculation {
        double comp1Win;
        double draw;
        double comp2Win;

        private OddsCalculation(double[] arr) {
            comp1Win = arr[0];
            draw = arr[1];
            comp2Win = arr[2];
        }
    }

    private static class AnonymousBetClass {
        MatchEvent matchEvent;
        Long chosenId;
        double galleons;

        private AnonymousBetClass(MatchEvent matchEvent, Long chosenId, double galleons) {
            this.matchEvent = matchEvent;
            this.chosenId = chosenId;
            this.galleons = galleons;
        }
    }
}
