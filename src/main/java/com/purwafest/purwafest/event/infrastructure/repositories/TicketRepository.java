package com.purwafest.purwafest.event.infrastructure.repositories;

import com.purwafest.purwafest.event.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
