package io.github.nikmang.playerinfo.models;

import io.github.nikmang.playerinfo.enums.TeamType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "Team")
@EqualsAndHashCode(exclude = "players")
@ToString(exclude = "players")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Size(min = 3, max = 20, message = "Name can only be between 3 and 20 letters alphanumeric")
    @Column(unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamType teamType;

    @ManyToMany
    @JoinTable(
            name = "Player_Team",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    Set<Player> players;
}
