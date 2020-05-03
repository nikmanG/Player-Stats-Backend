package io.github.nikmang.playerinfo.models;


import java.util.Date;

public interface Match {

    boolean wasTie();

    Date getMatchDate();

    Competitor getWinner();
}
