package com.purwafest.purwafest.image.infrastucture.repositories;

import com.purwafest.purwafest.image.domain.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
