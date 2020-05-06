package io.github.nikmang.playerinfo.repositories;

import io.github.nikmang.playerinfo.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {

    @Query(value = "SELECT * FROM League WHERE league_type = ?1", nativeQuery = true)
    List<League> getLeaguesByLeagueType(String LeagueType);
}
