package io.github.nikmang.playerinfo.models.duelling;

import io.github.nikmang.playerinfo.models.Player;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Duel_Player")
public class DuelPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    @NotNull
    private Player player;

    private long elo = 1200;

    @ColumnDefault("0")
    private int wins;

    @ColumnDefault("0")
    private int losses;
}
