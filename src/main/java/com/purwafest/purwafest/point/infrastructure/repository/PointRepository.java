package com.purwafest.purwafest.point.infrastructure.repository;

import com.purwafest.purwafest.point.domain.entities.Point;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point,Integer>, JpaSpecificationExecutor<Point> {
    @Query("SELECT COALESCE(SUM(p.amount - p.amountUsed), 0) " +
            "FROM Point p " +
            "WHERE p.user.id = :id " +
            "AND p.expiredAt >= :now " +
            "AND p.isRedeemed = false")
    Long getAvailablePointsByUserId(@Param("id") Integer id, @Param("now") Instant now);

    @Query("SELECT p FROM Point p " +
            "WHERE p.user.id = :userId " +
            "AND p.expiredAt > :now " +
            "AND p.deletedAt IS NULL " +
            "AND p.amountUsed < p.amount " +
            "AND p.isRedeemed = false " +
            "ORDER BY p.expiredAt ASC")
    List<Point> findUsablePointsByUserIdOrderByExpiry(Integer userId, Instant now);
}
