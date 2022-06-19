package com.hapinistay.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.dto.ResponseDto;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Image;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.repositories.RoomRepository;
import com.hapinistay.backend.repositories.HouseRepository;
import com.hapinistay.backend.util.Constants;



@Service("houseService")
@Transactional
public class HouseServiceImpl implements HouseService{

	@Autowired
	private HouseRepository houseRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private AddressSettingService addressSettingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CloudinaryService cloudinaryService;
	
	@Autowired
	private ImageService imageService;

	@Override
	public House findById(Long id, String lang) {
		House room = this.houseRepository.findOne(id.longValue());
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(room != null && room.getAddress() != null) {
			room.getAddress().setCityName(codeNames.get(room.getAddress().getCity()));
			room.getAddress().setDistrictName(codeNames.get(room.getAddress().getDistrict()));
			room.getAddress().setWardName(codeNames.get(room.getAddress().getWard()));
		}
		if(room != null) {
			room.getLanguageDto(lang);
		}
		return room;
	}

	@Override
	public House saveHouse(House house, String lang) {
		if(house != null && house.getId() != null) {
			this.updateHouse(house, lang);
		} else {
			house.setLanguageDto(lang);
			this.houseRepository.save(house);
		}
		return house;
		
	}

	@Override
	public House updateHouse(House house, String lang) {
		house.setLanguageDto(lang);
		this.houseRepository.save(house);
		return house;
	}

	@Override
	public void deleteAllHouses() {
		this.houseRepository.deleteAll();
		
	}

	@Override
	public List<House> findAllHouses() {
		return this.houseRepository.findAll();
	}

	@Override
	public boolean isHouseExist(House house) {
		return false;
	}
	
	private Pageable createPageRequest(Integer index, Integer size) {
	    return new PageRequest(index, size);
	}

