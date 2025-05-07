package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.enums.EventStatus;
import com.purwafest.purwafest.event.domain.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
  private String name;
  private String description;
  private String imageUrl;
  private String venue;
  private String location;
  private boolean isEventFree;
  private EventStatus status;

  public Event toEvent() {
    return Event.builder()
        .name(name)
        .description(description)
        .location(location)
        .venue(venue)
        .isEventFree(isEventFree)
        .status(status)
        .build();
  }
}
