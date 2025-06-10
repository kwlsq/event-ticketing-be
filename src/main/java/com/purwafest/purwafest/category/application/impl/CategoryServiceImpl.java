package com.purwafest.purwafest.category.application.impl;

import com.purwafest.purwafest.category.application.CategoryService;
import com.purwafest.purwafest.category.domain.entities.Category;
import com.purwafest.purwafest.category.infrastructure.repository.CategoryRepository;
import com.purwafest.purwafest.category.presentation.dto.CategoryResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<CategoryResponse> getAllCategories() {

    List<Category> categoryList = categoryRepository.findAll(Sort.by("id").ascending());
    List<CategoryResponse> categoryResponseList = new ArrayList<>();

    categoryList.forEach(category -> {
      CategoryResponse categoryResponse = CategoryResponse.toResponse(category);
      categoryResponseList.add(categoryResponse);
    });

    return categoryResponseList;
  }
}
