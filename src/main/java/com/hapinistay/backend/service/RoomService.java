package com.hapinistay.backend.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.Room;

public interface RoomService {
	
	Room findById(Integer id);
	
	Room findById(Integer id, String lang);

	void saveRoom(Room room, String lang);

	void updateRoom(Room room, String lang);
	
	void updateRoom(Room room);

	void deleteRoomById(Integer id);

	void deleteAllRooms();

	List<Room> findAllRooms();

	boolean isRoomExist(Room room);
	
	House saveRoomOfHouse(Room room, Long house_id);
	
	House saveRoomOfHouse(Room room, Long house_id, String lang);
	
	Room saveAvatarOfRoom(Image image, Long room_id);
	
	Room enableAvailableOfRoom(Long room_id, Long level, Long userId);
	
	Room disableAvailableOfRoom(Long room_id, Long userId);
	
	Room updateRoomStatus(Long roomId, String status, Long level);
	
	Page<Room> findByStatus(String status, Integer index, Integer size, String lang);
	
	Room deleteImageOfRoom(Long roomId, Long imageId);
}