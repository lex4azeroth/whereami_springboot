package com.almond.way.server.service;

import java.util.List;

import com.almond.way.server.model.DeviceLoL;

public interface DeviceInfoService {
	List<DeviceLoL> getDeviceLalInfo(String deviceId, String from, String to, String lineNum);
	
	List<DeviceLoL> getDeviceOriginalLalInfo(String deviceId, String from, String to, String lineNum);
}
