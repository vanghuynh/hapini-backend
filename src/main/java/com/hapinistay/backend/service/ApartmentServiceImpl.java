package com.hapinistay.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.model.History;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.repositories.ApartmentRepository;
import com.hapinistay.backend.util.DateUtil;



@Service("apartmentService")
@Transactional
public class ApartmentServiceImpl implements ApartmentService{

	@Autowired
	private ApartmentRepository apartmentRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private AddressSettingService addressSettingService;

	@Override
	public Apartment findById(Long id, String lang) {
		Apartment apartment = this.apartmentRepository.findOne(id.longValue());
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(apartment != null && apartment.getAddress() != null) {
			apartment.getAddress().setCityName(codeNames.get(apartment.getAddress().getCity()));
			apartment.getAddress().setDistrictName(codeNames.get(apartment.getAddress().getDistrict()));
			apartment.getAddress().setWardName(codeNames.get(apartment.getAddress().getWard()));
		}
		if(apartment != null) {
			apartment.getLanguageDto(lang);
		}
		return apartment;
	}

	@Override
	public Apartment saveApartment(Apartment apartment, String lang) {
		if(apartment != null && apartment.getId() != null) {
			this.updateApartment(apartment, lang);
		} else {
			apartment.setLanguageDto(lang);
			this.apartmentRepository.save(apartment);
		}
		return apartment;
	}

	@Override
	public Apartment updateApartment(Apartment apartment, String lang) {
		apartment.setLanguageDto(lang);
		this.apartmentRepository.save(apartment);
		return apartment;
	}

	@Override
	public void deleteAllApartments() {
		this.apartmentRepository.deleteAll();
	}

	@Override
	public List<Apartment> findAllApartments() {
		return this.apartmentRepository.findAll();
	}

	@Override
	public boolean isApartmentExist(Apartment apartment) {
		return false;
	}

	@Override
	public Page<Apartment> findByDistrictCode(String districtCode, Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Apartment> pageResult = this.apartmentRepository.findAll(page);
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		pageResult.getContent().forEach(e -> {
			e.getLanguageDto(lang);
			if(e.getAddress() != null) {
				e.getAddress().setCityName(codeNames.get(e.getAddress().getCity()));
				e.getAddress().setDistrictName(codeNames.get(e.getAddress().getDistrict()));
				e.getAddress().setWardName(codeNames.get(e.getAddress().getWard()));
			}
		});
		return pageResult;
	}

