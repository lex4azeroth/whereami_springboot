package com.almond.way.server.model;

public class LaL {

	private double longitude;
	private double latitude;

	public LaL(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof LaL) {
			return equalityValue((LaL) obj);
		} else {
			return false;
		}
		
		// TODO
		// hash code check
	}
	
	private boolean equalityValue(LaL lal) {
		return (lal.getLatitude() == this.latitude && lal.getLongitude() == this.longitude);
	}

}
