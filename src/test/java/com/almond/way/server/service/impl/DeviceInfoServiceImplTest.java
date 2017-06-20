package com.almond.way.server.service.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.exception.WhereAmIException;
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
