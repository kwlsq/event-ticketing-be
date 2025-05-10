package com.purwafest.purwafest.event.application.impl;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.event.application.EventTicketTypeServices;
import com.purwafest.purwafest.event.application.TicketServices;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.domain.entities.Ticket;
import com.purwafest.purwafest.event.domain.enums.TicketStatus;
import com.purwafest.purwafest.event.domain.exceptions.TicketNotFoundException;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.TicketRepository;
import com.purwafest.purwafest.event.presentation.dtos.TicketUpdateRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketServices {

  private final TicketRepository ticketRepository;
  private final EventTicketTypeServices eventTicketTypeServices;
  private final EventTicketTypeRepository eventTicketTypeRepository;
  private final UserRepository userRepository;

  public TicketServiceImpl (TicketRepository ticketRepository, EventTicketTypeServices eventTicketTypeServices, EventTicketTypeRepository eventTicketTypeRepository, UserRepository userRepository) {
    this.ticketRepository = ticketRepository;
    this.eventTicketTypeServices = eventTicketTypeServices;
    this.eventTicketTypeRepository = eventTicketTypeRepository;
    this.userRepository = userRepository;
  }

  @Override
  public void createTicket(Integer quantity, Integer ticketTypeID) {

    EventTicketType eventTicketType = eventTicketTypeRepository.findById(ticketTypeID).orElseThrow(() -> new RuntimeException("Ticket type not found!"));
    User user = userRepository.findById(Claims.getUserId()).orElseThrow(() -> new RuntimeException("User not found!"));

    if (quantity > eventTicketType.getStock()) {
      throw new ArithmeticException("Ticket stock insufficient!");
    }

//    Create list of ticket
    List<Ticket> ticketList = new ArrayList<>(quantity);

//    Count ticket that successfully created
    int createdTicket = 0;

    while (quantity > 0) {
      Ticket ticket = new Ticket();
      ticket.setEventTicketType(eventTicketType);
      ticket.setStatus(TicketStatus.BOOKED);
      ticket.setUser(user);
      ticketList.add(ticket);
      quantity--;
      createdTicket++;
    }

//    Update the ticket stock from organizer
    eventTicketTypeServices.updateTicketType(eventTicketType.getId(), createdTicket);

    ticketRepository.saveAll(ticketList);
  }

  @Override
  public List<Ticket> updateTicketStatus(TicketUpdateRequest request) {
    List<Ticket> ticketList = ticketRepository.findAllById(request.getTicketList());
    for (Ticket ticket : ticketList) {
      ticket.setStatus(request.getStatus());
      ticket.setModifiedAt(Instant.now());
    }
    return ticketRepository.saveAll(ticketList);
  }

  @Override
  public List<Ticket> getTicketByUserID(Integer userID) {
    return ticketRepository.findAllByUser_Id(userID);
  }

  @Override
  public List<Ticket> getAllTicket() {
    return ticketRepository.findAll();
  }
}
