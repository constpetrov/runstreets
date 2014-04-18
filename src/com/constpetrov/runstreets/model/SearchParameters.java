package com.constpetrov.runstreets.model;

import java.util.Set;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;

public class SearchParameters implements Parcelable {
	
	private final String name;
	
	private final boolean useOldName;
	
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
	
	public SearchParameters(String name, boolean useOldName, Set<Integer> areas, int type, int renameCount){
		this.name = name;
		this.useOldName= useOldName;
		this.areas = areas;
		this.type = type;
		this.renameCount = renameCount;
	}
	
	public SearchParameters(Parcel in){
		name = in.readString();
		useOldName = in.readInt()==1;
		areas = new HashSet<Integer>();
		int[] buffer = in.createIntArray();
		for(int area : buffer){
			areas.add(area);
		}
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
	public void writeToParcel(Parcel p, int p2)
	{
		p.writeString(name);
		p.writeInt(useOldName ? 1:0);
		int [] buffer = new int[areas.size()];
		int counter = 0;
		for(Integer area : areas){
			buffer[counter++] = area;
		}
		p.writeIntArray(buffer);
		p.writeInt(type);
		p.writeInt(renameCount);
	}

}
