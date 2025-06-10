package com.purwafest.purwafest.category.application;

import com.purwafest.purwafest.category.presentation.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
  List<CategoryResponse> getAllCategories();
}
