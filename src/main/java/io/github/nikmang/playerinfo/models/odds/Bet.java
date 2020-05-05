package io.github.nikmang.playerinfo.models.odds;

import io.github.nikmang.playerinfo.enums.EventType;
import lombok.Data;

@Data
public class Bet {

    private EventType eventType;

    private long matchId;

    private Long chosenId;

    private double galleons;
}
