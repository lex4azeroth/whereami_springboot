package com.almond.way.server.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.ServerApplication;
import com.almond.way.server.configuration.ActiveMqConfiguration;
import com.almond.way.server.configuration.DataSourceConfiguration;
import com.almond.way.server.configuration.MyBatisConfig;
import com.almond.way.server.configuration.MyBatisMapperScannerConfig;
import com.almond.way.server.configuration.WebMvcConfig;
import com.almond.way.server.model.MockZyzb;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"dev"})
@SpringBootTest(classes = {ServerApplication.class})
@ContextConfiguration(classes = {
                                 DataSourceConfiguration.class, 
                                 ActiveMqConfiguration.class, 
                                 MyBatisConfig.class, 
                                 MyBatisMapperScannerConfig.class,
                                 WebMvcConfig.class})
public class MockZyzbDaoTest {
	
	@Resource
	private MockZyzbDao mockZyzbDao;
	
	@Test
	public void testGetDeviceList() {
		List<MockZyzb> deviceList = mockZyzbDao.getDeviceList();
		int size = deviceList.size();
		assertTrue(size > 0);
	}
}
