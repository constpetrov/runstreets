package com.constpetrov.runstreets.model;

public enum RenameCountType {
	ANY("any"),
	MORE("more"),
	LESS("less"),
	EQUALS("eq");
	
	private String theName;

	RenameCountType(String name) {
    theName = name;
  }

  @Override public String toString() {
    return theName;
  }
}
