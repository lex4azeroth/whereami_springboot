package com.almond.way.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.almond.way.server.model.Equipment;

//@Mapper
public interface EquipmentDao {

	int registDevice(Equipment equipment);
	
	List<Equipment> getDeviceList();
}
