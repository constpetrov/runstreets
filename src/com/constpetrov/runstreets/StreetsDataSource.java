package com.constpetrov.runstreets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;

public class StreetsDataSource {

	private StreetsDBHelper dbHelper;
	private AssetManager assets;

	public StreetsDataSource(Context context) {
		dbHelper = new StreetsDBHelper(context);
		assets = context.getAssets();
	}
	
	private void checkAndCreate() throws IOException{
		Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT COUNT(*) as number FROM streets", null);
		if(c.getCount() != 0){
			return;
		}
		for(String name: assets.list("")){
			File sqlFile = new File(name);
			BufferedReader r = null;
			try{
				r = new BufferedReader(new FileReader(sqlFile));
				String line = r.readLine();
				while(line != null){
					dbHelper.getWritableDatabase().execSQL(line);
					line =r.readLine();
				}
			}
			finally{
				if(r != null){
					r.close();
				}
			}
		}
	}
	
	

}
