package com.constpetrov.runstreets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class StreetsDataSource {

	private final String TAG = "StreetsDataSource";
	
	private StreetsDBHelper dbHelper;
	private AssetManager assets;

	public StreetsDataSource(Context context) {
		dbHelper = new StreetsDBHelper(context);
		assets = context.getAssets();
	}
	
	public void checkAndCreate() {
		Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='streets'", null);
		if(c.getCount() != 0){
			return;
		}
		BufferedReader r = null;
		try{
			r = new BufferedReader(new InputStreamReader(assets.open("strets_info.sql")));
			String line = r.readLine();
			while(line != null){
				if(!"".equals(line)){
					try{
						dbHelper.getWritableDatabase().execSQL(line);
					} catch (SQLException e){
						Log.e(TAG, "Cannot exec SQL: " + line, e);
					}
				}
				line =r.readLine();
			}
		} catch(IOException e){
			Log.e(TAG, "Cannot read sql file", e);
		}
		finally{
			if(r != null){
				try{
				r.close();
				} catch (IOException e){
					Log.e(TAG, "Cannot close file", e);
				}
			}
		}
	}
	
	

}
