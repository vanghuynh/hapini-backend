package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.History;;

/**
 * @author huynhvang
 *
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {


}
