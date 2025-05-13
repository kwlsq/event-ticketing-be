package com.purwafest.purwafest.invoice.infrastucture.repositories;

import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
