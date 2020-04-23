package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ExternalApiService {
    private static final String minecraftNameServer = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final long cooldown = 3600000L;

    private RestTemplate restTemplate;
    private PlayerRepository playerRepository;

    public ExternalApiService(PlayerRepository playerRepository, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.playerRepository = playerRepository;
    }

    //Map uuid, name
    public Map<String, String> getPlayerNames(List<Player> players) {
        long currentTime = System.currentTimeMillis();
        Map<String, String> mappings = new HashMap<>();

        for(Player pl : players) {
            if(pl.getName() == null || currentTime - pl.getTimestampOfRetrieval() > cooldown) {
                mappings.put(pl.getUuid(), getPlayerName(pl.getUuid()));
                pl.setTimestampOfRetrieval(currentTime);
                pl.setName(mappings.get(pl.getUuid()));
                
                playerRepository.save(pl);
            } else {
                mappings.put(pl.getUuid(), pl.getName());
            }
        }

        return mappings;
    }

    public String getPlayerName(String uuid) {
        String formatted = uuid.replaceAll("-", "");

        return Objects.requireNonNull(this.restTemplate.getForObject(minecraftNameServer + formatted, MinecraftNamePacket.class)).name;
    }
}

class MinecraftNamePacket {
    @Setter
    String id;

    @Setter
    String name;
}
