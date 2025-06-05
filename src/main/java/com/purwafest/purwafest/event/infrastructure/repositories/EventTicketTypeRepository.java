package com.purwafest.purwafest.event.infrastructure.repositories;

import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface EventTicketTypeRepository extends JpaRepository<EventTicketType, Integer> {
  @Query("SELECT e.id, min(et.price) from EventTicketType et join et.event e where e.id in :eventIDs group by e.id")
  List<Object[]> findMinimumPriceByEventIDs(@Param("eventIDs") List<Integer> eventIDs);
  default Map<Integer, BigInteger> getMinimumPriceMap (List<Integer> eventIDs) {
    List<Object[]> results = findMinimumPriceByEventIDs(eventIDs);
    Map<Integer, BigInteger> eventMinPrice = new HashMap<>();
    for (Object[] result: results) {
      Integer eventID = (Integer)result[0];
      BigInteger minPrice = (BigInteger)result[1];
      eventMinPrice.put(eventID, minPrice);
    }

    return eventMinPrice;
  }
}
