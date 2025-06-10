package com.purwafest.purwafest.invoice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequestWrapper {
  List<InvoiceItemRequest> invoiceItemRequests;
  private BigInteger pointAmount;
  private Integer promotionID;
}
