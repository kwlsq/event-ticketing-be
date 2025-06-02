package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.common.PaginatedResponse;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventDetailsResponse;
import com.purwafest.purwafest.event.presentation.dtos.EventListResponse;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventServices {
  PaginatedResponse<EventListResponse> getAllEvent(Pageable pageable, String search, String location);
  Event createEvent(EventRequest request);
  void deleteEvent(Integer eventID);
  Event updateEvent(EventRequest newEvent);
  EventDetailsResponse getCurrentEvent(Integer eventID);
  List<Event> getEvents();
}
