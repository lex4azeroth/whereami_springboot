package com.almond.way.server.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.PublisherService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PublisherServiceImplTest {

	@Autowired
	@Qualifier("publisherServiceImpl")
	private PublisherService service;
	
	@Test
	public void testDoPost() {
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setAndroidID("testID");
		deviceInfo.setDate("2017-04-08 03:09:33");
		service.doPost(deviceInfo);
	}
}
