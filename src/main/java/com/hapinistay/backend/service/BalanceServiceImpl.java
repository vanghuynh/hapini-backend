package com.hapinistay.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hapinistay.backend.model.Balance;
import com.hapinistay.backend.repositories.BalanceRepository;



@Service("balanceService")
public class BalanceServiceImpl implements BalanceService{

	@Autowired
	private BalanceRepository balanceRepository;

	@Override
	public Balance findById(Long id) {
		return this.balanceRepository.findOne(id);
	}

	@Override
	public void saveBalance(Balance balance) {
		this.balanceRepository.save(balance);
	}

	@Override
	public void updateBalance(Balance balance) {
		this.balanceRepository.save(balance);
	}

	@Override
	public void deleteBalanceById(Long id) {
		this.balanceRepository.delete(id);
	}

	@Override
	public void deleteAllBalances() {
		this.balanceRepository.deleteAll();
	}

	@Override
	public List<Balance> findAllBalances() {
		return this.balanceRepository.findAll();
	}



}
