package com.almond.way.server.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.PublisherService;
import com.almond.way.server.service.impl.PublisherServiceImpl;

@Service
public class PublisherServiceImpl implements PublisherService {

	private static Logger logger = Logger.getLogger(PublisherServiceImpl.class.getName());
	
	@Autowired  
    private JmsMessagingTemplate jmsMessagingTemplate;  

	@Override
	public synchronized void doPost(final DeviceInfo deviceInfo) {
		String text = String.format("POST:[%s]", deviceInfo.toString());
		logger.info(text);	
		jmsMessagingTemplate.convertAndSend("almond-way", deviceInfo);
	}
}
