package com.purwafest.purwafest.event.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.event.application.EventTicketTypeServices;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.presentation.dtos.EventTicketTypeRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticketType")
public class EventTicketTypeRestController {
  private final EventTicketTypeServices eventTicketTypeServices;

  public EventTicketTypeRestController(EventTicketTypeServices eventTicketTypeServices) {
    this.eventTicketTypeServices = eventTicketTypeServices;
  }

  @PostMapping("/create/{eventID}")
  public ResponseEntity<?> createTicketType(@RequestBody @Valid EventTicketTypeRequest request,
                                            @PathVariable Integer eventID) {

    EventTicketType ticketType = eventTicketTypeServices.createTicketType(request, eventID);
    return Response.successfulResponse(
        "Success to create ticket type!",
        ticketType
    );
  }
}
