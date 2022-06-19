package com.hapinistay.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.House;

/**
 * @author huynhvang
 *
 */
@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

	Page<House> findByDescriptionViContainsOrNameViContainsAllIgnoreCase(String descriptionViPart,
            String nameViPart,
            Pageable pageReguest);
	
	Page<House> findByDescriptionEnContainsOrNameEnContainsAllIgnoreCase(String descriptionEnPart,
            String nameEnPart,
            Pageable pageReguest);
	
	Page<House> findBySearchTermNamedWithDescriptionOrName(@Param("searchTerm") String searchTerm, 
            Pageable pageRequest);
	
	Page<House> findByDistrictCode(@Param("districtCode") String districtCode, 
            Pageable pageRequest);
	
	Page<House> findNewHousesByDistrictCodeAndStatus(@Param("districtCode") String districtCode, @Param("status") String status,
            Pageable pageRequest);
	
	Page<House> findByStatusContainsAllIgnoreCase(String statusPart,
            Pageable pageReguest);
	
	Page<House> findByStatus(String status, Pageable pageReguest);
	
	Page<House> findByDistrictCodeWithAvailableRoom(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findByDistrictCodeWithAvailableRoomSortByPrice(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findByDistrictCodeWithAvailableRoomSortByLastDate(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findHousesByAvailableAndStatus(@Param("houseStatus") String houseStatus, @Param("roomStatus") String roomStatus, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findAllWithAvailableRoomSortByLevelLastDate(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
}
