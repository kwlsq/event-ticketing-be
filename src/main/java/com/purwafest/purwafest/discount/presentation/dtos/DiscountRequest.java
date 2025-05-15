package com.purwafest.purwafest.discount.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    private Integer promotionId;
    private boolean isUsed;
    private Integer userId;
    private UUID codeVoucher;
}
