package com.purwafest.purwafest.event.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.event.application.TicketServices;
import com.purwafest.purwafest.event.domain.entities.Ticket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ticket")
public class TicketRestController {
  private final TicketServices ticketServices;

  public TicketRestController(TicketServices ticketServices) {
    this.ticketServices = ticketServices;
  }

  @PostMapping("/create/{ticketTypeID}")
  public ResponseEntity<?> createTickets(@PathVariable Integer ticketTypeID,
                                    @RequestParam(value = "qty", defaultValue = "0") int qty) {

    ticketServices.createTicket(qty, ticketTypeID);

    return ResponseEntity.ok(new Response<>(200, "Tickets successfully created!"));
  }

  @GetMapping
  public ResponseEntity<?> getTicket() {
    Integer id = Claims.getUserId();
    return Response.successfulResponse(
        "Success to fetch ticket for user : " + id,
        ticketServices.getTicketByUserID(id)
    );
  }
}
