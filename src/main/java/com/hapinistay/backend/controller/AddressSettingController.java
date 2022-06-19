package com.hapinistay.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hapinistay.backend.dto.SettingDto;
import com.hapinistay.backend.model.AddressSetting;
import com.hapinistay.backend.model.Room;
import com.hapinistay.backend.service.AddressSettingService;
import com.hapinistay.backend.service.RoomService;
import com.hapinistay.backend.service.HouseService;

@RestController
@RequestMapping("/setting")
public class AddressSettingController {

	public static final Logger logger = LoggerFactory.getLogger(AddressSettingController.class);

	@Autowired
	AddressSettingService addressSettingService;

	// -------------------Retrieve All Address Setting---------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> listAllAddressSetting() {
		List<AddressSetting> addressSettings = this.addressSettingService.findAllAddressSettings();
		if (addressSettings == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<AddressSetting>>(addressSettings, HttpStatus.OK);
	}
	
	// -------------------Delete Address Setting------------------------------------------
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAddressSetting(@PathVariable("id") Long id) {
		logger.info("Deleting AddressSetting with id {}", id);
		AddressSetting addressSetting = this.addressSettingService.findById(id);
		if (addressSetting == null) {
			logger.error("AddressSetting with id {} not found.", id);
			return new ResponseEntity("Address setting with id " + id 
					+ " not found", HttpStatus.NOT_FOUND);
		}
		this.addressSettingService.deleteAddressSettingById(id);
		
		return new ResponseEntity<AddressSetting>(addressSetting, HttpStatus.OK);
	}

	// -------------------Create/Update AddressSetting-------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createAddressSetting(@RequestBody AddressSetting addressSetting) {
		logger.info("Creating/updating AddressSetting : {}", addressSetting);
		if(addressSetting != null) {
			this.addressSettingService.saveAddressSetting(addressSetting);
		}
		
		return new ResponseEntity<AddressSetting>(addressSetting, HttpStatus.CREATED);
	}
	
	// -------------------Retrieve Address Setting------------------------------------------
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAddressSetting(@PathVariable("id") Long id) {
		logger.info("Fetching AddressSetting with id {}", id);
		AddressSetting addressSetting = this.addressSettingService.findById(id);
		if (addressSetting == null) {
			logger.error("AddressSetting with id {} not found.", id);
			return new ResponseEntity("addressSetting with id " + id 
					+ " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<AddressSetting>(addressSetting, HttpStatus.OK);
	}
	
	// -------------------Retrieve All Cities------------------------------------------
	@RequestMapping(value = "/cities", method = RequestMethod.GET)
	public ResponseEntity<?> getAllCities(@RequestParam(required=false) String lang) {
		List<AddressSetting> cities = this.addressSettingService.findAllCity(lang);
		if (cities == null) {
			return new ResponseEntity("No City found ", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<AddressSetting>>(cities, HttpStatus.OK);
	}
	
	// -------------------Retrieve All Districts------------------------------------------
	@RequestMapping(value = "/districts", method = RequestMethod.GET)
	public ResponseEntity<?> getAllDistricts(@RequestParam(value = "cityCode", required = true) String cityCode, @RequestParam(required=false) String lang) {
		List<AddressSetting> districts = this.addressSettingService.findAllDistrict(cityCode, lang);
		if (districts == null) {
			return new ResponseEntity("No District found ", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<AddressSetting>>(districts, HttpStatus.OK);
	}
	
	// -------------------Retrieve All Wards------------------------------------------
	@RequestMapping(value = "/wards", method = RequestMethod.GET)
	public ResponseEntity<?> getAllWards(@RequestParam(value = "cityCode", required = true) String cityCode, 
			@RequestParam(value = "districtCode", required = true) String districtCode, @RequestParam(required=false) String lang) {
		List<AddressSetting> wards = this.addressSettingService.findAllWard(cityCode, districtCode, lang);
		if (wards == null) {
			return new ResponseEntity("No Wards found ", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<AddressSetting>>(wards, HttpStatus.OK);
	}

}