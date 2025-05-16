package com.purwafest.purwafest.image.application.impl;

import com.purwafest.purwafest.image.application.CloudinaryService;
import com.purwafest.purwafest.image.application.ImageService;
import com.purwafest.purwafest.image.domain.entities.Image;
import com.purwafest.purwafest.image.infrastucture.repositories.ImageRepository;
import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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
      image.setUrl(cloudinaryService.uploadFile(imageModel.getFile(), "purwafest_event"));

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

  @Override
  public ResponseEntity<Map> uploadMultiImage(ImageModel imageModel) {
    try {

      List<MultipartFile> multipartFileList = List.of(imageModel.getMultipartFiles());
      List<String> urlList = new ArrayList<>();

      if (imageModel.getName().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      multipartFileList.forEach(file -> {
        Image image = new Image();
        image.setAlt(imageModel.getName());
        image.setUrl(cloudinaryService.uploadFile(file, "purwafest_event"));
        imageRepository.save(image);
        urlList.add(image.getUrl());
      });

      return ResponseEntity.ok().body(Map.of("url", urlList));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
