package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.League;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchPlayer;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.models.quidditch.SnitchCatches;
import io.github.nikmang.playerinfo.repositories.LeagueRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchMatchRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchPlayerRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchTeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.SnitchCatchRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuidditchService {

    private final QuidditchMatchRepository quidditchMatchRepository;
    private final QuidditchTeamRepository quidditchTeamRepository;
    private final LeagueRepository leagueRepository;
    private final SnitchCatchRepository snitchCatchRepository;
    private final QuidditchPlayerRepository quidditchPlayerRepository;

    private final PlayerService playerService;
    private final TeamService teamService;

    public QuidditchService(
                            LeagueRepository leagueRepository,
                            QuidditchMatchRepository quidditchMatchRepository,
                            QuidditchTeamRepository quidditchTeamRepository,
                            SnitchCatchRepository snitchCatchRepository,
                            QuidditchPlayerRepository quidditchPlayerRepository,
                            PlayerService playerService,
                            TeamService teamService) {
        this.leagueRepository = leagueRepository;
        this.quidditchMatchRepository = quidditchMatchRepository;
        this.quidditchTeamRepository = quidditchTeamRepository;
        this.snitchCatchRepository = snitchCatchRepository;
        this.quidditchPlayerRepository = quidditchPlayerRepository;

        this.playerService = playerService;
        this.teamService = teamService;
    }

    /**
     * Records a match for two teams.
     *
     * @param winnerTeamName Name of winning team
     * @param loserTeamName Name of losing team
     * @param winnerScore Score winning team received
     * @param loserScore Score losing team received
     * @param catcherUuid Can be null. UUID of player that caught snitch
     *
     * @return Match result
     */
    public QuidditchMatch recordMatch(String winnerTeamName,
                                 String loserTeamName,
                                 long winnerScore,
                                 long loserScore,
                                 String catcherUuid) {
        Team winner = teamService.getOrCreateTeamByNameAndType(winnerTeamName, TeamType.QUIDDITCH);
        Team loser = teamService.getOrCreateTeamByNameAndType(loserTeamName, TeamType.QUIDDITCH);

        QuidditchTeam winnerTeam = quidditchTeamRepository.getByTeamId(winner.getId());
        QuidditchTeam loserTeam = quidditchTeamRepository.getByTeamId(loser.getId());

        if(winnerTeam == null) {
            winnerTeam = createQuidditchTeam(winner);
        }

        if(loserTeam == null) {
            loserTeam = createQuidditchTeam(loser);
        }

        QuidditchMatch quidditchMatch = new QuidditchMatch();

        quidditchMatch.setMatchDate(new Date());

        quidditchMatch.setWinnerScore(winnerScore);
        quidditchMatch.setLoserScore(loserScore);

        winnerTeam.addPoints(winnerScore, loserScore);
        loserTeam.addPoints(loserScore, winnerScore);
        
        quidditchMatch.setWinner(winner);
        quidditchMatch.setLoser(loser);

        if(catcherUuid != null) {
            quidditchMatch.setSnitchCatcher(playerService.getOrAddPlayer(catcherUuid));
        }

        if(quidditchMatch.wasTie()) {
            winner.setDraws(winner.getDraws() + 1);
            loser.setDraws(loser.getDraws() + 1);
        } else {
            winner.setWins(winner.getWins() + 1);
            loser.setLosses(winner.getLosses() + 1);
        }

        quidditchTeamRepository.saveAll(Arrays.asList(winnerTeam, loserTeam));
        quidditchMatchRepository.save(quidditchMatch);

        teamService.updateTeams(Arrays.asList(winner, loser));

        return quidditchMatch;
    }

    /**
     * Retrieves a quidditch player profile for a target player.
     * If no profile exists, one is created but not saved.
     *
     * @param uuid UUID of player
     * @return Quidditch player object with their details
     */
    public QuidditchPlayer getPlayerProfile(String uuid) {
        Player player = playerService.getOrAddPlayer(uuid);

        return getPlayerProfile(player);
    }

    /**
     * Retrieves a quidditch player profile for a target player.
     * If no profile exists, one is created but not saved.
     *
     * @param uuid UUID of player
     * @return Quidditch player object with their details
     */
    public List<SnitchCatches> getSnitchCatches(String uuid) {
        Player player = playerService.getOrAddPlayer(uuid);

        return snitchCatchRepository.getAllForPlayer(player.getId());
    }

    /**
     * Retrieves a list of quidditch teams by providing a league ID.
     *
     * @param leagueId League Id for which to get teams
     *
     * @return list of quidditch teams in league. <b>Empty list</b> if league not found or league not for quidditch
     */
    public List<QuidditchTeam> getTeamsForLeague(long leagueId) {
        League league = leagueRepository.findById(leagueId).orElse(null);

        if(league == null || league.getLeagueType() != TeamType.QUIDDITCH) {
            return Collections.emptyList();
        }

        List<Long> ids = league.getTeams().stream().map(Team::getId).collect(Collectors.toList());

        return quidditchTeamRepository.getQuidditchTeamsInTeamsList(ids);
    }

    /**
     * Gets Team by team Id.
     *
     * @param teamId Team ID as seen in database
     * @return Team with given Team ID or <i>null</i> if not found
     */
    public QuidditchTeam getByTeamId(long teamId) {
        return quidditchTeamRepository.getByTeamId(teamId);
    }

    /**
     * Gets all matches for a team.
     *
     * @param teamId Team ID as saved in database
     * @return List of matches team has played in. This is not ordered
     */
    public List<QuidditchMatch> retrieveAllMatchesForTeam(long teamId) {
        return quidditchMatchRepository.findAllByTeam(teamId);
    }

    /**
     * Gets all teams for quidditch in order of wins descending.
     *
     * @return All quidditch teams in descending order by number of wins
     */
    public List<QuidditchTeam> getTeams() {
        return quidditchTeamRepository.findAll(Sort.by("team.wins").descending());
    }

    private QuidditchTeam createQuidditchTeam(Team team) {
        QuidditchTeam quidditchTeam = new QuidditchTeam();

        quidditchTeam.setTeam(team);

        quidditchTeamRepository.save(quidditchTeam);
        return quidditchTeam;
    }

    private QuidditchPlayer getPlayerProfile(Player player) {
        QuidditchPlayer quidditchPlayer = quidditchPlayerRepository.getByPlayerId(player.getId());

        if(quidditchPlayer == null) {
            quidditchPlayer = new QuidditchPlayer();
            quidditchPlayer.setPlayer(player);
        }


        return quidditchPlayer;
    }
}
