package com.purwafest.purwafest.event.infrastructure.repositories;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.event.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

  @Query("SELECT t FROM Ticket t WHERE t.user.id = :userId AND t.deletedAt IS NULL")
  List<Ticket> findAllByUser_Id(Integer userId);
}
