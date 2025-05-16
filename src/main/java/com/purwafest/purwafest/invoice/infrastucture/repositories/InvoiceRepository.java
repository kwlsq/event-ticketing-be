package com.purwafest.purwafest.invoice.infrastucture.repositories;

import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

  @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.deletedAt IS NULL")
  List<Invoice> findAllByUser_Id(Integer userId);
}
