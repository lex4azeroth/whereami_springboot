package com.almond.way.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.model.DeviceLoL;

public interface DeviceInfoDao {
	DeviceInfo getDeviceInfo(DeviceInfo deviceInfo);
	
	List<DeviceLoL> getDeviceLaL(@Param("deviceId") String deviceId, @Param("from") String from, @Param("to") String to);
	
	List<DeviceLoL> getDeviceLaLWithNoId(@Param("from") String from, @Param("to") String to);
		
	void addDeviceInfo(DeviceInfo deviceInfo);
	
	void deleteDeviceInfoById(int id);
}
