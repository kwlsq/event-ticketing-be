package com.purwafest.purwafest.event.infrastructure.specification;

import com.purwafest.purwafest.event.domain.entities.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification{
  public static Specification<Event> searchByKeyword(String keyword, String location, Integer category) {
    return (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

      Predicate keywordPredicate = cb.conjunction();

      if (keyword != null && !keyword.isEmpty()) {
        keywordPredicate = cb.or(
            cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
            cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%")
        );
      }

//      If location is passed, filter by location and keyword
      if (location != null && !location.isEmpty()) {
        Predicate locationPredicate = cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
        return cb.and(keywordPredicate, locationPredicate);
      }

      if (category != 0) {
        Predicate categoryPredicate = cb.equal(root.get("category").get("id"), category);
        return cb.and(keywordPredicate, categoryPredicate);
      }

      return keywordPredicate;
    };
  }

  public static Specification<Event> isNotDeleted() {
    return (Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      return cb.isNull(root.get("deletedAt"));
    };
  }

  public static Specification<Event> getFilteredEvent(String searchText, String location, Integer category) {
    return Specification.where(isNotDeleted()).and(searchByKeyword((searchText), (location), (category)));
  }

}
