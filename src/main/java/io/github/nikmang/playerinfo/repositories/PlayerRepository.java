package io.github.nikmang.playerinfo.repositories;

import io.github.nikmang.playerinfo.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "SELECT * FROM Player WHERE uuid = ?1", nativeQuery = true)
    Player findByUuid(String uuid);
}
