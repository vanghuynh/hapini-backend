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
import com.hapinistay.backend.model.Contact;
import com.hapinistay.backend.service.AddressService;
import com.hapinistay.backend.service.ContactService;

@RestController
@RequestMapping("/contact")
public class ContactController {

	public static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private ContactService contactService;

	// -------------------Retrieve All Contacts---------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Contact>> listAllContact() {
		List<Contact> contacts = this.contactService.findAllContacts();
		if (contacts.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Contact>>(contacts, HttpStatus.OK);
	}
	
	
	// -------------------Retrieve Single Address------------------------------------------
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getContact(@PathVariable("id") Long id) {
		logger.info("Fetching Contact with id {}", id);
		Contact contact = this.contactService.findById(id);
		if (contact == null) {
			logger.error("Contact with id {} not found.", id);
			return new ResponseEntity("Contact with id " + id 
					+ " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Contact>(contact, HttpStatus.OK);
	}
	
	// -------------------Delete Single Address------------------------------------------
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
		logger.info("Deleting Contact with id {}", id);
		Contact contact = this.contactService.findById(id);
		if (contact == null) {
			logger.error("Contact with id {} not found.", id);
			return new ResponseEntity("Contact with id " + id 
					+ " not found", HttpStatus.NOT_FOUND);
		}
		this.contactService.deleteContactById(id);
		
		return new ResponseEntity<String>("Contact deleted", HttpStatus.OK);
	}

	// -------------------Create a Address-------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createContact(@RequestBody Contact contact) {
		logger.info("Creating Contact : {}",  contact);

		if (this.contactService.isContactExist(contact)) {
			logger.error("Unable to create. A Contact with name {} already exist", contact);
			return new ResponseEntity("Unable to create contact",HttpStatus.CONFLICT);
		}
		this.contactService.saveContact(contact);
		return new ResponseEntity<Contact>(contact, HttpStatus.CREATED);
	}

}