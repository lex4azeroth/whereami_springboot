package com.almond.way.server.service.impl;

import java.util.Queue;

import javax.jms.Destination;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.PublisherService;
import com.almond.way.server.service.impl.PublisherServiceImpl;

@Service
public class PublisherServiceImpl implements PublisherService {

	private static Logger logger = Logger.getLogger(PublisherServiceImpl.class.getName());
	
//	@Autowired()
////	@Qualifier("jmsTemplate")
//	private JmsTemplate jmsTemplate;
	
//	@Autowired()
//	@Qualifier("destination")
//	private Destination destination;
	
	@Autowired  
    private JmsMessagingTemplate jmsMessagingTemplate;  
  
    @Autowired  
    private Queue queue;  

	@Override
	public synchronized void doPost(final DeviceInfo deviceInfo) {
		String text = String.format("POST:[%s]", deviceInfo.toString());
		logger.info(text);	
//		jmsTemplate.convertAndSend("deviceInfoDestination", deviceInfo);
		jmsMessagingTemplate.convertAndSend("almond-way", deviceInfo);
	}
}
