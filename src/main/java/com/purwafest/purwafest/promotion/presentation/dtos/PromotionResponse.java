package com.purwafest.purwafest.promotion.presentation.dtos;

import com.purwafest.purwafest.event.domain.enums.PromotionType;
import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponse {
    private Integer id;
    private String name;
    private String description;
    private PromotionType type;
    private BigInteger value;
    private Boolean isReferralPromotion;

    public static PromotionResponse toResponse(Promotion promotion) {
        PromotionResponse promotionRequest = new PromotionResponse();
        promotionRequest.id = promotion.getId();
        promotionRequest.name = promotion.getName();
        promotionRequest.description = promotion.getDescription();
        promotionRequest.type = promotion.getType();
        promotionRequest.value = promotion.getValue();
        promotionRequest.isReferralPromotion = promotion.isReferralPromotion();

        return promotionRequest;
    }
}

