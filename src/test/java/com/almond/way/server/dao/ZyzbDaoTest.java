package com.almond.way.server.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.almond.way.server.model.ZYZB;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"local"})
@SpringBootTest(classes = {ServerApplication.class})
@Ignore
@ContextConfiguration(classes = {
                                 DataSourceConfiguration.class, 
                                 ActiveMqConfiguration.class, 
                                 MyBatisConfig.class, 
                                 MyBatisMapperScannerConfig.class,
                                 WebMvcConfig.class})
public class ZyzbDaoTest {
	
	@Resource
	private ZyzbDao mockZyzbDao;
	
	@Test
	public void testGetZyzbList() {
		List<ZYZB> zyzbList = mockZyzbDao.getZyzbList("2017-02-06 12:30:47", "2017-02-06 12:36:23", null);
		int size = zyzbList.size();
		assertTrue(size > 0);
	}
}
