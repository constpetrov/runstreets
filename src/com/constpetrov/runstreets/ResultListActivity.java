package com.constpetrov.runstreets;

import java.util.List;

import com.constpetrov.runstreets.gui.OnUpdateInfosListListener;
import com.constpetrov.runstreets.model.Street;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class ResultListActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_list);
		Intent intent = getIntent();
		List<Street> streets = intent.getParcelableArrayListExtra(FragActivity.QUERY_RESULT);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.result_frag);
		try{
			OnUpdateInfosListListener listener = (OnUpdateInfosListListener)fragment;
			listener.updateList(streets);
		} catch (ClassCastException e){
			
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


}
