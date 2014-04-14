package com.constpetrov.runstreets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.AreaHistory;
import com.constpetrov.runstreets.model.AreaInfo;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetHistory;
import com.constpetrov.runstreets.model.StreetInfo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class StreetsDataSource {
	
	private static final String TABLE_AREAS = "areas";
	private static final String TABLE_STREETS = "streets";
	private static final String TAG = "StreetsDataSource";
	private static final String[] STREETS_COLUMNS = {"id", 
											"code", 
											"name", 
											"type", 
											"doc", 
											"sort", 
											"sort_second", 
											"position"};
	
	private static final String[] STREETS_SEARCH_COLUMNS = {"name_lower", 
											"sort_lower", 
											"sort_second_lower"};
	
	private StreetsDBHelper dbHelper;
	private AssetManager assets;

	public StreetsDataSource(Context context) {
		dbHelper = new StreetsDBHelper(context);
		assets = context.getAssets();
		checkAndCreate();
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
	
	public List<StreetInfo> getStreetInfos(String name){
		List<StreetInfo> res = new LinkedList<StreetInfo>();
		List<Street> streets = findStreets(name);
		for(Street street: streets){
			StreetInfo info = new StreetInfo();
			info.setStreet(street);
			info.setStreetTypeName(getTypeName(street));
			info.setAreas(getAreas(street));
			info.setHistory(getHistory(street));
			res.add(info);
		}
		return res;
	}
	
	public List<AreaInfo> getAreaInfos(String name){
		List<AreaInfo> res = new LinkedList<AreaInfo>();
		List<Area> areas = findAreas(name);
		for(Area area: areas){
			AreaInfo info = new AreaInfo();
			info.setArea(area);
			info.setAreaTypeName(getTypeName(area));
			info.setStreets(getStreets(area));
			info.setHistory(getHistory(area));
			res.add(info);
		}
		return res;
	}
	
	public List<Area> getDistricts(){
		return getAreas(3);
	}
	
	public List<Area> getAdministrativeStates(){
		return getAreas(2);
	}
	
	private List<StreetHistory> getHistory(Street street) {
		List<StreetHistory> res = new LinkedList<StreetHistory>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("street_history", null, "id_street = " + street.getId(), null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToStreetHistory(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return res;
	}

	private List<Area> getAreas(Street street) {
		List<Area> res = new LinkedList<Area>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("street_areas", null, "street = " +street.getId(), null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(getArea(c.getInt(2)));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return res;
	}
	
	private List<Area> getAreas(int type) {
		List<Area> res = new LinkedList<Area>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_AREAS, null, "type = " +type, null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToArea(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return res;
	}
	
	public List<Area> getChildAreas(Area area){
		List<Area> res = new LinkedList<Area>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_AREAS, null, "id_parent = " +area.getId(), null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToArea(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		for(Area resArea: res){
			res.addAll(getChildAreas(resArea));
		}
		return res;
	}
	
	private List<Area> getParentAreas(Area area){
		List<Area> res = new LinkedList<Area>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_AREAS, null, "id = " +area.getParentId(), null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToArea(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		for(Area resArea: res){
			res.addAll(getParentAreas(resArea));
		}
		return res;
	}

	private String getTypeName(Street street) {
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("street_types", null, "id = " + street.getType(), null, null, null, null);
			c.moveToFirst();
			return c.getString(2);
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
			c.close();
		}
		return null;
	}
	
	private List<AreaHistory> getHistory(Area area) {
		List<AreaHistory> res = new LinkedList<AreaHistory>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("area_history", null, "id_area = " + area.getId(), null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToAreaHistory(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return res;
	}

	private List<Street> getStreets(Area area) {
		List<Street> res = new LinkedList<Street>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("street_areas", null, "area = " +area.getId(), null, null, null, null);
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(getStreet(c.getInt(1)));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return res;
	}

	private String getTypeName(Area area) {
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("area_types", null, "id = " + area.getType(), null, null, null, null);
			c.moveToFirst();
			return c.getString(2);
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
			c.close();
		}
		return null;
	}

	public List<Street> findStreets(String name){
		List<Street> res = new LinkedList<Street>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_STREETS, null, "name like %?s%", new String [] {name}, null, null, "sort");
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToStreet(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
			c.close();
		}
		return res;
	}
	
	private Street getStreet(int id){
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_STREETS, null, "id = " + id, null, null, null, "sort");
			c.moveToFirst();
			return cursorToStreet(c);
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return null;
	}
	
	private List<Area> findAreas(String name){
		List<Area> res = new LinkedList<Area>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_AREAS, null, "name like %?s%", new String [] {name}, null, null, "name");
			c.moveToFirst();
			while (!c.isAfterLast()){
				res.add(cursorToArea(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
			c.close();
		}
		return res;
	}
	
	private Area getArea(int id){
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_AREAS, null, "id = "+id, null, null, null, "name");
			c.moveToFirst();
			return cursorToArea(c);
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query", e);
		} finally {
			if (c != null)
				c.close();
		}
		return null;
	}
	
	private List<String> getStreetTypes(){
		List<String> res = new LinkedList<String>();
		Cursor c = dbHelper.getReadableDatabase().query("street_types", new String[]{"name"},"is_old = 0",null,null,null,null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			res.add(c.getString(0));
			c.moveToNext();
		}
		return res;
	}
	
	private Street cursorToStreet(Cursor c) {
		Street street = new Street();
		int i = 0;
		street.setId(c.getInt(i++));
		street.setCode(c.getInt(i++));
		street.setName(c.getString(i++));
		street.setType(c.getInt(i++));
		street.setDoc(c.getString(i++));
		street.setSort(c.getString(i++));
		street.setSort_second(c.getString(i++));
		street.setPosition(c.getString(i++));
		return street;
	}
	
	private Area cursorToArea(Cursor c) {
		Area area = new Area();
		int i = 0;
		area.setId(c.getInt(i++));
		area.setCode(c.getString(i++));
		area.setParentId(c.getInt(i++));
		area.setType(c.getInt(i++));
		area.setName(c.getString(i++));
		area.setDoc(c.getString(i++));
		return area;
	}
	
	private StreetHistory cursorToStreetHistory(Cursor c){
		StreetHistory hist = new StreetHistory();
		int i = 0;
		hist.setId(c.getInt(i++));
		hist.setStreetId(c.getInt(i++));
		hist.setName(c.getString(i++));
		hist.setType(c.getInt(i++));
		hist.setDoc(c.getString(i++));
		return hist;
	}
	
	private AreaHistory cursorToAreaHistory(Cursor c){
		AreaHistory hist = new AreaHistory();
		int i = 0;
		hist.setId(c.getInt(i++));
		hist.setAreaId(c.getInt(i++));
		hist.setCode(c.getString(i++));
		hist.setName(c.getString(i++));
		hist.setType(c.getInt(i++));
		hist.setDoc(c.getString(i++));
		return hist;
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
		addSearchColumnsToStreets();
	}
	
	@SuppressLint("DefaultLocale")
	private void addSearchColumnsToStreets(){
		boolean success = true;
		for(String column: STREETS_SEARCH_COLUMNS){
			success = success && addColumn(TABLE_STREETS, column, "TEXT");
		}
		if(!success){
			return;
		}
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_STREETS, null, "*", null, null, null, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				ContentValues update = new ContentValues();
				update.put(STREETS_SEARCH_COLUMNS[0], c.getString(2).toLowerCase());
				update.put(STREETS_SEARCH_COLUMNS[1], c.getString(5).toLowerCase());
				update.put(STREETS_SEARCH_COLUMNS[2], c.getString(6).toLowerCase());
				dbHelper.getWritableDatabase().update(TABLE_STREETS, update, "id = ?s", new String[] {String.valueOf(c.getInt(0))});
			}
		} catch (SQLException ex){
			Log.e(TAG, "Cannot execute query");
		} finally {
			if (c!= null){
				c.close();
			}
		}
	}
	
	private boolean addColumn(String table, String column, String type){
		try{
			dbHelper.getWritableDatabase().execSQL(String.format("ALTER TABLE %s ADD COLUMN '%s' %s", table, column, type));
		} catch (SQLException ex){
			Log.e(TAG, "Cannot execute query");
			return false;
		}
		return true;
	}

	public List<Street> findStreets(String name, Set<Area> areas,
			List<Rename> renames, Set<Integer> types) {
		String queryHeader = "SELECT DISTINCT * FROM streets";
		String joinWithAreas = ", street_areas ON streets.id = street_areas.street";
		String where = " WHERE street_areas.area IN ";
		StringBuilder areaString = new StringBuilder("(");
		for(Area area : areas){
			areaString.append(area.getId()).append(", ");
		}
		String inString = "";
		if(areaString.length() > 1){
			inString = areaString.substring(0, areaString.lastIndexOf(","));
			inString = inString + ")";
		}
		String nameString = " AND (streets.name_lower LIKE \"" + name + "%\" OR streets.name_lower LIKE \"%" + name+ "%\")";
		String fullQuery = queryHeader + joinWithAreas
							+ where + inString + nameString;
						
		List<Street> result = new ArrayList<Street>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().rawQuery(fullQuery, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				result.add(cursorToStreet(c));
				c.moveToNext();
			}
		} catch (SQLException e){
			Log.e(TAG, "Cannot execute query");
		} finally {
			if(c != null){
				c.close();
			}
		}
		return result;
	}
}
