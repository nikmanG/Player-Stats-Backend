package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.models.events.MatchEvent;
import io.github.nikmang.playerinfo.models.events.PlayerMatchEvent;
import io.github.nikmang.playerinfo.models.events.TeamMatchEvent;
import io.github.nikmang.playerinfo.services.EventService;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("public/team")
    public ResponseEntity<List<TeamMatchEvent>> getUpcomingTeamEvents(
            @RequestParam(value = "start_date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    Date date) {
        List<TeamMatchEvent> teamMatches = eventService.getFutureTeamMatches(date);

        return ResponseEntity
                .ok()
                .body(teamMatches);
    }

    @GetMapping("public/player")
    public ResponseEntity<List<PlayerMatchEvent>> getUpcomingPlayerEvents(
            @RequestParam(value = "start_date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    Date date) {
        List<PlayerMatchEvent> playerMatches = eventService.getFuturePlayerMatches(date);

        return ResponseEntity
                .ok()
                .body(playerMatches);
    }

    @PostMapping("public/add_event")
    public ResponseEntity<MatchEvent> addNewEvent(@RequestBody EventWrapper event) {
        MatchEvent eventResult = eventService.addMatchEvent(event.eventType, event.targetDate, event.id1, event.id2);

        if(eventResult == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity
                .ok()
                .body(eventResult);
    }

    @Data
    static class EventWrapper {
        EventType eventType;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date targetDate;
        String id1;
        String id2;
    }
}
