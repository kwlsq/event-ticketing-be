package com.purwafest.purwafest.event.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.image.domain.entities.Image;
import com.purwafest.purwafest.event.domain.entities.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDetailsResponse {
  private Integer id;
  private String name;
  private String description;
  private Instant date;
  private String venue;
  private String location;
  private List<Image> images;
  private List<Review> reviews;
  private List<TicketTypeResponse> eventTicketTypes;

  public static EventDetailsResponse toResponse(Event event) {
    EventDetailsResponse response = new EventDetailsResponse();
    response.id = event.getId();
    response.name = event.getName();
    response.description = event.getDescription();
    response.date = event.getDate();
    response.venue = event.getVenue();
    response.location = event.getLocation();
    response.images = new ArrayList<>(event.getImages());
    response.reviews = new ArrayList<>(event.getReviews());

    List<EventTicketType> eventTicketTypeList = new ArrayList<>(event.getTicketTypes());
    response.eventTicketTypes = TicketTypeResponse.toResponse(eventTicketTypeList);

    return response;
  }
}
