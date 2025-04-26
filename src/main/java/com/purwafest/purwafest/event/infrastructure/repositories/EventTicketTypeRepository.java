package com.purwafest.purwafest.event.infrastructure.repositories;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTicketTypeRepository extends JpaRepository<EventTicketType, Integer> {
}
