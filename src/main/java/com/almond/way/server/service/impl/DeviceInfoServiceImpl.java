package com.almond.way.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.DeviceLoL;
import com.almond.way.server.model.LaL;
import com.almond.way.server.service.DeviceInfoService;
import com.almond.way.server.service.impl.DeviceInfoServiceImpl;
import com.almond.way.server.utils.GPSUtil;
import com.almond.way.server.utils.LaLUtil;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private DeviceInfoDao deviceInfoDao;

	private static String deviceLog = "device id is [%s], from is [%s], to is [%s]";
	
	static final String NOTHING_FOUND = "No device info found";

	@Override
	public List<DeviceLoL> getDeviceLalInfo(String deviceId, String from, String to, int lineNum) {
		logger.info(String.format(deviceLog, deviceId, from, to));
		
		List<DeviceLoL> queriedLoL = deviceInfoDao.getDeviceLaL(deviceId, from, to);
		
		if (queriedLoL.isEmpty()) {
			queriedLoL = deviceInfoDao.getDeviceLaLWithNoId(from, to);
		}
		
		if (queriedLoL.isEmpty()) {
			logger.error(NOTHING_FOUND);
			return new ArrayList<DeviceLoL>();
		}

		return filterDeviceLal(queriedLoL, lineNum);
	}
	
	@Override
	public List<DeviceLoL> getDeviceOriginalLalInfo(String deviceId, String from, String to, int lineNum) {
		logger.info(String.format(deviceLog, deviceId, from, to));
		
		List<DeviceLoL> queriedLoL = deviceInfoDao.getDeviceLaL(deviceId, from, to);
		
		if (queriedLoL.isEmpty()) {
			queriedLoL = deviceInfoDao.getDeviceLaLWithNoId(from, to);
		}
		
		if (queriedLoL.isEmpty()) {
			logger.error(NOTHING_FOUND);
			return new ArrayList<DeviceLoL>();
		}
		
		List<DeviceLoL> convertedLoL = new ArrayList<DeviceLoL>();
		for (DeviceLoL lol : queriedLoL) {
			LaL lal = GPSUtil.WGS2BD(Double.valueOf(lol.getLatitude()), Double.valueOf(lol.getLongitude()));
			DeviceLoL dLoL = new DeviceLoL();
			dLoL.setId(lol.getId());
			dLoL.setLatitude(String.valueOf(lal.getLatitude()));
			dLoL.setLongitude(String.valueOf(lal.getLongitude()));
			convertedLoL.add(dLoL);
		}

		return convertedLoL;
	}
	
	List<DeviceLoL> filterDeviceLal(List<DeviceLoL> lolListToFilter, int lineNum) {
		logger.info("queried size [" + lolListToFilter.size() + "]");
		List<LaL> lalPoints = LaLUtil.getLalPoints(lineNum);
		List<DeviceLoL> resultMap = new ArrayList<DeviceLoL>();
		for (DeviceLoL dlol : lolListToFilter) {
			LaL target = GPSUtil.WGS2BD(Double.valueOf(dlol.getLatitude()), Double.valueOf(dlol.getLongitude()));
			for (int index = 0; index < lalPoints.size(); index++) {
				double distance = LaLUtil.getDistance(target, lalPoints.get(index));
				if (distance <= 10d) {
					logger.info("Distance: [" + distance + "]");
					DeviceLoL mockLoL = LaLUtil.makeMockLoL(
							dlol.getId(), 
							lalPoints.get(index).getLatitude(), 
							lalPoints.get(index).getLongitude());
					if (!resultMap.contains(mockLoL)) {
						resultMap.add(mockLoL);
					}
					break;
				}
			}
		}
		
		return resultMap;
	}
}
