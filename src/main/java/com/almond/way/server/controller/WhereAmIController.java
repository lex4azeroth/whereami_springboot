package com.almond.way.server.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.model.Equipment;
import com.almond.way.server.model.DeviceLoL;
import com.almond.way.server.controller.WhereAmIController;
import com.almond.way.server.service.DeviceInfoService;
import com.almond.way.server.service.EquipmentService;
import com.almond.way.server.service.PublisherService;

@Controller
@RequestMapping()
public class WhereAmIController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("publisherServiceImpl")
	private PublisherService publisher;
	
	@Autowired
	@Qualifier("deviceInfoServiceImpl")
	private DeviceInfoService deviceInfoService;
	
	@Autowired
	@Qualifier("equipmentServiceImpl")
	private EquipmentService equipmentService;
	
	@RequestMapping(value="mylocation", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE) 
    @ResponseBody
	public String postDeviceLocation(@RequestBody List<DeviceInfo> deviceInfos) {
		logger.info("postDeviceLocation...");
		int index = 0;
		for (; index < deviceInfos.size(); index++) {
			logger.info(String.format("Lat[%s]", deviceInfos.get(index).getLatitude()));
			publisher.doPost(deviceInfos.get(index));
		}
		
		String returnValue = String.format("%d records POSTED", index);
		logger.info("RETURN VALUE: " + returnValue);
		return returnValue;
	}
	
	@RequestMapping(value="equipments", method=RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Equipment> getEquipmentList() {
		logger.info("getting equipment list...");

		return equipmentService.getEquipmentList();
	}
	
	@RequestMapping(value="ihavebeen/{equid}/{from}/{to}/{line}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<DeviceLoL> getDeviceLals(
			@PathVariable("equid") String equid, 
			@PathVariable("from") String from, 
			@PathVariable("to") String to, 
			@PathVariable("line") String line) {
		logger.info("i have been...");
		int lineNum = Integer.parseInt(line);
		return deviceInfoService.getDeviceLalInfo(equid, from, to, lineNum);
	}
	
	@RequestMapping(value="ihavebeenoriginal/{equid}/{from}/{to}/{line}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<DeviceLoL> getDeviceOriginalLals(
			@PathVariable("equid") String equid, 
			@PathVariable("from") String from, 
			@PathVariable("to") String to, 
			@PathVariable("line") String line) {
		logger.info("i have been...");
		int lineNum = Integer.parseInt(line);
		return deviceInfoService.getDeviceOriginalLalInfo(equid, from, to, lineNum);
	}
}
