package com.purwafest.purwafest.promotion.application;

import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import com.purwafest.purwafest.promotion.presentation.dtos.CreatePromotionRequest;

public interface PromotionService {
    Promotion createPromotion(CreatePromotionRequest request);
}
