package fixtures;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;

public class DuelPlayerBuilder {
    private static long counter = 1;
    private DuelPlayer instance;

    public DuelPlayerBuilder() {
        instance = new DuelPlayer();
        instance.setId(counter++);
    }

    public DuelPlayerBuilder withElo(long elo) {
        instance.setElo(elo);

        return this;
    }

    public DuelPlayerBuilder withPlayer(Player player) {
        instance.setPlayer(player);

        return this;
    }

    public DuelPlayerBuilder withWins(int wins) {
        instance.setWins(wins);

        return this;
    }

    public DuelPlayerBuilder withLosses(int losses) {
        instance.setLosses(losses);

        return this;
    }

    public DuelPlayer build() {
        return instance;
    }
}
