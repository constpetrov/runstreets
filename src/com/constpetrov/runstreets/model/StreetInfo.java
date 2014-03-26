package com.constpetrov.runstreets.model;

import java.util.List;

public class StreetInfo {
	private Street street;
	private String streetTypeName;
	private List<Area> areas;
	private List<StreetHistory> history;
	public Street getStreet() {
		return street;
	}
	public void setStreet(Street street) {
		this.street = street;
	}
	public String getStreetTypeName() {
		return streetTypeName;
	}
	public void setStreetTypeName(String streetTypeName) {
		this.streetTypeName = streetTypeName;
	}
	public List<Area> getAreas() {
		return areas;
	}
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	public List<StreetHistory> getHistory() {
		return history;
	}
	public void setHistory(List<StreetHistory> history) {
		this.history = history;
	}

}
