package com.purwafest.purwafest.invoice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemRequestWrapper {
  List<InvoiceItemRequest> invoiceItemRequests;
}
