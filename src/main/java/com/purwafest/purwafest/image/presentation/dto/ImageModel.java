package com.purwafest.purwafest.image.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageModel {
  private String name;
  private MultipartFile file;
  private MultipartFile[] multipartFiles;
}
