package io.github.nikmang.playerinfo.models.events;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.models.Team;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "Team_Match_Event")
@EqualsAndHashCode(callSuper = true)
public class TeamMatchEvent extends MatchEvent {

    @NotNull
    @NotNull
    @OneToOne
    private Team team1;

    @NotNull
    @OneToOne
    private Team team2;
}