	@Override
	public Page<Apartment> findByDistrictCodeWithAvailableSortByPrice(String districtCode, boolean available,
			Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.apartmentRepository.findByDistrictCodeWithAvailableSortByPrice(districtCode, available, page);
		List<Apartment> apartments = new ArrayList<Apartment>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					apartments.add((Apartment)row[0]);
				}
				return apartments;
			});
		}
		if(apartments != null && apartments.size() > 0) {
			apartments.forEach(apartment -> {
				apartment.getLanguageDto(lang);
				if(apartment.getAddress() != null) {
					apartment.getAddress().getLanguageDto(lang);
				}
			});
		}
		this.improveSearchResult(apartments, lang);
		Page<Apartment> apartmentPage = new PageImpl<Apartment>(apartments, page, result.getTotalElements());
		return apartmentPage;
	}

	@Override
	public Page<Apartment> findByDistrictCodeWithAvailableSortByLastDate(String districtCode, boolean available,
			Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.apartmentRepository.findByDistrictCodeWithAvailableSortByLastDate(districtCode, available, page);
		List<Apartment> apartments = new ArrayList<Apartment>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					apartments.add((Apartment)row[0]);
				}
				return apartments;
			});
		}
		if(apartments != null && apartments.size() > 0) {
			apartments.forEach(apartment -> {
				apartment.getLanguageDto(lang);
				if(apartment.getAddress() != null) {
					apartment.getAddress().getLanguageDto(lang);
				}
			});
		}
		this.improveSearchResult(apartments, lang);
		Page<Apartment> apartmentPage = new PageImpl<Apartment>(apartments, page, result.getTotalElements());
		return apartmentPage;
	}

	@Override
	public Page<Apartment> findApartmentByAvailableAndStatus(String districtCode, String status, boolean available, Integer index,
			Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.apartmentRepository.findApartmentByAvailableAndStatus(districtCode, status, available, page);
		List<Apartment> apartments = new ArrayList<Apartment>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					apartments.add((Apartment)row[0]);
				}
				return apartments;
			});
		}
		if(apartments != null && apartments.size() > 0) {
			apartments.forEach(apartment -> {
				apartment.getLanguageDto(lang);
				if(apartment.getAddress() != null) {
					apartment.getAddress().getLanguageDto(lang);
				}
			});
		}
		this.improveSearchResult(apartments, lang);
		Page<Apartment> apartmentPage = new PageImpl<Apartment>(apartments, page, result.getTotalElements());
		return apartmentPage;
	}

	@Override
	public Page<Apartment> findAllWithAvailableSortByLevelLastDate(String districtCode, boolean available,
			Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.apartmentRepository.findAllWithAvailableSortByLevelLastDate(districtCode, available, page);
		List<Apartment> apartments = new ArrayList<Apartment>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					apartments.add((Apartment)row[0]);
				}
				return apartments;
			});
		}
		if(apartments != null && apartments.size() > 0) {
			apartments.forEach(apartment -> {
				apartment.getLanguageDto(lang);
				if(apartment.getAddress() != null) {
					apartment.getAddress().getLanguageDto(lang);
				}
			});
		}
		this.improveSearchResult(apartments, lang);
		Page<Apartment> apartmentPage = new PageImpl<Apartment>(apartments, page, result.getTotalElements());
		return apartmentPage;
	}

	@Override
	public Apartment createApartmentByUser(Apartment apartment, Long userId, String lang) {
		User user = this.userService.findById(userId);
		if(user != null) {
			apartment.setUser(user);
			user.getApartments().add(apartment);
			return this.saveApartment(apartment, lang);
		}
		return null;
	}

	@Override
	public Set<Apartment> getAllApartmentsOfUser(Long userId, String lang) {
		User user = this.userService.findById(userId);
		if (user == null) {
			return null;
		}
		Set<Apartment> apartments = user.getApartments();
		if(apartments != null && apartments.size() > 0) {
			apartments.forEach(apartment -> {
				apartment.getLanguageDto(lang);
				if(apartment.getAddress() != null) {
					apartment.getAddress().getLanguageDto(lang);
				}
			});
		}
		return this.improveSearchResult(apartments, lang);
	}

	@Override
	public Apartment deleteImageOfApartment(Long apartmentId, Long imageId) {
		Apartment apartment = this.apartmentRepository.findOne(apartmentId);
		if (apartmentId == null) {
			return null;
		}
		if(apartment.getImages() != null && apartment.getImages().size() > 0) {
			Image image = apartment.getImages().stream().filter(e -> e.getId().equals(imageId)).findFirst().orElse(null);
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
			
			boolean result = apartment.getImages().removeIf(e -> e.getId().equals(imageId) );
			if(result) {
				return apartment;
			}
		}
		return null;
	}


	private Pageable createPageRequest(Integer index, Integer size) {
	    return new PageRequest(index, size);
	}
	
	private Page<Apartment> improveSearchResult(Page<Apartment> page, String lang) {
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(page != null) {
			page.getContent().forEach(e -> {
				if(e.getAddress() != null) {
					e.getAddress().setCityName(codeNames.get(e.getAddress().getCity()));
					e.getAddress().setDistrictName(codeNames.get(e.getAddress().getDistrict()));
					e.getAddress().setWardName(codeNames.get(e.getAddress().getWard()));
				}
			});
		}
		return page;
	}
	
	private List<Apartment> improveSearchResult(List<Apartment> apartments, String lang) {
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(apartments != null) {
			apartments.forEach(e -> {
				if(e.getAddress() != null) {
					e.getAddress().setCityName(codeNames.get(e.getAddress().getCity()));
					e.getAddress().setDistrictName(codeNames.get(e.getAddress().getDistrict()));
					e.getAddress().setWardName(codeNames.get(e.getAddress().getWard()));
				}
			});
		}
		return apartments;
	}
	
	private Set<Apartment> improveSearchResult(Set<Apartment> apartments, String lang) {
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(apartments != null) {
			apartments.forEach(e -> {
				if(e.getAddress() != null) {
					e.getAddress().setCityName(codeNames.get(e.getAddress().getCity()));
					e.getAddress().setDistrictName(codeNames.get(e.getAddress().getDistrict()));
					e.getAddress().setWardName(codeNames.get(e.getAddress().getWard()));
				}
			});
		}
		return apartments;
	}

	@Override
	public Apartment updateApartmentStatus(Long apartmentId, String status, Long level, boolean available, Long userId, String lang) {
		Apartment apartment = this.apartmentRepository.findOne(apartmentId);
		if(apartment != null) {
			apartment.setStatus(status);
			apartment.setLevel(level);
			apartment.setAvailable(available);
			apartment.setLastUpdate(DateUtil.getCurrentDate());
			Apartment updatedApartment = this.updateApartment(apartment, lang);
			if(updatedApartment != null) {
				User user = this.userService.findById(userId);
				if(user != null) {
					History history = new History();
					history.setAction(status);
					history.setLastUpdate(DateUtil.getCurrentDate());
					history.setUser(user);
					user.getHistories().add(history);
					this.userService.updateUser(user);
				}
				return updatedApartment;
			}
		}
		return null;
	}
}
