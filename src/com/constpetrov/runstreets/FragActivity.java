package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.constpetrov.runstreets.QueryFragment.TaskCallbacks;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Street;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class FragActivity extends FragmentActivity implements TaskCallbacks{

	public static final String QUERY_RESULT = "query_result";
	
	private ArrayList<OptionItem<Area>> groups = new ArrayList<OptionItem<Area>>();
	
	private List<List<OptionItem<Area>>> children = new ArrayList<List<OptionItem<Area>>>();

	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frag);
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	public ArrayList<OptionItem<Area>> getGroups() {
		return groups;
	}

	public List<List<OptionItem<Area>>> getChildren() {
		return children;
	}
	
	public void updateGui() {
		try{
			((QueryFragment)getSupportFragmentManager().findFragmentById(R.id.query_frag)).updateGui();
		} catch (ClassCastException e){
			
		}
	}

	@Override
	public void onPreExecute(int titleId, int messageId) {
		dialog = new ProgressDialog(this);
		dialog.setTitle(titleId);
		dialog.setMessage(getString(messageId));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		
	}

	@Override
	public void onProgressUpdate(int percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostExecute() {
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		updateGui();
	}
	
	@Override
	public void onPostExecute(Collection<Street> result) {
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		ArrayList<Street> streets = new ArrayList<Street>();
		streets.addAll(result);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.result_frag);
		if(fragment != null){
				((ResultListFragment)fragment).updateList(streets);
		} else {
			Intent intent = new Intent(this, ResultListActivity.class);
			intent.putParcelableArrayListExtra(QUERY_RESULT, streets);
			startActivity(intent);
		}
	}
}
