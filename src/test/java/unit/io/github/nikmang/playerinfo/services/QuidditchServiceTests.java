package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
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

    @Before
    public void setup() {
        context = new QuidditchService(
                teamRepository,
                playerRepository,
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

        assertEquals("TEAM1", match.getWinner().getName());
        assertEquals("TEAM2", match.getLoser().getName());
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

        assertEquals("TEAM1", match.getWinner().getName());
        assertEquals("TEAM2", match.getLoser().getName());
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
}
