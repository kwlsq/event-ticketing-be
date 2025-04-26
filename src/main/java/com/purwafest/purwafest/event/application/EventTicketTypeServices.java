package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;

public interface EventTicketTypeServices {
  EventTicketType createTicketType(EventTicketType eventTicketType);
  EventTicketType updateTicketType(Integer ticketTypeID, Integer ticketBought);
}
