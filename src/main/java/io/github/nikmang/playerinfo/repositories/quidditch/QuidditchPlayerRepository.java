package io.github.nikmang.playerinfo.repositories.quidditch;

import io.github.nikmang.playerinfo.models.quidditch.QuidditchPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuidditchPlayerRepository extends JpaRepository<QuidditchPlayer, Long> {

    @Query(value = "SELECT * FROM quidditch_player WHERE player_id = ?1", nativeQuery = true)
    QuidditchPlayer getByPlayerId(long playerId);

}
