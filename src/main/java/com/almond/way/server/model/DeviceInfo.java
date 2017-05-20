package com.almond.way.server.model;

import java.io.Serializable;

public class DeviceInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -364368565847380246L;
	
	private int id;
	private String androidID;
	private String latitude;
	private String longitude;
	private String dateTime;
	
	public final static String SEPARATOR = ";";
	
	public String getAndroidID() {
		return androidID;
	}
	
	public void setAndroidID(String equipmentId) {
		this.androidID = equipmentId;
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
	
	public String getDate() {
		return dateTime.substring(0, dateTime.length() - 2);
	}
	
	public void setDate(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		StringBuilder sbToPost = new StringBuilder();
		sbToPost.append(this.getAndroidID());
		sbToPost.append(SEPARATOR);
		sbToPost.append(this.getLatitude());
		sbToPost.append(SEPARATOR);
		sbToPost.append(this.getLongitude());
		sbToPost.append(SEPARATOR);
		sbToPost.append(this.getDate());
		
		return sbToPost.toString();
		
	}
}
