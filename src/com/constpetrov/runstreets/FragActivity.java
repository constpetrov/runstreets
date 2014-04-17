package com.constpetrov.runstreets;

import java.util.Collection;

import com.constpetrov.runstreets.gui.OnQueryListener;
import com.constpetrov.runstreets.model.Street;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class FragActivity extends FragmentActivity implements OnQueryListener{

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
		// TODO Auto-generated method stub
		
	}
}
