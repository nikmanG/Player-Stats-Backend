package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Adds player to players table.
     * If they already exist then just returns existing object.
     *
     * @param uuid UUID of player
     *
     * @return Player object created, or existing one in database
     */
    public Player addPlayer(String uuid) {
        Player player = playerRepository.findByUuid(uuid);

        if(player != null)
            return player;

        player = new Player();
        player.setUuid(uuid);

        playerRepository.save(player);

        return player;
    }

    /**
     * Retrieves player with provided ID.
     *
     * @param id ID of player as in database
     *
     * @return Player with given ID. <b>null</b> if not found
     */
    public Player getPlayer(long id) {
        return playerRepository.findById(id).orElse(null);
    }
}
