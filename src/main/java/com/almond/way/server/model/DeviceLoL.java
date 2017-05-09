package com.almond.way.server.model;

public class DeviceLoL {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2778994230164611687L;

	private int id;
	private String latitude;
	private String longitude;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}