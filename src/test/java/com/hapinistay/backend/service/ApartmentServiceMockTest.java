package com.hapinistay.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.repositories.ApartmentRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;



/**
 * @author huynhvang
 *
 *Unit Testing with Mockito using MockitoRunner
 *Spring Boot Test Starter brings in a wide range of dependencies for Unit Testing
	Basic Test Framework - JUnit
	Mocking - Mockito
	Assertion - AssertJ, Hamcrest
	Spring Unit Test Framework - Spring Test
* */
@RunWith(MockitoJUnitRunner.class) //The JUnit Runner which causes all the initialization magic with @Mock and @InjectMocks to happen before the tests are run.
public class ApartmentServiceMockTest {

	@Mock
	private ApartmentRepository apartmentRepositoryMock; //Create a mock for ApartmentRepository
	
	@Mock
	private AddressSettingService addressSettingServiceMock;//Create a mock for AddressSettingService
	
	@InjectMocks
	ApartmentServiceImpl apartmentServiceImpl; //Inject the mocks as dependencies into ApartmentServiceImpl
	
	@Test
	public void testfindApartmentByAvailableAndStatus() {
		String districtCode = "D1";
		String status = "APPROVE";
		boolean available = true;
		int index = 0;
		int size = 10;
		Pageable page = new PageRequest(index, size);
		String lang = "";
		when(addressSettingServiceMock.findAllNameFromCode(lang)).thenReturn(new HashMap<String, String>(){
			{
				put("D1", "District 1");
				
			}
		});
		List<Object> apartments = new ArrayList<Object>();
		Apartment apartment1 = new Apartment();
		apartment1.setName("Name 1");
		apartment1.setDescription("description 1");
		apartments.add(new Object[] {apartment1, 1});
		//Object content = apartments;
		when(apartmentRepositoryMock.findApartmentByAvailableAndStatus(districtCode, status, available, page)).thenReturn(new PageImpl<Object>(apartments, page, 1));
		assertNotNull("Not null value", apartmentServiceImpl.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, ""));
		assertNotNull("Not null value", apartmentServiceImpl.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, "").getContent());
		assertEquals("Only one result returned", 1, apartmentServiceImpl.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, "").getContent().size());
	}
}
