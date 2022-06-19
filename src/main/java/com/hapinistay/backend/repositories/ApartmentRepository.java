package com.hapinistay.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Apartment;

/**
 * @author huynhvang
 *
 */
@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
	
	Page<Apartment> findByDistrictCode(@Param("districtCode") String districtCode, 
            Pageable pageRequest);
	
	Page<Object> findByDistrictCodeWithAvailableSortByPrice(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findByDistrictCodeWithAvailableSortByLastDate(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findApartmentByAvailableAndStatus(@Param("districtCode") String districtCode, @Param("status") String status, @Param("available") boolean available,
            Pageable pageRequest);
	
	Page<Object> findAllWithAvailableSortByLevelLastDate(@Param("districtCode") String districtCode, @Param("available") boolean available,
            Pageable pageRequest);
	
}
