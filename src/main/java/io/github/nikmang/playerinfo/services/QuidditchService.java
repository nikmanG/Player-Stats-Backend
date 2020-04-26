package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.quidditch.QuidditchMatch;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.quidditch.QuidditchMatchRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QuidditchService {

    private TeamRepository teamRepository;
    private PlayerRepository playerRepository;
    private QuidditchMatchRepository quidditchMatchRepository;

    public QuidditchService(TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            QuidditchMatchRepository quidditchMatchRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.quidditchMatchRepository = quidditchMatchRepository;
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
                                 int winnerScore,
                                 int loserScore,
                                 String catcherUuid) {
        Team winner = teamRepository.findTeamByNameAndType(winnerTeamName, TeamType.QUIDDITCH.toString());
        Team loser = teamRepository.findTeamByNameAndType(loserTeamName, TeamType.QUIDDITCH.toString());

        if(winner == null) {
            winner = createTeam(winnerTeamName);
        }

        if(loser == null) {
            loser = createTeam(winnerTeamName);
        }

        QuidditchMatch quidditchMatch = new QuidditchMatch();

        quidditchMatch.setMatchDate(new Date());

        quidditchMatch.setWinnerScore(winnerScore);
        quidditchMatch.setLoserScore(loserScore);

        winner.setWins(winner.getWins() + 1);
        loser.setLosses(winner.getLosses() + 1);
        
        quidditchMatch.setWinner(winner);
        quidditchMatch.setLoser(loser);

        if(catcherUuid != null) {
            quidditchMatch.setSnitchCatcher(playerRepository.findByUuid(catcherUuid));
        }

        quidditchMatchRepository.save(quidditchMatch);
        teamRepository.save(winner);
        teamRepository.save(loser);

        return quidditchMatch;
    }

    private Team createTeam(String name) {
        Team team = new Team();

        team.setName(name);
        team.setTeamType(TeamType.QUIDDITCH);

        teamRepository.save(team);

        return team;
    }
}
