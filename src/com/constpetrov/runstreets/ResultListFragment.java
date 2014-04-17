package com.constpetrov.runstreets;

import java.util.List;

import com.constpetrov.runstreets.gui.ExpandableResListAdapter;
import com.constpetrov.runstreets.gui.LoadInfosTask;
import com.constpetrov.runstreets.gui.OnLoadInfosListener;
import com.constpetrov.runstreets.gui.OnUpdateInfosListListener;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class ResultListFragment extends Fragment implements OnLoadInfosListener, OnUpdateInfosListListener{

	List<Street> streets;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_result, container, false);
	}

	@Override
	public void showInfos(List<List<StreetInfo>> infos) {
		((ExpandableListView)getActivity().findViewById(R.id.list))
			.setAdapter(new ExpandableResListAdapter(getActivity().getLayoutInflater(), streets, infos));
	}

	@Override
	public void updateList(List<Street> streets) {
		this.streets = streets; 
		LoadInfosTask loadTask = new LoadInfosTask(getActivity(), this);
		loadTask.execute(streets.toArray(new Street[streets.size()]));
	}
	
	

}
