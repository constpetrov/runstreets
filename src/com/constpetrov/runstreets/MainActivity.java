package com.constpetrov.runstreets;

import java.util.List;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends ListActivity {

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
		queryText.setText("SELECT * FROM street_history WHERE id_street IN (SELECT id_street FROM street_history GROUP BY id_street HAVING COUNT(*) > 1) ORDER BY id_street");
		execButton = (Button) findViewById(R.id.button1);
		execButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = queryText.getText().toString();
				if(!"".equals(query)){
					List<String> res = dataSource.execReadQuery(query);
					
					if(res != null && res.size() != 0){
						setListAdapter(new ArrayAdapter<String>(MainActivity.this, 
								android.R.layout.simple_list_item_1, res));
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
