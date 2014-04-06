package com.constpetrov.runstreets;

public class OptionItem {

	private String groupName;
	private String childName;
	private boolean selected;
	
	public OptionItem(String group, String child) {
	
	    this.groupName = group;
	    this.childName = child;
	    selected = false;
	}
	
	public void setGroupName(String name) {
	    this.groupName = name;
	}
	
	public void setChildName(String name) {
	    this.childName = name;
	}
	
	public void setSelected(boolean selected) {
	    this.selected = selected;
	}
	
	public String getGroupName() {
	    return groupName;
	}
	
	public String getChildName() {
	    return childName;
	}
	
	public boolean isSelected() {
	    return selected;
	}
}