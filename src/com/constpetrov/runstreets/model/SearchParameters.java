package com.constpetrov.runstreets.model;

import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class SearchParameters implements Parcelable {
	
	private final String name;
	
	private final String oldName;
	
	private final Set<Integer> areas;
	
	private final int type;
	
	private final int renameCount;
	
	public static final Parcelable.Creator<SearchParameters> CREATOR = new Parcelable.Creator<SearchParameters>(){

		@Override
		public SearchParameters createFromParcel(Parcel p1)
		{
			return new SearchParameters(p1);
		}

		@Override
		public SearchParameters[] newArray(int p1)
		{
			return new SearchParameters[p1];
		}
	};
	
	public SearchParameters(String name, String oldName, Set<Integer> areas, int type, int renameCount){
		this.name = name;
		this.oldName = oldName;
		this.areas = areas;
		this.type = type;
		this.renameCount = renameCount;
	}
	
	public SearchParameters(Parcel in){
		name = in.readString();
		oldName = in.readString();
		areas = new HashSet<Integer>();
		//TODO: add reading array of Area
		type = in.readInt();
		renameCount = in.readInt();
	}

	public String getName() {
		return name;
	}

	public Set<Integer> getAreas() {
		return areas;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p1, int p2)
	{
		// TODO: Implement this method
	}

}
