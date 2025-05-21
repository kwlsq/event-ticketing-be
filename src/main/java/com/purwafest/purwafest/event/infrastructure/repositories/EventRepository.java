package com.purwafest.purwafest.event.infrastructure.repositories;

import com.purwafest.purwafest.event.domain.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    @Query("SELECT COUNT(*) FROM Event e WHERE e.user.id = :userId")
    Integer countEventByUserId(Integer userId);
}
