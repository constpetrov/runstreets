package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;

public class QueryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		
		ArrayList<OptionItem> groups = new ArrayList<OptionItem>();
	    List<List<OptionItem>> children = new ArrayList<List<OptionItem>>();

	    for (int i = 0; i < 10; i++) {
	    	OptionItem groupName = new OptionItem("", "Group " + i);
	        groups.add(groupName);
	        List<OptionItem> temp = new ArrayList<OptionItem>();
	        for (int j = 0; j < 5; j++) {
	            temp.add(new OptionItem(groupName.getChildName(), "Child " + j));
	        }
	        children.add(temp);
	    }

	    children.get(0).get(3).setSelected(true);
	    children.get(5).get(2).setSelected(true);
	    children.get(8).get(1).setSelected(true);
	    children.get(3).get(4).setSelected(true);

	    ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView1);
	    elv.setAdapter(new ExpandableAdapter(getLayoutInflater(), groups, children));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

}
