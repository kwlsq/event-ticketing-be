package com.purwafest.purwafest.image.presentation;

import com.purwafest.purwafest.image.application.ImageService;
import com.purwafest.purwafest.image.infrastucture.repositories.ImageRepository;
import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/image")
public class ImageRestController {
  private final ImageService imageService;

  public ImageRestController (ImageService imageService) {
    this.imageService = imageService;
  }

  @PostMapping
  public ResponseEntity<Map> upload(ImageModel imageModel) {
    try {
//      If the image uploaded by user is just one
      if (imageModel.getMultipartFiles() == null) {
        return imageService.uploadImage(imageModel);
      }

//      If user upload more than one image
      return imageService.uploadMultiImage(imageModel);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
