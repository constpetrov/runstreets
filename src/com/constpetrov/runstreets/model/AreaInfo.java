package com.constpetrov.runstreets.model;

import java.util.List;

public class AreaInfo {
	private Area area;
	private String areaTypeName;
	private List<Street> streets;
	private List<AreaHistory> history;
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getAreaTypeName() {
		return areaTypeName;
	}
	public void setAreaTypeName(String areaTypeName) {
		this.areaTypeName = areaTypeName;
	}
	public List<Street> getStreets() {
		return streets;
	}
	public void setStreets(List<Street> streets) {
		this.streets = streets;
	}
	public List<AreaHistory> getHistory() {
		return history;
	}
	public void setHistory(List<AreaHistory> history) {
		this.history = history;
	}

}
