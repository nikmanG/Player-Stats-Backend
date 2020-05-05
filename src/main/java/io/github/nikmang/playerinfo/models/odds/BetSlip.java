package io.github.nikmang.playerinfo.models.odds;

import io.github.nikmang.playerinfo.enums.EventType;
import lombok.Data;

import java.util.List;

@Data
public class BetSlip {

    private String name;

    private List<Bet> bets;
}

@Data
class Bet2 {
    EventType eventType;
    long matchId;
    Long chosenId;
    double galleons;
}
