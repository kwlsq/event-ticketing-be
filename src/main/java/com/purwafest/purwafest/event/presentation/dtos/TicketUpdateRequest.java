package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.enums.TicketStatus;
import lombok.Data;

import java.util.List;

@Data
public class TicketUpdateRequest {
  private List<Integer> ticketList;
  private TicketStatus status;
}
