package unit.io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelMatchRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelPlayerRepository;
import io.github.nikmang.playerinfo.services.DuelService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class DuelServiceTests {

    private static Player player1;
    private static Player player2;

    private DuelService duelService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private DuelPlayerRepository duelPlayerRepository;

    @MockBean
    private DuelMatchRepository duelMatchRepository;

    @MockBean
    private TeamRepository teamRepository;

    @BeforeClass
    public static void setupOnce() {
        player1 = new Player();
        player1.setName("sirNik");
        player1.setUuid("1461170a-ce2b-4894-9713-ee476a2c703a");
        player1.setId(1L);

        player2 = new Player();
        player2.setName("Felixx61");
        player2.setUuid("367acdd7-ec2c-4e27-9478-31c1fe5cde8a");
        player2.setId(2L);
    }

    @Before
    public void setup() {
        when(playerRepository.findByUuid(matches("1461170a-ce2b-4894-9713-ee476a2c703a"))).thenReturn(player1);
        when(playerRepository.findByUuid(matches("367acdd7-ec2c-4e27-9478-31c1fe5cde8a"))).thenReturn(player2);

        when(playerRepository.findById(eq(1L))).thenReturn(Optional.of(player1));
        when(playerRepository.findById(eq(2L))).thenReturn(Optional.of(player2));

        DuelPlayer duelPlayer1 = new DuelPlayer();
        duelPlayer1.setElo(1100L);
        duelPlayer1.setPlayer(player1);


        DuelPlayer duelPlayer2 = new DuelPlayer();
        duelPlayer2.setPlayer(player2);

        when(duelPlayerRepository.findByPlayer(eq(1L))).thenReturn(duelPlayer1);
        when(duelPlayerRepository.findByPlayer(eq(2L))).thenReturn(duelPlayer2);

        this.duelService = new DuelService(
                playerRepository,
                teamRepository,
                duelPlayerRepository,
                duelMatchRepository);
    }

    @Test
    public void whenGettingAllPlayers() {
        //Given
        //When
        duelService.getAllPlayers();

        //Then
        verify(duelPlayerRepository, times(1)).findAll(eq(Sort.by("elo").descending()));
    }

    @Test
    public void whenMatchRecorded() {
        //Given
        //When
        DuelMatch duelMatch = duelService.recordMatch(
                "1461170a-ce2b-4894-9713-ee476a2c703a",
                "367acdd7-ec2c-4e27-9478-31c1fe5cde8a");

        //Then
        assertEquals(duelMatch.getWinner().getName(), "sirNik");
        assertEquals(duelMatch.getLoser().getName(), "Felixx61");

        assertEquals(1100L, duelMatch.getOldWinnerElo());
        assertEquals(1200L, duelMatch.getOldLoserElo());

        assertEquals(1115L, duelMatch.getNewWinnerElo());
        assertEquals(1185L, duelMatch.getNewLoserElo());

        verify(duelPlayerRepository, times(2)).save(any());
        verify(duelMatchRepository, times(1)).save(eq(duelMatch));
    }

    @Test
    public void whenGettingTeams() {
        //Given
        //When
        duelService.getTeams();

        //Then
        verify(teamRepository, times(1)).findAllTeamsByType(TeamType.DUEL.toString());
    }

    @Test
    public void whenGettingExistingPlayer() {
        //Given
        //When
        DuelPlayer pl = duelService.getPlayerProfile("1461170a-ce2b-4894-9713-ee476a2c703a");

        //Then
        verify(playerRepository, times(1)).findByUuid("1461170a-ce2b-4894-9713-ee476a2c703a");
        verify(duelPlayerRepository, times(1)).findByPlayer(1L);

        assertEquals(1100L, pl.getElo());
    }

    @Test
    public void whenGettingNewPlayer() {
        //Give
        Player pl = new Player();
        pl.setUuid("123");
        when(playerRepository.save(any())).thenReturn(pl);

        pl.setId(3L);

        //When
        DuelPlayer duelPlayer = duelService.getPlayerProfile(pl.getUuid());

        //Then
        verify(playerRepository, times(1)).findByUuid("123");
        verify(duelPlayerRepository, times(1)).findByPlayer(3L);

        assertEquals(1200L, duelPlayer.getElo());
    }

    @Test
    public void whenGettingNonExistentPlayerById() {
        //Give
        Player pl = new Player();
        pl.setUuid("123");
        pl.setId(3L);

        //When
        DuelPlayer duelPlayer = duelService.getPlayerProfile(pl.getId());

        //Then
        verify(playerRepository, times(1)).findById(pl.getId());

        assertNull(duelPlayer);
    }

    @Test
    public void whenRetrievingAllMatchesForNonExistentPlayer() {
        //Given
        Player pl = new Player();
        pl.setUuid("123");

        //When
        List<DuelMatch> matches = duelService.retrieveAllMatchesForPlayer(pl.getId());

        //Then
        verify(duelMatchRepository, times(1)).findAllByPlayer(eq(pl.getId()));

        assertEquals(0, matches.size());
    }
}
