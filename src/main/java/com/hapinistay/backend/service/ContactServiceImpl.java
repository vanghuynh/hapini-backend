package com.hapinistay.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.Contact;
import com.hapinistay.backend.repositories.ContactRepository;



@Service("contactService")
// @Transactional
public class ContactServiceImpl implements ContactService{

	@Autowired
	private ContactRepository contactRepository;

	@Override
	public Contact findById(Long id) {
		return this.contactRepository.findOne(id);
	}

	@Override
	public void saveContact(Contact contact) {
		this.contactRepository.save(contact);
		
	}

	@Override
	public void updateContact(Contact contact) {
		this.contactRepository.save(contact);
		
	}

	@Override
	public void deleteContactById(Long id) {
		this.contactRepository.delete(id);
		
	}

	@Override
	public void deleteAllContacts() {
		this.contactRepository.deleteAll();
		
	}

	@Override
	public List<Contact> findAllContacts() {
		return this.contactRepository.findAll();
	}

	@Override
	public boolean isContactExist(Contact Contact) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
