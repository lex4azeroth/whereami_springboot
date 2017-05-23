package com.almond.way.server.utils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.almond.way.server.exception.WhereAmIException;
import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.service.DataProcessorService;

@Component
public class DeviceInfoMessageReceiver {

	private static Logger logger = Logger.getLogger(DeviceInfoMessageReceiver.class.getName());
	
	@Autowired
	@Qualifier("dataProcessorServiceImpl")
	private DataProcessorService dataProcessor;

	@JmsListener(destination = "almond-way")
	public void receiveDeviceInfo(Message message) {
		if (message instanceof ObjectMessage) {
			logger.info("DeviceInfoMessageReceiver MESSAGE RECEIVED");
			ObjectMessage objMessage = (ObjectMessage) message;
			try {
				Object obj = objMessage.getObject();
				DeviceInfo deviceInfo = (DeviceInfo) obj;
				dataProcessor.processDeviceLocation(deviceInfo);
			} catch (JMSException ex) {
				throw new WhereAmIException(ex);
			}
		}
	}
}
