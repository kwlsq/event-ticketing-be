package com.purwafest.purwafest.invoice.presentation.dto;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.event.presentation.dtos.TicketTypeResponse;
import com.purwafest.purwafest.invoice.domain.entities.InvoiceItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemResponse {
  private Integer qty;
  private BigInteger subtotal;
  private TicketTypeResponse ticketTypeResponse;

  public static InvoiceItemResponse toResponse(InvoiceItems invoiceItems) {
    InvoiceItemResponse invoiceItemResponse = new InvoiceItemResponse();
    invoiceItemResponse.qty = invoiceItems.getQty();
    invoiceItemResponse.subtotal = invoiceItems.getSubtotal();

    EventTicketType ticketType = invoiceItems.getEventTicketType();

    TicketTypeResponse ticketTypeResponse = new TicketTypeResponse();
    ticketTypeResponse.setId(ticketType.getId());
    ticketTypeResponse.setName(ticketType.getName());
    ticketTypeResponse.setDate(ticketType.getSellDate());
    ticketTypeResponse.setAvailableQty(ticketType.getAvailableQty());
    ticketTypeResponse.setPrice(ticketType.getPrice());

    invoiceItemResponse.ticketTypeResponse = ticketTypeResponse;

    return invoiceItemResponse;
  }
}
