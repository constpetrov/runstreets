package com.constpetrov.runstreets.gui;

import java.util.List;

import com.constpetrov.runstreets.model.StreetInfo;

public interface OnLoadInfosListener {
	
	public void showInfos(List<List<StreetInfo>> infos);

}
