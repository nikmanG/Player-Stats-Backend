package io.github.nikmang.playerinfo.models.events;

import io.github.nikmang.playerinfo.enums.EventType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private EventType eventType;

    @NotNull
    private Date matchDate;
}
