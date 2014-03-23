package com.constpetrov.runstreets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StreetsDBHelper extends SQLiteOpenHelper
{
	private final static String DB_NAME = "runstreets";
	private final static int version = 1;
	public StreetsDBHelper(Context context){
		super(context, DB_NAME, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
}
