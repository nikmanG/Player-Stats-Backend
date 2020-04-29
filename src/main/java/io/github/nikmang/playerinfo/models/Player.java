package io.github.nikmang.playerinfo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@Table(name = "Player")
@EqualsAndHashCode(exclude = "teams")
@ToString(exclude = "teams")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min = 36, max = 36, message = "UUID is a fixed string at 36 characters")
    @Column(unique = true)
    private String uuid;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "Player_Team",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    Set<Team> teams;

    private String name;

    private Long timestampOfRetrieval;
}
