package com.hapinistay.backend.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.History;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.repositories.RoomRepository;
import com.hapinistay.backend.util.CommonUtil;
import com.hapinistay.backend.util.Constants;
import com.hapinistay.backend.util.DateUtil;
import com.hapinistay.backend.repositories.ImageRepository;



@Service("roomService")
@Transactional
public class RoomServiceImpl implements RoomService{
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private HouseService roomService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private ImageService imageService;

	@Override
	public Room findById(Integer id) {
		return this.roomRepository.findOne(id.longValue());
	}

	@Override
	public void saveRoom(Room room, String lang) {
		room.setLanguageDto(lang);
		this.roomRepository.save(room);
		
	}

	@Override
	public void updateRoom(Room room, String lang) {
		room.setLanguageDto(lang);
		this.roomRepository.save(room);
	}

	@Override
	public void deleteRoomById(Integer id) {
		Room room = this.findById(id);
		if(room != null) {
			room.setHouse(null);
			this.roomRepository.delete(room);
		}
		
	}

	@Override
	public void deleteAllRooms() {
		this.roomRepository.deleteAll();
		
	}

	@Override
	public List<Room> findAllRooms() {
		return this.roomRepository.findAll();
	}

	@Override
	public boolean isRoomExist(Room room) {
		return room.getId() != 0;
	}

	@Override
	public House saveRoomOfHouse(Room room, Long house_id) {
		House house = this.roomService.findById(house_id);
		if(house != null) {
			house.getRooms().add(room);
			return this.roomService.saveHouse(house);
		}
		return null;
	}

	@Override
	public Room saveAvatarOfRoom(Image image, Long room_id) {
		Room room = this.roomRepository.findOne(room_id);
		if(room != null) {
			room.setAvatar(image.getSecure_url());
			return this.roomRepository.save(room);
		}
		return null;
	}

	@Override
	public Room enableAvailableOfRoom(Long room_id, Long level, Long userId) {
		Room room = this.roomRepository.findOne(room_id);
		if(room != null) {
			room.setLevel(level);
			room.setAvailable(true);
			room.setStatus(Constants.STATUS_PENDING);
			room.setLastUpdate(DateUtil.getCurrentDate());
			Room updatedRoom = this.roomRepository.save(room);
			if(updatedRoom != null) {
				User user = this.userService.findById(userId);
				if(user != null) {
					History history = new History();
					history.setAction("ROOM ENABLE");
					history.setLastUpdate(DateUtil.getCurrentDate());
					history.setUser(user);
					user.getHistories().add(history);
					this.userService.updateUser(user);
				}
				return updatedRoom;
			}
		}
		return null;
	}

	@Override
	public Room disableAvailableOfRoom(Long room_id, Long userId) {
		Room room = this.roomRepository.findOne(room_id);
		if(room != null) {
			room.setAvailable(false);
			room.setLevel(Long.valueOf(0));
			return this.roomRepository.save(room);
		}
		return null;
	}

	@Override
	public void updateRoom(Room room) {
		this.roomRepository.save(room);
		
	}

	@Override
	public Room findById(Integer id, String lang) {
		Room room = this.findById(id);
		room.getLanguageDto(lang);
		return room;
	}

	@Override
	public House saveRoomOfHouse(Room room, Long house_id, String lang) {
		room.setLanguageDto(lang);
		return this.saveRoomOfHouse(room, house_id);
	}

	@Override
	public Room updateRoomStatus(Long roomId, String status, Long level) {
		Room room = this.findById(roomId.intValue());
		if(room != null) {
			room.setStatus(status);
			room.setLevel(level);
			room.setLastUpdate(DateUtil.getCurrentDate());
			this.updateRoom(room);
			return room;
		}
		return null;
	}

	@Override
	public Page<Room> findByStatus(String status, Integer index, Integer size, String lang) {
		Pageable page = CommonUtil.createPageRequest(index, size);
		Page<Room> result = this.roomRepository.findByStatus(status, page);
		if(result != null && result.getContent() != null) {
			result.getContent().forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		return result;
	}

	@Override
	public Room deleteImageOfRoom(Long roomId, Long imageId) {
		Room room = this.findById(roomId.intValue());
		if (room == null) {
			return null;
		}
		if(room.getImages() != null && room.getImages().size() > 0) {
			Image image = room.getImages().stream().filter(e -> e.getId().equals(imageId)).findFirst().orElse(null);
			if(image != null) {
				String publicId = image.getPublic_id();
				try {
					this.cloudinaryService.deleteImage(Arrays.asList(publicId));
					this.imageService.deleteImageById(imageId.intValue());
				} catch (Exception e1) {
					e1.printStackTrace();
					return null;
				}
			}
			
			boolean result = room.getImages().removeIf(e -> e.getId().equals(imageId) );
			if(result) {
				return room;
			}
		}
		return null;
	}

}
