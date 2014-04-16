package com.constpetrov.runstreets;

import java.util.Set;

import com.constpetrov.runstreets.model.Area;

public class SearchParameters {
	
	private final String name;
	
	private final Set<Area> areas;
	
	public SearchParameters(String name, Set<Area> areas){
		this.name = name;
		this.areas = areas;
	}

	public String getName() {
		return name;
	}

	public Set<Area> getAreas() {
		return areas;
	}

}
