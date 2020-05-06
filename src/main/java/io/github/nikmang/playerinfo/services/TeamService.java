package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.League;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.repositories.LeagueRepository;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchTeamRepository;
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
    private LeagueRepository leagueRepository;
    private QuidditchTeamRepository quidditchTeamRepository;

    public TeamService(
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            LeagueRepository leagueRepository,
            QuidditchTeamRepository quidditchTeamRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.leagueRepository = leagueRepository;
        this.quidditchTeamRepository = quidditchTeamRepository;
    }

    /**
     * Adds a new league to the database.
     *
     * @param name The name of the league
     * @param leagueType The type of teams that can be in the league
     *
     * @return The newly created league
     */
    public League addLeague(String name, TeamType leagueType) {
        League league = new League();

        league.setName(name);
        league.setLeagueType(leagueType);

        return leagueRepository.save(league);
    }

    /**
     * Add teams to a league.
     *
     * @param teamIds The teams to be added to the league
     * @param leagueId The id of the league to be adding to
     *
     * @return the league to which all the teams were added to. <b>null</b> if teams returned empty or if league was not found
     */
    public League addTeamToLeague(List<Long> teamIds, long leagueId) {
        List<Team> teams = teamRepository.findAllById(teamIds);
        League league = leagueRepository.findById(leagueId).orElse(null);

        if(league == null || teams.isEmpty()) {
            return null;
        }

        teams.forEach(t -> t.setLeague(league));
        league.getTeams().addAll(teams);

        teamRepository.saveAll(teams);
        return leagueRepository.save(league);
    }

    /**
     * Get all leagues that are for a certain team type.
     *
     * @param leagueType The type of league to look for
     *
     * @return The leagues found for said type
     */
    public List<League> getLeaguesForLeagueType(TeamType leagueType) {
        return leagueRepository.getLeaguesByLeagueType(leagueType.toString());
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
     * @param teamName Team name
     * @param teamType Type of team
     *
     * @return Team that was saved. Main difference would be the added internal ID
     */
    public Team addTeam(String teamName, TeamType teamType) {
        Team team = createTeam(teamName, teamType);

        if(teamType == TeamType.QUIDDITCH) {
            QuidditchTeam quidditchTeam = new QuidditchTeam();
            quidditchTeam.setTeam(team);
            quidditchTeamRepository.save(quidditchTeam);
        }

        return team;
    }

    /**
     * Add player to a team. Both values <i>must</i> be not null.
     *
     * @param team Team to get player
     * @param player Player to be added
     */
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
