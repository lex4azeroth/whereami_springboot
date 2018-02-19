package com.almond.way.server.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.PublisherService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PublisherServiceImplTest {

	@Mock
	private PublisherService service;
	
	@Test
	public void testDoPost() {
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setAndroidID("testID");
		deviceInfo.setDate("2017-04-08 03:09:33");
		service = Mockito.mock(PublisherService.class);
		service.doPost(deviceInfo);
	}
}
