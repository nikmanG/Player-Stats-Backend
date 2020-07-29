package io.github.nikmang.playerinfo.models.quidditch;

import io.github.nikmang.playerinfo.models.Player;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Quidditch_Player")
public class QuidditchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @NotNull
    private Player player;

    @ColumnDefault("0")
    private int seekerGames;

    @ColumnDefault("0")
    private int beaterGames;

    @ColumnDefault("0")
    private int chaserGames;

    @ColumnDefault("0")
    private int keeperGames;

    @ColumnDefault("0")
    private int goalsScored;

    @ColumnDefault("0")
    private int goalsConceded;
}
