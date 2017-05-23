package com.almond.way.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/way")
public class WhereAmIController {
	private static Logger logger = Logger.getLogger(WhereAmIController.class.getName());
	
	@Autowired
	@Qualifier("publisherServiceImpl")
	private PublisherService publisher;
	
	@Autowired
	@Qualifier("deviceInfoServiceImpl")
	private DeviceInfoService deviceInfoService;
	
	@Autowired
	@Qualifier("equipmentServiceImpl")
	private EquipmentService equipmentService;
	
	@RequestMapping(value="/mylocation", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE) 
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
	
	@RequestMapping(value="/equipments", method=RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_VALUE)
	public List<Equipment> getEquipmentList() {
		logger.info("getting equipment list...");

		return equipmentService.getEquipmentList();
	}
	
//	@RequestMapping(value="/ihavebeen", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public List<DeviceLoL> getDeviceLals(@Param) {
//		logger.info("i have been...");
////		List<DeviceLoL> deviceLoLs = deviceInfoService.getDeviceLalInfo("7bbd793805f2ba1d", "2017-02-12 02:09:51", "2017-02-12 02:56:59");
//		List<DeviceLoL> deviceLoLs = deviceInfoService.getDeviceLalInfo("7bbd793805f2ba1d", "2017-04-08 04:51", "2017-04-08 04:58");
//		Map<String, String> map = new HashMap<String, String>();
//		return deviceLoLs;
//	}
}
