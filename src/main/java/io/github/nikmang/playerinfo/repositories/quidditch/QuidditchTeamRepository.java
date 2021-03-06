package io.github.nikmang.playerinfo.repositories.quidditch;

import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuidditchTeamRepository extends JpaRepository<QuidditchTeam, Long> {

    @Query(value = "SELECT * FROM quidditch_team WHERE team_id = ?1", nativeQuery = true)
    QuidditchTeam getByTeamId(long teamId);

    @Query(value = "SELECT * FROM quidditch_team WHERE team_id in ?1", nativeQuery = true)
    List<QuidditchTeam> getQuidditchTeamsInTeamsList(List<Long> teamIds);
}
