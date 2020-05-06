package io.github.nikmang.playerinfo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.nikmang.playerinfo.enums.TeamType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "League", uniqueConstraints = {
        @UniqueConstraint(columnNames =  {"name", "leagueType"})
})
@EqualsAndHashCode(exclude = "teams")
@ToString(exclude = "teams")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamType leagueType;

    @NotNull
    private String name;

    @JsonManagedReference
    @OneToMany
    public List<Team> teams;
}
