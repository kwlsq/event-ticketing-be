package com.purwafest.purwafest.event.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.purwafest.purwafest.event.domain.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
  private String name;
  private String description;
  private String date;
  private String venue;
  private String location;

  @JsonProperty("isEventFree")
  private boolean isEventFree;

  private List<EventTicketTypeRequest> ticketTypeRequest;
  private String ticketSaleDate;

  public Event toEvent() {

    Instant instant = Instant.parse(date);
    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("America/Toronto"));

    return Event.builder()
        .name(name)
        .description(description)
        .date(localDateTime.atZone(ZoneId.of("America/Toronto")).toInstant())
        .location(location)
        .venue(venue)
        .isEventFree(isEventFree)
        .build();
  }
}
