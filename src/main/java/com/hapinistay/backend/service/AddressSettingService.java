package com.hapinistay.backend.service;


import java.util.List;
import java.util.Map;

import com.hapinistay.backend.model.AddressSetting;

public interface AddressSettingService {
	
	AddressSetting findById(Long id);

	void saveAddressSetting(AddressSetting AddressSetting);

	void updateAddressSetting(AddressSetting AddressSetting);

	void deleteAddressSettingById(Long id);

	void deleteAllAddressSettings();

	List<AddressSetting> findAllAddressSettings();

	boolean isAddressSettingExist(AddressSetting AddressSetting);
	
	List<AddressSetting> findAllCity();
	
	List<AddressSetting> findAllCity(String lang);
	
	List<AddressSetting> findAllDistrict(String cityCode);
	
	List<AddressSetting> findAllDistrict(String cityCode, String lang);
	
	List<AddressSetting> findAllWard(String cityCode, String districtCode);
	
	List<AddressSetting> findAllWard(String cityCode, String districtCode, String lang);
	
	Map<String, String> findNameFromCode(List<String> codes);
	
	Map<String, String> findAllNameFromCode(String lang);
	
	void saveAddressSettings(List<AddressSetting> AddressSettings);
}