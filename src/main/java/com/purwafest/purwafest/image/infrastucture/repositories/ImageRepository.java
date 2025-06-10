package com.purwafest.purwafest.image.infrastucture.repositories;

import com.purwafest.purwafest.image.domain.entities.Image;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

  @Query("SELECT e.id, i.url from Image i join i.event e where e.id in :eventIDs and i.isThumbnail = true")
  List<Object[]> findThumbnailImage(@Param("eventIDs") List<Integer> eventIDs);
  default Map<Integer, String> getThumbnailImage (List<Integer> eventIDs) {
    List<Object[]> results = findThumbnailImage(eventIDs);
    Map<Integer, String> thumbnailMap = new HashMap<>();
    for (Object[] result: results) {
      Integer eventID = (Integer)result[0];
      String imageID = (String)result[1];
      thumbnailMap.put(eventID, imageID);
    }
    return thumbnailMap;
  }
}
