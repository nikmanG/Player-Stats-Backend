package io.github.nikmang.playerinfo.services;

import io.github.nikmang.playerinfo.enums.TeamType;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import io.github.nikmang.playerinfo.models.duelling.DuelMatch;
import io.github.nikmang.playerinfo.models.duelling.DuelPlayer;
import io.github.nikmang.playerinfo.repositories.TeamRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelMatchRepository;
import io.github.nikmang.playerinfo.repositories.duelling.DuelPlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class DuelService {

    private static final int k = 24;

    private TeamRepository teamRepository;
    private DuelPlayerRepository duelPlayerRepository;
    private DuelMatchRepository duelMatchRepository;

    private PlayerService playerService;

    public DuelService(TeamRepository teamRepository,
                       DuelPlayerRepository duelPlayerRepository,
                       DuelMatchRepository duelMatchRepository,
                       PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.duelPlayerRepository = duelPlayerRepository;
        this.duelMatchRepository = duelMatchRepository;

        this.playerService = playerService;
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
        Player winner = playerService.getOrAddPlayer(winnerUuid);
        Player loser = playerService.getOrAddPlayer(loserUuid);

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
     * Returns the scores of a team match
     * Scoring is done on the following basis:
     * <ul>
     *     <li>Each win is a point for given team.</li>
     *     <li>Winning team gets 3 extra points.</li>
     *     <li><i>If</i> there is a tie, then both teams get 1 bonus point.</li>
     *     <li>Top dueller (ladder rank 1) that wins also gets a bonus point for their team.</li>
     * </ul>
     *
     * @param team1 First team playing.
     * @param team2 Second team playing.
     * @param topPlayerTeam1 Top ranked player from team 1.
     * @param topPlayerTeam2 Top ranked player from team 2.
     * @param winnerUUIDs List of winner UUIDs. <b>Do not</b> have to be in rank order.
     *
     * @return array in format <i>{team 1 score, team 2 score}</i>
     */
    public int[] getTeamMatchResults(
            Team team1,
            Team team2,
            String topPlayerTeam1,
            String topPlayerTeam2,
            Set<String> winnerUUIDs) {
        int team1Score = 0;
        int team2Score = 0;

        for(Player player : team1.getPlayers()) {
            if(winnerUUIDs.contains(player.getUuid())) {
                team1Score++;
            }
        }

        for(Player player : team2.getPlayers()) {
            if(winnerUUIDs.contains(player.getUuid())) {
                team2Score++;
            }
        }

        if(team1Score > team2Score) {
            team1Score += 3;
        } else if(team2Score > team1Score) {
            team2Score += 3;
        } else {
            team1Score++;
            team2Score++;
        }

        if(winnerUUIDs.contains(topPlayerTeam1)) {
            team1Score++;
        } else {
            team1Score++;
        }

        return new int[]{team1Score, team2Score};
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
        Player player = playerService.getOrAddPlayer(uuid);

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
        Player player = playerService.getPlayer(playerId);

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
        return duelMatchRepository.findAllByPlayer(playerId);
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
}
