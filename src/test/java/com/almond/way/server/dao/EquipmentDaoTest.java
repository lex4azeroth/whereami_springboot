package com.almond.way.server.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.ServerApplication;
import com.almond.way.server.configuration.ActiveMqConfiguration;
import com.almond.way.server.configuration.DataSourceConfiguration;
import com.almond.way.server.configuration.MyBatisConfig;
import com.almond.way.server.configuration.MyBatisMapperScannerConfig;
import com.almond.way.server.configuration.WebMvcConfig;
import com.almond.way.server.model.Equipment;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {"local"})
@SpringBootTest(classes = {ServerApplication.class})
@ContextConfiguration(classes = {
                                 DataSourceConfiguration.class, 
                                 ActiveMqConfiguration.class, 
                                 MyBatisConfig.class, 
                                 MyBatisMapperScannerConfig.class,
                                 WebMvcConfig.class})
public class EquipmentDaoTest {

	@Resource
	private EquipmentDao equipmentDao;
	
	@Autowired
	private DataSource dataSource;
	
	private static final String EQUIPMENT_ID = "TEST_EQU_ID";
	private static final String EQUIPMENT_NAME = "TEST_EQU_NAME";
	private Equipment equipment;
	
	private void creatDBSchema() throws SQLException {
		Statement stat = dataSource.getConnection().createStatement();
		stat.execute("CREATE TABLE IF NOT EXISTS WAY_EQU (ID varchar(36) NOT NULL AUTO_INCREMENT, EQU_ID varchar(36) NOT NULL UNIQUE, EQU_NAME varchar(50) NOT NULL, PRIMARY KEY(ID))");
		stat.execute("DELETE FROM WAY_EQU");
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
		
		if (equipment == null) {
			equipment = new Equipment();
			equipment.setEquipmentId(EQUIPMENT_ID);
			equipment.setEquipmentName(EQUIPMENT_NAME);
		}
	}
	
	@After
	public void tearDown() {
		equipment = equipmentDao.getDevice(equipment.getEquipmentId());
		if (equipment != null) {
			equipmentDao.deleteDevice(equipment.getId());
		}
	}
	
	@Test
	public void testRegistDevice() {
		int result = equipmentDao.registDevice(equipment);
		assertEquals(1, result);
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void testRegistDeviceDuplication() {
		equipmentDao.registDevice(equipment);
		
		equipmentDao.registDevice(equipment);
	}
	
	@Test
	public void testGetDeviceList() {
		List<Equipment> equipments = equipmentDao.getDeviceList();
		assertNotNull(equipments);
		assertTrue(equipments.size() >= 0);
	}
	
	@Test
	public void testGetDeviceByEquipmentId() {
		equipmentDao.registDevice(equipment);
		Equipment retrievedEqu = equipmentDao.getDevice(equipment.getEquipmentId());
		assertNotNull(retrievedEqu);
		assertEquals(EQUIPMENT_NAME, retrievedEqu.getEquipmentName());
		assertEquals(EQUIPMENT_ID, retrievedEqu.getEquipmentId());
	}
	
	@Test
	public void testUpdateDeviceName() {
		int result = equipmentDao.registDevice(equipment);
		assertEquals(1, result);
		
		String newName = "二狗子";
		equipment.setEquipmentName(newName);
		equipmentDao.updateDeviceName(equipment);
		Equipment updatedEqu = equipmentDao.getDevice(equipment.getEquipmentId());
		assertEquals(newName, updatedEqu.getEquipmentName());		
	}
}
