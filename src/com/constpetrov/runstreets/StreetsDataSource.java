package com.constpetrov.runstreets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

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
	
	public List<String> execReadQuery(String query){
		List<String> result = new LinkedList<String>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().rawQuery(query, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				StringBuilder b = new StringBuilder();
				for (int i = 0; i < c.getColumnCount(); i++){
					switch (c.getType(i)){
						case Cursor.FIELD_TYPE_STRING:{
							b.append(c.getString(i));
							break;
						}
						case Cursor.FIELD_TYPE_INTEGER:{
							b.append(c.getInt(i));
							break;
						}
					}
					if(i != c.getColumnCount()-1){
						b.append(",");
					}
				}
				result.add(b.toString());
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
			c.close();
		}
		return result;
	}
	
	public List<StreetInfo> findStreet(String name){
		List<StreetInfo> res = new LinkedList<StreetInfo>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("streets", null, "name like %?s%", new String [] {name}, null, null, "sort");
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToStreetInfo(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
			c.close();
		}
		updateRenameHistory(res);
		return res;
	}
	
	private void updateRenameHistory(List<StreetInfo> res) {
		// TODO Auto-generated method stub
		
	}

	private StreetInfo cursorToStreetInfo(Cursor c) {
		StreetInfo res = new StreetInfo();
		Street street = new Street();
		
		res.setStreet(street);
		return res;
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
