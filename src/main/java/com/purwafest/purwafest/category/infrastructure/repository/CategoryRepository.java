package com.purwafest.purwafest.category.infrastructure.repository;

import com.purwafest.purwafest.category.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
