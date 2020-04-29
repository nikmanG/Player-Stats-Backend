package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchTeam;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchMatchRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchTeamRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class QuidditchService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final QuidditchMatchRepository quidditchMatchRepository;
    private final QuidditchTeamRepository quidditchTeamRepository;

    public QuidditchService(TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            QuidditchMatchRepository quidditchMatchRepository,
                            QuidditchTeamRepository quidditchTeamRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.quidditchMatchRepository = quidditchMatchRepository;
        this.quidditchTeamRepository = quidditchTeamRepository;
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
        Team winner = teamRepository.findTeamByNameAndType(winnerTeamName, TeamType.QUIDDITCH.toString());
        Team loser = teamRepository.findTeamByNameAndType(loserTeamName, TeamType.QUIDDITCH.toString());

        if(winner == null) {
            winner = createTeam(winnerTeamName);
        }

        if(loser == null) {
            loser = createTeam(winnerTeamName);
        }

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

        winner.setWins(winner.getWins() + 1);
        loser.setLosses(winner.getLosses() + 1);
        
        quidditchMatch.setWinner(winner);
        quidditchMatch.setLoser(loser);

        if(catcherUuid != null) {
            quidditchMatch.setSnitchCatcher(playerRepository.findByUuid(catcherUuid));
        }

        quidditchTeamRepository.saveAll(Arrays.asList(winnerTeam, loserTeam));
        quidditchMatchRepository.save(quidditchMatch);
        teamRepository.saveAll(Arrays.asList(winner, loser));

        return quidditchMatch;
    }

    public QuidditchTeam getByTeamId(long teamId) {
        return quidditchTeamRepository.getByTeamId(teamId);
    }

    public List<QuidditchMatch> retrieveAllMatchesForTeam(long teamId) {
        return quidditchMatchRepository.findAllByTeam(teamId);
    }

    public List<QuidditchTeam> getTeams() {
        return quidditchTeamRepository.findAll(Sort.by("team.wins").descending());
    }

    private Team createTeam(String name) {
        Team team = new Team();

        team.setName(name);
        team.setTeamType(TeamType.QUIDDITCH);

        teamRepository.save(team);

        return team;
    }

    private QuidditchTeam createQuidditchTeam(Team team) {
        QuidditchTeam quidditchTeam = new QuidditchTeam();

        quidditchTeam.setTeam(team);

        quidditchTeamRepository.save(quidditchTeam);
        return quidditchTeam;
    }
}
