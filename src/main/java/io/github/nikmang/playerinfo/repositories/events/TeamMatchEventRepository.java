package io.github.nikmang.playerinfo.repositories.events;

import io.github.nikmang.playerinfo.models.events.TeamMatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TeamMatchEventRepository extends JpaRepository<TeamMatchEvent, Long> {

    @Query(value = "SELECT * FROM Team_Match_Event WHERE match_date >= ?1", nativeQuery = true)
    List<TeamMatchEvent> getMatchesAfterDate(Date date);
}
