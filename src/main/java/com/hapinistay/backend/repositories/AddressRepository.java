package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Address;

/**
 * @author huynhvang
 *
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


}
