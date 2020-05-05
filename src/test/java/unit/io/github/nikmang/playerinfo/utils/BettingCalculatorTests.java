package unit.io.github.nikmang.playerinfo.utils;

import fixtures.DuelMatchBuilder;
import fixtures.DuelPlayerBuilder;
import fixtures.PlayerBuilder;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.utils.BettingCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BettingCalculatorTests {

    private static DuelMatchBuilder matchBuilder;

    @BeforeClass
    public static void setupClass() {
        matchBuilder = new DuelMatchBuilder();
    }

    @Test
    public void testPlayersWithData() {
        //Given

        DuelPlayer duelPlayer1 = new DuelPlayerBuilder().withPlayer(new PlayerBuilder().build()).build();
        DuelPlayer duelPlayer2 = new DuelPlayerBuilder().withPlayer(new PlayerBuilder().build()).build();

        List<DuelMatch> player1Matches = matchBuilder.createMatchesForPlayer(
                10,
                duelPlayer1.getPlayer(),
                14,
                10);

        List<DuelMatch> player2Matches = matchBuilder.createMatchesForPlayer(
                10,
                duelPlayer2.getPlayer(),
                16,
                5);

        //When
        double[] odds = BettingCalculator.getOdds(duelPlayer1.getPlayer(), duelPlayer2.getPlayer(), player1Matches, player2Matches);

        //Then
        assertArrayEquals(new double[]{1.47, 0, 3.11}, odds, 0);
    }

    @Test
    public void testWithNoMatchData() {
        //Given
        Team t1 = mock(Team.class);
        Team t2 = mock(Team.class);

        when(t1.getId()).thenReturn(1L);
        when(t2.getId()).thenReturn(2L);

        //When
        double[] odds = BettingCalculator.getOdds(t1, t2, Collections.emptyList(), Collections.emptyList());

        //Then
        assertArrayEquals(new double[]{1,1,1}, odds, 0);
    }
}
