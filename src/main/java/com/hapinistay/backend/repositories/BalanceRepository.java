package com.hapinistay.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hapinistay.backend.model.Balance;

/**
 * @author huynhvang
 *
 */
@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {


}
