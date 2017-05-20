package com.almond.way.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.model.DeviceLoL;
import com.almond.way.server.model.LaL;
import com.almond.way.server.service.DeviceInfoService;
import com.almond.way.server.service.impl.DeviceInfoServiceImpl;
import com.almond.way.server.utils.LaLUtil;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {

	@Resource
	private DeviceInfoDao deviceInfoDao;
	
	private static Logger logger = Logger.getLogger(DeviceInfoServiceImpl.class.getName());
	
	@Override
	public List<DeviceLoL> getDeviceLalInfo(String deviceId, String from, String to) {
		logger.info("deviceId is [" + deviceId + "]");
		logger.info("from is [" + from + "]");
		logger.info("to is [" + to + "]");
		logger.info("before get");
		List<DeviceLoL> resultMap = deviceInfoDao.getDeviceLaL(deviceId, from, to);
		logger.info("after get");
		List<DeviceLoL> tempMap = new ArrayList<DeviceLoL>();
//		tempMap = resultMap;
		List<LaL> lalPoints = LaLUtil.getLalPoints();
		if (resultMap != null) {
			logger.info("resultMap not null " + resultMap.getClass().getName().toString());
			logger.info("quired size [" + resultMap.size() + "]");
			for (DeviceLoL dlol : resultMap) {
				LaL lalFrom = new LaL(Double.valueOf(dlol.getLongitude()), Double.valueOf(dlol.getLatitude()));
				for (int index = 0; index < lalPoints.size(); index++) {
					double distance = LaLUtil.getDistance(lalFrom, lalPoints.get(index));
					logger.info("Distance: [" + distance + "]");
					if (distance <= 10d) {
						DeviceLoL fakeLoL = new DeviceLoL();
						fakeLoL.setId(dlol.getId());
						double lon = lalPoints.get(index).getLongitude();
						double lat = lalPoints.get(index).getLatitude();
						fakeLoL.setLongitude(String.valueOf(lon));
						fakeLoL.setLatitude(String.valueOf(lat));
						if (!tempMap.contains(fakeLoL)) {
							tempMap.add(fakeLoL);
						}
						break;
					}
				}
			}
			
			logger.info("******filterred******");
			logger.info("sized is [" + tempMap.size() + "]");
			for (DeviceLoL fdlol : tempMap) {
				logger.info("id is [" + fdlol.getId() + "]");
				logger.info("latitude is [" + fdlol.getLatitude() + "]");
				logger.info("longitude is [" + fdlol.getLongitude() + "]");
			}
			
			return tempMap;
		} else {
			logger.error("resultmap is null");
		}
		
		return null;
	}

}
