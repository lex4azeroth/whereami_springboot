package com.almond.way.server.service;

import com.almond.way.server.model.DeviceInfo;

public interface PublisherService {
	void doPost(DeviceInfo deviceInfo);
}
