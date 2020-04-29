package io.github.nikmang.playerinfo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.nikmang.playerinfo.enums.TeamType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "Team", uniqueConstraints = {
        @UniqueConstraint(columnNames =  {"name", "teamType"})
})
@EqualsAndHashCode(exclude = "players")
@ToString(exclude = "players")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min = 3, max = 20, message = "Name can only be between 3 and 20 letters alphanumeric")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "Player_Team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    Set<Player> players;

    private String logoUrl;

    @ColumnDefault("0")
    private int wins;

    @ColumnDefault("0")
    private int losses;
}
