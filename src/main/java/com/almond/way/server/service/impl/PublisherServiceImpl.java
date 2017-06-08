package com.almond.way.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired  
    private JmsMessagingTemplate jmsMessagingTemplate;  

	@Override
	public synchronized void doPost(final DeviceInfo deviceInfo) {
		String text = String.format("POST:[%s]", deviceInfo.toString());
		logger.info(text);	
		jmsMessagingTemplate.convertAndSend("almond-way", deviceInfo);
	}
}
