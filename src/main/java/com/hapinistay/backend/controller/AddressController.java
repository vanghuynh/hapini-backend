package com.hapinistay.backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.hapinistay.backend.model.Address;
import com.hapinistay.backend.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	public static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	@Autowired
	AddressService addressService; //Service which will do all data retrieval/manipulation work

	// -------------------Retrieve All Addresss---------------------------------------------

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Address>> listAllAddresss() {
		List<Address> addresss = addressService.findAllAddresss();
		if (addresss.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Address>>(addresss, HttpStatus.OK);
	}
	
	// -------------------Retrieve All Addresss with paging---------------------------------------------

	@RequestMapping(value = "/page/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<List<Address>> listAllAddresssWithPaging(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size) {
		List<Address> addresss = addressService.findAllAddresss();
		if (addresss.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<Address>>(addresss, HttpStatus.OK);
	}
	
	
	// -------------------Retrieve Single Address------------------------------------------

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAddress(@PathVariable("id") Long id) {
		logger.info("Fetching Address with id {}", id);
		Address address = addressService.findById(id);
		if (address == null) {
			logger.error("Address with id {} not found.", id);
			return new ResponseEntity("Address with id " + id 
					+ " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Address>(address, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Address------------------------------------------
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
		logger.info("Deleting Address with id {}", id);
		Address address = addressService.findById(id);
		if (address == null) {
			logger.error("Address with id {} not found.", id);
			return new ResponseEntity("Rp with id " + id 
					+ " not found", HttpStatus.NOT_FOUND);
		}
		this.addressService.deleteAddressById(id);
		
		return new ResponseEntity<String>("Address deleted", HttpStatus.OK);
	}

	// -------------------Create a Address-------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> createAddress(@RequestBody Address address, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Address : {}", address);

		if (addressService.isAddressExist(address)) {
			logger.error("Unable to create. A Address with name {} already exist", address.getId());
			return new ResponseEntity("Unable to create. A Address with name " + 
			address.getId() + " already exist.",HttpStatus.CONFLICT);
		}
		addressService.saveAddress(address);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/Address/{id}").buildAndExpand(address.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

}