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
		
		ArrayList<OptionItem> groups = new ArrayList<OptionItem>();
	    List<List<OptionItem>> children = new ArrayList<List<OptionItem>>();
	    
	    

	    for(Area area: groupAreas){
	    	OptionItem areaItem = new OptionItem("", area.toString());
	    	groups.add(areaItem);
	    	List<OptionItem> tmp = new LinkedList<OptionItem>();
	    	for(Area district: dataSource.getChildAreas(area)){
	    		OptionItem districtItem = new OptionItem("", district.getDistrictName());
	    		tmp.add(districtItem);
	    	}
	    	children.add(tmp);
	    }

	    ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView1);
	    elv.setAdapter(new ExpandableAdapter(getLayoutInflater(), groups, children));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

}
