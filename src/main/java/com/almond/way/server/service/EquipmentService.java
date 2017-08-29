package com.almond.way.server.service;

import java.util.List;

import com.almond.way.server.model.Equipment;

public interface EquipmentService {
	int registEquipment(Equipment equipment);
	
	List<Equipment> getEquipmentList();
	
	boolean isEquipmentRegisted(String id);
	
	String udpateDeviceName(Equipment equipment);
	
	String getDeviceName(String equId);
}
