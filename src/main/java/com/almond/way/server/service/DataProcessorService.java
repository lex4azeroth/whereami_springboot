package com.almond.way.server.service;

import com.almond.way.server.model.DeviceInfo;

public interface DataProcessorService {
	
	boolean processData(String dataInString);
	
	void processDeviceLocation(DeviceInfo deviceInfo);
}
