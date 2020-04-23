package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelMatchRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelPlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DuelService {

    private static final int k = 24;

    private PlayerRepository playerRepository;
    private DuelPlayerRepository duelPlayerRepository;
    private DuelMatchRepository duelMatchRepository;

    public DuelService(
            PlayerRepository playerRepository,
            DuelPlayerRepository duelPlayerRepository,
            DuelMatchRepository duelMatchRepository) {
        this.playerRepository = playerRepository;
        this.duelPlayerRepository = duelPlayerRepository;
        this.duelMatchRepository = duelMatchRepository;
    }

    public DuelMatch recordMatch(String winnerUuid, String loserUuid) {
        Player winner = playerRepository.findByUuid(winnerUuid);
        Player loser = playerRepository.findByUuid(loserUuid);

        DuelPlayer winnerDuelPlayer = getPlayerProfile(winnerUuid);
        DuelPlayer loserDuelPlayer = getPlayerProfile(loserUuid);

        long[] updatedElo = calculateElo(winnerDuelPlayer.getElo(), loserDuelPlayer.getElo());

        DuelMatch duelMatch = new DuelMatch();
        duelMatch.setWinner(winner);
        duelMatch.setLoser(loser);

        duelMatch.setNewWinnerElo(updatedElo[0]);
        duelMatch.setOldWinnerElo(winnerDuelPlayer.getElo());

        duelMatch.setNewLoserElo(updatedElo[1]);
        duelMatch.setOldLoserElo(loserDuelPlayer.getElo());

        duelMatch.setMatchDate(new Date());

        winnerDuelPlayer.setWins(winnerDuelPlayer.getWins() + 1);
        loserDuelPlayer.setLosses(loserDuelPlayer.getLosses() + 1);

        winnerDuelPlayer.setElo(updatedElo[0]);
        loserDuelPlayer.setElo(updatedElo[1]);

        duelMatchRepository.save(duelMatch);
        duelPlayerRepository.save(winnerDuelPlayer);
        duelPlayerRepository.save(loserDuelPlayer);

        return duelMatch;
    }

    // Retrieves player profile or creates a new one
    // Does NOT save new profile if created
    public DuelPlayer getPlayerProfile(String uuid) {
        Player player = playerRepository.findByUuid(uuid);
        DuelPlayer duelPlayer = duelPlayerRepository.findByPlayer(player.getId());

        if(duelPlayer == null) {
            duelPlayer = new DuelPlayer();

            duelPlayer.setPlayer(player);
        }

        return duelPlayer;
    }

    public List<DuelMatch> retrieveAllMatchesForPlayer(String uuid) {
        Player player = playerRepository.findByUuid(uuid);

        return duelMatchRepository.findAllByPlayer(player.getId());
    }

    public List<DuelMatch> retrieveAllWonMatchesForPlayer(String uuid) {
        Player player = playerRepository.findByUuid(uuid);

        List<DuelMatch> allMatches = retrieveAllMatchesForPlayer(uuid);

        return allMatches.stream().filter(m -> m.getWinner().equals(player)).collect(Collectors.toList());
    }

    public List<DuelPlayer> getAllPlayers() {
        return duelPlayerRepository.findAll(Sort.by("elo").descending());
    }

    // Returns {winner Elo, loser Elo}
    // Doing this with some redundant parts for the sake of understanding the algorithm
    private long[] calculateElo(long winnerElo, long loserElo) {
        long[] result = new long[2];

        double winnerProb = 1 / (1 + Math.pow(10, (loserElo - winnerElo)/400.0));
        double loserProb = 1 / (1 + Math.pow(10, (winnerElo - loserElo)/400.0));

        result[0] = Math.round(winnerElo + k * (1 - winnerProb));
        result[1] = Math.round(loserElo + k * (0 - loserProb));

        return result;
    }
}
