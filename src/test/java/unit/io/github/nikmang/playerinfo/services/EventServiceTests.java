package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.events.MatchEvent;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.events.PlayerMatchEventRepository;
import io.github.nikmang.playerinfo.repositories.events.TeamMatchEventRepository;
import io.github.nikmang.playerinfo.services.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EventServiceTests {

    private EventService context;

    @MockBean
    private TeamMatchEventRepository teamMatchEventRepository;
    @MockBean
    private PlayerMatchEventRepository playerMatchEventRepository;
    @MockBean
    private PlayerRepository playerRepository;
    @MockBean
    private TeamRepository teamRepository;

    @Before
    public void setup() {
        this.context = new EventService(
                teamMatchEventRepository,
                playerMatchEventRepository,
                playerRepository,
                teamRepository);
    }

    @Test
    public void testAddMatchSamePlayer() {
        //Given
        //When
        MatchEvent matchEvent = context.addMatchEvent(
                EventType.DUEL_SINGLE,
                new Date(),
                "player",
                "player");

        //Then
        assertNull(matchEvent);

        verify(playerRepository, never()).findByUuid(any());
        verify(playerMatchEventRepository, never()).save(any());
    }

    @Test
    public void testAddMatchOldTime() {
        //Given
        //When
        MatchEvent matchEvent = context.addMatchEvent(
                EventType.DUEL_SINGLE,
                new Date(System.currentTimeMillis() - 10_000L),
                "player1",
                "player2");

        //Then
        assertNull(matchEvent);

        verify(playerRepository, never()).findByUuid(any());
        verify(playerMatchEventRepository, never()).save(any());
    }

    @Test
    public void testAddDuelSingleMatch() {
        //Given
        //When
        MatchEvent matchEvent = context.addMatchEvent(
                EventType.DUEL_SINGLE,
                new Date(System.currentTimeMillis() + 10_000L),
                "player1",
                "player2");

        //Then
        verify(playerRepository, times(1)).findByUuid(eq("player1"));
        verify(playerRepository, times(1)).findByUuid(eq("player2"));
        verify(playerMatchEventRepository, times(1)).save(any());
    }

    @Test
    public void testAddQuidditchMatch() {
        //Given
        //When
        MatchEvent matchEvent = context.addMatchEvent(
                EventType.QUIDDITCH,
                new Date(System.currentTimeMillis() + 10_000L),
                "team1",
                "team2");

        //Then
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("team1"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("team2"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamMatchEventRepository, times(1)).save(any());
    }

    @Test
    public void testTeamMatchesNullDate() {
        //Given
        //When
        context.getFutureTeamMatches(null);

        //Then
        verify(teamMatchEventRepository, times(1)).getMatchesAfterDate(any());
    }

    @Test
    public void testTeamMatchesProvidedDate() {
        //Given
        Date date = new Date();

        //When
        context.getFutureTeamMatches(date);

        //Then
        verify(teamMatchEventRepository, times(1)).getMatchesAfterDate(eq(date));
    }

    @Test
    public void testPlayerMatchesNullDate() {
        //Given
        //When
        context.getFuturePlayerMatches(null);

        //Then
        verify(playerMatchEventRepository, times(1)).getMatchesAfterDate(any());
    }

    @Test
    public void testPlayerMatchesProvidedDate() {
        //Given
        Date date = new Date();

        //When
        context.getFuturePlayerMatches(date);

        //Then
        verify(playerMatchEventRepository, times(1)).getMatchesAfterDate(eq(date));

    }
}
