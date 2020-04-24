package io.github.nikmang.playerinfo.repositories.quidditch;

import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuidditchMatchRepository extends JpaRepository<QuidditchMatch, Long> {
    @Query(value = "SELECT * FROM Quidditch_Match WHERE winner_id = ?1 OR loser_id = ?1", nativeQuery = true)
    List<QuidditchMatch> findAllByTeam(long teamId);
}
