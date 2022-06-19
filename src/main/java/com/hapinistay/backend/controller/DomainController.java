package com.hapinistay.backend.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.hapinistay.backend.model.Address;
import com.hapinistay.backend.service.AddressService;

@RestController
@RequestMapping("/domain")
public class DomainController {

	public static final Logger logger = LoggerFactory.getLogger(DomainController.class);


	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<Domain> listAllDomain() {
		Domain domain = new Domain();
		String[] whitelist = {
				"*.bosch.com",
		        "bshg.com",
		        "boschrexroth.de",
		        "boschrexroth.co.uk",
		        "boschrexroth.pl",
		        "quickblox.com",
		        "bosch-connectivity.com",
		        "cz.boschrexroth.com",
		        "dk.boschrexroth.com",
		        "ee.boschrexroth.com",
		        "it.boschrexroth.com",
		        "lt.boschrexroth.com",
		        "lv.boschrexroth.com",
		        "se.boschrexroth.com",
		        "sk.boschrexroth.com",
		        "joincoup.com",
		        "bosch-si.com",
		        "vang.huynh2@vn.bosch.com",
		        "thai.nguyenvan2@vn.bosch.com",
		        "thai.nguyenvan3@vn.bosch.com",
		        "thai.nguyenvan4@vn.bosch.com",
		         "thai.nguyenvan5@vn.bosch.com",
		         "dang.nguyenhai3@vn.bosch.com",
		        "dang.nguyenhai4@vn.bosch.com"};
		String[] blacklist = {"vn.bosch.com", "vang.huynh@de.bosch.com",  "thai.nguyenvan5@vn.bosch.com"};
		domain.setWhitelist(Arrays.asList(whitelist));
		domain.setBlacklist(Arrays.asList(blacklist));
		
		return new ResponseEntity<Domain>(domain, HttpStatus.OK);
	}
	

}

class Domain{
	private List<String> whitelist;
	private List<String> blacklist;
	public List<String> getWhitelist() {
		return whitelist;
	}
	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}
	public List<String> getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}
	
}