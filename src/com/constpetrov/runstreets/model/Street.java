package com.constpetrov.runstreets.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Street implements Parcelable, Comparable<Street>{
	private int id;
	private int code;
	private String name;
	private int type;
	private String doc;
	private String sort;
	private String sort_second;
	private String position;
	
	public final static Parcelable.Creator<Street> CREATOR = new Parcelable.Creator<Street>(){

		@Override
		public Street createFromParcel(Parcel source) {
			return new Street(source);
		}

		@Override
		public Street[] newArray(int size) {
			return new Street[size];
		}}; 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
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
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSort_second() {
		return sort_second;
	}
	public void setSort_second(String sort_second) {
		this.sort_second = sort_second;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Override
	public String toString(){	
		return name;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Street other = (Street) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(code);
		dest.writeString(name);
		dest.writeInt(type);
		dest.writeString(doc);
		dest.writeString(sort);
		dest.writeString(sort_second);
		dest.writeString(position);
	}
	
	public Street(Parcel in){
		id = in.readInt();
		code = in.readInt();
		name = in.readString();
		type = in.readInt();
		doc = in.readString();
		sort = in.readString();
		sort_second = in.readString();
		position = in.readString();
	}
	public Street() {
		
	}
	@Override
	public int compareTo(Street another) {
		if(this.getSort() != null && another.getSort() != null){
			if(this.getSort().compareTo(another.getSort()) == 0){
				if(this.getSort_second() != null && another.getSort_second() != null){
					return this.getSort_second().compareTo(another.getSort_second());
				} else return 0;
			} else return this.getSort().compareTo(another.getSort());
		}
		return this.getName().compareTo(another.getName());
	}

}
