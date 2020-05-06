package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.League;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.repositories.LeagueRepository;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchMatchRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchTeamRepository;
import io.github.nikmang.playerinfo.services.QuidditchService;
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
    private TeamRepository teamRepository;
    @MockBean
    private PlayerRepository playerRepository;
    @MockBean
    private QuidditchMatchRepository quidditchMatchRepository;
    @MockBean
    private QuidditchTeamRepository quidditchTeamRepository;
    @MockBean
    private LeagueRepository leagueRepository;

    @Before
    public void setup() {
        context = new QuidditchService(
                teamRepository,
                playerRepository,
                leagueRepository,
                quidditchMatchRepository,
                quidditchTeamRepository);
    }

    @Test
    public void testRecordMatchWhenNewTeam() {
        //Given
        //When
        QuidditchMatch match = context.recordMatch(
                "TEAM1",
                "TEAM2",
                190,
                180,
                "12345-54321");

        //Then
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("TEAM1"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("TEAM2"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamRepository, times(2)).save(any());

        verify(quidditchTeamRepository, times(2)).getByTeamId(anyLong());
        verify(quidditchTeamRepository, times(2)).save(any());

        verify(quidditchMatchRepository, times(1)).save(any());

        assertEquals("TEAM1", ((Team) match.getWinner()).getName());
        assertEquals("TEAM2", ((Team) match.getLoser()).getName());
        assertEquals(190, match.getWinnerScore());
        assertEquals(180, match.getLoserScore());

        verify(playerRepository, times(1)).findByUuid(eq("12345-54321"));

        verify(quidditchMatchRepository, times(1)).save(any());
        verify(quidditchTeamRepository, times(1)).saveAll(any());
        verify(teamRepository, times(1)).saveAll(any());
    }

    @Test
    public void testRecordMatch() {
        //Given
        Team team1 = new Team();
        team1.setName("TEAM1");

        Team team2 = new Team();
        team2.setName("TEAM2");

        when(teamRepository.findTeamByNameAndType(eq("TEAM1"), eq(TeamType.QUIDDITCH.toString()))).thenReturn(team1);
        when(teamRepository.findTeamByNameAndType(eq("TEAM2"), eq(TeamType.QUIDDITCH.toString()))).thenReturn(team2);

        //When
        QuidditchMatch match = context.recordMatch(
                "TEAM1",
                "TEAM2",
                190,
                180,
                "12345-54321");

        //Then
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("TEAM1"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("TEAM2"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamRepository, never()).save(any());

        verify(quidditchTeamRepository, times(2)).getByTeamId(anyLong());
        verify(quidditchTeamRepository, times(2)).save(any());

        verify(quidditchMatchRepository, times(1)).save(any());

        assertEquals("TEAM1", ((Team) match.getWinner()).getName());
        assertEquals("TEAM2", ((Team) match.getLoser()).getName());
        assertEquals(190, match.getWinnerScore());
        assertEquals(180, match.getLoserScore());

        verify(playerRepository, times(1)).findByUuid(eq("12345-54321"));

        verify(quidditchMatchRepository, times(1)).save(any());
        verify(quidditchTeamRepository, times(1)).saveAll(any());
        verify(teamRepository, times(1)).saveAll(any());
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
        //When
        context.getTeamsForLeague(1L);

        //Then
        verify(quidditchTeamRepository, times(1)).getQuidditchTeamsInTeamsList(any());
    }
}
