package com.hapinistay.backend.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.AddressSetting;
import com.hapinistay.backend.repositories.AddressSettingRepository;
import com.hapinistay.backend.util.Constants;



@Service("addressSetting")
@Transactional
public class AddressSettingServiceImpl implements AddressSettingService{

	@Autowired
	private AddressSettingRepository addressSettingRepository;

	@Override
	public AddressSetting findById(Long id) {
		return this.addressSettingRepository.findOne(id);
	}

	@Override
	public void saveAddressSetting(AddressSetting AddressSetting) {
		this.addressSettingRepository.save(AddressSetting);
		
	}

	@Override
	public void updateAddressSetting(AddressSetting AddressSetting) {
		this.addressSettingRepository.save(AddressSetting);
		
	}

	@Override
	public void deleteAddressSettingById(Long id) {
		this.addressSettingRepository.delete(id);
		
	}

	@Override
	public void deleteAllAddressSettings() {
		this.addressSettingRepository.deleteAll();
		
	}

	@Override
	public List<AddressSetting> findAllAddressSettings() {
		return this.addressSettingRepository.findAll();
	}

	@Override
	public boolean isAddressSettingExist(AddressSetting AddressSetting) {
		return this.addressSettingRepository.findAll().stream().anyMatch(e -> e.getCode().equals(AddressSetting.getCode()));
	}

	@Override
	public List<AddressSetting> findAllCity() {
		return this.addressSettingRepository.findAll().stream().filter(e -> Constants.CITY.equals(e.getType())).collect(Collectors.toList());
	}

	@Override
	public List<AddressSetting> findAllDistrict(String cityCode) {
		return this.addressSettingRepository.findAll().stream()
				.filter(e -> Constants.DISTRICT.equals(e.getType()) && cityCode.equals(e.getCityCode()))
				.collect(Collectors.toList());
	}

	@Override
	public List<AddressSetting> findAllWard(String cityCode, String districtCode) {
		return this.addressSettingRepository.findAll().stream()
				.filter(e -> Constants.WARD.equals(e.getType()) && cityCode.equals(e.getCityCode()) && districtCode.equals(e.getDistrictCode()))
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, String> findNameFromCode(List<String> codes) {
		Map<String, String> codeName = new HashMap<String, String>();
		this.addressSettingRepository.findAll().stream()
				.filter(e -> codes.contains(e.getCode()))
				//.collect(Collectors.toMap((c) -> c::getCode, (c) -> c::getname));
				.forEach(e -> codeName.put(e.getCode(), e.getName()));
		return codeName;
	}

	@Override
	public Map<String, String> findAllNameFromCode(String lang) {
		Map<String, String> codeName = new HashMap<String, String>();
		List<AddressSetting> addressSettings = this.addressSettingRepository.findAll();
		if(addressSettings != null) {
			addressSettings.forEach(e -> {
				e.getLanguageDto(lang);
			});
			codeName = addressSettings.stream()
					.collect(Collectors.toMap(AddressSetting::getCode, AddressSetting :: getName, (oldValue, newValue) -> oldValue));
		}
		return codeName;
	}

	@Override
	public void saveAddressSettings(List<AddressSetting> AddressSettings) {
		this.addressSettingRepository.save(AddressSettings);
		
	}

	@Override
	public List<AddressSetting> findAllCity(String lang) {
		List<AddressSetting> cities = this.addressSettingRepository.findAll().stream()
				.filter(e -> Constants.CITY.equals(e.getType())).collect(Collectors.toList());
		if(cities != null) {
			cities.forEach(city -> city.getLanguageDto(lang));
			return cities;
		}
		return null;
	}

	@Override
	public List<AddressSetting> findAllDistrict(String cityCode, String lang) {
		List<AddressSetting> districts = this.addressSettingRepository.findAll().stream()
				.filter(e -> Constants.DISTRICT.equals(e.getType()) && cityCode.equals(e.getCityCode()))
				.collect(Collectors.toList());
		if(districts != null) {
			districts.forEach(district -> district.getLanguageDto(lang));
			return districts;
		}
		return null;
	}

	@Override
	public List<AddressSetting> findAllWard(String cityCode, String districtCode, String lang) {
		List<AddressSetting> wards = this.addressSettingRepository.findAll().stream()
				.filter(e -> Constants.WARD.equals(e.getType()) && cityCode.equals(e.getCityCode()) && districtCode.equals(e.getDistrictCode()))
				.collect(Collectors.toList());
		if(wards != null) {
			wards.forEach(ward -> ward.getLanguageDto(lang));
			return wards;
		}
		return null;
	}
	

}
