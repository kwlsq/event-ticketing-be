package com.purwafest.purwafest.discount.infrastructure.repository;

import com.purwafest.purwafest.discount.domain.entities.Discount;
import com.purwafest.purwafest.event.domain.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer>, JpaSpecificationExecutor<Discount> {
    @Query("SELECT d FROM Discount d WHERE d.promotion.id = :promotionID AND d.user.id = :userID")
    Discount findDiscountByPromotionIDAndUserID(Integer promotionID, Integer userID);
}
