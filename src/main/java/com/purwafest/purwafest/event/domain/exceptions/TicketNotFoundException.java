package com.purwafest.purwafest.event.domain.exceptions;

public class TicketNotFoundException extends RuntimeException {
  public TicketNotFoundException(String message) {
    super(message);
  }
}
