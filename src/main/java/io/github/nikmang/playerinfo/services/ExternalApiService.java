package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ExternalApiService {
    private static final String minecraftNameServer = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String backupMinecraftNameServer = "https://playerdb.co/api/player/minecraft/";

    private static final long cooldown = 3600000L;

    private RestTemplate restTemplate;
    private PlayerRepository playerRepository;

    public ExternalApiService(PlayerRepository playerRepository, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves a list of current player names based on their UUIDs.
     * Only does retrieval once per hour per player.
     * Primary retrieval location is sessionserver on mojang, backup is playerdb.
     *
     * @param players List of players to retrieve names for
     *
     * @return Map of player names in the form of key: uuid, value: name
     */
    public Map<String, String> getPlayerNames(Collection<Player> players) {
        long currentTime = System.currentTimeMillis();
        Map<String, String> mappings = new HashMap<>();

        for(Player pl : players) {
            if(pl.getName() == null || currentTime - pl.getTimestampOfRetrieval() > cooldown) {
                mappings.put(pl.getUuid(), getPlayerName(pl.getUuid()));
                pl.setTimestampOfRetrieval(currentTime);
                pl.setName(mappings.get(pl.getUuid()));
            } else {
                mappings.put(pl.getUuid(), pl.getName());
            }
        }

        playerRepository.saveAll(players);

        return mappings;
    }

    /**
     * Retrieves a name for a given uuid.
     * Only does retrieval once per hour per player.
     * Primary retrieval location is sessionserver on mojang, backup is playerdb.
     *
     * @param uuid UUID of player
     *
     * @return name of player
     */
    public String getPlayerName(String uuid) {
        String formatted = uuid.replaceAll("-", "");

        try {
            return Objects.requireNonNull(this.restTemplate.getForObject(minecraftNameServer + formatted, MinecraftNamePacket.class)).name;
        } catch(HttpClientErrorException e) {
            return Objects.requireNonNull(this.restTemplate.getForObject(backupMinecraftNameServer + uuid, BackupNamePacket.class)).data.player.username;
        }
    }

    public static class MinecraftNamePacket {
        @Setter
        String id;

        @Setter
        String name;
    }

    public static class BackupNamePacket {
        @Setter
        boolean success;

        @Setter
        InnerPacket data;
    }

    public static class InnerPacket {
        @Setter
        InnerInnerPacket player;
    }

    public static class InnerInnerPacket {
        @Setter
        String username;
    }
}