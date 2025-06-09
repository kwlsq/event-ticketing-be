package com.purwafest.purwafest.invoice.presentation.dto;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.presentation.dtos.UpdateProfileRequest;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.presentation.dtos.EventDetailsResponse;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import com.purwafest.purwafest.invoice.domain.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigInteger;
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
  private PaymentStatus status;
  private String paymentMethod;
  private Instant paymentDate;
  private BigInteger amount;
  private BigInteger fees;
  private BigInteger pointUsed;
  private BigInteger finalAmount;
  private EventDetailsResponse eventDetailsResponse;
  private List<InvoiceItemResponse> invoiceItems = new ArrayList<>();

  public static InvoiceResponse toResponse(Invoice invoice) {
    InvoiceResponse response = new InvoiceResponse();
    response.id = invoice.getId();
    response.userName = invoice.getUser().getName();
    response.email = invoice.getUser().getEmail();
    response.msisdn = invoice.getUser().getMsisdn();
    response.status = invoice.getStatus();
    response.paymentMethod = invoice.getPaymentMethod();
    response.paymentDate = invoice.getPaymentDate();
    response.amount = invoice.getAmount();
    response.fees = invoice.getFees();
    response.pointUsed = invoice.getValuePointUsage();
    response.finalAmount = invoice.getFinalAmount();
    response.eventDetailsResponse = EventDetailsResponse.toResponse(invoice.getEvent());

    invoice.getInvoiceItems().forEach(invoiceItem -> {
      InvoiceItemResponse request = InvoiceItemResponse.toResponse(invoiceItem);
      response.invoiceItems.add(request);
    });

    return response;
  }
}
