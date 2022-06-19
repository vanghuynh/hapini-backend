package com.hapinistay.backend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hapinistay.backend.dto.ResponseDto;
import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.service.ApartmentService;
import com.hapinistay.backend.service.CloudinaryService;
import com.hapinistay.backend.service.HouseService;
import com.hapinistay.backend.service.ImageService;
import com.hapinistay.backend.service.RoomService;
import com.hapinistay.backend.service.UserService;
import com.hapinistay.backend.util.Constants;
import com.hapinistay.backend.util.DateUtil;

@RestController
@RequestMapping("/apartments")
public class ApartmentController {

	public static final Logger logger = LoggerFactory.getLogger(ApartmentController.class);
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	CloudinaryService cloudinaryService;
	
	@Autowired
	ApartmentService apartmentService;

	
	/**
	 * Retrieve single apartment
	 * @param id
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getApartment(@PathVariable("id") Long id,  @RequestParam(required=false) String lang) {
		logger.info("Fetching house with id {}", id);
		Apartment apartment = this.apartmentService.findById(id, lang);
		if (apartment == null) {
			logger.error("Apartment with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Apartment with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("", apartment), HttpStatus.OK);
	}
	
	/**
	 * Delete single apartment
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "{userId}/{apartmentId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> deleteApartment(@PathVariable("userId") Long userId, @PathVariable("apartmentId") Long apartmentId) {
		logger.info("Deleting apartment with id {}", apartmentId);
		Apartment apartment = this.apartmentService.findById(apartmentId, null);
		if (apartmentId == null) {
			logger.error("Apartment with id {} not found.", apartmentId);
			return new ResponseEntity<>(new ResponseDto("Apartment with id " + apartmentId 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		List<String> publicIds = new ArrayList<>();
		if(apartment.getImages() != null && apartment.getImages().size() > 0) {
			publicIds.addAll(apartment.getImages().stream().map(e -> e.getPublic_id()).collect(Collectors.toList()));
		}
		if(publicIds.size() > 0) {
			try {
				this.cloudinaryService.deleteImage(publicIds);
			} catch (Exception e1) {
				e1.printStackTrace();
				return new ResponseEntity<>(new ResponseDto("Error delete images", publicIds), HttpStatus.NOT_FOUND);
			}
		}
		this.userService.removeApartmentOfUser(userId, apartmentId);
		ResponseDto res = new ResponseDto("Apartment deleted " + apartmentId, null);
		return new ResponseEntity<ResponseDto>(res, HttpStatus.OK);
	}
	
	/**
	 * @param userId
	 * @param apartment
	 * @param lang
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> createApartmentByUser( @RequestParam(required=true) Long userId, @RequestBody Apartment apartment, @RequestParam(required=false) String lang) {
		logger.info("Creating apartment with id {}, by user id  : {}", apartment, userId);
		User user = this.userService.findById(userId);
		if (user == null) {
			return new ResponseEntity<>(new ResponseDto("User with id " + userId + " not exist" , null), HttpStatus.NOT_FOUND);
		}
		if (apartment == null) {
			return new ResponseEntity<>(new ResponseDto("Invalid apartment " + apartment , null), HttpStatus.NOT_FOUND);
		}
		Apartment savedApartment = this.apartmentService.createApartmentByUser(apartment, userId, lang);
		return new ResponseEntity<>(new ResponseDto("OK", savedApartment), HttpStatus.CREATED);
	}
	
	/**
	 * Get all apartments of user
	 * @param userId
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getAllApartmentsOfUser(@PathVariable("userId") Long userId, @RequestParam(required=false) String lang) {
		User user = this.userService.findById(userId);
		if (user == null) {
			return new ResponseEntity<>(new ResponseDto("User with id " + userId + " not exist" , null), HttpStatus.NO_CONTENT);
		}
		Set<Apartment> userApartments = this.apartmentService.getAllApartmentsOfUser(userId, lang);
		return new ResponseEntity<>(new ResponseDto("OK", userApartments) , HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/advanced-search-order/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchApartmentByDistrictWithOrderBy(@PathVariable("index") Integer index, @PathVariable("size") Integer size,
			@RequestParam(value="available", required = false) boolean available,
			@RequestParam(value="districtCode", required = false) String districtCode,
			@RequestParam(value="status", required = false) String status,
			@RequestParam(value="priceOrderBy", required = false)  boolean priceOrderByAsc,
			@RequestParam(value="lastDateOrderBy", required = false)  boolean lastDateOrderBy,
			@RequestParam(required=false) String lang) {
		if(districtCode == null) {
			districtCode = "";
		}
		Page<Apartment> apartments;
		if(priceOrderByAsc) {
			apartments = this.apartmentService.findByDistrictCodeWithAvailableSortByPrice(districtCode, available, index, size, lang);
		} else if(lastDateOrderBy) {
			apartments = this.apartmentService.findByDistrictCodeWithAvailableSortByLastDate(districtCode, available, index, size, lang);
		} else if(available == true) {
			apartments = this.apartmentService.findApartmentByAvailableAndStatus(districtCode, "APPROVE", available, index, size, lang);
		} else if(districtCode != null && districtCode != "") {
			apartments = this.apartmentService.findAllWithAvailableSortByLevelLastDate(districtCode, true, index, size, lang);
		} else if(status != null && status != "") {
			apartments = this.apartmentService.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, lang);
		} else {
			apartments = this.apartmentService.findAllWithAvailableSortByLevelLastDate("", true, index, size, lang);
		}
		if (apartments == null || apartments.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", apartments), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search-status/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchApartmentByStatus(@PathVariable("index") Integer index, @PathVariable("size") Integer size,
			@RequestParam(value="available", required = false) boolean available,
			@RequestParam(value="districtCode", required = false) String districtCode,
			@RequestParam(value="status", required = false) String status,
			@RequestParam(required=false) String lang) {
		if(districtCode == null) {
			districtCode = "";
		}
		Page<Apartment> apartments;
		apartments = this.apartmentService.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, lang);
		if (apartments == null || apartments.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", apartments), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/hot-post-search/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchApartmentByHotPost(@PathVariable("index") Integer index, @PathVariable("size") Integer size, 
			@RequestParam(value="available", required = false) boolean available,
			@RequestParam(value="apartmentStatus", required = false) String apartmentStatus,
			@RequestParam(required=false) String lang) {
		if(apartmentStatus == null) {
			apartmentStatus = "";
		}
		Page<Apartment> apartments = this.apartmentService.findApartmentByAvailableAndStatus("", apartmentStatus, available, index, size, lang);
		if (apartments == null || apartments.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", apartments), HttpStatus.OK);
	}
	
	/**
	 * Delete image of apartment
	 * @param apartmentId
	 * @param imageId
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "images/{apartmentId}/{imageId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> deleteImageOfApartment(@PathVariable("apartmentId") Long apartmentId, @PathVariable("imageId") Long imageId) {
		logger.info("Deleting apartment with id {}", apartmentId);
		Apartment apartment = this.apartmentService.findById(apartmentId, null);
		if (apartment == null) {
			logger.error("Apartment with id {} not found.", apartmentId);
			return new ResponseEntity<>(new ResponseDto("Apartment with id " + apartmentId 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		
		Apartment result = this.apartmentService.deleteImageOfApartment(apartmentId, imageId);
		if(result == null) {
			return new ResponseEntity<ResponseDto>(new ResponseDto("Can not delete image", imageId), HttpStatus.NOT_FOUND);
		}
		ResponseDto res = new ResponseDto("OK", result);
		return new ResponseEntity<ResponseDto>(res, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/images", method = RequestMethod.POST)
	//@Transactional
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	public ResponseEntity<ResponseDto> createApartmentWithImages(@RequestBody Apartment apartment, @RequestParam(required=true) Long user_id, @RequestParam(required=false) List<Integer> imageIds,  @RequestParam(required=false) String lang) {
		logger.info("Creating apartment : {}", apartment);
		if (user_id == null || user_id == 0) {
			logger.error("Invalid user id", user_id);
			return new ResponseEntity<>(new ResponseDto("Invalid user Id", user_id),HttpStatus.NOT_FOUND);
		}
		
		//set create data
		apartment.setLastUpdate(DateUtil.getCurrentDate());
		apartment.setStatus(Constants.STATUS_PENDING);
		apartment.setAvailable(true);
		apartment.setLevel(Constants.LEVEL_STANDARD);
		List<Image> images = new ArrayList<Image>();
		apartment.setImages(images);
		if(apartment.getId() != null && apartment.getId() != 0) {
			User user = this.userService.findById(user_id);
			apartment.setUser(user);
			Apartment savedApartment = this.apartmentService.updateApartment(apartment, lang);
			return new ResponseEntity<>(new ResponseDto("OK", savedApartment), HttpStatus.CREATED);
		}

		Apartment newApartment = this.apartmentService.createApartmentByUser(apartment, user_id, lang);
		if(imageIds != null && imageIds.size() > 0) {
			imageIds.forEach(e -> {
				Image image = this.imageService.findById(e);
				if(image != null) {
		            image.setApartment(newApartment);
		            this.imageService.saveImage(image);
		            images.add(image);
				}
			});
			newApartment.getImages().addAll(images);
		}
		return new ResponseEntity<>(new ResponseDto("OK", newApartment), HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> changeApartmentStatus(@PathVariable("id") Long id, @RequestParam(value="userId", required = true) Long userId,
			@RequestParam(required=false) String status, @RequestParam(value="level", required = true) Long level,
			@RequestParam(value="available", required = true) boolean available,
			@RequestParam(required=false) String lang) {
		logger.info("Change apartment status with id {}", id);
		Apartment apartment = this.apartmentService.findById(id, "");
		if (apartment == null) {
			logger.error("Apartment with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Apartment with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		Apartment resultApartment = this.apartmentService.updateApartmentStatus(id, status, level, available, userId, lang);
		if(resultApartment == null) {
			logger.error("User with id {} not found.", userId);
			return new ResponseEntity<>(new ResponseDto("User with id " + userId 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseDto("OK", resultApartment), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/{id}/enable", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> enableApartmemtAvailable(@PathVariable("id") Long id, @RequestParam(value="level", required = true) Long level,
			 @RequestParam(value="userId", required = true) Long userId, @RequestParam(required=false) String lang) {
		logger.info("Enable available of apartment with id {}", id);
		Apartment apartment = this.apartmentService.findById(id, lang);
		if (apartment == null) {
			logger.error("Apartment with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Apartment with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		if (!Constants.LEVEL_STANDARD.equals(level) && !Constants.LEVEL_VIP1.equals(level)) {
			logger.error("Apartment with level {} not found.", level);
			return new ResponseEntity<>(new ResponseDto("Apartment level " + level 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}	
		apartment = this.apartmentService.updateApartmentStatus(id, Constants.STATUS_PENDING, level, true, userId, lang);
		if (apartment == null) {
			logger.error("Error update apartment status with id {}", id);
			return new ResponseEntity<>(new ResponseDto("Error update apartment status with id " + id, null), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", apartment), HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/{id}/disable", method = RequestMethod.GET)
	public ResponseEntity<?> disableRoomAvailable(@PathVariable("id") Long id, @RequestParam(value="userId", required = true) Long userId, @RequestParam(required=false) String lang) {
		logger.info("Disable available of apartment with id {}", id);
		Apartment apartment = this.apartmentService.findById(id, lang);
		if (apartment == null) {
			logger.error("Apartment with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Apartment with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		apartment = this.apartmentService.updateApartmentStatus(id, Constants.STATUS_PENDING, Long.valueOf(0), false, userId, lang);
		if (apartment == null) {
			logger.error("Error update apartment status with id {}", id);
			return new ResponseEntity<>(new ResponseDto("Error update apartment status with id " + id, null), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", apartment), HttpStatus.OK);
	}
}