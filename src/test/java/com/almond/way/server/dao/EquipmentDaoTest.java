package com.almond.way.server.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import com.almond.way.server.model.Equipment;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EquipmentDaoTest {

	@Resource
	private EquipmentDao equipmentDao;
	
	private static final String EQUIPMENT_ID = "TEST_EQU_ID";
	private static final String EQUIPMENT_NAME = "TEST_EQU_NAME";
	private Equipment equipment;
	
	@Before
	public void setup() {
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
		
//		equipmentDao.deleteDevice(retrievedEqu.getId());
	}
}
