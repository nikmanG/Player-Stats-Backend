package io.github.nikmang.playerinfo.repositories.duelling;

import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DuelMatchRepository extends JpaRepository<DuelMatch, Long> {

    @Query(value = "SELECT * FROM Duel_Match WHERE winner_id = ?1 OR loser_id = ?1", nativeQuery = true)
    List<DuelMatch> findAllByPlayer(long playerId);
}
