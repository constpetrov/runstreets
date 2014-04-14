package com.constpetrov.runstreets;

public class OptionItem<T> {

	private T area;
	private String name;
	private boolean selected;
	
	public OptionItem(T area, String name) {
		this.area = area;
	    this.name = name;
	    selected = false;
	}
	
	public T getItem() {
		return area;
	}

	public void setItem(T area) {
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