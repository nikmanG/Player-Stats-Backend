package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.controllers.EventController;
import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.events.EventGroup;
import io.github.nikmang.playerinfo.models.events.MatchEvent;
import io.github.nikmang.playerinfo.models.events.PlayerMatchEvent;
import io.github.nikmang.playerinfo.models.events.TeamMatchEvent;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.events.EventGroupRepository;
import io.github.nikmang.playerinfo.repositories.events.PlayerMatchEventRepository;
import io.github.nikmang.playerinfo.repositories.events.TeamMatchEventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventService {

    private TeamMatchEventRepository teamMatchEventRepository;
    private PlayerMatchEventRepository playerMatchEventRepository;
    private EventGroupRepository eventGroupRepository;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    public EventService(
            TeamMatchEventRepository teamMatchEventRepository,
            PlayerMatchEventRepository playerMatchEventRepository,
            EventGroupRepository eventGroupRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {
        this.teamMatchEventRepository = teamMatchEventRepository;
        this.playerMatchEventRepository = playerMatchEventRepository;
        this.playerRepository = playerRepository;
        this.eventGroupRepository = eventGroupRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Adds a multi-day event group. Start date and end date are both <b>inclusive</b>.
     *
     * @param startDate Start date of the event
     * @param endDate End date of the event
     * @param name Name of the event
     *
     * @return The created event group
     */
    public EventGroup addEventGroup(Date startDate, Date endDate, String name) {
        if(startDate.after(endDate))
            return null;

        EventGroup eventGroup = new EventGroup();
        eventGroup.setEndDate(endDate);
        eventGroup.setStartDate(startDate);
        eventGroup.setName(name);

        return eventGroupRepository.save(eventGroup);
    }

    /**
     * Gets all future events from a certain date. Date is <b>inclusive</b>.
     *
     * @param startDate The minimum date for events to start
     *
     * @return List of all events that start on or after the provided start date
     */
    public List<EventGroup> getFutureEvents(Date startDate) {
        if(startDate == null)
            startDate = new Date();

        return eventGroupRepository.getEventsOnOrAfterDate(startDate);
    }

    /**
     * Add a list of events to a created event.
     *
     * @param eventId The id of the target event
     * @param matches All matches that apply to this event
     * @return the event group with the new matches
     */
    public EventGroup addEventsToEventGroup(long eventId, List<MatchEvent> matches) {
        EventGroup eventGroup = eventGroupRepository.getOne(eventId);

        eventGroup.setMatches(matches
                .stream()
                .filter(m -> m.getMatchDate().compareTo(eventGroup.getStartDate()) >= 0 && m.getMatchDate().compareTo(eventGroup.getEndDate()) <= 0)
                .collect(Collectors.toList()));

        return eventGroupRepository.save(eventGroup);
    }

    public EventGroup getEventById(long eventId) {
        return eventGroupRepository.getOne(eventId);
    }

    /**
     * Adds a list of future matches to the database.
     *
     * @param events List of events provided by the {@linkplain EventController}
     *
     * @return List of created match events
     */
    public List<MatchEvent> addMatches(List<EventController.EventWrapper> events) {
        List<TeamMatchEvent> teamMatchEvents = new ArrayList<>();
        List<PlayerMatchEvent> playerMatchEvents = new ArrayList<>();

        List<MatchEvent> matchEvents = events
                .stream()
                .map(e -> createMatchEvent(
                        e.getEventType(),
                        e.getTargetDate(),
                        e.getId1(),
                        e.getId2()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        matchEvents.forEach(m -> {
            if(m instanceof TeamMatchEvent)
                teamMatchEvents.add((TeamMatchEvent) m);
            else
                playerMatchEvents.add((PlayerMatchEvent) m);
        });

        teamMatchEventRepository.saveAll(teamMatchEvents);
        playerMatchEventRepository.saveAll(playerMatchEvents);

        return matchEvents;
    }

    /**
     * Add a future match event to the database.
     *
     * @param eventType The event type of the match
     * @param matchDate The date of the match. <b>Must</b> be set in the future to current time
     * @param id1 The id of the first participant. For players it is <i>UUID</i>, for teams it is their <i>name</i>
     * @param id2 The id of the second participant. For players it is <i>UUID</i>, for teams it is their <i>name</i>
     *
     * @return The created match object
     */
    public MatchEvent addMatchEvent(EventType eventType, Date matchDate, String id1, String id2) {
        MatchEvent matchEvent = createMatchEvent(eventType, matchDate, id1, id2);

        if(matchEvent == null)
            return null;

        if(matchEvent instanceof PlayerMatchEvent)
            return playerMatchEventRepository.save(((PlayerMatchEvent) matchEvent));

        return teamMatchEventRepository.save(((TeamMatchEvent) matchEvent));
    }

    /**
     * Gets all matches passed a given date.
     * If null supplied then current date is used.
     *
     * @param date Date used as the minimum for matches (inclusive)
     *
     * @return List of team matches that are to be played on provided date or after
     */
    public List<TeamMatchEvent> getFutureTeamMatches(Date date) {
        if(date == null) {
            date = new Date();
        }

        return teamMatchEventRepository.getMatchesAfterDate(date);
    }

    /**
     * Gets all matches passed a given date.
     * If null supplied then current date is used.
     *
     * @param date Date used as the minimum for matches (inclusive)
     *
     * @return List of team matches that are to be played on provided date or after
     */
    public List<PlayerMatchEvent> getFuturePlayerMatches(Date date) {
        if(date == null) {
            date = new Date();
        }

        return playerMatchEventRepository.getMatchesAfterDate(date);
    }

    /**
     * Retrieves match with provided ID for a given event.
     * {@link EventType#DUEL_SINGLE} will draw from player matches. All others will draw from team matches.
     *
     * @param matchId The id of the the match
     * @param eventType The event type of the match
     *
     * @return Match with given ID. Event type only used to differentiate which table to go in hence may not get that event type.
     */
    public MatchEvent getMatchById(long matchId, EventType eventType) {
        if(eventType == EventType.DUEL_SINGLE)
            return playerMatchEventRepository.getOne(matchId);

        return teamMatchEventRepository.getOne(matchId);
    }

    private MatchEvent createMatchEvent(EventType eventType, Date matchDate, String id1, String id2) {
        if(id1.equals(id2) || matchDate.compareTo(new Date()) < 0)
            return null;

        switch (eventType) {
            case DUEL_SINGLE:
                Player player1 = playerRepository.findByUuid(id1);
                Player player2 = playerRepository.findByUuid(id2);

                PlayerMatchEvent playerMatchEvent = new PlayerMatchEvent();

                playerMatchEvent.setEventType(EventType.DUEL_SINGLE);
                playerMatchEvent.setMatchDate(matchDate);
                playerMatchEvent.setPlayer1(player1);
                playerMatchEvent.setPlayer2(player2);

                return playerMatchEvent;
            case DUEL_TEAM:
                return null;
            case QUIDDITCH:
                Team team1 = teamRepository.findTeamByNameAndType(id1, TeamType.QUIDDITCH.toString());
                Team team2 = teamRepository.findTeamByNameAndType(id2, TeamType.QUIDDITCH.toString());

                TeamMatchEvent teamMatchEvent = new TeamMatchEvent();

                teamMatchEvent.setEventType(EventType.QUIDDITCH);
                teamMatchEvent.setMatchDate(matchDate);
                teamMatchEvent.setTeam1(team1);
                teamMatchEvent.setTeam2(team2);
                return teamMatchEvent;
        }

        return null;
    }
}
