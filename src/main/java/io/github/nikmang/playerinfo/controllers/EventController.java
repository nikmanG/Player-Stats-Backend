package io.github.nikmang.playerinfo.controllers;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.models.events.EventGroup;
import io.github.nikmang.playerinfo.models.events.MatchEvent;
import io.github.nikmang.playerinfo.models.events.PlayerMatchEvent;
import io.github.nikmang.playerinfo.models.events.TeamMatchEvent;
import io.github.nikmang.playerinfo.repositories.events.EventGroupRepository;
import io.github.nikmang.playerinfo.services.EventService;
import lombok.Data;
import org.apache.catalina.connector.Response;
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

    @GetMapping("public/events")
    public ResponseEntity<List<EventGroup>> getUpcomingEvents(
            @RequestParam(value = "start_date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    Date date) {
        List<EventGroup> events = eventService.getFutureEvents(date);

        return ResponseEntity
                .ok()
                .body(events);
    }

    @PostMapping("private/add_event")
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

    @PostMapping("private/add_event_group")
    public ResponseEntity<EventGroup> addNewEventGroup(@RequestBody EventGroupWrapper eventGroupWrapper)  {
        EventGroup eventGroup = eventService.addEventGroup(eventGroupWrapper.startDate, eventGroupWrapper.endDate, eventGroupWrapper.name);

        if(eventGroup == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity
                .ok()
                .body(eventGroup);
    }

    @PostMapping("private/add_matches_to_event")
    public ResponseEntity<EventGroup> addMatchesToEvent(@RequestBody EventJoinWrapper eventJoinWrapper) {
        List<MatchEvent> matchEvents = eventService.addMatches(eventJoinWrapper.matches);

        EventGroup eventGroup = eventService.addEventsToEventGroup(eventJoinWrapper.eventId, matchEvents);

        if(eventGroup == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity
                .ok()
                .body(eventGroup);
    }

    @Data
    static class EventJoinWrapper {
        long eventId;
        List<EventWrapper> matches;
    }
    @Data
    static class EventGroupWrapper {
        String name;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date endDate;
    }

    @Data
    public static class EventWrapper {
        EventType eventType;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        Date targetDate;
        String id1;
        String id2;
    }
}
