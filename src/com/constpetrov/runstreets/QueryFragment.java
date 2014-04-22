package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.gui.ExecQuery;
import com.constpetrov.runstreets.gui.LoadDBTask;
import com.constpetrov.runstreets.gui.OnQueryListener;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.gui.UpdateGuiListener;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.SearchParameters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class QueryFragment extends Fragment implements UpdateGuiListener{
	
	private OnQueryListener mListener;
	private Button findButton;
	private TextView areas;
	private TextView types;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.fragment_query, container, false);
		
		LoadDBTask t = new LoadDBTask(getActivity(), this);
		t.execute();
		
		return result;
		
	}
	
	
	
	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnQueryListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }	



	@Override
	public void updateGui() {
		ArrayList<OptionItem<Area>> groups = ((FragActivity)getActivity()).getGroups();
		List<List<OptionItem<Area>>> children = ((FragActivity)getActivity()).getChildren();
		
		if(groups.size() == 0 || children.size() == 0){
			List<Area> groupAreas = StreetsDataSource.get().getAdministrativeStates();
		    for(Area area: groupAreas){
		    	OptionItem<Area> areaItem = new OptionItem<Area>(area, area.toString());
		    	groups.add(areaItem);
		    	List<OptionItem<Area>> tmp = new LinkedList<OptionItem<Area>>();
		    	for(Area district: StreetsDataSource.get().getChildAreas(area)){
		    		OptionItem<Area> districtItem = new OptionItem<Area>(district, district.getDistrictName());
		    		tmp.add(districtItem);
		    	}
		    	children.add(tmp);
		    }
		}

	    StringBuilder b = new StringBuilder();
	    int groupNumber = 0;
	    for(OptionItem<Area> group: groups){
	    	String childString = "";
	    	if(group.isSelected()){
	    		childString = "все районы";
	    	} else {
	    		StringBuilder childBuilder = new StringBuilder();
	    		for(OptionItem<Area> child: children.get(groupNumber)){
	    			if(child.isSelected()){
	    				childBuilder.append(child.getName());
	    				childBuilder.append(", ");
	    			}
	    		}
	    		if(childBuilder.lastIndexOf(",") != -1){
	    			childString = childBuilder.substring(0, childBuilder.lastIndexOf(","));
	    		}
	    	}
	    	groupNumber++;
	    	if(childString.length() == 0){
	    		continue;
	    	}
	    	b.append(group.getName());
    		b.append(":");
    		b.append("\n");
    		b.append(childString);
    		b.append("\n");
	    }
		
		String areasStr = b.length() ==0 ? "Все районы":b.toString();
		
		
	    if(findButton == null){
		    findButton = (Button)getActivity().findViewById(R.id.button1);
		    findButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					findStreets(getAreas(), getRenames(), getTypes(), getName(), isUseOldNames());
				}
			});
	    }
	    if(areas == null){
		    areas = (TextView)getActivity().findViewById(R.id.areas);
		    areas.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AreaDialogFragment dialog = new AreaDialogFragment();
					dialog.show(getFragmentManager(), "dialog");
					
				}
			});
	    }
		areas.setText(areasStr);
	    
		if(types == null){
		    types = (TextView)getActivity().findViewById(R.id.types);
		    types.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO show dialog
					
				}
			});
		}
	}
	
	protected void findStreets(Set<Integer> areas,
			List<Rename> renames, Set<Integer> types, String name, boolean useOldNames) {
		SearchParameters params = new SearchParameters(name, useOldNames, areas, Collections.<Integer> emptySet(), 0);
		ExecQuery query = new ExecQuery(getActivity(), mListener);
		query.execute(params);
	}

	private Set<Integer> getAreas() {
		Set<Integer> result = new HashSet<Integer>();
		for(OptionItem<Area> oi: ((FragActivity)getActivity()).getGroups()){
			if(oi.isSelected()){
				result.add(oi.getItem().getId());
			}
		}
		for(List<OptionItem<Area>> childGroup: ((FragActivity)getActivity()).getChildren()){
			for(OptionItem<Area> oi: childGroup){
				if(oi.isSelected()){
					result.add(oi.getItem().getId());
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
		return ((EditText)getActivity().findViewById(R.id.editText)).getText().toString();
	}
	
	private boolean isUseOldNames(){
		CheckBox box = (CheckBox)getActivity().findViewById(R.id.fragment_queryUseOldNames);
		if(box == null){
			return false;
		}
		return box.isSelected();
	}
	
	

}
