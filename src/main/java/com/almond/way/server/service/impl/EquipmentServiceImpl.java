package com.almond.way.server.service.impl;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.almond.way.server.dao.EquipmentDao;
import com.almond.way.server.model.Equipment;
import com.almond.way.server.service.EquipmentService;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private EquipmentDao equipmentDao;
	
	private List<Equipment> equipmentsInCache; 

	@Override
	public synchronized int registEquipment(Equipment equipment) {
		Objects.requireNonNull(equipment);
		int id = equipmentDao.registDevice(equipment);
		equipment.setId(id);
		equipmentsInCache.add(equipment);
		logger.info("Now we have [" + equipmentsInCache.size() + "] equipments in cache.");
		return id;
	}

	@Override
	public synchronized List<Equipment> getEquipmentList() {
		if (equipmentsInCache == null || equipmentsInCache.isEmpty()) {
			logger.info("Equipment # in cache is 0");
			equipmentsInCache = equipmentDao.getDeviceList();
		}
		
		logger.info("After query, Now we have [" + equipmentsInCache.size() + "] equipments in cache.");

		return equipmentsInCache;
	}

	@Override
	public synchronized boolean isEquipmentRegisted(String id) {
		logger.info(String.format("Is device %s registerd in cache?", id));
		for (Equipment equ : getEquipmentList()) {
			if (equ.getEquipmentId().equals(id)) {
				logger.info("Equipment [" + id + "] registed in cache.");
				return true;
			}
		}
		
		logger.info("Equipment [" + id + "] not registed in cache.");
		return false;
	}

}
