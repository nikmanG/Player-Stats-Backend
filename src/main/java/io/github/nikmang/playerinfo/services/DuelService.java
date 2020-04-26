package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import io.github.nikmang.playerinfo.repositories.PlayerRepository;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelMatchRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelPlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DuelService {

    private static final int k = 24;

    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private DuelPlayerRepository duelPlayerRepository;
    private DuelMatchRepository duelMatchRepository;

    public DuelService(
            PlayerRepository playerRepository,
            TeamRepository teamRepository,
            DuelPlayerRepository duelPlayerRepository,
            DuelMatchRepository duelMatchRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.duelPlayerRepository = duelPlayerRepository;
        this.duelMatchRepository = duelMatchRepository;
    }

    /**
     * Saves and returns match between two players.
     * If player is not found then an entry is created for them.
     *
     * @param winnerUuid UUID of winner
     * @param loserUuid UUID of loser
     *
     * @return Match record with updated elo information for both players
     */
    public DuelMatch recordMatch(String winnerUuid, String loserUuid) {
        Player winner = playerRepository.findByUuid(winnerUuid);
        Player loser = playerRepository.findByUuid(loserUuid);

        if(winner == null) {
            winner = createPlayer(winnerUuid);
        }

        if(loser == null) {
            loser = createPlayer(loserUuid);
        }

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

    /**
     * Retrieves all duel teams in the system.
     *
     * @return All teams that apply to dueling
     */
    public List<Team> getTeams() {
        return teamRepository.findAllTeamsByType(TeamType.DUEL.toString());
    }

    /**
     * Retrieve {@linkplain DuelPlayer} information for a given player.
     * If player does not have a profile, a new one is created (but not saved to system).
     *
     * @param uuid UUID of player
     *
     * @return information on player's duels
     */
    public DuelPlayer getPlayerProfile(String uuid) {
        Player player = playerRepository.findByUuid(uuid);

        if(player == null) {
            player = createPlayer(uuid);
        }

        return getPlayerProfile(player);
    }

    /**
     * Retrieve {@linkplain DuelPlayer} information for a given player.
     * If player does not have a profile, a new one is created (but not saved to system).
     *
     * @param playerId ID of player as saved in the database
     *
     * @return information on player's duels. Will return <b>null</b> if player profile cannot be found.
     */
    public DuelPlayer getPlayerProfile(long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if(player == null) {
            return null;
        }

        return getPlayerProfile(player);
    }

    /**
     * Gets all matches that a player has dueled in.
     *
     * @param playerId ID of player as saved in the database
     *
     * @return List of matches that the player was in
     */
    public List<DuelMatch> retrieveAllMatchesForPlayer(long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if(player == null) {
            return Collections.emptyList();
        }

        return duelMatchRepository.findAllByPlayer(player.getId());
    }

    /**
     * Gets all matches that a player has dueled in <i>and won</i>.
     *
     * @param playerId ID of player as saved in the database
     *
     * @return List of matches that the player was in
     */
    public List<DuelMatch> retrieveAllWonMatchesForPlayer(long playerId) {
        Player player = playerRepository.findById(playerId).orElse(null);

        if(player == null) {
            return Collections.emptyList();
        }

        List<DuelMatch> allMatches = retrieveAllMatchesForPlayer(playerId);

        return allMatches.stream().filter(m -> m.getWinner().equals(player)).collect(Collectors.toList());
    }

    /**
     * Retrieves all players that have dueling profiles.
     * Sorted by elo amount descending.
     *
     * @return List of duel profiles
     */
    public List<DuelPlayer> getAllPlayers() {
        return duelPlayerRepository.findAll(Sort.by("elo").descending());
    }

    // Gets player profile. Does not save a new one if one is created.
    private DuelPlayer getPlayerProfile(Player player) {
        DuelPlayer duelPlayer = duelPlayerRepository.findByPlayer(player.getId());

        if(duelPlayer == null) {
            duelPlayer = new DuelPlayer();

            duelPlayer.setPlayer(player);
        }

        return duelPlayer;
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

    private Player createPlayer(String uuid) {
        Player player = new Player();
        player.setUuid(uuid);
        player = playerRepository.save(player);

        return player;
    }
}
