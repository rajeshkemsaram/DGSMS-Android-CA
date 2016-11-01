package com.ideabytes.dgsms.ca.adapters;

import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogEmergencyDetails;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/*******************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : CustomAdapterEmrCountryList
 * author:  Suman
 * Description : Customized dialog to show emergency phone numbers for countries
 ********************************************************************************/
public class CustomAdapterEmrCountryList extends BaseAdapter implements DBConstants {

	//private final String TAG = "CustomAdapterEmergencyNumbers";
	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adapterErmListActivity = null;//Activity reference
	private Dialog dialogEmergencyNumbersCountryList;
	public CustomAdapterEmrCountryList(List<String> items,
			Activity parentActivity,Dialog dialogEmergencyNumbersCountryList) {
		this.listItems = items;
		this.adapterErmListActivity = parentActivity;
		this.dialogEmergencyNumbersCountryList = dialogEmergencyNumbersCountryList;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		Button button;//button to display list items(here country names)
		
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterErmListActivity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.custom_dialog_emergency_numbers, null);
			holder = new ViewHolder();
			holder.button = (Button) convertView.findViewById(R.id.Diloag_Emrgency_Button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                dialogEmergencyNumbersCountryList.cancel();
				AlertDialogEmergencyDetails alert = new AlertDialogEmergencyDetails(adapterErmListActivity);
				alert.showdialog(listItems.get(position), new Utils().getEmergencyNumbers(listItems.get(position)));
			}
		});
		

		holder.button.setText(listItems.get(position));
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
