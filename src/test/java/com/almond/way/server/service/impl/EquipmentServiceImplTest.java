package com.almond.way.server.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.almond.way.server.dao.EquipmentDao;
import com.almond.way.server.model.Equipment;
import com.almond.way.server.service.EquipmentService;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceImplTest {

	@Mock
	private EquipmentDao equipmentDao;

	private Equipment equipment;
	private List<Equipment> mockEquipmentList = new ArrayList<Equipment>();

	@InjectMocks
	private EquipmentService service = new EquipmentServiceImpl();

	private static final String EQU_ID = "equid";
	private static final String EQU_NAME = "equname";
	private static final Integer ID = new Integer(1);

	@Before
	public void setup() {
		equipment = new Equipment();
		equipment.setEquipmentId(EQU_ID);
		equipment.setEquipmentName(EQU_NAME);

		MockitoAnnotations.initMocks(this);
		Mockito.when(equipmentDao.registDevice(equipment)).thenReturn(ID);

		mockEquipmentList.add(equipment);
		Mockito.when(equipmentDao.getDeviceList()).thenReturn(mockEquipmentList);

	}

	@Test
	public void testGetEquipmentListWithEmptyCach() {
		List<Equipment> equipmentsInCache = service.getEquipmentList();
		assertTrue(equipmentsInCache.size() == 1);
	}

	@Test
	public void testRegistEquipment() {
		int registedId = service.registEquipment(equipment);
		assertEquals(ID.intValue(), registedId);
	}

	@Test
	public void testEquipmentIsRegisted() {
		assertTrue(service.isEquipmentRegisted(EQU_ID));
	}

	@Test
	public void testEquipmentIsNotRegisted() {
		assertFalse(service.isEquipmentRegisted("Invalid Id"));
	}
}
