package io.github.nikmang.playerinfo.models.quidditch;

import io.github.nikmang.playerinfo.models.Match;
import io.github.nikmang.playerinfo.models.Player;
import io.github.nikmang.playerinfo.models.Team;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "Quidditch_Match")
public class QuidditchMatch implements Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @NotNull
    private Team winner;

    @OneToOne
    @NotNull
    private Team loser;

    @NotNull
    private long winnerScore;

    @NotNull
    private long loserScore;

    @OneToOne
    private Player snitchCatcher;

    @NotNull
    private Date matchDate;

    @Override
    public boolean wasTie() {
        return winnerScore == loserScore && snitchCatcher == null;
    }
}
