package com.hapinistay.backend.controller;

import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.hapinistay.backend.model.Apartment;
import com.hapinistay.backend.service.AddressSettingService;
import com.hapinistay.backend.service.ApartmentService;
import com.hapinistay.backend.service.CloudinaryService;
import com.hapinistay.backend.service.ImageService;
import com.hapinistay.backend.service.UserService;

import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)//Spring Runner is used to launch up a spring context in unit tests.
@WebMvcTest(controllers = { ApartmentController.class }, secure = false)// It will auto-configure the Spring MVC infrastructure for our unit tests.
//In most of the cases, @WebMvcTest will be limited to bootstrap a single controller.
//It is used along with @MockBean to provide mock implementations for required dependencies.
//@WebMvcTest also auto-configures MockMvc which offers a powerful way of easy testing MVC controllers without starting a full HTTP server.
public class ApartmentControllerWebMvcTest {

	@Autowired
    private MockMvc mockMvc;
 
    @MockBean
    private ApartmentService apartmentServiceMock;
    
    @MockBean
	private AddressSettingService addressSettingServiceMock;
    
    @MockBean
	UserService userService;
	
    @MockBean
	private ImageService imageService;
	
    @MockBean
	CloudinaryService cloudinaryService;
 
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
    
    // write test cases here
    @Test
    public void givenApartments_whenSearchApartmentByDistrictWithOrderByPriceOneItem_thenReturnJsonArray()
      throws Exception {
         String lang = "";
    	when(addressSettingServiceMock.findAllNameFromCode(lang)).thenReturn(new HashMap<String, String>(){
			{
				put("D1", "District 1");
				
			}
		});
    	
        Apartment apartment1 = new Apartment();
        apartment1.setName("apartment name 1");
        apartment1.setDescription("apartment description 1");
        
        List<Apartment> apartments = new ArrayList<Apartment>();
        apartments.add(apartment1);
        int index = 0;
        int size = 10;
        Pageable page = new PageRequest(index, size);
        
        Page<Apartment> apartmentPage = new PageImpl<Apartment>(apartments, page, 1);
     
        Mockito.when(apartmentServiceMock.findByDistrictCodeWithAvailableSortByPrice("", true, index, size, "")).thenReturn(apartmentPage);
     
        mockMvc.perform(MockMvcRequestBuilders.get("/apartments/advanced-search-order/0/10")
          .contentType(contentType)
          .param("priceOrderBy", "true")
          .param("districtCode", "")
          .param("available", "true")
          .param("lang", "")
          )
          .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
          .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.containsString("OK")))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data.content", Matchers.hasSize(1)))
          .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[0].name", Matchers.is(apartment1.getName())));
    }
}
