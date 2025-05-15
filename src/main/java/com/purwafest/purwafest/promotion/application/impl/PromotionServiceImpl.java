package com.purwafest.purwafest.promotion.application.impl;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
import com.purwafest.purwafest.promotion.application.PromotionService;
import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import com.purwafest.purwafest.promotion.infrastructure.repository.PromotionRepository;
import com.purwafest.purwafest.promotion.presentation.dtos.CreatePromotionRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final EventRepository eventRepository;

    public PromotionServiceImpl(PromotionRepository promotionRepository, EventRepository eventRepository){
        this.promotionRepository = promotionRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Promotion createPromotion(CreatePromotionRequest request) {
        Event event = null;

        if (request.getIsReferralPromotion()) {
            long referralPromotionCount = promotionRepository.countByIsReferralPromotionTrue();
            if (referralPromotionCount > 0) {
                throw new IllegalArgumentException("Can't create referral promotion anymore");
            }
        } else {
            event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + request.getEventId()));
        }

        Promotion promotion = Promotion.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .value(request.getValue())
                .period(request.getPeriod())
                .usageCount(0)
                .maxUsage(request.getMaxUsage())
                .isReferralPromotion(request.getIsReferralPromotion())
                .event(event)
                .build();

        return promotionRepository.save(promotion);
    }

}
