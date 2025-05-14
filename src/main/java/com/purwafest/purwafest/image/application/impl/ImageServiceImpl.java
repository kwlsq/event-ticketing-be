package com.purwafest.purwafest.image.application.impl;

import com.purwafest.purwafest.image.application.CloudinaryService;
import com.purwafest.purwafest.image.application.ImageService;
import com.purwafest.purwafest.image.domain.entities.Image;
import com.purwafest.purwafest.image.infrastucture.repositories.ImageRepository;
import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

  private final CloudinaryService cloudinaryService;
  private final ImageRepository imageRepository;

  public ImageServiceImpl (CloudinaryService cloudinaryService, ImageRepository imageRepository) {
    this.cloudinaryService = cloudinaryService;
    this.imageRepository = imageRepository;
  }

  @Override
  public ResponseEntity<Map> uploadImage(ImageModel imageModel) {
    try {
      if (imageModel.getName().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }
      if (imageModel.getFile().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }
      Image image = new Image();
      image.setAlt(imageModel.getName());
      image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "folder_1"));
      if (image.getUrl() == null) {
        return ResponseEntity.badRequest().build();
      }
      imageRepository.save(image);
      return ResponseEntity.ok().body(Map.of("url", image.getUrl()));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
