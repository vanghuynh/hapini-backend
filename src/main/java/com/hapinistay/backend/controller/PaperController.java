package com.hapinistay.backend.controller;

import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.util.UriComponentsBuilder;

import com.hapinistay.backend.dto.CustomerDto;
import com.hapinistay.backend.dto.ResponseDto;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.Paper;
import com.hapinistay.backend.model.User;
import com.hapinistay.backend.service.PaperService;
import com.hapinistay.backend.service.UserService;
import com.hapinistay.backend.util.CustomErrorType;

@RestController
@RequestMapping("/papers")
public class PaperController {

	public static final Logger logger = LoggerFactory.getLogger(PaperController.class);

	@Autowired
	PaperService paperService; //Service which will do all data retrieval/manipulation work
	
	@Autowired
    private ServletContext servletContext;

	// -------------------Retrieve All papers---------------------------------------------
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> listAllPapers() {
		List<Paper> papers = paperService.findAllPapers();
		if (papers.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", papers), HttpStatus.OK);
	}
	
	
	// -------------------Retrieve Single paper------------------------------------------

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> getPaper(@PathVariable("id") Integer id) {
		logger.info("Fetching Paper with id {}", id);
		Paper paper = this.paperService.findById(id);
		if (paper == null) {
			logger.error("Paper with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("User with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", paper), HttpStatus.OK);
	}

	// -------------------Create a paper-------------------------------------------
	@PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
	@RequestMapping(value = "", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<ResponseDto> createPaper(@RequestBody Paper paper) {
		logger.info("Creating paper : {}", paper);

		if (this.paperService.isPaperExist(paper)) {
			logger.error("Unable to create. A Paper with name {} already exist", paper.getTitle());
			return new ResponseEntity(new CustomErrorType("Unable to paper. A paper with name " + 
					paper.getTitle() + " already exist."),HttpStatus.CONFLICT);
		}
		this.paperService.savePaper(paper);

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<ResponseDto>(new ResponseDto("OK", paper), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/search/{index}/{size}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDto> searchPapersByAvailableWithPageable(@PathVariable("index") Integer index,
			@PathVariable("size") Integer size, @RequestParam(value="available", required = false) boolean available) {
		Page<Paper> papers = this.paperService.findPapersByAvailableSortByDate(available, index, size);
		if (papers == null || papers.getNumberOfElements() == 0) {
			return new ResponseEntity<>(new ResponseDto("OK", null), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(new ResponseDto("OK", papers), HttpStatus.OK);
	}
	
}