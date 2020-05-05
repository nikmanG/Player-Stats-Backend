package io.github.nikmang.playerinfo.models.events;

import io.github.nikmang.playerinfo.enums.EventType;
import io.github.nikmang.playerinfo.models.Competitor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name="event_sequence", strategy = "increment")
    private long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @NotNull
    private Date matchDate;

    public abstract Competitor getCompetitor1();

    public abstract Competitor getCompetitor2();
}
