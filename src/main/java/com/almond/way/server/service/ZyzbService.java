package com.almond.way.server.service;

import java.util.List;

import com.almond.way.server.model.CBM;
import com.almond.way.server.model.ZYZB;

public interface ZyzbService {
	
	static final String NOTHING_FOUND = "No device info found";
	static final String INVALID_INPUT = "Neither from nor to can be empty.";
	
	List<ZYZB> getZyzbList(String from, String to, String cbm);
	
	List<CBM> getCbmList();
}
