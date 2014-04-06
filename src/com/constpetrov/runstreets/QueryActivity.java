package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.constpetrov.runstreets.model.Area;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;



public class QueryActivity extends Activity {
	StreetsDataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		dataSource = new StreetsDataSource(this);
		
		List<Area> groupAreas = dataSource.getAdministrativeStates();
		
		ArrayList<OptionItem<Area>> groups = new ArrayList<OptionItem<Area>>();
	    List<List<OptionItem<Area>>> children = new ArrayList<List<OptionItem<Area>>>();
	    
	    

	    for(Area area: groupAreas){
	    	OptionItem<Area> areaItem = new OptionItem<Area>(area, area.toString());
	    	groups.add(areaItem);
	    	List<OptionItem<Area>> tmp = new LinkedList<OptionItem<Area>>();
	    	for(Area district: dataSource.getChildAreas(area)){
	    		OptionItem<Area> districtItem = new OptionItem<Area>(district, district.getDistrictName());
	    		tmp.add(districtItem);
	    	}
	    	children.add(tmp);
	    }

	    ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView1);
	    elv.setAdapter(new ExpandableAdapter<Area>(getLayoutInflater(), groups, children));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

}
