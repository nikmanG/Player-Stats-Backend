package io.github.nikmang.playerinfo.models.duelling;

import io.github.nikmang.playerinfo.models.Player;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "Duel_Match")
public class DuelMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    @NotNull
    private Player winner;

    @OneToOne
    @NotNull
    private Player loser;

    @NotNull
    private long oldWinnerElo;

    @NotNull
    private long oldLoserElo;

    @NotNull
    private long newWinnerElo;

    @NotNull
    private long newLoserElo;

    @NotNull
    private Date matchDate;
}
