package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.presentation.dtos.EventTicketTypeRequest;

public interface EventTicketTypeServices {
  EventTicketType createTicketType(EventTicketTypeRequest eventTicketTypeRequest, Integer eventID);
  EventTicketType updateTicketType(Integer ticketTypeID, Integer ticketBought);
}
