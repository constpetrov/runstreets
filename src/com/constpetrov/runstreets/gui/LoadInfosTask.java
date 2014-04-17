package com.constpetrov.runstreets.gui;

import java.util.LinkedList;
import java.util.List;

import com.constpetrov.runstreets.R;
import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadInfosTask extends
		AsyncTask<Street, Integer, List<List<StreetInfo>>> {

	private ProgressDialog dialog;
	
	private Context context;
	
	private OnLoadInfosListener listener;
	
	public LoadInfosTask(Context context, OnLoadInfosListener listener){
		this.context = context;
		this.listener = listener;
		dialog = new ProgressDialog(context);
	}
	
	@Override
	protected List<List<StreetInfo>> doInBackground(Street... params) {
		int count = 0;
		dialog.setMax(params.length);
		List<List<StreetInfo>> infos = new LinkedList<List<StreetInfo>>();
		for(Street street: params){
			List<StreetInfo> infoList = new LinkedList<StreetInfo>();
			infos.add(infoList);
			infoList.add(StreetsDataSource.get().getStreetInfo(street.getId()));
			publishProgress(++count);
		}
		return infos;
	}

	@Override
	protected void onPostExecute(List<List<StreetInfo>> result) {
		dialog.dismiss();
		listener.showInfos(result);
	}

	@Override
	protected void onPreExecute() {
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle(R.string.show_results);
		dialog.setMessage(context.getString(R.string.please_wait));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		dialog.setProgress(values[0]);
	}
}
