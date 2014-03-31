package com.constpetrov.runstreets;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

public class QueryActivity extends Activity {
	
	private String [] groups = {"1","2","3","4","5","6"};
	
	private String [][] elements = {
			{"1.1","1.2","1.3"},
			{"2.1","2.2","2.3"},
			{"3.1","3.2","3.3"},
			{"4.1","4.2","4.3"},
			{"5.1","5.2","5.3"},
			{"6.1","6.2","6.3"}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		
		SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, 
				createGroupList(), R.layout.child_row, new String []{"groupName"}, new int[] {R.id.checkBox1}, 
				createChildList(), R.layout.child_row, new String []{"element"}, new int []{R.id.checkBox1});
		
		ExpandableListView list = (ExpandableListView)findViewById(R.id.expandableListView1);
		list.setAdapter(adapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

	
	private List<Map<String, String>> createGroupList(){
		List<Map<String, String>> res = new LinkedList<Map<String, String>>();
		for(String groupName: groups){
			Map<String, String> m = new HashMap<String, String>();
			m.put("groupName", groupName);
			res.add(m);
		}
		return res;
	}
	
	private List<List<Map<String, String>>> createChildList() {
	      List<List<Map<String, String>>> result = new LinkedList<List<Map<String, String>>>();
	      for( int i = 0 ; i < elements.length ; i++ ) {
	// Second-level lists
	        List<Map<String, String>> secList = new LinkedList<Map<String, String>>();
	        for( int n = 0 ; n < elements[i].length ; n++ ) {
	        	HashMap<String, String> child = new HashMap<String, String>();
	        	child.put( "element", elements[i][n] );
	            secList.add(child);  
	        }
	        result.add( secList );
	      }
	      return result;
	}
}
