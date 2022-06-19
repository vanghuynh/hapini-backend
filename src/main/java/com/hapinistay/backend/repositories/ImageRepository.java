package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {


}
