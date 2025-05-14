package com.purwafest.purwafest.promotion.infrastructure.repository;

import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {
    long countByIsReferralPromotionTrue();
}
