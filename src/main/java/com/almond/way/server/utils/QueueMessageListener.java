package com.almond.way.server.utils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.DataProcessorService;

public class QueueMessageListener implements MessageListener {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("dataProcessorServiceImpl")
	private DataProcessorService dataProcessor;
	
	@Override
	public void onMessage(Message message) {
		
		if (message instanceof ObjectMessage) {
			logger.info("MESSAGE RECEIVED");
			ObjectMessage objMessage = (ObjectMessage) message;
			try {
				Object obj = objMessage.getObject();
				DeviceInfo deviceInfo = (DeviceInfo) obj;
				dataProcessor.processDeviceLocation(deviceInfo);   
			} catch (JMSException ex) {
				logger.error(ex.getMessage().toString());
				throw new WhereAmIException(ex);
			}
		}
	}
}