package com.purwafest.purwafest.event.application.impl;

import com.purwafest.purwafest.common.PaginatedResponse;
import com.purwafest.purwafest.event.application.EventServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.domain.enums.EventStatus;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.specification.EventSpecification;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventServices {
  private final EventRepository eventRepository;

  public EventServiceImpl (EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public PaginatedResponse<Event> getAllEvent(Pageable pageable, String search) {

    Page<Event> data = eventRepository.findAll(EventSpecification.getFilteredEvent(search), pageable).map(event -> event);

    PaginatedResponse<Event> paginatedResponse = new PaginatedResponse<>();
    paginatedResponse.setPage(pageable.getPageNumber());
    paginatedResponse.setSize(pageable.getPageSize());
    paginatedResponse.setTotalElements(data.getTotalElements());
    paginatedResponse.setTotalPages(data.getTotalPages());
    paginatedResponse.setContent(data.getContent());

    return paginatedResponse;
  }

  @Override
  public Event createEvent(EventRequest request) {
    Event event = request.toEvent();
    return eventRepository.save(event);
  }
}
