package com.purwafest.purwafest.event.application.impl;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.infrastructure.repository.UserRepository;
import com.purwafest.purwafest.common.PaginatedResponse;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.event.application.EventServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.domain.enums.EventStatus;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.EventTicketTypeRepository;
import com.purwafest.purwafest.event.infrastructure.repositories.specification.EventSpecification;
import com.purwafest.purwafest.event.presentation.dtos.EventDetailsResponse;
import com.purwafest.purwafest.event.presentation.dtos.EventListResponse;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventServices {
  private final EventRepository eventRepository;
  private final UserRepository userRepository;
  private final EventTicketTypeRepository eventTicketTypeRepository;

  public EventServiceImpl (EventRepository eventRepository, UserRepository userRepository, EventTicketTypeRepository eventTicketTypeRepository) {
    this.eventRepository = eventRepository;
    this.userRepository = userRepository;
    this.eventTicketTypeRepository = eventTicketTypeRepository;
  }

  @Override
  public PaginatedResponse<EventListResponse> getAllEvent(Pageable pageable, String search, String location) {

    Page<Event> data = eventRepository.findAll(EventSpecification.getFilteredEvent(search, location), pageable).map(event -> event);

    List<EventListResponse> eventListResponses = new ArrayList<>();

    data.getContent().forEach(event -> {
      EventListResponse response = EventListResponse.toResponse(event);
      eventListResponses.add(response);
    });

    return getEventListResponsePaginatedResponse(pageable, data, eventListResponses);
  }

  private static PaginatedResponse<EventListResponse> getEventListResponsePaginatedResponse(Pageable pageable, Page<Event> data, List<EventListResponse> eventListResponses) {
    boolean hasNext = data.getNumber() < data.getTotalPages() - 1;
    boolean hasPrevious = data.getNumber() > 0;

    PaginatedResponse<EventListResponse> paginatedResponse = new PaginatedResponse<>();
    paginatedResponse.setPage(pageable.getPageNumber());
    paginatedResponse.setSize(pageable.getPageSize());
    paginatedResponse.setTotalElements(data.getTotalElements());
    paginatedResponse.setTotalPages(data.getTotalPages());
    paginatedResponse.setContent(eventListResponses);
    paginatedResponse.setHasNext(hasNext);
    paginatedResponse.setHasPrevious(hasPrevious);
    return paginatedResponse;
  }

  @Override
  public Event createEvent(EventRequest request) {
    Integer userID = Claims.getUserId();
    Optional<User> user = userRepository.findById(userID);

    if (user.isPresent()) {
//      Create event first to get eventID
      Event event = request.toEvent();
      event.setUser(user.get());
      event.setStatus(EventStatus.UPCOMING);
      eventRepository.save(event);

//      Parse sell date to Instant data type
      Instant sellDate = Instant.parse(request.getTicketSaleDate());

      request.getTicketTypeRequest().forEach(eachEventTicketType -> {
        EventTicketType eventTicketType = eachEventTicketType.toEventTicketType();
        eventTicketType.setEvent(event);
        eventTicketType.setSellDate(sellDate);
        eventTicketTypeRepository.save(eventTicketType);
      });

//      Return created event
      return event;
    }

    return null;
  }

  @Override
  @Transactional
  public void deleteEvent(Integer eventID) {
    Optional<Event> optionalEvent = eventRepository.findById(eventID);
    if(optionalEvent.isPresent()) {
      Event event = optionalEvent.get();
//      Soft delete the event
      event.setDeletedAt(Instant.now());
      eventRepository.save(event);
    } else {
      throw new RuntimeException("Event not found!");
    }
  }

  @Override
  public Event updateEvent(EventRequest newEvent) {
    Event event = newEvent.toEvent();

    Event savedEvent = eventRepository.findById(event.getId()).orElseThrow(() -> new RuntimeException("Event not found!"));

    savedEvent.setName(event.getName());
    savedEvent.setCategory(event.getCategory());
    savedEvent.setDate(event.getDate());
    savedEvent.setDescription(event.getDescription());
    savedEvent.setStatus(event.getStatus());
    savedEvent.setVenue(event.getVenue());
    savedEvent.setLocation(event.getLocation());

    return savedEvent;
  }

  @Override
  public EventDetailsResponse getCurrentEvent(Integer eventID) {
    Event event = eventRepository.findById(eventID).orElseThrow(() -> new RuntimeException("Event not found!"));
    return EventDetailsResponse.toResponse(event);
  }

  @Override
  public List<Event> getEvents() {
    return eventRepository.findAll();
  }
}