	@Override
	public Page<House> findAllHousePageable(Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<House> pageResult = this.houseRepository.findAll(page);
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
	
	private Page<House> improveSearchResult(Page<House> page, String lang) {
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
	
	private List<House> improveSearchResult(List<House> houses, String lang) {
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(houses != null) {
			houses.forEach(e -> {
				if(e.getAddress() != null) {
					e.getAddress().setCityName(codeNames.get(e.getAddress().getCity()));
					e.getAddress().setDistrictName(codeNames.get(e.getAddress().getDistrict()));
					e.getAddress().setWardName(codeNames.get(e.getAddress().getWard()));
				}
			});
		}
		return houses;
	}
	
	private Set<House> improveSearchResult(Set<House> houses, String lang) {
		Map<String, String> codeNames = this.addressSettingService.findAllNameFromCode(lang);
		if(houses != null) {
			houses.forEach(e -> {
				if(e.getAddress() != null) {
					e.getAddress().setCityName(codeNames.get(e.getAddress().getCity()));
					e.getAddress().setDistrictName(codeNames.get(e.getAddress().getDistrict()));
					e.getAddress().setWardName(codeNames.get(e.getAddress().getWard()));
				}
			});
		}
		return houses;
	}
	
	@Override
	public Room addRoomToHouse(Long house_id, Room room, String lang) {
		House house = this.houseRepository.findOne(house_id);
		if (house == null) {
			return null;
		}
		room.setHouse(house);
		room.setLanguageDto(lang);
		house.getRooms().add(room);
		Room savedRoom = this.roomRepository.save(room);
		savedRoom.getLanguageDto(lang);
		return savedRoom;
	}

	@Override
	public House removeRoomFromHouse(Long house_id, Long room_id) {
		House house = this.houseRepository.findOne(house_id);
		if (house == null) {
			return null;
		}
		if(house.getRooms() != null) {
			Room room = house.getRooms().stream().filter(e -> e.getId() == room_id).findFirst().get();
			if(room != null && room.getImages()!= null && room.getImages().size() > 0) {
				List<String> publicIds = room.getImages().stream().map(e -> e.getPublic_id()).collect(Collectors.toList());
				try {
					this.cloudinaryService.deleteImage(publicIds);
				} catch (Exception e1) {
					e1.printStackTrace();
					return null;
				}
			}
			
			boolean result = house.getRooms().removeIf(e -> e.getId() == room_id);
			if(result) {
				this.houseRepository.save(house);
			}
		}
		return house;
	}

	@Override
	public House createHouseByUser(House house, Long user_id, String lang) {
		User user = this.userService.findById(user_id);
		if(user != null) {
			house.setUser(user);
			user.getHouses().add(house);
			return this.saveHouse(house, lang);
		}
		return null;
	}

	@Override
	public Page<House> findByDescriptionContainsOrNameContainsAllIgnoreCase(String descriptionPart, String namePart,
			Pageable pageReguest, String lang) {
		Page<House> result = this.houseRepository.findByDescriptionViContainsOrNameViContainsAllIgnoreCase(descriptionPart, namePart, pageReguest);
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize(String descriptionPart,
			String namePart, Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<House> result;
		if(lang != null && Constants.LANG_EN.equals(lang)) {
			result = this.houseRepository.findByDescriptionEnContainsOrNameEnContainsAllIgnoreCase(descriptionPart, namePart, page);
		} else {
			result = this.houseRepository.findByDescriptionViContainsOrNameViContainsAllIgnoreCase(descriptionPart, namePart, page);
		}
		 
		if(result != null & result.getContent() != null) {
			result.getContent().forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findBySearchTermNamedWithDescriptionOrName(String searchTerm,
			Pageable pageReguest, String lang) {
		Page<House> result =  this.houseRepository.findBySearchTermNamedWithDescriptionOrName(searchTerm, pageReguest);
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findByDistrictCode(String districtCode, Pageable pageRequest, String lang) {
		Page<House> result = this.houseRepository.findByDistrictCode(districtCode, pageRequest);
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findByDistrictCode(String districtCode, Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<House> result = this.houseRepository.findByDistrictCode(districtCode, page);
		if(result != null && result.getContent() != null) {
			result.getContent().forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		return this.improveSearchResult(result, lang);
	}

	@Override
	public House findById(Long id) {
		return this.houseRepository.findOne(id.longValue());
	}

	@Override
	public House saveHouse(House house) {
		return this.houseRepository.save(house);
	}

	@Override
	public House updateHouse(House house) {
		return this.saveHouse(house);
	}

	@Override
	public Set<House> getAllHousesOfUser(Long userId, String lang) {
		User user = this.userService.findById(userId);
		if (user == null) {
			return null;
		}
		Set<House> userHouses = user.getHouses();
		if(userHouses != null && userHouses.size() > 0) {
			userHouses.forEach(house -> {
				house.getLanguageDto(lang);
				if(house.getRooms() != null && house.getRooms() != null) {
					house.getRooms().forEach(room -> {
						room.getLanguageDto(lang);
					});
				}
				if(house.getAddress() != null) {
					house.getAddress().getLanguageDto(lang);
				}
			});
		}
		return this.improveSearchResult(userHouses, lang);
	}

	@Override
	public Page<House> findByStatus(String status, Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<House> result = this.houseRepository.findByStatus(status, page);
		if(result != null && result.getContent() != null) {
			result.getContent().forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findNewHousesByDistrictCodeAndStatus(String districtCode, String status, Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<House> result = this.houseRepository.findNewHousesByDistrictCodeAndStatus(districtCode, status, page);
		if(result != null && result.getContent() != null) {
			result.getContent().forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findByDistrictCodeWithAvailableRoom(String districtCode, boolean available, Integer index,
			Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<House> result = this.houseRepository.findByDistrictCodeWithAvailableRoom(districtCode, available, page);
		if(result != null && result.getContent() != null) {
			result.getContent().forEach(e -> {
				e.getLanguageDto(lang);
			});
		}
		return this.improveSearchResult(result, lang);
	}

	@Override
	public Page<House> findByDistrictCodeWithAvailableRoomSortByPrice(String districtCode, boolean available,
			String priceOrderBy, Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.houseRepository.findByDistrictCodeWithAvailableRoomSortByPrice(districtCode, available, page);
		List<House> houses = new ArrayList<House>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					houses.add((House)row[0]);
				}
				houses.forEach(house -> {
					house.getLanguageDto(lang);
				});
				this.improveSearchResult(houses, lang);
				return houses;
			});
		}
		Page<House> housePage = new PageImpl<House>(houses, page, result.getTotalElements());
		return housePage;
	}

	@Override
	public Page<House> findByDistrictCodeWithAvailableRoomSortByDate(String districtCode, boolean available,
		 Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.houseRepository.findByDistrictCodeWithAvailableRoomSortByLastDate(districtCode, available, page);
		List<House> houses = new ArrayList<House>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					houses.add((House)row[0]);
				}
				houses.forEach(house -> {
					house.getLanguageDto(lang);
				});
				this.improveSearchResult(houses, lang);
				return houses;
			});
		}
		Page<House> housePage = new PageImpl<House>(houses, page, result.getTotalElements());
		return housePage;
	}

	@Override
	public Page<House> findHousesByAvailableAndStatus(String houseStatus, String roomStatus, boolean available,
			Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.houseRepository.findHousesByAvailableAndStatus(houseStatus, roomStatus, available, page);
		List<House> houses = new ArrayList<House>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					houses.add((House)row[0]);
				}
				houses.forEach(house -> {
					house.getLanguageDto(lang);
				});
				this.improveSearchResult(houses, lang);
				return houses;
			});
		}
		Page<House> housePage = new PageImpl<House>(houses, page, result.getTotalElements());
		return housePage;
	}

	@Override
	public Page<House> findAllWithAvailableRoomSortByLevelLastDate(String districtCode, boolean available,
			Integer index, Integer size, String lang) {
		Pageable page = this.createPageRequest(index, size);
		Page<Object> result = this.houseRepository.findAllWithAvailableRoomSortByLevelLastDate(districtCode, available, page);
		List<House> houses = new ArrayList<House>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					houses.add((House)row[0]);
				}
				houses.forEach(house -> {
					house.getLanguageDto(lang);
				});
				this.improveSearchResult(houses, lang);
				return houses;
			});
		}
		Page<House> housePage = new PageImpl<House>(houses, page, result.getTotalElements());
		return housePage;
	}

	@Override
	public House deleteImageOfHouse(Long houseId, Long imageId) {
		House house = this.findById(houseId);
		if (house == null) {
			return null;
		}
		if(house.getImages() != null && house.getImages().size() > 0) {
			Image image = house.getImages().stream().filter(e -> e.getId().equals(imageId)).findFirst().orElse(null);
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
			
			boolean result = house.getImages().removeIf(e -> e.getId().equals(imageId) );
			if(result) {
				return house;
						//this.updateHouse(house);
			}
		}
		return null;
	}

}
