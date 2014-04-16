package com.constpetrov.runstreets;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ResultListActivity extends ExpandableListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_result_list);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent =getIntent();
		List<Street> streets = intent.getParcelableArrayListExtra(QueryActivity.QUERY_RESULT);
		List<List<StreetInfo>> infos = new LinkedList<List<StreetInfo>>();
		LoadInfosTask loadTask = new LoadInfosTask();
		loadTask.execute(streets.toArray(new Street[streets.size()]));
		try{
			infos = loadTask.get();
		} catch (Exception e) {
			
		}
		setListAdapter(new ExpandableResListAdapter(getLayoutInflater(), streets, infos));
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class LoadInfosTask extends AsyncTask<Street, Integer, List<List<StreetInfo>>>{
		ProgressDialog dialog = new ProgressDialog(ResultListActivity.this, ProgressDialog.STYLE_HORIZONTAL);
		
		
		@Override
		protected List<List<StreetInfo>> doInBackground(Street... params) {
			int totalCount = params.length;
			int count = 0;
			List<List<StreetInfo>> infos = new LinkedList<List<StreetInfo>>();
			for(Street street: params){
				List<StreetInfo> infoList = new LinkedList<StreetInfo>();
				infos.add(infoList);
				infoList.add(StreetsDataSource.get().getStreetInfo(street.getId()));
				publishProgress((int) ((100.0/(double)totalCount) * (double)++count));
			}
			return infos;
		}

		@Override
		protected void onPostExecute(List<List<StreetInfo>> result) {
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			dialog.setMax(100);
			dialog.setMessage("Загрузка");
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			dialog.setProgress(values[0]);
		}
		
		
		
	}

}
