package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {


}
