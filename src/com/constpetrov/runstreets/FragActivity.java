package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.constpetrov.runstreets.gui.OnQueryListener;
import com.constpetrov.runstreets.gui.OnUpdateInfosListListener;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.gui.UpdateGuiListener;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.Street;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class FragActivity extends FragmentActivity implements OnQueryListener, UpdateGuiListener{

	public static final String QUERY_RESULT = "query_result";
	
	private ArrayList<OptionItem<Area>> groups = new ArrayList<OptionItem<Area>>();
	
	private List<List<OptionItem<Area>>> children = new ArrayList<List<OptionItem<Area>>>();
	
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

	@Override
	public void showResults(Collection<Street> result) {
		ArrayList<Street> streets = new ArrayList<Street>();
		streets.addAll(result);
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.result_frag);
		if(fragment != null){
			try{
				OnUpdateInfosListListener listener = (OnUpdateInfosListListener)fragment;
				listener.updateList(streets);
			} catch (ClassCastException e){
				
			}
		} else {
			Intent intent = new Intent(this, ResultListActivity.class);
			intent.putParcelableArrayListExtra(QUERY_RESULT, streets);
			startActivity(intent);
		}

		
	}

	public ArrayList<OptionItem<Area>> getGroups() {
		return groups;
	}

	public List<List<OptionItem<Area>>> getChildren() {
		return children;
	}
	
	@Override
	public void updateGui() {
		try{
			((UpdateGuiListener)getSupportFragmentManager().findFragmentById(R.id.query_frag)).updateGui();
		} catch (ClassCastException e){
			
		}
	}
}
