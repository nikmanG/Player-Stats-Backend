package io.github.nikmang.playerinfo.models.duelling;

import io.github.nikmang.playerinfo.models.Match;
import io.github.nikmang.playerinfo.models.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "Duel_Match")
public class DuelMatch implements Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @OneToOne
    private Player winner;

    @NotNull
    @OneToOne
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

    @Override
    public boolean wasTie() {
        return false;
    }
}
