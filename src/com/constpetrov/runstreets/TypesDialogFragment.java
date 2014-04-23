package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.List;

import com.constpetrov.runstreets.gui.CheckboxAdapter;
import com.constpetrov.runstreets.gui.OptionItem;
import com.constpetrov.runstreets.model.Type;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class TypesDialogFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View res = inflater.inflate(R.layout.dialog_type, container, false);
		
		ListView list = (ListView)res.findViewById(R.id.type_list);
		
		final ArrayList<OptionItem<Type>> groups = new ArrayList<OptionItem<Type>>();
		
		writeGroups(((FragActivity)getActivity()).getStreetTypes(), groups);
		
		list.setAdapter(new CheckboxAdapter<Type>(groups, inflater));
		
		Button okButton = (Button)res.findViewById(R.id.dialog_ok);
		okButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				writeGroups(groups, ((FragActivity)getActivity()).getStreetTypes());
				((FragActivity)getActivity()).updateGui();
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
		d.setTitle(R.string.dialog_type_title);
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
	
	

}
