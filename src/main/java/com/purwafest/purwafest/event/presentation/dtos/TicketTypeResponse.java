package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeResponse {
  private Integer id;
  private String name;
  private Instant date;
  private Integer availableQty;
  private BigInteger price;

  public static List<TicketTypeResponse> toResponse(List<EventTicketType> request) {
    List<TicketTypeResponse> responseList = new ArrayList<>();

    request.forEach(eventTicketType -> {

//      Match the data in ticket type with the DTO
      TicketTypeResponse ticketTypeResponse = new TicketTypeResponse();
      ticketTypeResponse.id = eventTicketType.getId();
      ticketTypeResponse.name = eventTicketType.getName();
      ticketTypeResponse.date = eventTicketType.getSellDate();
      ticketTypeResponse.availableQty = eventTicketType.getAvailableQty();
      ticketTypeResponse.price = eventTicketType.getPrice();

//      Save the ticket type to show to user
      responseList.add(ticketTypeResponse);
    });

    return responseList;
  }
}
