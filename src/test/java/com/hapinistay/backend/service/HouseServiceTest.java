package com.hapinistay.backend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.hapinistay.backend.HapiniStayApp;
import com.hapinistay.backend.model.House;
import com.hapinistay.backend.service.HouseService;

import org.junit.Assert;
import org.junit.Before;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HapiniStayApp.class)
@TestPropertySource(
		  locations = "classpath:application-test.properties")
public class HouseServiceTest {
	
	@Autowired
	HouseService houseService;
	
	@Before
	public void settup() throws Exception {
		this.houseService.deleteAllHouses();
		House house = new House();
		house.setNameVi("house");
		house.setDescriptionVi("house description");
		this.houseService.saveHouse(house);
	}
	
	@Test 
	public void insertHouse() throws Exception {
	}
	
	@Test
	public void searchHouseByNameAndDescription_matchNameAndDescription() throws Exception {
		Page<House> houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCase("house", "house", new PageRequest(0, 5), "vi");
		assertNotNull(houses);
		assertTrue("Number of room is 1", houses.getContent().size() == 1);
	}
	
	@Test
	public void searchHouseByNameAndDescription_matchName() throws Exception {
		Page<House> houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCase("house", "aaaaa", new PageRequest(0, 5), "vi");
		assertNotNull(houses);
		assertTrue("Number of room is 1", houses.getContent().size() == 1);
	}
	
	@Test
	public void searchHouseByNameAndDescriptionByIndexAndSize_matchName() throws Exception {
		Page<House> houses = this.houseService.findByDescriptionContainsOrNameContainsAllIgnoreCaseWithIndexAndSize("house", "aaaaa",0, 5, "vi");
		assertNotNull(houses);
		assertTrue("Number of room is 1", houses.getContent().size() == 1);
	}
	
	@Test
	public void testFindBySearchTermNamedWithDescriptionOrName() throws Exception {
		Page<House> houses = this.houseService.findBySearchTermNamedWithDescriptionOrName("house",  new PageRequest(0, 5), "vi");
		assertNotNull(houses);
		assertTrue("Number of room is 1", houses.getContent().size() == 1);
	}
}
