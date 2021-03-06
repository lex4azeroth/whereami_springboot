package com.almond.way.server.dao;

import java.util.List;

import com.almond.way.server.model.Equipment;

public interface EquipmentDao {

	int registDevice(Equipment equipment);
	
	List<Equipment> getDeviceList();
	
	Equipment getDevice(String equimpentId);
	
	int deleteDevice(int id);
	
	int updateDeviceName(Equipment equipment);
}
