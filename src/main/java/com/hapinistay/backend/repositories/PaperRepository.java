package com.hapinistay.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Paper;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
	
	Page<Object> findPapersByAvailable(@Param("available") boolean available, 
            Pageable pageRequest);

}
