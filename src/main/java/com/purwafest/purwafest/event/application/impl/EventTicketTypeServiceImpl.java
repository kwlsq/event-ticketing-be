package com.purwafest.purwafest.event.application.impl;

import com.purwafest.purwafest.event.application.EventTicketTypeServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import com.purwafest.purwafest.event.presentation.dtos.EventTicketTypeRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Service
public class EventTicketTypeServiceImpl implements EventTicketTypeServices {
  private final EventTicketTypeRepository eventTicketTypeRepository;
  private final EventRepository eventRepository;

  public EventTicketTypeServiceImpl (EventTicketTypeRepository eventTicketTypeRepository, EventRepository eventRepository) {
    this.eventTicketTypeRepository = eventTicketTypeRepository;
    this.eventRepository = eventRepository;
  }

  @Override
  public EventTicketType createTicketType (EventTicketTypeRequest eventTicketTypeRequest, Integer eventID) {
    Event event = eventRepository.findById(eventID).orElseThrow(() -> new RuntimeException("Event not found!"));
    Set<EventTicketType> ticketType = event.getTicketTypes();

    // check if ticket type with the same name already exist
    boolean ticketExist = ticketType.stream()
        .anyMatch(ticket -> ticket.getName().equalsIgnoreCase(eventTicketTypeRequest.getName()));

    if(ticketExist) {
      throw new RuntimeException("Ticket type with the same name already exist!");
    }

    EventTicketType eventTicketType = eventTicketTypeRequest.toEventTicketType();
    eventTicketType.setEvent(event);
    return eventTicketTypeRepository.save(eventTicketType);
  }

  @Override
  public void updateTicketType(Integer ticketTypeID, Integer ticketBought) {
    Optional<EventTicketType> ticketTypeOptional = eventTicketTypeRepository.findById(ticketTypeID);
    if (ticketTypeOptional.isEmpty()) {
      throw new RuntimeException();
    }

    EventTicketType eventTicketType = ticketTypeOptional.get();
    eventTicketType.setAvailableQty(eventTicketType.getStock() - ticketBought);
    eventTicketType.setModifiedAt(Instant.now());
  }
}
