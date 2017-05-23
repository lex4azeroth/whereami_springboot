package com.almond.way.server.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.DataProcessorService;
import com.almond.way.server.service.EquipmentService;

@Service
public class DataProcessorServiceImpl implements DataProcessorService {

	@Resource
	private DeviceInfoDao deviceInfoDao;
	
	@Autowired
	@Qualifier("equipmentServiceImpl")
	private EquipmentService equipmentService;
	
	@Override
	public boolean processData(String dataInString) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void processDeviceLocation(DeviceInfo deviceInfo) {
//		if (!equipmentService.isEquipmentRegisted(deviceInfo.getAndroidID())) {
//			Equipment equ = new Equipment();
//			equ.setEquipmentId(deviceInfo.getAndroidID());
//			String defaultName = String.format("default_name_%s", deviceInfo.getAndroidID());
//			equ.setEquipmentName(defaultName);
//			equipmentService.registEquipment(equ);
//		}
		
		deviceInfoDao.addDeviceInfo(deviceInfo);
	}

}
