package com.purwafest.purwafest.event.infrastructure.repositories;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    @Query("SELECT COUNT(*) FROM Event e WHERE e.user.id = :userId")
    Integer countEventByUserId(Integer userId);

    @Query("SELECT e FROM Event e WHERE e.user.id = :userId AND e.deletedAt IS NULL")
    List<Event> findAllByUser_Id(Integer userId);
}
