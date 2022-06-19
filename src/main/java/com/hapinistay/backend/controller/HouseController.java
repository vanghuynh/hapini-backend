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
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.service.CloudinaryService;
import com.hapinistay.backend.service.HouseService;
import com.hapinistay.backend.service.ImageService;
import com.hapinistay.backend.service.RoomService;
import com.hapinistay.backend.service.UserService;
import com.hapinistay.backend.util.Constants;
import com.hapinistay.backend.util.DateUtil;

@RestController
@RequestMapping("/houses")
public class HouseController {

	public static final Logger logger = LoggerFactory.getLogger(HouseController.class);

	@Autowired
	HouseService houseService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	CloudinaryService cloudinaryService;

	/**
	 * Retrieve all houses
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> listAllHouses() {
		List<House> houses = this.houseService.findAllHouses();
		if (houses.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("", null),HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("", houses), HttpStatus.OK);
	}
	
	/**
	 * Retrieve all houses with paging
	 * @param index
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/page/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> listAllHousesWithPaging(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size) {
		List<House> houses = this.houseService.findAllHouses();
		if (houses.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("", houses), HttpStatus.OK);
	}
	
	
	/**
	 * Retrieve single house
	 * @param id
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getHouse(@PathVariable("id") Long id,  @RequestParam(required=false) String lang) {
		logger.info("Fetching house with id {}", id);
		House house = this.houseService.findById(id, lang);
		if (house == null) {
			logger.error("House with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		//this code have problem when edit house
//		if(house.getRooms() != null && house.getRooms().size() > 0) {
//			//remove room not available from house
//			List<Room> rooms = house.getRooms();
//			rooms.removeIf(room -> room.getAvailable() == true);
//			house.setRooms(rooms);
//		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("", house), HttpStatus.OK);
	}
	
	/**
	 * Delete single house
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "{userId}/{houseId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> deleteHouse(@PathVariable("userId") Long userId, @PathVariable("houseId") Long houseId) {
		logger.info("Deleting house with id {}", houseId);
		House house = this.houseService.findById(houseId);
		if (house == null) {
			logger.error("House with id {} not found.", houseId);
			return new ResponseEntity<>(new ResponseDto("House with id " + houseId 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		List<String> publicIds = new ArrayList<>();
		if(house.getImages() != null && house.getImages().size() > 0) {
			publicIds.addAll(house.getImages().stream().map(e -> e.getPublic_id()).collect(Collectors.toList()));
		}
		if(house.getRooms() != null && house.getRooms().size() > 0) {
			house.getRooms().forEach(room -> {
				publicIds.addAll(room.getImages().stream().map(e -> e.getPublic_id()).collect(Collectors.toList()));
			});
		}
		if(publicIds.size() > 0) {
			try {
				this.cloudinaryService.deleteImage(publicIds);
			} catch (Exception e1) {
				e1.printStackTrace();
				return new ResponseEntity<>(new ResponseDto("Error delete images", publicIds), HttpStatus.NOT_FOUND);
			}
		}
		this.userService.removeHouseOfUser(userId, houseId);
		ResponseDto res = new ResponseDto("House deleted", houseId);
		return new ResponseEntity<ResponseDto>(res, HttpStatus.OK);
	}

	/**
	 * Create a house
	 * @param room
	 * @param lang
	 * @return
	 */
	//@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<ResponseDto> createHouse(@RequestBody House house, @RequestParam(required=false) String lang) {
		logger.info("Creating house : {}", house);
		if (this.houseService.isHouseExist(house)) {
			logger.error("Unable to create. A house with id {} already exist", house.getId());
			return new ResponseEntity<>(new ResponseDto("Unable to create. A Room with name " + 
			house.getId() + " already exist.", null),HttpStatus.CONFLICT);
		}
		House savedHouse = this.houseService.saveHouse(house, lang);

		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", savedHouse), HttpStatus.CREATED);
	}
	
	/**
	 * Create a house with images
	 * @param room
	 * @param user_id
	 * @param imageIds
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/images", method = RequestMethod.POST)
	@Transactional
	//@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	public ResponseEntity<ResponseDto> createHouseWithImages(@RequestBody House house, @RequestParam(required=true) Long user_id, @RequestParam(required=false) List<Integer> imageIds,  @RequestParam(required=false) String lang) {
		logger.info("Creating house : {}", house);
		if (user_id == null || user_id == 0) {
			logger.error("Invalid user id", user_id);
			return new ResponseEntity<>(new ResponseDto("Invalid user Id", user_id),HttpStatus.NOT_FOUND);
		}
		
		if (this.houseService.isHouseExist(house)) {
			logger.error("Unable to create house with name {} already exist", house.getName());
			return new ResponseEntity<>(new ResponseDto("Unable to create house with name " + 
			house.getName() + " already exist.", null),HttpStatus.CONFLICT);
		}
		//set create data
		house.setLastUpdate(DateUtil.getCurrentDate());
		Set<Image> images = new HashSet<Image>();
		house.setImages(images);
		if(house.getId() != null && house.getId() != 0) {
			User user = this.userService.findById(user_id);
			house.setUser(user);
			List<Room> rooms = this.houseService.findById(house.getId()).getRooms();
			if(rooms != null && rooms.size() > 0) {
				rooms.forEach(room -> {
					room.getLanguageDto(lang);
					room.setLanguageDto(lang);
					});
				house.setRooms(rooms);
			}
			House savedHouse = this.houseService.updateHouse(house, lang);
			return new ResponseEntity<>(new ResponseDto("OK", savedHouse), HttpStatus.CREATED);
		}

		House newHouse = this.houseService.createHouseByUser(house, user_id, lang);
		if(imageIds != null && imageIds.size() > 0) {
			imageIds.forEach(e -> {
				Image image = this.imageService.findById(e);
				if(image != null) {
		            image.setHouse(newHouse);
		            this.imageService.saveImage(image);
		            images.add(image);
				}
			});
			newHouse.getImages().addAll(images);
		}
		return new ResponseEntity<>(new ResponseDto("OK", newHouse), HttpStatus.CREATED);
	}
		
	/**
	 * Get all house with paging
	 * @param index
	 * @param size
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/pageable/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> listAllHouseWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size,
			@RequestParam(required=false) String lang
			) {
		Page<House> houses = this.houseService.findAllHousePageable(index, size, lang);
		if (houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	/**
	 * Search house by name and description
	 * @param index
	 * @param size
	 * @param searchTerm
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/search/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHousesByNameAndDesciptionWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="searchTerm", required = false) String searchTerm, @RequestParam(required=false) String lang) {
		Page<House> houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize(searchTerm, searchTerm, index, size, lang);
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("OK", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	/**
	 * Search house by district code
	 * @param index
	 * @param size
	 * @param districtCode
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/searchdistrict/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHouseByDistrictCodeWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="districtCode", required = false) String districtCode, @RequestParam(required=false) String lang) {
		Page<House> houses = this.houseService.findByDistrictCode(districtCode, index, size, lang);
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
		
	/**
	 * Search house by term or house id
	 * @param index
	 * @param size
	 * @param searchTerm
	 * @param roomId
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/searchtermid/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHousesByTermOrIdWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, 
			@RequestParam(value="searchTerm", required = false) String searchTerm, 
			@RequestParam(value="houseId", required = false) Long houseId, 
			@RequestParam(required=false) String lang) {
		Page<House> houses = null;
		if(searchTerm != null && searchTerm != "") {
			houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize(searchTerm, searchTerm, index, size, lang);
		} 
		if(houseId != null && houseId != 0) {
			House house = this.houseService.findById(houseId, lang);
			if(house != null) {
				houses = new PageImpl<House>(Arrays.asList(house));
			}
		}
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	/**
	 * Search houses by term, house id, district code or get all houses
	 * @param index
	 * @param size
	 * @param searchTerm
	 * @param roomId
	 * @param districtCode
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/searchtermiddistrict/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHousesByAllOrTermOrIdOrDistrictWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, 
			@RequestParam(value="searchTerm", required = false) String searchTerm, 
			@RequestParam(value="houseId", required = false) Long houseId,
			@RequestParam(value="districtCode", required = false) String districtCode,
			@RequestParam(required=false) String lang) {
		Page<House> houses = null;
		if(searchTerm != null && searchTerm != "") {
			houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize(searchTerm, searchTerm, index, size, lang);
		} else if(houseId != null && houseId != 0) {
			House house = this.houseService.findById(houseId, lang);
			if(house != null) {
				houses = new PageImpl<House>(Arrays.asList(house));
			}
		} else
		if(districtCode != null && districtCode != "") {
			houses = this.houseService.findByDistrictCode(districtCode, index, size, lang);
		} else {
			houses = this.houseService.findAllHousePageable(index, size, lang);
		}
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		//get only available rooms
		//this code have problem when edit house
//		houses.getContent().forEach(house -> {
//			if(house.getRooms() != null && house.getRooms().size() > 0) {
//				//remove room not available from house
//				List<Room> rooms = house.getRooms();
//				rooms.removeIf(room -> room.getAvailable() == true);
//				house.setRooms(rooms);
//			}
//		});
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	/**
	 * Get rooms of house
	 * @param id
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getRoomsOfHouse(@PathVariable("id") Long id,
			@RequestParam(required=false) String lang) {
		logger.info("Fetching rooms of house with id {}", id);
		House house = this.houseService.findById(id, lang);
		if (house == null) {
			logger.error("House with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("House with id " + id + " not found", id) , HttpStatus.NOT_FOUND);
		}
		List<Room> rooms = house.getRooms();
		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", rooms), HttpStatus.OK);
	}
	
	/**
	 * Add room to house
	 * @param id
	 * @param room
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/{id}/rooms", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> addRoomToHouse(@PathVariable("id") Long id, @RequestBody Room room,
			@RequestParam(required=false) String lang) {
		logger.info("Add room to house with id {}", id);
		Room roomResult = this.houseService.addRoomToHouse(id, room, lang);
		if (roomResult == null) {
			logger.error("House with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseDto("OK", roomResult), HttpStatus.OK);
	}
	
	/**
	 * Add room to house with images
	 * @param id
	 * @param room
	 * @param imageIds
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/{id}/rooms/images", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	public ResponseEntity<ResponseDto> addRoomToHouseWithImage(@PathVariable("id") Long id, @RequestBody Room room,
			@RequestParam(required=false) List<Integer> imageIds,
			@RequestParam(required=false) String lang) {
		logger.info("Add room to house with id {}", id);
		House house = this.houseService.findById(id, lang);
		if (house == null) {
			logger.error("House with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("House with id " + id + " not found", id) , HttpStatus.NOT_FOUND);
		}
		//in case edit room
		List<Image> images = new ArrayList<Image>();
		room.setImages(images);
		if(room.getId() != null && room.getId() != 0) {
			room.setHouse(house);
			this.roomService.updateRoom(room, lang);
			return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.OK);
		}
		//add new room to house
		room.setLevel(Constants.LEVEL_STANDARD);
		room.setAvailable(true);
		room.setStatus(Constants.STATUS_PENDING);
		Room roomResult = this.houseService.addRoomToHouse(id, room, lang);
		if (roomResult == null) {
			logger.error("House with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", id), HttpStatus.NOT_FOUND);
		}
		if(imageIds != null && imageIds.size() > 0) {
			imageIds.forEach(e -> {
				Image image = this.imageService.findById(e);
				if(image != null) {
		            image.setRoom(roomResult);
		            this.imageService.saveImage(image);
		            images.add(image);
				}
			});
            roomResult.getImages().addAll(images);
		}
		return new ResponseEntity<>(new ResponseDto("OK", roomResult), HttpStatus.OK);
	}
	
	/**
	 * Get all rooms
	 * @return
	 */
	@RequestMapping(value = "/rooms", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getAllRooms() {
		logger.info("Fetching all rooms");
		List<Room> rooms = this.roomService.findAllRooms();
		return new ResponseEntity<>(new ResponseDto("OK", rooms), HttpStatus.OK);
	}
	
	/**
	 * Remove room from house
	 * @param id
	 * @param roomId
	 * @return
	 */
	@RequestMapping(value = "/{id}/rooms/{roomId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> removeRoomFromHouse(@PathVariable("id") Long id, @PathVariable("roomId") Long roomId) {
		logger.info("Deleting room id {} from house with id {}", roomId, id);
		if (id == null || roomId == null) {
			logger.error("Invalid parameters with house id {}, room id {", id, roomId);
			return new ResponseEntity<>(new ResponseDto("House or room with id not found", null), HttpStatus.NOT_FOUND);
		}
		House house = this.houseService.removeRoomFromHouse(id, roomId);
		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", house.getRooms()), HttpStatus.OK);
	}
	
	/**
	 * Create house by user
	 * @param userId
	 * @param house
	 * @param lang
	 * @return
	 */
	//@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/user/{useId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> createHouseByUser(@PathVariable("userId") Long userId, @RequestBody House house, @RequestParam(required=false) String lang) {
		logger.info("Creating house with id {}, by user id  : {}", house, userId);
		if (this.houseService.isHouseExist(house)) {
			logger.error("Unable to create house with name {}", house.getName());
			return new ResponseEntity<>(new ResponseDto("Unable to create house with name " + 
			house.getName(), null),HttpStatus.CONFLICT);
		}
		House savedHouse = this.houseService.createHouseByUser(house, userId, lang);
		return new ResponseEntity<>(new ResponseDto("OK", savedHouse), HttpStatus.CREATED);
	}
	
	/**
	 * Get all houses of user
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getAllHousesOfUser(@PathVariable("userId") Long userId, @RequestParam(required=false) String lang) {
		User user = this.userService.findById(userId);
		if (user == null) {
			return new ResponseEntity<>(new ResponseDto("User with id " + userId + " not exist" , null), HttpStatus.NO_CONTENT);
		}
		Set<House> userHouses = this.houseService.getAllHousesOfUser(userId, lang);
		return new ResponseEntity<>(new ResponseDto("OK", userHouses) , HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search-status/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHouseByStatusWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="status", required = false) String status, @RequestParam(required=false) String lang) {
		Page<House> houses = this.houseService.findByStatus(status, index, size, lang);
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search-status-district/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHouseByStatusDistrictWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="status", required = false) String status, @RequestParam(value="districtCode", required = false) String districtCode, @RequestParam(required=false) String lang) {
		Page<House> houses = this.houseService.findNewHousesByDistrictCodeAndStatus(districtCode, status, index, size, lang);
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> changeHouseStatus(@PathVariable("id") Long id,
			@RequestParam(required=false) String status) {
		logger.info("Change house status with id {}", id);
		House house = this.houseService.findById(id);
		if (house == null) {
			logger.error("House with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("House with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		house.setStatus(status);
		House houseResult = this.houseService.updateHouse(house);
		return new ResponseEntity<>(new ResponseDto("OK", houseResult), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/advanced-search/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHouseByDistrictAndRoomAttribute(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="available", required = false) boolean available, @RequestParam(value="districtCode", required = false) String districtCode, @RequestParam(required=false) String lang) {
		Page<House> houses = this.houseService.findByDistrictCodeWithAvailableRoom(districtCode, available, index, size, lang);
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/advanced-search-order/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHouseByDistrictAndRoomAttributeOrderBy(@PathVariable("index") Integer index, @PathVariable("size") Integer size,
			@RequestParam(value="available", required = false) boolean available,
			@RequestParam(value="districtCode", required = false) String districtCode,
			@RequestParam(value="priceOrderBy", required = false)  boolean priceOrderByAsc,
			@RequestParam(value="lastDateOrderBy", required = false)  boolean lastDateOrderBy,
			@RequestParam(value="searchTerm", required = false) String searchTerm, 
			@RequestParam(required=false) String lang) {
		Page<House> houses;
		if(priceOrderByAsc) {
			houses = this.houseService.findByDistrictCodeWithAvailableRoomSortByPrice(districtCode, available, "price", index, size, lang);
		} else if(lastDateOrderBy) {
			houses = this.houseService.findByDistrictCodeWithAvailableRoomSortByDate(districtCode, available, index, size, lang);
		} else if(available == true) {
			houses = this.houseService.findByDistrictCodeWithAvailableRoom(districtCode, available, index, size, lang);
		} else if(districtCode != null && districtCode != "") {
			//houses = this.houseService.findByDistrictCode(districtCode, index, size, lang);
			houses = this.houseService.findAllWithAvailableRoomSortByLevelLastDate(districtCode, true, index, size, lang);
		} else if(searchTerm != null && searchTerm != "") {
			houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize(searchTerm, searchTerm, index, size, lang);
		} else {
			//houses = this.houseService.findAllHousePageable(index, size, lang);
			houses = this.houseService.findAllWithAvailableRoomSortByLevelLastDate("", true, index, size, lang);
		}
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/hot-post-search/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchHouseByHotPost(@PathVariable("index") Integer index, @PathVariable("size") Integer size, 
			@RequestParam(value="available", required = false) boolean available,
			@RequestParam(value="houseStatus", required = false) String houseStatus,
			@RequestParam(value="roomStatus", required = false) String roomStatus, 
			@RequestParam(required=false) String lang) {
		Page<House> houses = this.houseService.findHousesByAvailableAndStatus(houseStatus, roomStatus, available, index, size, lang);
		if (houses == null || houses.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", houses), HttpStatus.OK);
	}
	
	/**
	 * Delete image of house
	 * @param houseId
	 * @param imageId
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "images/{houseId}/{imageId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> deleteImageOfHouse(@PathVariable("houseId") Long houseId, @PathVariable("imageId") Long imageId) {
		logger.info("Deleting house with id {}", houseId);
		House house = this.houseService.findById(houseId);
		if (house == null) {
			logger.error("House with id {} not found.", houseId);
			return new ResponseEntity<>(new ResponseDto("House with id " + houseId 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		
		House result = this.houseService.deleteImageOfHouse(houseId, imageId);
		if(result == null) {
			return new ResponseEntity<ResponseDto>(new ResponseDto("Can not delete image", imageId), HttpStatus.NOT_FOUND);
		}
		ResponseDto res = new ResponseDto("OK", result);
		return new ResponseEntity<ResponseDto>(res, HttpStatus.OK);
	}

}