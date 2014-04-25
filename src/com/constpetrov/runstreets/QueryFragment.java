package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.RenameCountType;
import com.constpetrov.runstreets.model.SearchParameters;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.Type;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class QueryFragment extends Fragment{
	
	private TaskCallbacks mCallbacks;
	private Button findButton;
	private TextView areas;
	private TextView types;
	private Spinner rct;
	private EditText rename;
	
	public static interface TaskCallbacks {
	    void onPreExecute(int titleId, int messageId, boolean withProgress);
	    void onProgressUpdate(int percent);
	    void onCancelled();
	    void onPostExecute();
	    void onPostExecute(Collection<Street> result);
	}

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		LoadDBTask t = new LoadDBTask();
		t.execute();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.fragment_query, container, false);
		
		return result;
		
	}
	
	
	
	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
        	mCallbacks = (TaskCallbacks) activity;
        	//mCallbacks.onPostExecute();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }	

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	public void updateGui() {
		ArrayList<OptionItem<Area>> groups = ((FragActivity)getActivity()).getGroups();
		List<List<OptionItem<Area>>> children = ((FragActivity)getActivity()).getChildren();
		
		if(groups.size() == 0 || children.size() == 0){
			List<Area> groupAreas = null;
			try{
				groupAreas = StreetsDataSource.get().getAdministrativeStates();
			} catch (Exception e){
				return;
			}
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
    		b.append(": ");
    		b.append(childString);
    		b.append("\n");
	    }
		
		String areasStr = b.length() ==0 ? "Все районы →":b.toString();
		
		ArrayList<OptionItem<Type>> streetTypes = ((FragActivity)getActivity()).getStreetTypes();
		if(streetTypes.size() == 0){
			List<Type> types = StreetsDataSource.get().getStreetTypes();
			for(Type t: types){
				OptionItem<Type> item = new OptionItem<Type>(t, t.getName());
				streetTypes.add(item);
			}
		}
		
		b = new StringBuilder();
		String typesString = "Все типы →";  
		for(OptionItem<Type> t: streetTypes){
			if(t.isSelected()){
				b.append(t.getName());
				b.append(", ");
			}
		}
		
		if(b.lastIndexOf(",") != -1){
			typesString = b.substring(0, b.lastIndexOf(","));
		}

		
	    findButton = (Button)getActivity().findViewById(R.id.button1);
		findButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findStreets(getAreas(), getRenames(), getRenameType(), getTypes(), getName(), isUseOldNames());
			}
		});
	    
	    areas = (TextView)getActivity().findViewById(R.id.areas);
	    areas.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AreaDialogFragment dialog = new AreaDialogFragment();
				dialog.show(getFragmentManager(), "dialog");
				
			}
		});
		areas.setText(areasStr);
	    
	    types = (TextView)getActivity().findViewById(R.id.types);
	    types.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TypesDialogFragment dialog = new TypesDialogFragment();
				dialog.show(getFragmentManager(), "type_dialog");
			}
		});
	    types.setText(typesString);
	    
	    rct = (Spinner)getActivity().findViewById(R.id.spinner1);
	    rct.setAdapter(new ArrayAdapter<RenameCountType>(getActivity(),
	    	      android.R.layout.simple_list_item_1, RenameCountType.values()));
	    
	    
	    rename = (EditText)getActivity().findViewById(R.id.renames);
	    
	}
	
	protected void findStreets(Set<Integer> areas,
			int renames, RenameCountType renType, Set<Integer> types, String name, boolean useOldNames) {
		SearchParameters params = new SearchParameters(name, useOldNames, areas, types, renType, renames);
		ExecQuery query = new ExecQuery();
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
	
	private int getRenames(){
		if (!"".equals(rename.getText().toString())){
			return Integer.valueOf(rename.getText().toString());
		} 
		return 0;
	}
	
	private RenameCountType getRenameType(){
		return (RenameCountType)rct.getSelectedItem();
	}
	
	private Set<Integer> getTypes(){
		Set<Integer> result = new HashSet<Integer>();
		for(OptionItem<Type> t: ((FragActivity)getActivity()).getStreetTypes()){
			if(t.isSelected()){
				result.add(t.getItem().getId());
			}
		}
		return result;
	}

	private String getName(){
		return ((EditText)getActivity().findViewById(R.id.editText)).getText().toString();
	}
	
	private boolean isUseOldNames(){
		CheckBox box = (CheckBox)getActivity().findViewById(R.id.fragment_queryUseOldNames);
		if(box == null){
			return false;
		}
		return box.isChecked();
	}
	
	class LoadDBTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute()
		{
			if(mCallbacks != null){
				mCallbacks.onPreExecute(R.string.db_update, R.string.please_wait, false);
			}
		}

		@Override
		protected Void doInBackground(Void... unused)
		{
			StreetsDataSource.get(getActivity());
			return null;
		}

		@Override
		protected void onPostExecute(final Void success)
		{
			if(mCallbacks != null){
				mCallbacks.onPostExecute();
			}
		}
	}
	
	class ExecQuery extends
			AsyncTask<SearchParameters, Void, Collection<Street>> {
		
		@Override
		protected Collection<Street> doInBackground(SearchParameters... params) {
			Collection<Street> res = StreetsDataSource.get().findStreets(params[0]); 
			return res;
		}
		
		@Override
		protected void onPostExecute(final Collection<Street> result) {
			mCallbacks.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			if(mCallbacks != null){
				mCallbacks.onPreExecute(R.string.exec_query, R.string.please_wait, false);
			}
		}
	}
}
