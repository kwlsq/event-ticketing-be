package com.purwafest.purwafest.event.presentation;

import com.purwafest.purwafest.event.application.EventServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventRestController {
  private final EventServices eventServices;

  public EventRestController(EventServices eventServices) {
    this.eventServices = eventServices;
  }

  @PostMapping("/create")
  public Event createEvent(@RequestBody EventRequest request) {
    return eventServices.createEvent(request);
  }

  @GetMapping
  public List<Event> getAllEvent() {
    return eventServices.getAllEvent();
  }
}
