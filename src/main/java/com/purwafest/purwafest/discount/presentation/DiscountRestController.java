package com.purwafest.purwafest.discount.presentation;

import com.purwafest.purwafest.discount.application.DiscountService;
import com.purwafest.purwafest.discount.infrastructure.repository.DiscountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/discount")
public class DiscountRestController {
    private final DiscountService discountService;

    public DiscountRestController(DiscountService discountService){
        this.discountService = discountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDiscount(){
        return null;
    }
}
