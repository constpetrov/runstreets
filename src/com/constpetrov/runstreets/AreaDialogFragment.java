package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.constpetrov.runstreets.gui.ExpandableCheckboxAdapter;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.gui.UpdateGuiListener;
import com.constpetrov.runstreets.model.Area;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

public class AreaDialogFragment extends DialogFragment {

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View res = inflater.inflate(R.layout.dialog_area, container, false);
		
		ExpandableListView list = (ExpandableListView)res.findViewById(R.id.list);
		
		final ArrayList<OptionItem<Area>> groups = new ArrayList<OptionItem<Area>>();
		final List<List<OptionItem<Area>>> children = new LinkedList<List<OptionItem<Area>>>();
		
		writeGroups(((FragActivity)getActivity()).getGroups(), groups);
		writeChildren(((FragActivity)getActivity()).getChildren(), children);
		
		list.setAdapter(new ExpandableCheckboxAdapter<Area>(inflater, groups, children));
		
		Button okButton = (Button)res.findViewById(R.id.dialog_ok);
		okButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				writeGroups(groups, ((FragActivity)getActivity()).getGroups());
				writeChildren(children, ((FragActivity)getActivity()).getChildren());
				((UpdateGuiListener)getActivity()).updateGui();
				dismiss();
			}
			
		});
		
		Button cancelButton = (Button)res.findViewById(R.id.dialog_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				dismiss();
			}
			
		});
		
		return res;
	}
	
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog d =super.onCreateDialog(savedInstanceState); 
		d.setTitle(R.string.dialog_area_title);
		return d;
	}



	private <T> void writeGroups(List<OptionItem<T>> source, List<OptionItem<T>> destination){
		destination.clear();
		for(OptionItem<T> item: source){
			OptionItem<T> newItem = new OptionItem<T>(item.getItem(), item.getName());
			newItem.setSelected(item.isSelected());
			destination.add(newItem);
		}
	}
	
	private <T> void writeChildren(List<List<OptionItem<T>>> source, List<List<OptionItem<T>>> destination){
		destination.clear();
		for(List<OptionItem<T>> innerList: source){
			List<OptionItem<T>> newInnerList = new LinkedList<OptionItem<T>>();
			for(OptionItem<T> item: innerList){
				OptionItem<T> newItem = new OptionItem<T>(item.getItem(), item.getName());
				newItem.setSelected(item.isSelected());
				newInnerList.add(newItem);
			}
			destination.add(newInnerList);
		}
	}
	

}
