package com.almond.way.server.model;

import java.util.ArrayList;
import java.util.List;

public class Line {
	
	private String name;
	private List<LaL> lals;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void appendLal(LaL lal) {
		if (lals == null) {
			lals = new ArrayList<>();
		}
		
		lals.add(lal);
	}
	
	public int getCount() {
		return getLals().size();
	}
	
	public String getName() {
		return name;
	}
	
	public List<LaL> getLals() {
		if (lals == null) {
			// throw exception here;
		}
		return lals;
	}

}
