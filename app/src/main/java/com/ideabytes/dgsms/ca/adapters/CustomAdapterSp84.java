package com.ideabytes.dgsms.ca.adapters;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogSp84;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
/*****************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : CustomAdapterShowListBannerData
 * author:  Suman
 * Description : Customized dialog to show sp84 status
 ******************************************************/
public class CustomAdapterSp84 extends BaseAdapter implements DBConstants {

	//private final String TAG = "CustomAdapterSp84";
	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adapterSp84Activity = null;//Activity reference
	private Dialog dialogSp84;

	public CustomAdapterSp84(List<String> items, Activity parentActivity,Dialog dialogSp84) {
		this.listItems = items;
		this.adapterSp84Activity = parentActivity;
		this.dialogSp84 =dialogSp84;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		RadioGroup radioGroup = null;//radio group of radio buttons
		RadioButton radioButton = null;//radio button to select preferred sp84 status
		TextView tv1 = null;//text view to display 'sp84 status'
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterSp84Activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_dialog_single_selection, null);
			holder = new ViewHolder();
			holder.radioGroup = (RadioGroup) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Radio_Group);
			holder.radioButton = (RadioButton) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Radio_Btn);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.tv_virus_type);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.radioGroup
		.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					// User restricted to select any of Virus that are
					// in the list
					if (!listItems.get(position).equals(
							Utils.getResString(R.string.Dialog_Sp84_Title))) {
						AddPlacardDialogActivity.eT_Erap.setVisibility(View.VISIBLE);
						new AddPlacardDialogActivity().displayButtonViewChange();
						AddPlacardDialogActivity.tV_UnDesc.setText(listItems.get(position).substring(3));
						// Dismiss dialog on proper Type of Virus
						// selection
						dialogSp84.cancel();
					} else {
						AddPlacardDialogActivity.eT_Erap
						.setVisibility(View.GONE);
						new AddPlacardDialogActivity().displayButtonViewChange();
						// User selects "Select Description" display
						// toast to select any of descriptions
						Toast.makeText(
								adapterSp84Activity,
								"You Must Select Any one of Virus Type",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		holder.tv1.setText(listItems.get(position));
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
