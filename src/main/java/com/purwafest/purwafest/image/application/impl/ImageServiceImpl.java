package com.purwafest.purwafest.image.application.impl;

import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.infrastructure.repositories.EventRepository;
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
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

  private final CloudinaryService cloudinaryService;
  private final ImageRepository imageRepository;
  private final EventRepository eventRepository;

  public ImageServiceImpl (CloudinaryService cloudinaryService, ImageRepository imageRepository, EventRepository eventRepository) {
    this.cloudinaryService = cloudinaryService;
    this.imageRepository = imageRepository;
    this.eventRepository = eventRepository;
  }
  @Override
  public ResponseEntity<Map> uploadMultiImage(MultipartFile[] multipartFile, Integer eventID) {
    Optional<Event> eventOptional = eventRepository.findById(eventID);
    try {
      List<Image> imageLists = new ArrayList<>();

//      Throw exception if event not found
      if (eventOptional.isEmpty()) throw new RuntimeException("Event not found!");

      for (int i = 0; i < multipartFile.length; i++){
        MultipartFile file = multipartFile[i];
        Image image = new Image();
        image.setUrl(cloudinaryService.uploadFile(file, "purwafest_event"));
        image.setEvent(eventOptional.get());
        image.setAlt("Image " + (i + 1) + " of " + eventOptional.get().getName());
        image.setOrderImage(Integer.toString(i + 1));

//        Set the thumbnail only from first image
        image.setThumbnail(i == 0);

        imageRepository.save(image);
        imageLists.add(image);
      };

      return ResponseEntity.ok().body(Map.of("image for " + eventOptional.get().getName(), imageLists));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
