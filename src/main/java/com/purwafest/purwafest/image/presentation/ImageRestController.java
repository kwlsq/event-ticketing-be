package com.purwafest.purwafest.image.presentation;

import com.purwafest.purwafest.image.application.ImageService;
import com.purwafest.purwafest.image.infrastucture.repositories.ImageRepository;
import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @PostMapping("/{eventID}")
  public ResponseEntity<Map> upload(@RequestParam("multipartFiles") MultipartFile[] multipartFiles,
                                    @PathVariable Integer eventID) {
    try {
      return imageService.uploadMultiImage(multipartFiles, eventID);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
