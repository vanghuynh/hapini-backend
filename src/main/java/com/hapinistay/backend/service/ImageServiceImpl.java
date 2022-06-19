package com.hapinistay.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.repositories.ImageRepository;



@Service("imageService")
@Transactional
public class ImageServiceImpl implements ImageService{

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private HouseService houseService;
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private ApartmentService apartmentService;

	@Override
	public Image findById(Integer id) {
		return this.imageRepository.findOne(id.longValue());
	}

	@Override
	public void saveImage(Image image) {
		this.imageRepository.save(image);
		
	}

	@Override
	public void updateImage(Image image) {
		this.imageRepository.save(image);
		
	}

	@Override
	public void deleteImageById(Integer id) {
		this.imageRepository.delete(id.longValue());
		
	}

	@Override
	public void deleteAllImages() {
		this.imageRepository.deleteAll();
		
	}

	@Override
	public List<Image> findAllImages() {
		return this.imageRepository.findAll();
	}

	@Override
	public boolean isImageExist(Image image) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public House saveImageOfHouse(Image image, Long house_id) {
		House house =  this.houseService.findById(house_id);
		if(house != null) {
			house.getImages().add(image);
			this.houseService.updateHouse(house);
			return house;
		}
		return null;
	}

	@Override
	public House saveAvatarOfHouse(Image image, Long house_id) {
		House house =  this.houseService.findById(house_id);
		if(house != null) {
			house.setAvatar(image.getSecure_url());
			this.houseService.updateHouse(house);
			return house;
		}
		return null;
	}

	@Override
	public Room saveAvatarOfRoom(Image image, Long room_id) {
		Room room =  this.roomService.findById(room_id.intValue());
		if(room != null) {
			room.setAvatar(image.getSecure_url());
			this.roomService.updateRoom(room);
			return room;
		}
		return null;
	}

	@Override
	public Room saveImageOfRoom(Image image, Long room_id) {
		Room room =  this.roomService.findById(room_id.intValue());
		if(room != null) {
			room.getImages().add(image);
			this.roomService.updateRoom(room);
			return room;
		}
		return null;
	}

	@Override
	public Apartment saveImageOfApartment(Image image, Long apartment_id) {
		Apartment apartment =  this.apartmentService.findById(apartment_id, null);
		if(apartment != null) {
			apartment.getImages().add(image);
			this.apartmentService.updateApartment(apartment, null);
			return apartment;
		}
		return null;
	}

}
