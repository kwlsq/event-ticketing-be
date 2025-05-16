package com.purwafest.purwafest.image.application;

import com.purwafest.purwafest.image.presentation.dto.ImageModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ImageService {
  ResponseEntity<Map> uploadImage(ImageModel imageModel);
}
