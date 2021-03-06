package com.constpetrov.runstreets.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.constpetrov.runstreets.gui.Utils;
import com.constpetrov.runstreets.model.Area;
import com.constpetrov.runstreets.model.AreaHistory;
import com.constpetrov.runstreets.model.AreaInfo;
import com.constpetrov.runstreets.model.Rename;
import com.constpetrov.runstreets.model.RenameCountType;
import com.constpetrov.runstreets.model.SearchParameters;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetHistory;
import com.constpetrov.runstreets.model.StreetInfo;
import com.constpetrov.runstreets.model.Type;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Path.FillType;
import android.util.Log;

public class StreetsDataSource {
	
	private static final String COOL_QUERY = "SELECT * FROM {0} WHERE s_id IN (SELECT id_street FROM street_history GROUP BY id_street HAVING COUNT(*) {1})";
	
	private static final String TABLE_AREAS = "areas";
	private static final String TABLE_STREETS = "streets";
	private static final String TABLE_STREET_HISTORY = "street_history";
	private static final String TAG = "StreetsDataSource";
	@SuppressWarnings("unused")
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
	
	private static final String[] STREET_HISTORY_SEARCH_COLUMNS = {"name_lower"};
	
	private StreetsDBHelper dbHelper;
	private AssetManager assets;
	
	private static StreetsDataSource instance;
	
	public static synchronized StreetsDataSource get(Context... contexts){
		if(instance == null){
			if(contexts.length < 1){
				throw new IllegalArgumentException();
			}
			instance = new StreetsDataSource(contexts[0]);
		}
		return instance;
	}

	private StreetsDataSource(Context context) {
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
	
	public StreetInfo getStreetInfo(int id){
		Street street = getStreet(id);
		StreetInfo info = new StreetInfo();
		info.setStreet(street);
		info.setStreetTypeName(getTypeName(street));
		info.setAreas(getAreas(street));
		info.setHistory(getHistory(street));
		return info;
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
	
	public List<StreetHistory> getHistory(Street street) {
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
		return getChildAreas(area.getId());
	}
	
	public List<Area> getChildAreas(Integer area){
		List<Area> res = new LinkedList<Area>();
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_AREAS, null, "id_parent = " +area, null, null, null, null);
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
			res.addAll(getChildAreas(resArea.getId()));
		}
		return res;
	}
	
	@SuppressWarnings("unused")
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

