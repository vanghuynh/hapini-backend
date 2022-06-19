package com.hapinistay.backend.controller;

import static org.junit.Assert.assertNotNull;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.hapinistay.backend.HapiniStayApp;
import com.hapinistay.backend.model.House;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;

import org.hamcrest.Matchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HapiniStayApp.class)
@WebAppConfiguration
@TestPropertySource(
		  locations = "classpath:application-test.properties")
public class HouseRestControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));
	
	private MockMvc mockMvc;
	
	private String userName = "bdussault";
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters){
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(e -> e instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);
		assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);		
	}
	
	@Before
	public void settup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		House house = new House();
		house.setName("house");
		house.setDescription("house description");
		this.mockMvc.perform(MockMvcRequestBuilders.post("/houses")
                .content("{ \"name\": \"house\"}")
				.contentType(contentType))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andDo(print());
	}
	
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
	
	@Test
	public void getAllRoomsPaging() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/houses/pageable/0/2")
					.contentType(contentType))
					.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
					.andDo(print())
			        .andExpect(MockMvcResultMatchers.content().string(containsString("house")));
	}
	
	
	
	@Test
	public void getAllRooms() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/houses")
				.contentType(contentType))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andDo(print())
		        .andExpect(MockMvcResultMatchers.content().string(containsString("house")));
	}
	
	@Test
	public void searchRoomWithPaginglRoomsPage0() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/houses/search/0/5")
				.contentType(contentType)
				.param("searchTerm", "house")
				)
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
		        .andExpect(MockMvcResultMatchers.content().string(containsString("house")))
		        //.andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
		        ;
	}
	
	@Test
	public void searchRoomWithPaginglRoomsWithNoResult() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/houses/advanced-search-order/0/5")
				.contentType(contentType)
				.param("searchTerm", "aaaaaaaa")
				)
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
}
