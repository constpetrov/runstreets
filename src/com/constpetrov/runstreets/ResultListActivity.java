package com.constpetrov.runstreets;

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
	private List<Street> streets;
	private List<List<StreetInfo>> result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_list);
		Intent intent = getIntent();
		if(intent != null){
			List<Street> streetsFromIntent = intent.getParcelableArrayListExtra(FragActivity.QUERY_RESULT);
			ResultListFragment fragment = (ResultListFragment)getSupportFragmentManager().findFragmentById(R.id.result_frag);
			if(streetsFromIntent != null && streetsFromIntent.size() != 0){
				result = null;
				fragment.updateList(streetsFromIntent);
			} else {
				if(result != null){
					fragment.showInfos(result);
				}
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
	public void onPreExecute() {
		dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle(R.string.show_results);
		dialog.setMessage(getString(R.string.please_wait));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		
	}

	@Override
	public void onProgressUpdate(int percent) {
		dialog.setProgress(percent);
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostExecute(List<List<StreetInfo>> result) {
		dialog.dismiss();
		ResultListFragment fragment = (ResultListFragment)getSupportFragmentManager().findFragmentById(R.id.result_frag);
		this.result= result; 
		fragment.showInfos(result);
	}


}
