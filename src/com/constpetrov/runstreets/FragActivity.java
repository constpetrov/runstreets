package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.constpetrov.runstreets.QueryFragment.TaskCallbacks;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.RenameCountType;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;
import com.constpetrov.runstreets.model.Type;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class FragActivity extends FragmentActivity implements TaskCallbacks, ResultListFragment.TaskCallbacks{

	public static final String QUERY_RESULT = "query_result";
	
	private ArrayList<OptionItem<Area>> groups = new ArrayList<OptionItem<Area>>();
	
	private List<List<OptionItem<Area>>> children = new ArrayList<List<OptionItem<Area>>>();
	
	private ArrayList<OptionItem<Type>> streetTypes = new ArrayList<OptionItem<Type>>();
	
	private RenameCountType renameCountType = RenameCountType.ANY;
	
	private int renameCount;

	ProgressDialog dialog;
	
	private ArrayList<Street> streets;
	private ArrayList<StreetInfo> result;
	
	public final static String KEY_STREETS = "streets";
	public final static String KEY_RESULT = "result";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frag);
		updateGui();
		if(savedInstanceState != null){
			streets = savedInstanceState.getParcelableArrayList(KEY_STREETS);
			result = savedInstanceState.getParcelableArrayList(KEY_RESULT);
			if(result != null){
				Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.result_frag);
				if(fragment != null){
						((ResultListFragment)fragment).showInfos(result);
				}
			}
		}
	}
	
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(KEY_STREETS, streets);
		outState.putParcelableArrayList(KEY_RESULT, result);
	}


	public ArrayList<OptionItem<Area>> getGroups() {
		return groups;
	}

	public List<List<OptionItem<Area>>> getChildren() {
		return children;
	}
	
	public ArrayList<OptionItem<Type>> getStreetTypes() {
		return streetTypes;
	}
	




	public void updateGui() {
		try{
			((QueryFragment)getSupportFragmentManager().findFragmentById(R.id.query_frag)).updateGui();
		} catch (ClassCastException e){
			
		}
	}

	@Override
	public void onPreExecute(int titleId, int messageId, boolean withProgress) {
		dialog = new ProgressDialog(this);
		if(withProgress){
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}
		dialog.setTitle(titleId);
		dialog.setMessage(getString(messageId));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		
	}

	@Override
	public void onProgressUpdate(int percent) {
		if(dialog != null){
			dialog.setProgress(percent);
		}
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostExecute() {
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		updateGui();
	}
	
	@Override
	public void onPostExecute(Collection<Street> result) {
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		this.streets = new ArrayList<Street>();
		this.streets.addAll(result);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.result_frag);
		if(fragment != null){
				((ResultListFragment)fragment).updateList(streets);
		} else {
			Intent intent = new Intent(this, ResultListActivity.class);
			intent.putParcelableArrayListExtra(QUERY_RESULT, streets);
			startActivity(intent);
		}
	}


	@Override
	public void onPostExecute(List<StreetInfo> result) {
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
		}
		this.result = new ArrayList<StreetInfo>();
		this.result.addAll(result);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.result_frag);
		if(fragment != null){
				((ResultListFragment)fragment).showInfos(result);
		}		
	}
}
