package com.almond.way.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.almond.way.server.model.DeviceInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.minidev.json.JSONArray;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MockWhereAmIControllerTests {

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test  
    public void testMocHomeSweetHome() throws Exception {  
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get( "/way/mockhome" );  
      
        ResultActions resultActions = mockMvc.perform( mockHttpServletRequestBuilder );  
        resultActions.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().equals("index");
    }
	
	@Test
	public void testMockPost() throws Exception {
		List<DeviceInfo> deviceInfos = new ArrayList<>();
		DeviceInfo di = new DeviceInfo();
		di.setId(1234);
		di.setAndroidID("aid123");
		di.setDate("now1");
		di.setLatitude("lat1");
		di.setLongitude("lon1");
		deviceInfos.add(di);
		
		di.setId(567);
		di.setAndroidID("aid567");
		di.setDate("now2");
		di.setLatitude("lat2");
		di.setLongitude("lon2");
		deviceInfos.add(di);
		
        JSONArray json = new JSONArray();  
        json.addAll(deviceInfos);
        
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(deviceInfos);
        String responseString = mockMvc.perform( post("/way/mockpost").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        
        assertEquals(responseString, "2 records POSTED");
	}
}
