package com.almond.way.server.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.almond.way.server.dao.DeviceInfoDao;

@RunWith(SpringRunner.class)
@SpringBootTest
//@WebAppConfiguration
public class DeviceInfoDaoTests {
	
	@Resource
	private DeviceInfoDao deviceInfoDao;
	
	@Test
	public void testGetDeviceLaL() {
		deviceInfoDao.getDeviceInfo(null);
//		deviceInfoDao.getDeviceLaL("", "", "");
	}

}
