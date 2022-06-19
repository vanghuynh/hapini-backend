package com.hapinistay.backend.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public class CommonUtil {
	
	public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException 
	{
	    File convFile = new File( multipart.getOriginalFilename());
	    multipart.transferTo(convFile);
	    return convFile;
	}
	
	public static Pageable createPageRequest(Integer index, Integer size) {
	    return new PageRequest(index, size);
	}
	
	public static String getRandomCode() {
		int length = 10;
	    boolean useLetters = true;
	    boolean useNumbers = false;
	    String code = RandomStringUtils.random(length, useLetters, useNumbers);
	    return code;
	}
	
}
