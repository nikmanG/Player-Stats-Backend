package io.github.nikmang.playerinfo.models.quidditch;

import io.github.nikmang.playerinfo.models.Player;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "Snitch_Catches")
public class SnitchCatches {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @NotNull
    private Player catcher;

    @OneToOne
    @NotNull
    private Player opponent;

    @ColumnDefault("0")
    private long timeLength;
}
