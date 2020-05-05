package io.github.nikmang.playerinfo.utils;

import io.github.nikmang.playerinfo.models.Competitor;
import io.github.nikmang.playerinfo.models.Match;

import java.util.List;

public class BettingCalculator {
    private static final int MAX_MATCHES = 30;

    private BettingCalculator() {
    }

    /**
     * Calculates odds on teams winning between each other.
     * Match count is capped at 30 to maintain relevance. Total matches taken into account is the minimum of
     * home team matches, away team matches and max match count (30).
     *
     * <b>All values provided must not be null.</b>
     *
     * @param home        Quidditch team set as home team
     * @param away        Quidditch team set as away team
     * @param homeMatches Matches home team was in
     * @param awayMatches Matches away team was in
     * @return double array in the form of {home win probability, draw probability, away win probability}
     */
    public static double[] getOdds(
            Competitor home,
            Competitor away,
            List<? extends Match> homeMatches,
            List<? extends Match> awayMatches) {
        int targetMatches = Math.min(Math.min(homeMatches.size(), awayMatches.size()), MAX_MATCHES);

        if (targetMatches == 0)
            return new double[]{1, 1, 1};

        Tally homeTally = getTally(targetMatches, home.getIdentifier(), homeMatches);
        Tally awayTally = getTally(targetMatches, away.getIdentifier(), awayMatches);

        double homePrice = homeTally.wins + awayTally.losses;
        double drawPrice = homeTally.draws + awayTally.draws;
        double awayPrice = awayTally.wins + homeTally.losses;

        double homeWin = homePrice / (targetMatches * 2.0);
        double drawWin = drawPrice / (targetMatches * 2.0);
        double awayWin = awayPrice / (targetMatches * 2.0);

        return new double[]{
                homeWin == 0 ? 0.0 : Math.round(100 / homeWin) / 100.0,
                drawWin == 0 ? 0.0 : Math.round(100 / drawWin) / 100.0,
                awayWin == 0 ? 0.0 : Math.round(100 / awayWin) / 100.0};
    }

    private static Tally getTally(long limit, long competitorIdentifier, List<? extends Match> matches) {
        return matches
                .stream()
                .sorted((d1, d2) -> d2.getMatchDate().compareTo(d1.getMatchDate()))
                .limit(limit)
                .map(d -> {
                    Tally tally = new Tally();

                    if (d.wasTie())
                        tally.draws++;
                    else if (d.getWinner().getIdentifier().equals(competitorIdentifier))
                        tally.wins++;
                    else
                        tally.losses++;

                    return tally;
                })
                .reduce((d1, d2) -> {
                    d1.wins += d2.wins;
                    d1.losses += d2.losses;

                    return d1;
                }).orElse(new Tally());
    }

    private static class Tally {
        int wins = 0;
        int losses = 0;
        int draws = 0;
    }
}
