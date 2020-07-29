package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.League;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.repositories.LeagueRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchMatchRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchPlayerRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchTeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.SnitchCatchRepository;
import io.github.nikmang.playerinfo.services.PlayerService;
import io.github.nikmang.playerinfo.services.QuidditchService;
import io.github.nikmang.playerinfo.services.TeamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class QuidditchServiceTests {

    private QuidditchService context;

    @MockBean
    private QuidditchMatchRepository quidditchMatchRepository;

    @MockBean
    private QuidditchTeamRepository quidditchTeamRepository;

    @MockBean
    private SnitchCatchRepository snitchCatchRepository;

    @MockBean
    private QuidditchPlayerRepository quidditchPlayerRepository;

    @MockBean
    private LeagueRepository leagueRepository;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private TeamService teamService;

    @Before
    public void setup() {
        context = new QuidditchService(
                leagueRepository,
                quidditchMatchRepository,
                quidditchTeamRepository,
                snitchCatchRepository,
                quidditchPlayerRepository,
                playerService,
                teamService);
    }

    @Test
    public void testRecordMatch() {
        //Given
        Team team1 = new Team();
        team1.setName("TEAM1");

        Team team2 = new Team();
        team2.setName("TEAM2");

        when(teamService.getOrCreateTeamByNameAndType(eq("TEAM1"), eq(TeamType.QUIDDITCH))).thenReturn(team1);
        when(teamService.getOrCreateTeamByNameAndType(eq("TEAM2"), eq(TeamType.QUIDDITCH))).thenReturn(team2);

        //When
        QuidditchMatch match = context.recordMatch(
                "TEAM1",
                "TEAM2",
                190,
                180,
                "12345-54321");

        //Then
        verify(teamService, times(1)).getOrCreateTeamByNameAndType(eq("TEAM1"), eq(TeamType.QUIDDITCH));
        verify(teamService, times(1)).getOrCreateTeamByNameAndType(eq("TEAM2"), eq(TeamType.QUIDDITCH));

        verify(quidditchTeamRepository, times(2)).getByTeamId(anyLong());
        verify(quidditchTeamRepository, times(2)).save(any());

        verify(quidditchMatchRepository, times(1)).save(any());

        assertEquals("TEAM1", ((Team) match.getWinner()).getName());
        assertEquals("TEAM2", ((Team) match.getLoser()).getName());
        assertEquals(190, match.getWinnerScore());
        assertEquals(180, match.getLoserScore());

        verify(playerService, times(1)).getOrAddPlayer(eq("12345-54321"));

        verify(quidditchMatchRepository, times(1)).save(any());
        verify(quidditchTeamRepository, times(1)).saveAll(any());
        verify(teamService, times(1)).updateTeams(any());
    }

    @Test
    public void testGetTeamById() {
        //Given
        //When
        context.getByTeamId(1L);

        //Then
        verify(quidditchTeamRepository, times(1)).getByTeamId(eq(1L));
    }

    @Test
    public void testRetrieveAllMatchesForTeam() {
        //Given
        //When
        context.retrieveAllMatchesForTeam(1L);

        //Then
        verify(quidditchMatchRepository, times(1)).findAllByTeam(eq(1L));
    }

    @Test
    public void testGetTeams() {
        //Given
        //When
        context.getTeams();

        //Then
        verify(quidditchTeamRepository, times(1)).findAll(eq(Sort.by("team.wins").descending()));
    }

    @Test
    public void testGetTeamsByLeagueWhenInvalidLeague() {
        //Given
        League wrong = new League();
        wrong.setLeagueType(TeamType.DUEL);

        when(leagueRepository.findById(anyLong())).thenReturn(Optional.of(wrong));

        //When
        List<QuidditchTeam> teams = context.getTeamsForLeague(1L);

        //Then
        assertEquals(0, teams.size());
        verify(quidditchTeamRepository, never()).getQuidditchTeamsInTeamsList(any());
    }

    @Test
    public void testGetTeamsByLeague() {
        //Given
        Team team = new Team();
        team.setId(10L);

        League league = new League();
        league.setLeagueType(TeamType.QUIDDITCH);
        league.setTeams(Collections.singletonList(team));

        when(leagueRepository.findById(anyLong())).thenReturn(Optional.of(league));

        //When
        context.getTeamsForLeague(1L);

        //Then
        verify(quidditchTeamRepository, times(1)).getQuidditchTeamsInTeamsList(any());
    }
}
