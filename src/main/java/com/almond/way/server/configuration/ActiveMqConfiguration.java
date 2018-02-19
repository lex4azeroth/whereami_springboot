package com.almond.way.server.configuration;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jms.support.converter.MessageConverter;

import com.almond.way.server.utils.DeviceInfoMessageConverter;


@Profile("prod")
@Configuration
public class ActiveMqConfiguration {
	
	@Bean
	public MessageConverter deviceInfoMessageConverter() {
		return new DeviceInfoMessageConverter();
	}
    
    @Bean  
    public Queue queue() {  
        return new ActiveMQQueue("almond-way");  
    }  
  
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory (){  
        ActiveMQConnectionFactory activeMQConnectionFactory =  
                new ActiveMQConnectionFactory(  
                        ActiveMQConnectionFactory.DEFAULT_USER,  
                        ActiveMQConnectionFactory.DEFAULT_PASSWORD,  
//                        "tcp://192.168.0.100:61616");  
                        ActiveMQConnectionFactory.DEFAULT_BROKER_URL);  
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;  
    }
}
