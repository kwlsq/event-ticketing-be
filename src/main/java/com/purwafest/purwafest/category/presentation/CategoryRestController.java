package com.purwafest.purwafest.category.presentation;

import com.purwafest.purwafest.category.application.CategoryService;
import com.purwafest.purwafest.category.domain.entities.Category;
import com.purwafest.purwafest.category.presentation.dto.CategoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryRestController {

  private final CategoryService categoryService;

  public CategoryRestController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public List<CategoryResponse> getAllCategories() {
    return categoryService.getAllCategories();
  }
}
