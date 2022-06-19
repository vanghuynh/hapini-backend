package com.hapinistay.backend.service;


import java.util.List;

import com.hapinistay.backend.model.Balance;

public interface BalanceService {
	
	Balance findById(Long id);

	void saveBalance(Balance balance);

	void updateBalance(Balance balance);

	void deleteBalanceById(Long id);

	void deleteAllBalances();

	List<Balance> findAllBalances();
	
}