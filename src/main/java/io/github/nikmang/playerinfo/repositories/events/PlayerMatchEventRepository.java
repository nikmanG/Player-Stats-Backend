package io.github.nikmang.playerinfo.repositories.events;

import io.github.nikmang.playerinfo.models.events.PlayerMatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PlayerMatchEventRepository extends JpaRepository<PlayerMatchEvent, Long> {

    @Query(value = "SELECT * FROM Player_Match_Event WHERE match_date >= ?1", nativeQuery = true)
    List<PlayerMatchEvent> getMatchesAfterDate(Date date);
}
