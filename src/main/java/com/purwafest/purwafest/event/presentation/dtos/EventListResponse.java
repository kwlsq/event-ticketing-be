package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventListResponse {
  private Integer id;
  private String name;
  private Instant date;
  private String venue;
  private String location;
  private boolean isEventFree;
  private BigInteger startingPrice;
  private String thumbnailUrl;

  public static EventListResponse toResponse(Event event) {
    EventListResponse eventListRequest = new EventListResponse();
    eventListRequest.id = event.getId();
    eventListRequest.name = event.getName();
    eventListRequest.date = event.getDate();
    eventListRequest.venue = event.getVenue();
    eventListRequest.location = event.getLocation();
    eventListRequest.isEventFree = event.isEventFree();
    return eventListRequest;
  }
}
