package com.purwafest.purwafest.point.infrastructure.repository;

import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import com.purwafest.purwafest.point.domain.entities.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {

}
