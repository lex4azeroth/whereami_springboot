package com.almond.way.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.almond.way.server.dao.EquipmentDao;
import com.almond.way.server.model.Equipment;
import com.almond.way.server.service.EquipmentService;

@Service
public class EquipmentServiceImpl implements EquipmentService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private EquipmentDao equipmentDao;
	
	private static final ThreadLocal<List<Equipment>> equipmentsInCacheThreadLocal = new ThreadLocal<List<Equipment>>() {
		@Override
		protected List<Equipment> initialValue() {
			return new ArrayList<Equipment>();
		}
	};

	@Override
	public synchronized int registEquipment(Equipment equipment) {
		Objects.requireNonNull(equipment);
		int id = equipmentDao.registDevice(equipment);
		equipment.setId(id);
		List<Equipment> equipmentsInCache = equipmentsInCacheThreadLocal.get();
		equipmentsInCache.add(equipment);
		logger.info("Now we have [" + equipmentsInCache.size() + "] equipments in cache.");
		equipmentsInCacheThreadLocal.set(equipmentsInCache);
		return id;
	}

	@Override
	public List<Equipment> getEquipmentList() {
//		List<Equipment> equipmentsInCache = equipmentsInCacheThreadLocal.get();
//		
//		if (equipmentsInCache.isEmpty()) {
//			logger.info("Equipment # in cache is 0");
//			equipmentsInCache = equipmentDao.getDeviceList();
//			equipmentsInCacheThreadLocal.set(equipmentsInCache);
//		}
//		
//		logger.info("After query, Now we have [" + equipmentsInCache.size() + "] equipments in cache.");
//
//		return equipmentsInCache;
		
		return equipmentDao.getDeviceList();
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

	@Override
	public String udpateDeviceName(Equipment equipment) {
		Objects.requireNonNull(equipment);
		int changedLine = equipmentDao.updateDeviceName(equipment);
		if (changedLine == 1) {
//			List<Equipment> equipmentsInCache = getEquipmentList();
//			for (Equipment equ : equipmentsInCache) {
//				if (equ.getEquipmentId().equals(equipment.getEquipmentId())) {
//					synchronized (equipmentsInCacheThreadLocal) {
//						equ.setEquipmentName(equipment.getEquipmentName());
//						equipmentsInCacheThreadLocal.set(equipmentsInCache);
//					}
//				}
//			}
//			equipmentsInCacheThreadLocal.remove();
			return equipment.getEquipmentName();
		}
		
		return "Update failed";
	}

	@Override
	public String getDeviceName(String equId) {
		List<Equipment> equipmentsInCache = getEquipmentList();
		for (Equipment equ : equipmentsInCache) {
			if (equ.getEquipmentId().equals(equId)) {
				return equ.getEquipmentName();
			}
		}
		
		return "not found";
	}
}
