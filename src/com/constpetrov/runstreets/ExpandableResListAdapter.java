package com.constpetrov.runstreets;

import java.util.List;

import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ExpandableResListAdapter extends BaseExpandableListAdapter {

	private final List<Street> streets;
	private final List<List<StreetInfo>> infos;
	private final LayoutInflater inflater;
	
	public ExpandableResListAdapter(LayoutInflater inflater, List<Street> streets, List<List<StreetInfo>> infos){
		this.streets = streets;
		this.infos = infos;
		this.inflater = inflater;
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		if(infos == null || infos.size() < arg0){
			return null;
		}
		if(infos.get(arg0) == null || infos.get(arg0).size() < arg1){
			return null;
		}
		return infos.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(final int groupPosition,
            final int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
		View view = null;
		StreetInfo info = infos.get(groupPosition).get(childPosition);
		HistoryAdapter listAdapter = new HistoryAdapter(info.getHistory(), inflater);
		ListView renameList = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.result_subitem, null);
        } else {
            view = convertView; 
        }
        
        TextView typeView = (TextView)view.findViewById(R.id.type);
        typeView.setText(info.getStreetTypeName());
        
        renameList = (ListView)view.findViewById(R.id.renames);
        renameList.setAdapter(listAdapter);
		
        Utils.setListViewHeightBasedOnChildren(renameList);
        
        TextView areasView = (TextView)view.findViewById(R.id.areas);
        StringBuilder areaSB =new StringBuilder("Районы:\n");
        if(info.getAreas() != null)
        for(int i =0; i < info.getAreas().size(); i++){
        	areaSB.append(info.getAreas().get(i).getName());
        	if(i != info.getAreas().size() - 1){
        		areaSB.append(", ");
        	}
        }
        areasView.setText(areaSB.toString());
        return view;
	}

	@Override
	public int getChildrenCount(int arg0) {
		if (infos != null && infos.size() > arg0
                && infos.get(arg0) != null)
            return infos.get(arg0).size();

        return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		if (streets != null && streets.size() > arg0)
            return streets.get(arg0);

        return null;
	}

	@Override
	public int getGroupCount() {
		if (streets != null)
            return streets.size();

        return 0;
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.result_item, null);
            
            
        } else {
            view = convertView;         
        }
        
        TextView nameView = (TextView)view.findViewById(R.id.street_name);
        
        nameView.setText(streets.get(groupPosition).toString());
        
        return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
