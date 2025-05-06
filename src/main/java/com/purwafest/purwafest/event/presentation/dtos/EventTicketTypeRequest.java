package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.domain.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventTicketTypeRequest {
  private String name;
  private BigInteger price;
  private Integer stock;

  public EventTicketType toEventTicketType() {
    return EventTicketType.builder()
        .name(name)
        .price(price)
        .stock(stock)
        .build();
  }
}
