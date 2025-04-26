package com.purwafest.purwafest.event.application;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;

import java.util.List;

public interface EventServices {
  List<Event> getAllEvent();
  Event createEvent(EventRequest request);
}
