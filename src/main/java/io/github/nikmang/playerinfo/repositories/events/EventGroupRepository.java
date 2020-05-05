package io.github.nikmang.playerinfo.repositories.events;

import io.github.nikmang.playerinfo.models.events.EventGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EventGroupRepository extends JpaRepository<EventGroup, Long> {

    @Query(value = "SELECT * FROM Event_Group WHERE start_date >= ?1", nativeQuery = true)
    List<EventGroup> getEventsOnOrAfterDate(Date date);
}
