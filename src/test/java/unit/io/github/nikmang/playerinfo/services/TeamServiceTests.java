package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchTeamRepository;
import io.github.nikmang.playerinfo.services.TeamService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class TeamServiceTests {

    private TeamService context;
    private Team testTeam;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private QuidditchTeamRepository quidditchTeamRepository;

    @Before
    public void setup() {
        this.context = new TeamService(teamRepository, playerRepository, quidditchTeamRepository);
        this.testTeam = new Team();
        this.testTeam.setTeamType(TeamType.QUIDDITCH);
        this.testTeam.setName("TEST TEAM");
        this.testTeam.setPlayers(new HashSet<>());
    }

    @Test
    public void testGetTeamById() {
        //Given
        //When
        context.getTeamById(1L);

        //Then
        verify(teamRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void testGetExistingTeamByNameAndType() {
        //Given
        when(teamRepository.findTeamByNameAndType(anyString(), any())).thenReturn(testTeam);

        //When
        context.getTeamByNameAndType("TEST TEAM", TeamType.QUIDDITCH);

        //Then
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("TEST TEAM"), eq(TeamType.QUIDDITCH.toString()));
        verify(teamRepository, never()).save(any());
    }

    @Test
    public void testGetNewTeamByNameAndType() {
        //Given
        when(teamRepository.findTeamByNameAndType(anyString(), any())).thenReturn(null);

        //When
        Team t = context.getTeamByNameAndType("TEST TEAM 2", TeamType.DUEL);

        //Then
        verify(teamRepository, times(1)).findTeamByNameAndType(eq("TEST TEAM 2"), eq(TeamType.DUEL.toString()));
        verify(teamRepository, times(1)).save(any());

        assertEquals("TEST TEAM 2", t.getName());
        assertEquals(TeamType.DUEL, t.getTeamType());
    }

    @Test
    public void testAddPlayerToTeam() {
        //Given
        Player player = new Player();
        player.setUuid("1234-45678");

        //When
        context.addPlayerToTeam(testTeam, player);

        //Then
        verify(teamRepository, times(1)).save(any());
        assertTrue(testTeam.getPlayers().contains(player));
    }

    @Test
    public void getTeamsForNullPlayer() {
        //Given
        //When
        Map<TeamType, Team> teams =  context.getTeamsByPlayerId(2L);

        //Then
        verify(teamRepository, never()).findAll();
        assertNotNull(teams);
        assertEquals(0, teams.size());
    }


    @Test
    public void getTeamsForValidPlayer() {
        //Given
        Player player = new Player();
        player.setUuid("1234-45678");
        player.setId(2L);

        testTeam.getPlayers().add(player);

        Team secondTeam = new Team();
        secondTeam.setName("BAD TEAM");
        secondTeam.setPlayers(Collections.emptySet());

        when(teamRepository.findAll()).thenReturn(Arrays.asList(testTeam, secondTeam));
        when(playerRepository.findById(anyLong())).thenReturn(Optional.of(player));

        //When
        Map<TeamType, Team> teams = context.getTeamsByPlayerId(2L);

        //Then
        verify(teamRepository, times(1)).findAll();
        assertEquals(1, teams.size());

        assertTrue(teams.get(TeamType.QUIDDITCH).getPlayers().contains(player));
    }
}
