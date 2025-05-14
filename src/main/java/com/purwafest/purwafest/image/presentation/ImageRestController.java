package com.purwafest.purwafest.image.presentation;

import com.purwafest.purwafest.image.application.ImageService;
import com.purwafest.purwafest.image.infrastucture.repositories.ImageRepository;
import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/image")
public class ImageRestController {
  private final ImageRepository imageRepository;
  private final ImageService imageService;

  public ImageRestController (ImageRepository imageRepository, ImageService imageService) {
    this.imageRepository = imageRepository;
    this.imageService = imageService;
  }

  @PostMapping
  public ResponseEntity<Map> upload(ImageModel imageModel) {
    try {
      return imageService.uploadImage(imageModel);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
