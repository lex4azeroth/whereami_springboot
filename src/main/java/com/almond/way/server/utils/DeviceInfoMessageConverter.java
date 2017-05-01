package com.almond.way.server.utils;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
public class DeviceInfoMessageConverter implements MessageConverter {

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		ObjectMessage objMessage = (ObjectMessage) message;
		
		return objMessage.getObject();
	}

	@Override
	public Message toMessage(Object obj, Session session) throws JMSException, MessageConversionException {
		return session.createObjectMessage((Serializable) obj);
	}

}
