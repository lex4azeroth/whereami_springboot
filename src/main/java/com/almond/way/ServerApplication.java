package com.almond.way;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
//@MapperScan("com.almond.way.server.dao")
//public class ServerApplication extends WebMvcConfigurerAdapter {
public class ServerApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(ServerApplication.class);
    }
    
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
