package com.purwafest.purwafest.invoice.application;

import com.purwafest.purwafest.common.PaginatedResponse;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceItemRequest;
import com.purwafest.purwafest.invoice.presentation.dto.InvoiceResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;

public interface InvoiceService {
  InvoiceResponse createInvoice(Integer eventID, List<InvoiceItemRequest> invoiceItemRequests, BigInteger points, Integer userID, Integer discountID);
  PaginatedResponse<InvoiceResponse> getAllInvoice(Pageable pageable);
}
