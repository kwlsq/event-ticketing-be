package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.domain.entities.Ticket;
import com.purwafest.purwafest.event.domain.enums.TicketStatus;
import com.purwafest.purwafest.event.presentation.dtos.TicketUpdateRequest;

import java.util.List;

public interface TicketServices {
  List<Ticket> createTicket(EventTicketType eventTicketType, Integer quantity);
  List<Ticket> updateTicketStatus(TicketUpdateRequest request);
  Ticket deleteTicket(Integer ticketID);
}
