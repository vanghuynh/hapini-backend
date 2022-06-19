package com.hapinistay.backend.controller;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hapinistay.backend.dto.ResponseDto;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.service.RoomService;
import com.hapinistay.backend.util.Constants;
import com.hapinistay.backend.service.CloudinaryService;
import com.hapinistay.backend.service.HouseService;

@RestController
@RequestMapping("/rooms")
public class RoomController {

	public static final Logger logger = LoggerFactory.getLogger(RoomController.class);

	@Autowired
	HouseService houseService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	CloudinaryService cloudinaryService;

	/**
	 * Get all rooms in database
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> listAllRooms() {
		List<Room> rooms = this.roomService.findAllRooms();
		if (rooms.isEmpty()) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", rooms), HttpStatus.OK);
	}
	
	/**
	 * Get single room
	 * @param id
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getRoom(@PathVariable("id") Long id, @RequestParam(required=false) String lang) {
		logger.info("Fetching room with id {}", id);
		Room room = this.roomService.findById(id.intValue(), lang);
		if (room == null) {
			logger.error("Room with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.OK);
	}
	
	/**
	 * Delete room with id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> deleteRoom(@PathVariable("id") Long id) {
		logger.info("Deleting room with id {}", id);
		Room room = this.roomService.findById(id.intValue());
		if (room == null) {
			logger.error("Room with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		if(room.getImages()!= null && room.getImages().size() > 0) {
			List<String> publicIds = room.getImages().stream().map(e -> e.getPublic_id()).collect(Collectors.toList());
			try {
				this.cloudinaryService.deleteImage(publicIds);
			} catch (Exception e1) {
				e1.printStackTrace();
				return new ResponseEntity<>(new ResponseDto("Error delete images", publicIds), HttpStatus.NOT_FOUND);
			}
		}
		this.roomService.deleteRoomById(id.intValue());
		
		return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.OK);
	}

	/**
	 * Create or update room
	 * @param room
	 * @param lang
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> createRoom(@RequestBody Room room, @RequestParam(required=false) String lang) {
		logger.info("Creating room : {}", room);
		if(room.getId() == 0 || room.getId() == null) {
			room.setLevel(Constants.LEVEL_STANDARD);
			room.setAvailable(true);
			this.roomService.saveRoom(room, lang);
		} else {
			this.roomService.updateRoom(room, lang);
		}
		
		return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.CREATED);
	}
	
	/**
	 * Enable room available
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/{id}/enable", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> enableRoomAvailable(@PathVariable("id") Long id, @RequestParam(value="level", required = true) Long level,
			 @RequestParam(value="userId", required = true) Long userId) {
		logger.info("Enable available of room with id {}", id);
		Room room = this.roomService.findById(id.intValue());
		if (room == null) {
			logger.error("Room with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		if (!Constants.LEVEL_STANDARD.equals(level) && !Constants.LEVEL_VIP1.equals(level)) {
			logger.error("Room level {} not found.", level);
			return new ResponseEntity<>(new ResponseDto("Room level " + level 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		
		room = this.roomService.enableAvailableOfRoom(id, level, userId);
		return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.OK);
	}
	
	/**
	 * Disable room available
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "/{id}/disable", method = RequestMethod.GET)
	public ResponseEntity<?> disableRoomAvailable(@PathVariable("id") Long id, @RequestParam(value="userId", required = true) Long userId) {
		logger.info("Disable available of room with id {}", id);
		Room room = this.roomService.findById(id.intValue());
		if (room == null) {
			logger.error("Room with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		room = this.roomService.disableAvailableOfRoom(id,  userId);
		return new ResponseEntity<>(new ResponseDto("OK", room), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search-status/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchRoomByStatusWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="status", required = false) String status, @RequestParam(required=false) String lang) {
		Page<Room> rooms = this.roomService.findByStatus(status, index, size, lang);
		if (rooms == null || rooms.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("NO_CONTENT", null), HttpStatus.NO_CONTENT);
		}
		rooms.forEach(room -> {
				if(room.getHouse() != null) {
					room.setHouseId(room.getHouse().getId());
				}
			});
		return new ResponseEntity<>(new ResponseDto("OK", rooms), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
	@RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> changeRoomStatus(@PathVariable("id") Long id,
			@RequestParam(required=false) String status, @RequestParam(value="level", required = true) Long level) {
		logger.info("Change room status with id {}", id);
		Room room = this.roomService.findById(id.intValue());
		if (room == null) {
			logger.error("Room with id {} not found.", id);
			return new ResponseEntity<>(new ResponseDto("Room with id " + id 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		Room resultRoom = this.roomService.updateRoomStatus(id, status, level);
		return new ResponseEntity<>(new ResponseDto("OK", resultRoom), HttpStatus.OK);
	}


	/**
	 * Delete image of room
	 * @param roomId
	 * @param imageId
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER_AUTHORITY')")
	@RequestMapping(value = "images/{roomId}/{imageId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDto> deleteImageOfRoom(@PathVariable("roomId") Long roomId, @PathVariable("imageId") Long imageId) {
		logger.info("Deleting room image with id {}", roomId);
		Room room = this.roomService.findById(roomId.intValue());
		if (room == null) {
			logger.error("Room with id {} not found.", roomId);
			return new ResponseEntity<>(new ResponseDto("Room with id " + roomId 
					+ " not found", null), HttpStatus.NOT_FOUND);
		}
		
		Room result = this.roomService.deleteImageOfRoom(roomId, imageId);
		if(result == null) {
			return new ResponseEntity<ResponseDto>(new ResponseDto("Can not delete image", imageId), HttpStatus.NOT_FOUND);
		}
		ResponseDto res = new ResponseDto("OK", result);
		return new ResponseEntity<ResponseDto>(res, HttpStatus.OK);
	}
}