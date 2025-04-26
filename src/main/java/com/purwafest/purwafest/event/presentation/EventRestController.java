package com.purwafest.purwafest.event.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.event.application.EventServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<?> createEvent(@RequestBody @Valid EventRequest request) {
    return Response.successfulResponse(
        "Success to create event!",
        eventServices.createEvent(request)
    );
  }

  @GetMapping
  public List<Event> getAllEvent() {
    return eventServices.getAllEvent();
  }
}
