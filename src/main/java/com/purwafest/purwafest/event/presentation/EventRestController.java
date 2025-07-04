package com.purwafest.purwafest.event.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.event.application.EventServices;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  @GetMapping("/public")
  public ResponseEntity<?> getEventList(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size,
                                        @RequestParam(value = "sort", defaultValue = "id") String sort,
                                        @RequestParam(value = "search", defaultValue = "") String search,
                                        @RequestParam(value = "location", defaultValue = "") String location,
                                        @RequestParam(value = "category", defaultValue = "") Integer category) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(getSortOrder(sort)));

    return Response.successfulResponse(
        "Event fetched successfully!",
        eventServices.getAllEvent(pageable, search, location, category)
    );
  }

  @GetMapping("/public/{eventID}")
  public ResponseEntity<?> getCurrentEvent(@PathVariable Integer eventID) {
    return Response.successfulResponse(
        "Fetched event successfully!",
        eventServices.getCurrentEvent(eventID)
    );
  }

  @DeleteMapping("/{eventID}")
  public ResponseEntity<?> deleteEvent(@PathVariable Integer eventID) {
    try {
      eventServices.deleteEvent(eventID);
      return ResponseEntity.ok("Event deleted!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(e.getMessage());
    }
  }

  private Sort.Order getSortOrder(String sort) {
    String[] sortParts = sort.split(",");
    String property = sortParts[0];
    Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1])
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;
    return Sort.Order.by(property).with(direction);
  }

  @GetMapping("/admin")
  public List<Event> getEvents() {
    return eventServices.getEvents();
  }
}
