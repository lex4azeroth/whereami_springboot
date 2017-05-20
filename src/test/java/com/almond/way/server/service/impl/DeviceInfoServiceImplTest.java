package com.almond.way.server.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.DeviceLoL;

@RunWith(MockitoJUnitRunner.class) 
public class DeviceInfoServiceImplTest {

	@Mock
	private DeviceInfoDao deviceInfoDao;
	
	private DeviceInfoServiceImpl service;
	
	private static final String EMPTY = "";
	
	@Before
	public void setup() {
		deviceInfoDao = Mockito.mock(DeviceInfoDao.class);
		service = new DeviceInfoServiceImpl(deviceInfoDao);
	}
	
	@Test(expected = WhereAmIException.class)
	public void testGetDeviceLalInfoFail() {
		try {
			Mockito.when(deviceInfoDao.getDeviceLaL(EMPTY, EMPTY, EMPTY)).thenReturn(null);
			service.getDeviceLalInfo(EMPTY, EMPTY, EMPTY, 1);
		} catch (WhereAmIException e) {
			assertEquals(DeviceInfoServiceImpl.NO_THING_FOUND, e.getMessage());
			throw e;
		}
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
