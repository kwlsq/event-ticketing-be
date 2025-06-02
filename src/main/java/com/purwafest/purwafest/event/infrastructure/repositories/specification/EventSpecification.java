package com.purwafest.purwafest.event.infrastructure.repositories.specification;

import com.purwafest.purwafest.event.domain.entities.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification{
  public static Specification<Event> searchByKeyword(String keyword) {
    return (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (keyword == null || keyword.isEmpty()) {
        return cb.conjunction();
      }

      return cb.or(
          cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
          cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"),
          cb.like(cb.lower(root.get("location")), "%" + keyword.toLowerCase() + "%")
      );
    };
  }

  public static Specification<Event> getFilteredEvent(String searchText) {
    return Specification.where(searchByKeyword((searchText)));
  }

}
