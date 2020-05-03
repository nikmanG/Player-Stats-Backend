package fixtures;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DuelMatchBuilder {
    public List<DuelMatch> createMatchesForPlayer(
            int createPlayers,
            Player targetPlayer,
            int totalMatches,
            int totalWins) {
        List<Player> newPlayers = new ArrayList<>();

        for(int i = 0; i < createPlayers; i++) {
            newPlayers.add(new PlayerBuilder().build());
        }

        List<DuelMatch> matches = new ArrayList<>();

        long startMatchDate = System.currentTimeMillis();
        int winCount = 0;

        for(int i = 0; i < totalMatches; i++) {
            DuelMatch match = new DuelMatch();

            if((winCount < totalWins && ThreadLocalRandom.current().nextBoolean())
                    || (totalMatches - i <= totalWins - winCount)) {
                match.setWinner(targetPlayer);
                match.setLoser(newPlayers.get(ThreadLocalRandom.current().nextInt(newPlayers.size())));
                winCount++;
            } else {
                match.setLoser(targetPlayer);
                match.setWinner(newPlayers.get(ThreadLocalRandom.current().nextInt(newPlayers.size())));
            }

            match.setMatchDate(new Date(startMatchDate));
            matches.add(match);

            startMatchDate -= 82_800_000L;
        }

        return matches;
    }
}
