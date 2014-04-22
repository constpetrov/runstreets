package com.constpetrov.runstreets;

import java.util.LinkedList;
import java.util.List;

import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.gui.ExpandableResListAdapter;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class ResultListFragment extends Fragment {

	List<Street> streets;
	
	public static interface TaskCallbacks {
	    void onPreExecute(int titleId, int messageId, boolean withProgress);
	    void onProgressUpdate(int percent);
	    void onCancelled();
	    void onPostExecute(List<StreetInfo> result);
	}
	
	TaskCallbacks mCallbacks;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (TaskCallbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_result, container, false);
	}

	public void showInfos(List<StreetInfo> infos) {
		((ExpandableListView)getActivity().findViewById(R.id.list))
			.setAdapter(new ExpandableResListAdapter(getActivity().getLayoutInflater(), streets, infos));
	}

	public void updateList(List<Street> streets) {
		this.streets = streets; 
		LoadInfosTask loadTask = new LoadInfosTask();
		loadTask.execute(streets.toArray(new Street[streets.size()]));
	}
	
	class LoadInfosTask extends
		AsyncTask<Street, Integer, List<StreetInfo>> {
	
		@Override
		protected List<StreetInfo> doInBackground(Street... params) {
			int count = 0;
			List<StreetInfo> infos = new LinkedList<StreetInfo>();
			for(Street street: params){
				infos.add(StreetsDataSource.get().getStreetInfo(street.getId()));
				publishProgress((int)((double)++count * 100.0 / (double)params.length));
			}
			return infos;
		}
		
		@Override
		protected void onPostExecute(List<StreetInfo> result) {
			if (mCallbacks != null) {
		        mCallbacks.onPostExecute(result);
		    }
		}
		
		@Override
		protected void onPreExecute() {
			if (mCallbacks != null) {
		        mCallbacks.onPreExecute(R.string.show_results, R.string.please_wait, true);
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			if (mCallbacks != null) {
		        mCallbacks.onProgressUpdate(values[0]);
		    }
		}
	}


}
