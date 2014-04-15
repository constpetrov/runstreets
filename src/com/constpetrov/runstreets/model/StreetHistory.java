package com.constpetrov.runstreets.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreetHistory {
	private int id;
	private int streetId;
	private String name;
	private int type;
	private String doc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStreetId() {
		return streetId;
	}
	public void setStreetId(int streetId) {
		this.streetId = streetId;
	}
	public String getName() {
		return name.substring(0, name.lastIndexOf(" "));
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	
	public String getYear(){
		Pattern p = Pattern.compile(".*(\\d{4}).*");
		Matcher m = p.matcher(doc);
		if(m.matches()){
			return m.group(1);
		}
		return "";
	}
	

}
