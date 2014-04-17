package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.gui.CreateUIListener;
import com.constpetrov.runstreets.gui.ExecQuery;
import com.constpetrov.runstreets.gui.ExpandableCheckboxAdapter;
import com.constpetrov.runstreets.gui.LoadDBTask;
import com.constpetrov.runstreets.gui.OnQueryListener;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.SearchParameters;
import com.constpetrov.runstreets.model.Street;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class QueryFragment extends Fragment implements CreateUIListener{
	
	private ArrayList<OptionItem<Area>> groups;
	
	private List<List<OptionItem<Area>>> children;
	
	private OnQueryListener mListener;

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
	public void createUI() {
		groups = new ArrayList<OptionItem<Area>>();
		children = new ArrayList<List<OptionItem<Area>>>();
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

	    ExpandableListView elv = (ExpandableListView) getActivity().findViewById(R.id.expandableListView1);
	    elv.setAdapter(new ExpandableCheckboxAdapter<Area>(getActivity().getLayoutInflater(), groups, children));
	    
	    final Button findButton = (Button)getActivity().findViewById(R.id.button1);
	    findButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findStreets(getAreas(), getRenames(), getTypes(), getName());
			}
		});
	}
	
	protected void findStreets(Set<Integer> areas,
			List<Rename> renames, Set<Integer> types, String name) {
		List<Street> result = new ArrayList<Street>();
		SearchParameters params = new SearchParameters(name, null, areas, 0, 0);
		ExecQuery query = new ExecQuery(getActivity(), mListener);
		query.execute(params);
	}

	private Set<Integer> getAreas() {
		Set<Integer> result = new HashSet<Integer>();
		for(OptionItem<Area> oi: groups){
			if(oi.isSelected()){
				result.add(oi.getItem().getId());
			}
		}
		for(List<OptionItem<Area>> childGroup: children){
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
	
	

}
