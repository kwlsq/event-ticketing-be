package com.purwafest.purwafest.discount.application.impl;

import com.purwafest.purwafest.discount.application.DiscountService;
import com.purwafest.purwafest.discount.domain.entities.Discount;
import com.purwafest.purwafest.discount.infrastructure.repository.DiscountRepository;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository){
        this.discountRepository=discountRepository;
    }

    @Override
    public Boolean isReferralDiscountEligible(Integer userID){
        return discountRepository.existsByUserIdAndIsUsedFalseAndPromotionId(userID,17);
    }
}
