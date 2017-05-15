package com.almond.way.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.model.DeviceLoL;

//@Mapper
public interface DeviceInfoDao {
//	@Select("SELECT * FROM WAY_LAL WHERE EQU_ID=#{androidID} AND TIMESTAMP=#{dateTime}")
	DeviceInfo getDeviceInfo(DeviceInfo deviceInfo);
	
	List<DeviceLoL> getDeviceLaL(@Param("deviceId") String deviceId, @Param("from") String from, @Param("to") String to);
	
	Map<String, Object> getDeviceLaLById(@Param("deviceId") String deviceId);
	
	void addDeviceInfo(DeviceInfo deviceInfo);
}
