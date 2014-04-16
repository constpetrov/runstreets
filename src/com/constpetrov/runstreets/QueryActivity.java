package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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

	private ArrayList<OptionItem<Area>> groups;
	
	private List<List<OptionItem<Area>>> children;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		LoadDBTask t = new LoadDBTask();
		t.execute(this);
		
		
	}

	private void initUI() {
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

	    ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView1);
	    elv.setAdapter(new ExpandableCheckboxAdapter<Area>(getLayoutInflater(), groups, children));
	    
	    final Button findButton = (Button)findViewById(R.id.button1);
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
		ExecQuery query = new ExecQuery();
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
		return ((EditText)findViewById(R.id.editText)).getText().toString();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}
	
	class LoadDBTask extends AsyncTask<Context, Void, Void> {
		
		private ProgressDialog dialog = new ProgressDialog(QueryActivity.this);
		@Override
		protected void onPreExecute()
		{
			dialog.setTitle(R.string.db_update);
			dialog.setMessage(QueryActivity.this.getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Context... context)
		{
			StreetsDataSource.get(context[0]);
			return null;
		}

		@Override
		protected void onPostExecute(final Void success)
		{
			if(dialog.isShowing()){
				dialog.dismiss();
			}
			initUI();
		}
	}
	
	class ExecQuery extends AsyncTask<SearchParameters, Void, Collection<Street>> {
		private ProgressDialog pd = new ProgressDialog(QueryActivity.this);
		
		@Override
		protected Collection<Street> doInBackground(SearchParameters... params) {
			Collection<Street> res = StreetsDataSource.get().findStreets(params[0].getName(), params[0].getAreas(), null, null); 
			return res;
		}

		@Override
		protected void onPostExecute(final Collection<Street> result) {
			pd.dismiss();
			Toast.makeText(QueryActivity.this, "Streets found: "+ result.size(), Toast.LENGTH_SHORT).show();
			Intent showResults = new Intent(QueryActivity.this, ResultListActivity.class);
			showResults.putParcelableArrayListExtra(QUERY_RESULT, (ArrayList<Street>)result);
			QueryActivity.this.startActivity(showResults);
		}

		@Override
		protected void onPreExecute() {
			pd.setTitle(R.string.exec_query);
			pd.setMessage(QueryActivity.this.getString(R.string.please_wait));
			pd.setCanceledOnTouchOutside(false);
			pd.setCancelable(false);
			pd.show();
		}
	}

	
	
}
