package com.constpetrov.runstreets.gui;

import java.util.Collection;

import com.constpetrov.runstreets.model.Street;

public interface OnQueryListener {
	public void showResults(Collection<Street> result);

}
