package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.event.domain.entities.Ticket;
import com.purwafest.purwafest.event.presentation.dtos.TicketUpdateRequest;

import java.util.List;

public interface TicketServices {
  void createTicket(Integer quantity, Integer ticketTypeID);
  List<Ticket> updateTicketStatus(TicketUpdateRequest request);
  List<Ticket> getTicketByUserID(Integer userID);
  List<Ticket> getAllTicket();
}
