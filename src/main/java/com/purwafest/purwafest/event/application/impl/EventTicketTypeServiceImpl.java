package com.purwafest.purwafest.event.application.impl;

import com.purwafest.purwafest.event.application.EventTicketTypeServices;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventTicketTypeServiceImpl implements EventTicketTypeServices {
  private final EventTicketTypeRepository eventTicketTypeRepository;

  public EventTicketTypeServiceImpl (EventTicketTypeRepository eventTicketTypeRepository) {
    this.eventTicketTypeRepository = eventTicketTypeRepository;
  }

  @Override
  public EventTicketType createTicketType (EventTicketType eventTicketType) {
    return eventTicketTypeRepository.save(eventTicketType);
  }

  @Override
  public EventTicketType updateTicketType(Integer ticketTypeID, Integer ticketBought) {
    Optional<EventTicketType> ticketTypeOptional = eventTicketTypeRepository.findById(ticketTypeID);
    if (ticketTypeOptional.isEmpty()) {
      throw new RuntimeException();
    }

    EventTicketType eventTicketType = ticketTypeOptional.get();
    eventTicketType.setStock(eventTicketType.getStock() - ticketBought);
    return  eventTicketType;
  }
}
