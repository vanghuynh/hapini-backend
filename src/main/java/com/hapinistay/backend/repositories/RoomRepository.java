package com.hapinistay.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	Page<Room> findByStatus(String status, Pageable pageReguest);

}
