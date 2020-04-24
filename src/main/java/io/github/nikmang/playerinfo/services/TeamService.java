package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamById(long id) {
        return teamRepository.findById(id).orElse(null);
    }
}
