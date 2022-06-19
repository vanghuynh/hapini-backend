package com.hapinistay.backend.service;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.model.House;

public interface ApartmentService {
	
	Apartment findById(Long id, String lang);
	

	Apartment saveApartment(Apartment apartment, String lang);

	Apartment updateApartment(Apartment apartment, String lang);

	void deleteAllApartments();

	List<Apartment> findAllApartments();

	boolean isApartmentExist(Apartment apartment);
	
	
	Page<Apartment> findByDistrictCode(String districtCode, 
			Integer index, Integer size, String lang);
	
	Page<Apartment> findByDistrictCodeWithAvailableSortByPrice(String districtCode, boolean available,
			Integer index, Integer size, String lang);
	
	Page<Apartment> findByDistrictCodeWithAvailableSortByLastDate(String districtCode, boolean available,
			Integer index, Integer size, String lang);
	
	Page<Apartment> findApartmentByAvailableAndStatus(String districtCode, String status, boolean available,
			Integer index, Integer size, String lang);
	
	Page<Apartment> findAllWithAvailableSortByLevelLastDate(String districtCode, @Param("available") boolean available,
			Integer index, Integer size, String lang);
	
	Apartment createApartmentByUser(Apartment apartment, Long userId, String lang);
	
	Set<Apartment> getAllApartmentsOfUser(Long userId, String lang);
	
	Apartment deleteImageOfApartment(Long apartmentId, Long imageId);
	
	Apartment updateApartmentStatus(Long apartmentId, String status, Long level, boolean available, Long userId, String lang);
}