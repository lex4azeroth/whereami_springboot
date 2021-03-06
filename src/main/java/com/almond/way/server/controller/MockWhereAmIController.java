package com.almond.way.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.controller.MockWhereAmIController;

@Controller
@RequestMapping("/way")
public class MockWhereAmIController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/mockhome")
	public String mocHomeSweetHome() {
		return "index";
	}
	
	@RequestMapping(value="/mockpost", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String mockPost(@RequestBody List<DeviceInfo> deviceInfos) {
		int index = 0;
		for (; index < deviceInfos.size(); index++) {
			logger.info(String.format("Lat[%s]", deviceInfos.get(index).getLatitude()));
			logger.info(String.format("[MOCK POSTED] %s", deviceInfos.get(index).toString()));
		}
		
		return String.format("%d records POSTED", index);
	}
	
	@RequestMapping(value="/mockihavebeen/{equid}/{from}/{to}/{line}", method=RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String mockIhavebeen(
			@PathVariable("equid") String id, 
			@PathVariable("from") String from, 
			@PathVariable("to") String to, 
			@PathVariable("line") int line) {
		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append(id).append(from).append(to).append(line);
		return requestBuilder.toString();
	}
	
	@RequestMapping(value="/mocktest/{id}", method=RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String mockTest(@PathVariable("id") String id) {
		return id;
	}
}