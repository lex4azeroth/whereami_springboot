package com.almond.way.server.service;

public interface DataSyncerService {

	Object fetchData();

	void updateRedis();
}
