package io.github.nikmang.playerinfo.repositories;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "SELECT * FROM Team WHERE name = ?1 AND team_type = ?2", nativeQuery = true)
    Team findTeamByNameAndType(String teamName, String type);

    @Query(value = "SELECT * FROM Team WHERE team_type = ?1", nativeQuery = true)
    List<Team> findAllTeamsByType(String type);
}
