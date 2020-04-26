package io.github.nikmang.playerinfo.models.quidditch;

import io.github.nikmang.playerinfo.models.Team;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Quidditch_Team")
public class QuidditchTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    @NotNull
    private Team team;

    @ColumnDefault("0")
    private long pointsFor;

    @ColumnDefault("0")
    private long pointsAgainst;

    public void addPoints(long pointsFor, long pointsAgainst) {
        this.pointsFor += pointsFor;
        this.pointsAgainst += pointsAgainst;
    }
}
