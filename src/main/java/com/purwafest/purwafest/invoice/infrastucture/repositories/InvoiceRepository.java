package com.purwafest.purwafest.invoice.infrastucture.repositories;

import com.purwafest.purwafest.dashboard.presentation.dtos.ChartData;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

  @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.deletedAt IS NULL")
  List<Invoice> findAllByUser_Id(Integer userId);

  @Query("SELECT SUM(i.finalAmount) FROM Invoice i WHERE i.user.id = :userId")
  Long sumFinalAmountByUserId(@Param("userId") Integer userId);

  @Query("SELECT new com.purwafest.purwafest.dashboard.presentation.dtos.ChartData(" +
          "i.finalAmount, i.paymentDate) " +
          "FROM Invoice i " +
          "WHERE i.user.id = :userId " +
          "AND i.status = 'PAID' " +
          "AND i.paymentDate BETWEEN :startDate AND :endDate")
  List<ChartData> findInvoicesByUserAndDateRange(
          @Param("userId") Integer userId,
          @Param("startDate") Instant startDate,
          @Param("endDate") Instant endDate);

}
