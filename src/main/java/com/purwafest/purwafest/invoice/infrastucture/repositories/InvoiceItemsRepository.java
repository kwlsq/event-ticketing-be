package com.purwafest.purwafest.invoice.infrastucture.repositories;

import com.purwafest.purwafest.invoice.domain.entities.InvoiceItems;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemsRepository extends JpaRepository<InvoiceItems, Integer> {
    @Query("SELECT COALESCE(SUM(ii.qty), 0) FROM InvoiceItems ii WHERE ii.invoice.event.user.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);
}
