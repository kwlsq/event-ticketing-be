package com.purwafest.purwafest.promotion.infrastructure.repository;

import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {
    long countByIsReferralPromotionTrue();

    @Query("""
                SELECT p
                FROM Promotion p
                WHERE p.maxUsage > p.usageCount
                AND p.event.id = :eventId
                AND p.expiredDate >= CURRENT_TIMESTAMP
            """)
    List<Promotion> findValidPromotions(@Param("eventId") Integer eventId);
}