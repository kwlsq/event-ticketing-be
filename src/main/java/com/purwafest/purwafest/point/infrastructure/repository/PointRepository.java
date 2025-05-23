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

@Repository
public interface PointRepository extends JpaRepository<Point,Integer>, JpaSpecificationExecutor<Point> {
    @Query("SELECT COALESCE(SUM(p.amount), 0) " +
            "FROM Point p " +
            "WHERE p.user.id = :id " +
            "AND p.expiredAt >= :now")
    Long getTotalPointsByUserIdNotExpired(@Param("id") Integer id, @Param("now") Instant now);
}
