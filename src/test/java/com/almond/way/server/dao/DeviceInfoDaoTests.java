package com.almond.way.server.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.model.DeviceLoL;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceInfoDaoTests {
	
	@Resource
	private DeviceInfoDao deviceInfoDao;
	
	private static final String EQUIPMENT_ID = "815683fda35de8d5";
	private static final String FROM = "2017-04-08 04:51";
	private static final String TO = "2017-04-08 04:58";
	private static final String TIMESTAMP = "2017-04-08 03:09:33";
	@Test
	public void testGetDeviceLaL() {
		List<DeviceLoL> deviceInfos = 
				deviceInfoDao.getDeviceLaL(EQUIPMENT_ID, FROM, TO);
		
		assertNotNull(deviceInfos);
		assertTrue(deviceInfos.size() > 0);
	}
	
	@Test
	public void testGetDeviceInfo() {
		DeviceInfo info = new DeviceInfo();
		info.setAndroidID(EQUIPMENT_ID);
		info.setDate(TIMESTAMP);
		DeviceInfo retrievedInfo = deviceInfoDao.getDeviceInfo(info);
		assertNotNull(retrievedInfo);
		assertEquals(EQUIPMENT_ID, retrievedInfo.getAndroidID());
		assertEquals(TIMESTAMP, retrievedInfo.getDate());
	}
	
	@Test
	public void testGetDeviceInfoFail() {
		DeviceInfo info = new DeviceInfo();
		info.setAndroidID(EQUIPMENT_ID);
		info.setDate("");
		DeviceInfo retrievedInfo = deviceInfoDao.getDeviceInfo(info);
		assertNull(retrievedInfo);
	}
	
	@Test
	public void testAddDeviceInfoAndDeleteItById() {
		DeviceInfo info = new DeviceInfo();
		info.setAndroidID("TEST_CANYOUSEEME");
		info.setDate(TIMESTAMP);
		info.setLatitude("TEST_LAT");
		info.setLongitude("TEST_LON");
		deviceInfoDao.addDeviceInfo(info);
		
		DeviceInfo retrievedInfo = deviceInfoDao.getDeviceInfo(info);
		assertNotNull(retrievedInfo);
		assertEquals("TEST_CANYOUSEEME", retrievedInfo.getAndroidID());
		assertEquals(TIMESTAMP, retrievedInfo.getDate());
		assertEquals("TEST_LAT", retrievedInfo.getLatitude());
		assertEquals("TEST_LON", retrievedInfo.getLongitude());
		
		deviceInfoDao.deleteDeviceInfoById(retrievedInfo.getId());
	}

}
