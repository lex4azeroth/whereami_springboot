package com.almond.way.server.configuration;

//import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfiguration {
	@Value("${jdbc.driver}")
	private String driver;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;
////	@Value("${jdbc.maxActive}")
////	private int maxActive;
////	@Value("${jdbc.maxIdel}")
////	private int maxIdel;
////	@Value("${jdbc.maxWait}")
////	private long maxWait;
//
	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
//		dataSource.setMaxActive(maxActive);
//		dataSource.setMaxIdle(maxIdel);
//		dataSource.setMaxWait(maxWait);
//		dataSource.setValidationQuery("SELECT 1");
//		dataSource.setTestOnBorrow(true);
		return dataSource;
	}
}
