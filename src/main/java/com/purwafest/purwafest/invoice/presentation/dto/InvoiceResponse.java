package com.purwafest.purwafest.invoice.presentation.dto;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.presentation.dtos.UpdateProfileRequest;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventDetailsResponse;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
  private Integer id;
  private String userName;
  private String email;
  private String msisdn;
  private String eventName;
  private String venue;
  private String location;
  private Instant date;
  private List<InvoiceItemRequest> invoiceItems = new ArrayList<>();

  public static InvoiceResponse toResponse(Invoice invoice) {
//    return InvoiceResponse.builder()
//        .id(invoice.getId())
//        .userName(invoice.getUser().getName())
//        .email(invoice.getUser().getEmail())
//        .msisdn(invoice.getUser().getMsisdn())
//        .eventName(invoice.getEvent().getName())
//        .venue(invoice.getEvent().getVenue())
//        .location(invoice.getEvent().getLocation())
//        .date(invoice.getEvent().getDate())
//
    InvoiceResponse response = new InvoiceResponse();
    response.id = invoice.getId();
    response.userName = invoice.getUser().getName();
    response.email = invoice.getUser().getEmail();
    response.msisdn = invoice.getUser().getMsisdn();
    response.eventName = invoice.getEvent().getName();
    response.venue = invoice.getEvent().getVenue();
    response.location = invoice.getEvent().getLocation();
    response.date = invoice.getEvent().getDate();

    invoice.getInvoiceItems().forEach(invoiceItem -> {
      InvoiceItemRequest request = InvoiceItemRequest.toResponse(invoiceItem);
      response.invoiceItems.add(request);
    });

    return response;
  }
}
