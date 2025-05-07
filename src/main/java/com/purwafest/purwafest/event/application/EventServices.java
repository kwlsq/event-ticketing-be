package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.common.PaginatedResponse;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventServices {
  PaginatedResponse<Event> getAllEvent(Pageable pageable, String search);
  Event createEvent(EventRequest request);
}
