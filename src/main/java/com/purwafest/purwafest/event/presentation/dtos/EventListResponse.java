package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.Event;
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
    eventListRequest.startingPrice = getStartingPrice(event);

    event.getImages().forEach(image -> {
      if (image.isThumbnail()) {
        eventListRequest.thumbnailUrl = image.getUrl();
      }
    });

    return eventListRequest;
  }

  private static BigInteger getStartingPrice(Event event) {
//    set default the lowest price to 0
    final BigInteger[] lowestPrice = {BigInteger.ZERO};

    List<EventTicketType> eventTicketType = new ArrayList<>(event.getTicketTypes());

    eventTicketType.forEach(ticketType -> {
      BigInteger ticketPrize = ticketType.getPrice();
      if (ticketPrize.compareTo(lowestPrice[0]) > 0) {
        lowestPrice[0] = ticketPrize;
      }
    });

    return lowestPrice[0];
  }
}
