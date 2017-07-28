package com.almond.way.server.model;

import java.io.Serializable;

public class MockZyzb implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -959233232307255096L;

	private String pdaID;
	private String pdaName;
	public String getPdaID() {
		return pdaID;
	}
	public void setPdaID(String pdaID) {
		this.pdaID = pdaID;
	}
	public String getPdaName() {
		return pdaName;
	}
	public void setPdaName(String pdaName) {
		this.pdaName = pdaName;
	}
	
}
