package io.github.nikmang.playerinfo.repositories.duelling;

import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DuelPlayerRepository extends JpaRepository<DuelPlayer, Long> {

    @Query(value = "SELECT * FROM Duel_Player WHERE player_id = ?1", nativeQuery = true)
    DuelPlayer findByPlayer(long playerId);

}
