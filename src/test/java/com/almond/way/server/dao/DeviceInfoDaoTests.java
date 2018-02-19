package com.almond.way.server.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
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
import com.almond.way.server.dao.DeviceInfoDao;
import com.almond.way.server.model.DeviceInfo;
import com.almond.way.server.model.DeviceLoL;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"local"})
@SpringBootTest(classes = {ServerApplication.class})
@ContextConfiguration(classes = {
                                 DataSourceConfiguration.class,
                                 ActiveMqConfiguration.class,
                                 MyBatisConfig.class,
                                 MyBatisMapperScannerConfig.class,
                                 WebMvcConfig.class})
public class DeviceInfoDaoTests {

	@Resource
	private DeviceInfoDao deviceInfoDao;

	@Autowired
	private DataSource dataSource;

	private static final String EQUIPMENT_ID = "815683fda35de8d5";
	private static final String FROM = "2017-04-08 04:51";
	private static final String TO = "2017-04-08 04:58";
	private static final String INNER = "2017-04-08 04:55";
	private static final String TIMESTAMP = "2017-04-08 03:09:3322";
	private static final String EXPECTED_TIMESTAMP = "2017-04-08 03:09:33";

	private void creatDBSchema() throws SQLException {
		Statement stat = dataSource.getConnection().createStatement();
		stat.execute("CREATE TABLE IF NOT EXISTS WAY_LAL (ID varchar(36) NOT NULL AUTO_INCREMENT, EQU_ID varchar(36) NOT NULL, LATITUDE varchar(36) NOT NULL, LONGITUDE varchar(36) NOT NULL, TIMESTAMP varchar(36) NOT NULL, PRIMARY KEY(ID))");
		stat.execute("DELETE FROM WAY_LAL");
		stat.close();
	}

	@Before
	public void setup() {
		try {
			creatDBSchema();
		}
		catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testGetDeviceLaL() {
		insertLaL();
		List<DeviceLoL> deviceInfos =
		                deviceInfoDao.getDeviceLaL(EQUIPMENT_ID, FROM, TO);

		assertNotNull(deviceInfos);
		assertTrue(deviceInfos.size() > 0);
	}

	@Test
	public void testGetDeviceInfoFail() {
		DeviceInfo info = new DeviceInfo();
		info.setAndroidID(EQUIPMENT_ID);
		info.setDate("");
		DeviceInfo retrievedInfo = deviceInfoDao.getDeviceInfo(info);
		assertNull(retrievedInfo);
	}

	@Test
	public void testAddDeviceInfoAndDeleteItById() {
		DeviceInfo info = new DeviceInfo();
		info.setAndroidID("TEST_CANYOUSEEME");
		info.setDate(TIMESTAMP);
		info.setLatitude("TEST_LAT");
		info.setLongitude("TEST_LON");
		deviceInfoDao.addDeviceInfo(info);

		DeviceInfo retrievedInfo = deviceInfoDao.getDeviceInfo(info);
		assertNotNull(retrievedInfo);
		assertEquals("TEST_CANYOUSEEME", retrievedInfo.getAndroidID());
		assertEquals(EXPECTED_TIMESTAMP, retrievedInfo.getDate());
		assertEquals("TEST_LAT", retrievedInfo.getLatitude());
		assertEquals("TEST_LON", retrievedInfo.getLongitude());

		deviceInfoDao.deleteDeviceInfoById(retrievedInfo.getId());
	}

	private void insertLaL() {
		DeviceInfo info = new DeviceInfo();
		info.setAndroidID(EQUIPMENT_ID);
		info.setDate(INNER);
		info.setLatitude("TEST_LAT");
		info.setLongitude("TEST_LON");
		deviceInfoDao.addDeviceInfo(info);
	}
}
