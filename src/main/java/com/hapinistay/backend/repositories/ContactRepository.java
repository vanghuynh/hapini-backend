package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Contact;

/**
 * @author huynhvang
 *
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {


}
