package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.Street;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;



public class QueryActivity extends Activity {
	
	private StreetsDataSource dataSource;
	
	private ArrayList<OptionItem<Area>> groups;
	
	private List<List<OptionItem<Area>>> children;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		dataSource = new StreetsDataSource(this);
		
		List<Area> groupAreas = dataSource.getAdministrativeStates();
		
		groups = new ArrayList<OptionItem<Area>>();
	    children = new ArrayList<List<OptionItem<Area>>>();
	    
	    

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
	    
	    final Button findButton = (Button)findViewById(R.id.button1);
	    findButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Street> streets = findStreets(getAreas(), getRenames(), getTypes(), getName());
				Toast.makeText(QueryActivity.this, "Streets found: "+ streets.size(), Toast.LENGTH_SHORT).show();
			}

			
		});
		
	}
	
	protected List<Street> findStreets(Set<Area> areas,
			List<Rename> renames, Set<Integer> types, String name) {
		List<Street> result = dataSource.findStreets(name, areas, renames, types);
		return result;
	}

	private Set<Area> getAreas() {
		Set<Area> result = new HashSet<Area>();
		for(OptionItem<Area> oi: groups){
			if(oi.isSelected()){
				result.add(oi.getItem());
			}
		}
		for(List<OptionItem<Area>> childGroup: children){
			for(OptionItem<Area> oi: childGroup){
				if(oi.isSelected()){
					result.add(oi.getItem());
				}
			}
		}
		return result;
	}
	
	private List<Rename> getRenames(){
		return null;
	}
	
	private Set<Integer> getTypes(){
		return null;
	}

	private String getName(){
		return ((EditText)findViewById(R.id.editText)).getText().toString();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

}
