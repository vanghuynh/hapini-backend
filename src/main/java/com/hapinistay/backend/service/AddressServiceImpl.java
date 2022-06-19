package com.hapinistay.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.Address;
import com.hapinistay.backend.repositories.AddressRepository;



@Service("addressService")
@Transactional
public class AddressServiceImpl implements AddressService{

	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Address findById(Long id) {
		return this.addressRepository.findOne(id.longValue());
	}

	@Override
	public void saveAddress(Address address) {
		this.addressRepository.save(address);
		
	}

	@Override
	public void updateAddress(Address address) {
		this.addressRepository.save(address);
		
	}

	@Override
	public void deleteAddressById(Long id) {
		this.addressRepository.delete(id.longValue());
		
	}

	@Override
	public void deleteAllAddresss() {
		this.addressRepository.deleteAll();
		
	}

	@Override
	public List<Address> findAllAddresss() {
		return this.addressRepository.findAll();
	}

	@Override
	public boolean isAddressExist(Address address) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
