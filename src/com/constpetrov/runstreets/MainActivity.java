package com.constpetrov.runstreets;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private StreetsDataSource dataSource;
	
	Button execButton;
	EditText queryText;
	ListView resultView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataSource = new StreetsDataSource(this);
		dataSource.checkAndCreate();
		
		queryText = (EditText) findViewById(R.id.editText1);
		resultView = (ListView) findViewById(R.id.listView1);
		execButton = (Button) findViewById(R.id.button1);
		execButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = queryText.getText().toString();
				if(!"".equals(query)){
					List<String> res = dataSource.execReadQuery(query);
					
					for(String str: res){
						TextView child = new TextView(getApplicationContext());
						child.setText(str);
						resultView.addView(child);
					}
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
