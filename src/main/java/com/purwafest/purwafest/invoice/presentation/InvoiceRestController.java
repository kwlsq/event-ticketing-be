package com.purwafest.purwafest.invoice.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.common.security.Claims;
import com.purwafest.purwafest.invoice.application.InvoiceService;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceItemRequest;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceRequestWrapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  @PostMapping("/create/{eventID}")
  public ResponseEntity<?> createInvoice (@RequestBody InvoiceRequestWrapper requests, @PathVariable Integer eventID) {
    List<InvoiceItemRequest> invoiceItemRequests = requests.getInvoiceItemRequests();
    BigInteger points = requests.getPointAmount();
    Integer promotionID = requests.getPromotionID();
    Integer userID = Claims.getUserId();
    return Response.successfulResponse(
        "Successful to create invoice!",
        invoiceService.createInvoice(eventID, invoiceItemRequests,points, userID, promotionID)
    );
  }

  @GetMapping
  public ResponseEntity<?> getAllInvoice(@RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "2") int size) {

    Pageable pageable = PageRequest.of(page, size);

    return Response.successfulResponse(
        "Successfully to fetch all invoices!",
        invoiceService.getAllInvoice(pageable)
    );
  }
}
