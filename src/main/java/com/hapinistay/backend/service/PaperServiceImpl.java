package com.hapinistay.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Paper;
import com.hapinistay.backend.repositories.PaperRepository;



@Service("paperService")
@Transactional
public class PaperServiceImpl implements PaperService{

	@Autowired
	private PaperRepository paperRepository;

	@Override
	public Paper findById(Integer id) {
		return this.paperRepository.findOne(id.longValue());
	}

	@Override
	public void savePaper(Paper paper) {
		this.paperRepository.save(paper);
	}

	@Override
	public void updatePaper(Paper paper) {
		this.paperRepository.save(paper);
		
	}

	@Override
	public void deletePaperById(Integer id) {
		this.paperRepository.delete(id.longValue());
		
	}

	@Override
	public void deleteAllPapers() {
		this.paperRepository.deleteAll();
		
	}

	@Override
	public List<Paper> findAllPapers() {
		return this.paperRepository.findAll();
	}

	@Override
	public boolean isPaperExist(Paper paper) {
		return false;
	}

	@Override
	public Page<Paper> findPapersByAvailableSortByDate(boolean available, Integer index, Integer size) {
		Pageable request = this.createPageRequest(index, size);
		Page<Object> result = this.paperRepository.findPapersByAvailable(available, request);
		List<Paper> papers = new ArrayList<Paper>();
		if(result != null && result.getContent() != null) {
			result.map(item -> {
				if(item != null) {
					Object[] row = (Object[])item; 
					papers.add((Paper)row[0]);
				}
				return papers;
			});
		}
		Page<Paper> paperPage = new PageImpl<Paper>(papers, request, result.getTotalElements());
		return paperPage;
	}


	private Pageable createPageRequest(Integer index, Integer size) {
	    return new PageRequest(index, size);
	}
	

}
