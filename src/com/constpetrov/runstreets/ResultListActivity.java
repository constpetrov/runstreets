package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.List;

import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class ResultListActivity extends FragmentActivity implements ResultListFragment.TaskCallbacks{
	ProgressDialog dialog;
	private ArrayList<Street> streets;
	private ArrayList<StreetInfo> result;
	
	public final static String KEY_STREETS = "streets";
	public final static String KEY_RESULT = "result";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_list);
		if(savedInstanceState != null){
			streets = savedInstanceState.getParcelableArrayList(KEY_STREETS);
			result = savedInstanceState.getParcelableArrayList(KEY_RESULT);
		}
		
		Intent intent = getIntent();
		if(intent != null){
			List<Street> streetsFromIntent = intent.getParcelableArrayListExtra(FragActivity.QUERY_RESULT);
			ResultListFragment fragment = (ResultListFragment)getSupportFragmentManager().findFragmentById(R.id.result_frag);
			if(streets != null && result != null && streetsFromIntent.equals(streets)){
				fragment.showInfos(result);
			} else {
				result = null;
				streets = new ArrayList<Street>();
				streets.addAll(streetsFromIntent);
				fragment.updateList(streetsFromIntent);
			}
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putParcelableArrayList(KEY_STREETS, streets);
		outState.putParcelableArrayList(KEY_RESULT, result);
	}

	@Override
	public void onPreExecute(int titleId, int messageId, boolean withProgress) {
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
	public void onPostExecute(List<StreetInfo> result) {
		if(dialog != null){
			dialog.dismiss();
		}
		ResultListFragment fragment = (ResultListFragment)getSupportFragmentManager().findFragmentById(R.id.result_frag);
		this.result = new ArrayList<StreetInfo>();
		this.result.addAll(result); 
		fragment.showInfos(result);
	}


}
