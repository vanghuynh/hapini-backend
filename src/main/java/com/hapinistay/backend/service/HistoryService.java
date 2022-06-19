package com.hapinistay.backend.service;


import java.util.List;

import com.hapinistay.backend.model.History;;

public interface HistoryService {
	
	History findById(Long id);

	void saveHistory(History history);

	void updateHistory(History history);

	void deleteHistoryById(Long id);

	void deleteAllHistories();

	List<History> findAllHistories();
	
}