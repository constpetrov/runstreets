package com.constpetrov.runstreets.model;

import java.util.StringTokenizer;

public class Area {
	private int id;
	private String code;
	private int parentId;
	private int type;
	private String name;
	private String doc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	
	@Override
	public String toString(){
		return getAbbr();
	}
	
	private String getAbbr() {
		if(getName().startsWith("Зелен")){
			return "Зеленоград";
		}
		StringTokenizer tokenizer = new StringTokenizer(getName(), " -");
		StringBuilder b = new StringBuilder();
		while (tokenizer.hasMoreTokens()){
			b.append(tokenizer.nextToken().substring(0,1).toUpperCase());
		}
		return b.toString();
	}
	
	public String getDistrictName(){
		return getName().replace("Район ", "").replace(" район", "");
	}
	
}
