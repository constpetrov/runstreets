package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.Street;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.os.*;
import android.app.*;



public class QueryActivity extends Activity {
	
	private final String TAG = "QueryActivity";
	
	public static final String QUERY_RESULT = "queryResult";

	private StreetsDataSource dataSource;
	
	private ArrayList<OptionItem<Area>> groups;
	
	private List<List<OptionItem<Area>>> children;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		LoadDBTask t = new LoadDBTask();
		t.execute(this);
		try{
			dataSource = t.get();
		} catch (Exception e) {
			Log.e(TAG, "Cannot update db", e);
		}
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
	    elv.setAdapter(new ExpandableCheckboxAdapter<Area>(getLayoutInflater(), groups, children));
	    
	    final Button findButton = (Button)findViewById(R.id.button1);
	    findButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Street> streets = findStreets(getAreas(), getRenames(), getTypes(), getName());
				Toast.makeText(QueryActivity.this, "Streets found: "+ streets.size(), Toast.LENGTH_SHORT).show();
				Intent showResults = new Intent(QueryActivity.this, ResultListActivity.class);
				showResults.putParcelableArrayListExtra(QUERY_RESULT, (ArrayList<Street>)streets);
				QueryActivity.this.startActivity(showResults);
			}

			
		});
		
	}
	
	protected List<Street> findStreets(Set<Area> areas,
			List<Rename> renames, Set<Integer> types, String name) {
		List<Street> result = new ArrayList<Street>();
		SearchParameters params = new SearchParameters(name, areas);
		ExecQuery query = new ExecQuery();
		query.execute(params);
		try{
			result.addAll(query.get());
		} catch (Exception e){
			Log.e(TAG, "Cannot execute query task", e);
		}
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
	
	class LoadDBTask extends AsyncTask<Context, Void, StreetsDataSource> {
		
		private ProgressDialog dialog = new ProgressDialog(QueryActivity.this);
		@Override
		protected void onPreExecute()
		{
			dialog.setTitle(R.string.db_update);
			dialog.setMessage("Подождите…");
			dialog.show();
		}

		@Override
		protected StreetsDataSource doInBackground(Context... context)
		{
			return StreetsDataSource.get(context[0]);
		}

		@Override
		protected void onPostExecute(final StreetsDataSource success)
		{
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
	}
	
	class ExecQuery extends AsyncTask<SearchParameters, Void, Collection<Street>> {
		private ProgressDialog pd;
		
		@Override
		protected Collection<Street> doInBackground(SearchParameters... params) {
			Collection<Street> res = dataSource.findStreets(params[0].getName(), params[0].getAreas(), null, null); 
			return res;
		}

		@Override
		protected void onPostExecute(final Collection<Street> result) {
			pd.dismiss();
		}

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(QueryActivity.this, "", "Поиск", false);
		}
		
		
	}

	
	
}
