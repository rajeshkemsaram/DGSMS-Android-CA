package com.ideabytes.dgsms.ca.adapters;

import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ideabytes.dgsms.landstar.R;

/**************************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : CustomAdapterSpecialCase
 * author:  Suman
 * Description : Customized dialog to show transaction history
 ***************************************************************/
public class CustomAdapterTransHistory extends BaseAdapter implements DBConstants {
	
	private final String TAG = "CustomAdapterTransHistory";
	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adapterHistoryActivity;//Activity reference
	public CustomAdapterTransHistory(List<String> items,
			Activity parent) {
		this.listItems = items;
		this.adapterHistoryActivity = parent;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView tv1 = null;//text view to display 'transaction history'
		TextView tv2 = null;//text view to display 'transaction history'
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterHistoryActivity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.custom_dialog_trans_history, null);
			holder = new ViewHolder();
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.tv_history_data);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//        String text1 = listItems.get(position).substring(0, listItems.get(position).indexOf("Units"));
//        Log.v(TAG,"text1 "+text1);
//        String text2 = text1.substring(text1.indexOf("DGWt"), text1.length());
//        Log.v(TAG,"text2 "+text2);
		holder.tv1.setText(listItems.get(position));
       // holder.tv2.setText(text2);
		return convertView;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listItems.indexOf(getItem(position));
	}
}
