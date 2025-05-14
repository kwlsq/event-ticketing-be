package com.purwafest.purwafest.promotion.presentation.dtos;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.enums.PromotionType;
import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePromotionRequest {
    private String name;
    private String description;
    private PromotionType type;
    private Integer value;
    private Integer period;
    private Integer maxUsage;
    private Integer usageCount;
    private Boolean isReferralPromotion;
    private Integer eventId;

}
