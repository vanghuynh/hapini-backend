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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.hapinistay.backend.HapiniStayApp;
import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.repositories.ApartmentRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;



/**
 * @author huynhvang
 *
 *Unit test launching up the complete Spring context.
 *Spring Boot Test Starter brings in a wide range of dependencies for Unit Testing
	Basic Test Framework - JUnit
	Mocking - Mockito
	Assertion - AssertJ, Hamcrest
	Spring Unit Test Framework - Spring Test
	
* */
@RunWith(SpringRunner.class) //Spring Runner is used to launch up a spring context in unit tests.
@SpringBootTest(classes = HapiniStayApp.class)//This annotation indicates that the context under test is a @SpringBootApplication. 
											  //The complete HapiniStayApp is launched up during the unit test.
											  //The @SpringBootTest annotation can be used when we need to bootstrap the entire container. 
@TestPropertySource(
		  locations = "classpath:application-test.properties")
public class ApartmentServiceMockBeanSpringContextTest {

	@MockBean
	private ApartmentRepository apartmentRepositoryMock; //@MockBean annotation creates a mock for ApartmentRepository. This mock is used in the Spring Context instead of the real ApartmentRepository.
	
	@MockBean
	private AddressSettingService addressSettingServiceMock;
	
	@Autowired
	ApartmentService apartmentService; //Pick the ApartmentServicee from the Spring Context and autowire it in.
	
	@Test
	public void testFindApartmentByAvailableAndStatusWithSpringContext() {
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
		assertNotNull("Not null value", apartmentService.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, ""));
		assertNotNull("Not null value", apartmentService.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, "").getContent());
		assertEquals("Only one result returned", 1, apartmentService.findApartmentByAvailableAndStatus(districtCode, status, available, index, size, "").getContent().size());
	}
}

//Launching the entire spring context makes the unit test slower. 
//Unit tests will also start failing if there are errors in other beans in the contexts. 
//So, the MockitoJUnitRunner approach is preferred.


//JUnit is the most popular Java Unit testing framework
//Unit Testing focuses on writing automated tests for individual classes and methods.
//The important thing about automation testing is that these tests can be run with continuous integration - as soon as some code changes.


//Mockito is the most popular mocking framework in Java.
//In the example below ApartmentService depends on ApartmentRepository. 
//When we write a unit test for ApartmentService, we will want to use a mock ApartmentRepository - one which does not connect to a database.

