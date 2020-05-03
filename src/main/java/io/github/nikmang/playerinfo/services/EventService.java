package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.events.MatchEvent;
import io.github.nikmang.playerinfo.models.events.PlayerMatchEvent;
import io.github.nikmang.playerinfo.models.events.TeamMatchEvent;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.events.PlayerMatchEventRepository;
import io.github.nikmang.playerinfo.repositories.events.TeamMatchEventRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private TeamMatchEventRepository teamMatchEventRepository;
    private PlayerMatchEventRepository playerMatchEventRepository;
    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;

    public EventService(
            TeamMatchEventRepository teamMatchEventRepository,
            PlayerMatchEventRepository playerMatchEventRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {
        this.teamMatchEventRepository = teamMatchEventRepository;
        this.playerMatchEventRepository = playerMatchEventRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
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

                return playerMatchEventRepository.save(playerMatchEvent);
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
                return teamMatchEventRepository.save(teamMatchEvent);
        }

        return null;
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
}
