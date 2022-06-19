package com.hapinistay.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hapinistay.backend.model.History;
import com.hapinistay.backend.repositories.HistoryRepository;



@Service("historyService")
public class HistoryServiceImpl implements HistoryService{

	@Autowired
	private HistoryRepository historyRepository;

	@Override
	public History findById(Long id) {
		return this.historyRepository.findOne(id);
	}

	@Override
	public void saveHistory(History history) {
		this.historyRepository.save(history);
	}

	@Override
	public void updateHistory(History history) {
		this.historyRepository.save(history);
		
	}

	@Override
	public void deleteHistoryById(Long id) {
		this.historyRepository.delete(id);
		
	}

	@Override
	public void deleteAllHistories() {
		this.historyRepository.deleteAll();
		
	}

	@Override
	public List<History> findAllHistories() {
		return this.historyRepository.findAll();
	}


}
