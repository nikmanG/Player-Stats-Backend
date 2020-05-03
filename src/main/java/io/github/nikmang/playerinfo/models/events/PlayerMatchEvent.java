package io.github.nikmang.playerinfo.models.events;

import io.github.nikmang.playerinfo.models.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Player_Match_Event")
@EqualsAndHashCode(callSuper = true)
public class PlayerMatchEvent extends MatchEvent {

    @NotNull
    @NotNull
    @OneToOne
    private Player player1;

    @NotNull
    @OneToOne
    private Player player2;
}
