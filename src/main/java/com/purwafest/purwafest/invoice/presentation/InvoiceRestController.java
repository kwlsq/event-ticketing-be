package com.purwafest.purwafest.invoice.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.invoice.application.InvoiceService;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceItemRequest;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceItemRequestWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceRestController {

  private final InvoiceService invoiceService;

  InvoiceRestController (InvoiceService invoiceService){
    this.invoiceService = invoiceService;
  }

  @PostMapping("/{eventID}")
  public ResponseEntity<?> createInvoice (@RequestBody InvoiceItemRequestWrapper requests, @PathVariable Integer eventID) {
    List<InvoiceItemRequest> invoiceItemRequests = requests.getInvoiceItemRequests();
    BigInteger points = requests.getPointAmount();
    Integer userID = Claims.getUserId();
    return Response.successfulResponse(
        "Successful to create invoice!",
        invoiceService.createInvoice(eventID, invoiceItemRequests,points, userID)
    );
  }

  @GetMapping
  public ResponseEntity<?> getAllInvoice() {
    return Response.successfulResponse(
        "Successfully to fetch all invoices!",
        invoiceService.getAllInvoice()
    );
  }
}