	public String getTypeName(Street street) {
		return getStreetTypeName(street.getType());
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
	
	public List<Type> getStreetTypes(){
		List<Type> res = new LinkedList<Type>();
		Cursor c = dbHelper.getReadableDatabase().query("street_types", new String[]{"id", "name"},null,null,null,null,null);
		c.moveToFirst();
		while(!c.isAfterLast()){
			res.add(cursorToType(c));
			c.moveToNext();
		}
		return res;
	}
	
	private Type cursorToType(Cursor c){
		Type res = new Type();
		res.setId(c.getInt(0));
		res.setName(c.getString(1));
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
			r = new BufferedReader(new InputStreamReader(assets.open("streets_info.sql")));
			String line = r.readLine();
			while(line != null){
				if(!"".equals(line) && !" ".equals(line)){
					try{
						dbHelper.getWritableDatabase().execSQL(line);
					} catch (Exception e){
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
		addSearchColumnsToStreetHistory();
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
			c = dbHelper.getReadableDatabase().query(TABLE_STREETS, null, null, null, null, null, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				ContentValues update = new ContentValues();
				update.put(STREETS_SEARCH_COLUMNS[0], c.getString(2).toLowerCase());
				if(c.getString(5) != null){
					update.put(STREETS_SEARCH_COLUMNS[1], c.getString(5).toLowerCase());
				}
				if(c.getString(6) != null){
					update.put(STREETS_SEARCH_COLUMNS[2], c.getString(6).toLowerCase());
				}
				dbHelper.getWritableDatabase().update(TABLE_STREETS, update, "id = ?", new String[] {String.valueOf(c.getInt(0))});
				c.moveToNext();
			}
		} catch (SQLException ex){
			Log.e(TAG, "Cannot execute query");
		} finally {
			if (c!= null){
				c.close();
			}
		}
	}
	
	@SuppressLint("DefaultLocale")
	private void addSearchColumnsToStreetHistory(){
		boolean success = true;
		for(String column: STREET_HISTORY_SEARCH_COLUMNS){
			success = success && addColumn(TABLE_STREET_HISTORY, column, "TEXT");
		}
		if(!success){
			return;
		}
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query(TABLE_STREET_HISTORY, null, null, null, null, null, null);
			c.moveToFirst();
			while(!c.isAfterLast()){
				ContentValues update = new ContentValues();
				update.put(STREET_HISTORY_SEARCH_COLUMNS[0], c.getString(2).toLowerCase());
				dbHelper.getWritableDatabase().update(TABLE_STREET_HISTORY, update, "id = ?", new String[] {String.valueOf(c.getInt(0))});
				c.moveToNext();
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

	public Collection<Street> findStreets(SearchParameters params){
		String selectByName = "SELECT *, streets.id s_id, streets.type s_type, streets.type h_type FROM streets "
				+ "WHERE streets.name_lower LIKE \"{0}%\" "
				+ "OR streets.name_lower LIKE \"%{0}%\"";
		String selectByOldName = "SELECT *, streets.id s_id, streets.type s_type, street_history.type h_type FROM streets, street_history ON streets.id = street_history.id_street "
				+ "WHERE streets.name_lower LIKE \"{0}%\" "
				+ "OR streets.name_lower LIKE \"%{0}%\" "
				+ "OR street_history.name_lower LIKE \"{0}%\" "
				+ "OR street_history.name_lower LIKE \"%{0}%\"";
		String selectByRenames = "SELECT * FROM ({0}) WHERE s_id IN (SELECT id_street FROM street_history GROUP BY id_street HAVING COUNT(*) {1})";
		String selectByArea = "SELECT DISTINCT * FROM ({0}) a, street_areas ON a.id = street_areas.street WHERE street_areas.area IN ({1})";
		String selectByType = "SELECT * FROM ({0}) a WHERE a.s_type IN ({1}) OR a.h_type IN ({1})";
		
		String[] fullQuery= new String[] {"", ""};
		
		fullQuery[0] = Utils.replace(selectByName, params.getName().toLowerCase());

		if(params.isUseOldName()){
			fullQuery[1] = Utils.replace(selectByOldName, params.getName().toLowerCase());
		} 
		if(params.getRenameCountType() != RenameCountType.ANY)
		for(int i = 0; i < fullQuery.length; i++){
			if(!"".equals(fullQuery[i])){
				fullQuery[i] = Utils.replace(selectByRenames, fullQuery[i], constructRename(params.getRenameCountType(), params.getRenameCount()));
			}
		}
		
		if (params.getAreas().size()!=0){
			StringBuilder areaString = new StringBuilder();
			Set<Integer> areas = new HashSet<Integer>();
			for(Integer areaId : params.getAreas()){
				areas.add(areaId);
				for(Area area: getChildAreas(areaId)){
					areas.add(area.getId());
				}
			}
			for(Integer area : areas){
				areaString.append(area).append(", ");
			}
			String inString = areaString.substring(0, areaString.lastIndexOf(","));
			for(int i = 0; i < fullQuery.length; i++){
				if(!"".equals(fullQuery[i])){
					fullQuery[i] = Utils.replace(selectByArea, fullQuery[i], inString);
				}
			}
		}
		
		if (params.getTypes().size()!= 0){
			StringBuilder typeString = new StringBuilder();
			for(Integer type : params.getTypes()){
				typeString.append(type).append(", ");
			}
			String inString = typeString.substring(0, typeString.lastIndexOf(","));
			
			for(int i = 0; i < fullQuery.length; i++){
				if(!"".equals(fullQuery[i])){
					fullQuery[i] = Utils.replace(selectByType, fullQuery[i], inString);
				}
			}
		}
		
		Set<Street> result = new TreeSet<Street>();
		for(String query : fullQuery){
			Cursor c = null;
			try{
				c = dbHelper.getReadableDatabase().rawQuery(query, null);
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
		}
		return result;
	}

	private String constructRename(RenameCountType renameCountType,
			int renameCount) {
		String rename = "";
		switch (renameCountType) {
			case MORE: {rename = "> "; break;}
			case LESS: {rename = "< "; break;}
			case EQUALS: {rename = "= "; break;}
		default:
			break;
		}
		rename = rename + renameCount;
		return rename;
	}

	public String getStreetTypeName(int type) {
		Cursor c = null;
		try{
			c = dbHelper.getReadableDatabase().query("street_types", null, "id = " + type, null, null, null, null);
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

}
