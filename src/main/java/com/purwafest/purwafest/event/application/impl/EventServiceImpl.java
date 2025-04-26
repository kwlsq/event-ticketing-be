package com.purwafest.purwafest.event.application.impl;

import com.purwafest.purwafest.event.application.EventServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.enums.EventStatus;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventServices {
  private final EventRepository eventRepository;

  public EventServiceImpl (EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public List<Event> getAllEvent() {
    return eventRepository.findAll();
  }

  @Override
  public Event createEvent(EventRequest request) {
    Event event = request.toEvent();
    return eventRepository.save(event);
  }
}
