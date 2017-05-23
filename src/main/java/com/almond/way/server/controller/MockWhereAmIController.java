package com.almond.way.server.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.controller.MockWhereAmIController;

@Controller
@RequestMapping("/way")
public class MockWhereAmIController {
private static Logger logger = Logger.getLogger(MockWhereAmIController.class.getName());
	
	@RequestMapping(value="/mockhome")
	public String mocHomeSweetHome() {
		return "index";
	}
	
	@RequestMapping(value="/mockpost", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String mockPost(@RequestBody List<DeviceInfo> deviceInfos) {
		logger.info("[MOCK POST]");
		int index = 0;
		for (; index < deviceInfos.size(); index++) {
			logger.info(String.format("Lat[%s]", deviceInfos.get(index).getLatitude()));
			logger.info(String.format("[MOCK POSTED] %s", deviceInfos.get(index).toString()));
		}
		
		return String.format("%d records POSTED", index);
	}
	
	@RequestMapping(value="/mockihavebeen", method=RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String mockIhavebeen() {
		return null;
	}
}