package com.hapinistay.backend.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.hapinistay.backend.model.Paper;

public interface PaperService {
	
	Paper findById(Integer id);

	void savePaper(Paper paper);

	void updatePaper(Paper paper);

	void deletePaperById(Integer id);

	void deleteAllPapers();

	List<Paper> findAllPapers();

	boolean isPaperExist(Paper paper);
	
	Page<Paper> findPapersByAvailableSortByDate(boolean available,
			Integer index, Integer size);
}