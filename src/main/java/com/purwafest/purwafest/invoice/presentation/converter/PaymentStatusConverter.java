package com.purwafest.purwafest.invoice.presentation.converter;

import com.purwafest.purwafest.invoice.domain.enums.PaymentStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {

  @Override
  public String convertToDatabaseColumn(PaymentStatus status) {
    return status == null ? null : status.name().toLowerCase();
  }

  @Override
  public PaymentStatus convertToEntityAttribute(String dbData) {
    return dbData == null ? null : PaymentStatus.valueOf(dbData.toUpperCase());
  }
}
