package com.constpetrov.runstreets;

import com.constpetrov.runstreets.model.Area;

public class OptionItem {

	private Area area;
	private String name;
	private boolean selected;
	
	public OptionItem(Area area, String name) {
		this.area = area;
	    this.name = name;
	    selected = false;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setName(String name) {
	    this.name = name;
	}
	
	public void setSelected(boolean selected) {
	    this.selected = selected;
	}
	
	public String getName() {
	    return name;
	}
	
	public boolean isSelected() {
	    return selected;
	}
}