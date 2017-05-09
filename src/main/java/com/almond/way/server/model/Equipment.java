package com.almond.way.server.model;

import java.io.Serializable;

public class Equipment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 646275505968936688L;
	
	private int id;
	private String equipmentId;
	private String equipmentName;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
}