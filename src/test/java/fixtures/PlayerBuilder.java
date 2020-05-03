package fixtures;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;

import java.util.UUID;

public class PlayerBuilder {
    private static long counter = 1;
    private Player instance;

    public PlayerBuilder() {
        instance = new Player();
        instance.setId(counter++);

        instance.setUuid(UUID.randomUUID().toString());
    }

    public PlayerBuilder withUUID(String uuid) {
        instance.setUuid(uuid);

        return this;
    }

    public PlayerBuilder withName(String name) {
        instance.setName(name);

        return this;
    }

    public PlayerBuilder withTimestampOfNameRetrival(long timestamp) {
        instance.setTimestampOfRetrieval(timestamp);

        return this;
    }

    public Player build() {
        return instance;
    }
}
