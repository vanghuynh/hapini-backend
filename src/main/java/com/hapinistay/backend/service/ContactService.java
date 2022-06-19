package com.hapinistay.backend.service;


import java.util.List;

import com.hapinistay.backend.model.Contact;

public interface ContactService {
	
	Contact findById(Long id);

	void saveContact(Contact contact);

	void updateContact(Contact contact);

	void deleteContactById(Long id);

	void deleteAllContacts();

	List<Contact> findAllContacts();

	boolean isContactExist(Contact Contact);
	
}