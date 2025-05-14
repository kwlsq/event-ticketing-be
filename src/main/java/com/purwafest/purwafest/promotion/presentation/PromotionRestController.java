package com.purwafest.purwafest.promotion.presentation;

import com.purwafest.purwafest.common.Response;
import com.purwafest.purwafest.promotion.application.PromotionService;
import com.purwafest.purwafest.promotion.presentation.dtos.CreatePromotionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/promotion")
public class PromotionRestController {
    private final PromotionService promotionService;

    public PromotionRestController(PromotionService promotionService){this.promotionService = promotionService;}

    @PostMapping("/create")
    public ResponseEntity<?> createPromotion(@RequestBody CreatePromotionRequest request){
        return Response.successfulResponse(
                "Create promotion Success",
                promotionService.createPromotion(request)
        );
    }
}
