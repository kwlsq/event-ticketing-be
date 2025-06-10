package com.purwafest.purwafest.category.presentation.dto;

import com.purwafest.purwafest.category.domain.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
  private Integer id;
  private String name;

  public static CategoryResponse toResponse(Category category) {
    CategoryResponse categoryResponse = new CategoryResponse();
    categoryResponse.id = category.getId();
    categoryResponse.name = category.getName();

    return categoryResponse;
  }
}
