package com.hapinistay.backend.service;


import java.util.List;

import com.hapinistay.backend.model.Address;

public interface AddressService {
	
	Address findById(Long id);

	void saveAddress(Address Address);

	void updateAddress(Address Address);

	void deleteAddressById(Long id);

	void deleteAllAddresss();

	List<Address> findAllAddresss();

	boolean isAddressExist(Address Address);
	
}