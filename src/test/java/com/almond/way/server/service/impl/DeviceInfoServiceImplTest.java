package com.almond.way.server.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.model.DeviceLoL;
import com.almond.way.server.service.DeviceInfoService;

@RunWith(MockitoJUnitRunner.class) 
public class DeviceInfoServiceImplTest {

	@Mock
	private DeviceInfoDao deviceInfoDao;
	
	@InjectMocks
	private DeviceInfoService service = new DeviceInfoServiceImpl();
	
	private static final String EMPTY = "";
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		deviceInfoDao = Mockito.mock(DeviceInfoDao.class);
	}
	
	@Test()
	public void testGetDeviceLalInfoFail() {
		Mockito.when(deviceInfoDao.getDeviceLaL(EMPTY, EMPTY, EMPTY)).thenReturn(null);
		List<DeviceLoL> infoList = service.getDeviceLalInfo(EMPTY, EMPTY, EMPTY, 1);
		assertTrue(infoList.isEmpty());
	}
	
	@Test()
	public void testGetDeviceOriginalLalInfoFail() {
		Mockito.when(deviceInfoDao.getDeviceLaL(EMPTY, EMPTY, EMPTY)).thenReturn(null);
		List<DeviceLoL> infoList = service.getDeviceLalInfo(EMPTY, EMPTY, EMPTY, 1);
		assertTrue(infoList.isEmpty());
	}
	
	@Test
	public void testFilterDeviceLal() {
		// TO Refactor
//		List<DeviceLoL> lolList = new ArrayList<>();
//		List<DeviceLoL> filtered = service.filterDeviceLal(lolList, 1);
//		assertNotNull(filtered);
//		assertTrue(filtered.size() == 17);
	}
}
