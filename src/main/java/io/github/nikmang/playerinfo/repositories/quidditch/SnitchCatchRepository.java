package io.github.nikmang.playerinfo.repositories.quidditch;

import io.github.nikmang.playerinfo.models.quidditch.SnitchCatches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SnitchCatchRepository extends JpaRepository<SnitchCatches, Long> {

    @Query(value = "SELECT * FROM snitch_catches WHERE catcher = ?1", nativeQuery = true)
    List<SnitchCatches> getByCatcherId(long catcherId);

    @Query(value = "SELECT * FROM snitch_catches WHERE opponent = ?1", nativeQuery = true)
    List<SnitchCatches> getByOpponentId(long opponentId);

    @Query(value = "SELECT * FROM snitch_catches WHERE catcher = ?1 OR opponent = ?1", nativeQuery = true)
    List<SnitchCatches> getAllForPlayer(long player);
}
