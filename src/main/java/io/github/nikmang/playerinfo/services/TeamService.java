package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private TeamRepository teamRepository;
    private PlayerRepository playerRepository;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves a team by their ID.
     *
     * @param id ID of team as seen in the database
     *
     * @return Team with said ID. <b>null</b> if not found.
     */
    public Team getTeamById(long id) {
        return teamRepository.findById(id).orElse(null);
    }

    /**
     * Gets a team by given name and team type.
     * Creates a team if it does not exist yet.
     *
     * @param name Name of team
     * @param type type of team
     *
     * @return Team with given name for given type.
     */
    public Team getTeamByNameAndType(String name, TeamType type) {
        Team team = teamRepository.findTeamByNameAndType(name, type.toString());

        if(team == null) {
            team = createTeam(name, type);
        }

        return team;
    }

    /**
     * Saves team to repository.
     *
     * @param team Team to be saved.
     *
     * @return Team that was saved. Main difference would be the added internal ID
     */
    public Team addTeam(Team team) {
        return this.teamRepository.save(team);
    }

    public void addPlayerToTeam(Team team, Player player) {
       team.getPlayers().add(player);
        teamRepository.save(team);
    }

    /**
     * Retrieve all teams a player belongs to.
     *
     * @param playerId ID of player (as found in database)
     *
     * @return Map of teams that a player belongs to in the form of K: {@linkplain TeamType} V: {@linkplain Team}
     */
    public Map<TeamType, Team> getTeamsByPlayerId(long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if(player == null) {
            return Collections.emptyMap();
        }

        return teamRepository.findAll().stream().filter(t -> t.getPlayers().contains(player)).collect(Collectors.toMap(Team::getTeamType, Function.identity()));
    }

    private Team createTeam(String name, TeamType type) {
        Team team = new Team();

        team.setName(name);
        team.setTeamType(type);

        teamRepository.save(team);

        return team;
    }
}
