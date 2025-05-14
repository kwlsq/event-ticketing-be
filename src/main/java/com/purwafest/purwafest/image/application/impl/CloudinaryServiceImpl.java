package com.purwafest.purwafest.image.application.impl;

import com.cloudinary.Cloudinary;
import com.purwafest.purwafest.image.application.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
  private final Cloudinary cloudinary;

  public CloudinaryServiceImpl(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  @Override
  public String uploadFile(MultipartFile file, String folderName) {
    try {
      HashMap<Object, Object> options = new HashMap<>();
      options.put("folder", folderName);
      Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
      String publicID = (String) uploadedFile.get("public_id");
      return cloudinary.url().secure(true).generate(publicID);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
