package com.purwafest.purwafest.invoice.presentation.dto;

import com.purwafest.purwafest.invoice.domain.entities.InvoiceItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequest {
  private Integer eventTicketTypeID;
  private Integer qty;

  public static InvoiceItemRequest toResponse(InvoiceItems invoiceItems) {
    InvoiceItemRequest request = new InvoiceItemRequest();
    request.eventTicketTypeID = invoiceItems.getEventTicketType().getId();
    request.qty = invoiceItems.getQty();
    return request;
  }
}
