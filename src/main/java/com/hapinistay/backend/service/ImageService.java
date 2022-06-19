package com.hapinistay.backend.service;


import java.util.List;

import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.model.House;

public interface ImageService {
	
	Image findById(Integer id);

	void saveImage(Image image);

	void updateImage(Image image);

	void deleteImageById(Integer id);

	void deleteAllImages();

	List<Image> findAllImages();

	boolean isImageExist(Image image);
	
	House saveImageOfHouse(Image image, Long house_id);
	
	House saveAvatarOfHouse(Image image, Long house_id);
	
	Room saveAvatarOfRoom(Image image, Long room_id);
	
	Room saveImageOfRoom(Image image, Long room_id);
	
	Apartment saveImageOfApartment(Image image, Long apartment_id);
}