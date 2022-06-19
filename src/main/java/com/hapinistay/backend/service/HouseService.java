package com.hapinistay.backend.service;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.House;

public interface HouseService {
	
	House findById(Long id, String lang);
	
	House findById(Long id);

	House saveHouse(House house, String lang);
	
	House saveHouse(House house);

	House updateHouse(House house, String lang);
	
	House updateHouse(House house);

	void deleteAllHouses();

	List<House> findAllHouses();

	boolean isHouseExist(House house);
	
	Page<House> findAllHousePageable(Integer index, Integer size, String lang);
	
	Room addRoomToHouse(Long house_id, Room room, String lang);
	
	House removeRoomFromHouse(Long houseId, Long roomId);
	
	House createHouseByUser(House house, Long userId, String lang);
	
	Page<House> findByDescriptionContainsOrNameContainsAllIgnoreCase(String descriptionPart,
            String namePart,
            Pageable pageReguest, String lang);
	
	Page<House> findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize(String descriptionPart,
            String namePart,
            Integer index, Integer size,
            String lang);
	
	Page<House> findBySearchTermNamedWithDescriptionOrName(String searchTerm,
            Pageable pageReguest, String lang);
	
	Page<House> findByDistrictCode(@Param("districtCode") String districtCode, 
            Pageable pageRequest, String lang);
	
	Page<House> findByDistrictCode(@Param("districtCode") String districtCode, 
			Integer index, Integer size, String lang);
	
	Set<House> getAllHousesOfUser(Long userId, String lang);
	
	Page<House> findByStatus(String status, Integer index, Integer size, String lang);
	
	Page<House> findNewHousesByDistrictCodeAndStatus(@Param("districtCode") String districtCode, @Param("status") String status,
			Integer index, Integer size, String lang);
	
	Page<House> findByDistrictCodeWithAvailableRoom(String districtCode, boolean available,
			Integer index, Integer size, String lang);
	
	Page<House> findByDistrictCodeWithAvailableRoomSortByPrice(String districtCode, boolean available, String priceOrderBy,
			Integer index, Integer size, String lang);
	
	Page<House> findByDistrictCodeWithAvailableRoomSortByDate(String districtCode, boolean available,
			Integer index, Integer size, String lang);
	
	Page<House> findHousesByAvailableAndStatus(String houseStatus, String roomStatus, boolean available,
			Integer index, Integer size, String lang);
	
	Page<House> findAllWithAvailableRoomSortByLevelLastDate(String districtCode, boolean available,
			Integer index, Integer size, String lang);
	
	House deleteImageOfHouse(Long houseId, Long imageId);
}