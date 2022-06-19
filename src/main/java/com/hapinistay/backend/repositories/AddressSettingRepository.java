package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.AddressSetting;

@Repository
public interface AddressSettingRepository extends JpaRepository<AddressSetting, Long> {

	

}
