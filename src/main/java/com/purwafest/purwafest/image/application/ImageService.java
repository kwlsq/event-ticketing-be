package com.purwafest.purwafest.image.application;

import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageService {
  ResponseEntity<Map> uploadImage(ImageModel imageModel);
  ResponseEntity<Map> uploadMultiImage(ImageModel imageModel);
}
