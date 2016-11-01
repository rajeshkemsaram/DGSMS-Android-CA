package com.ideabytes.dgsms.ca.adapters;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;
import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogPackage;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
/*********************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : CustomAdapterPackageGroup
 * author:  Suman
 * Description : Customized dialog to show package group
 ********************************************************/
public class CustomAdapterPackageGroup extends BaseAdapter implements DBConstants {	

	//private final String TAG = "CustomAdapterPackageGroup";
	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adapterPckGrpActivity = null;//Activity reference
	private Dialog dialogPackage;
	public CustomAdapterPackageGroup(List<String> items, Activity parentActivity,Dialog dialogPackage) {
		this.listItems = items;
		this.adapterPckGrpActivity = parentActivity;
		this.dialogPackage =dialogPackage;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		RadioGroup radioGroup = null;//radio group of radio buttons
		RadioButton radioButton = null;//radio button to select preferred package group
		TextView tv1 = null;//text view to display 'package group'
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterPckGrpActivity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.custom_dialog_single_selection_center_text, null);
			holder = new ViewHolder();
			holder.radioGroup = (RadioGroup) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Radio_Group);
			holder.radioButton = (RadioButton) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Radio_Btn);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.radioGroup
		.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					// User restricted to select any of pkg group that are
					// in the list
					if (!listItems.get(position).equals(
							Utils.getResString(R.string.Dialog_Package_Group))) {
						new AddPlacardDialogActivity().enableErap(listItems.get(position));
						AddPlacardDialogActivity.pkg_group = listItems
								.get(position);
						if ((AddPlacardDialogActivity.eT_Erap
								.isShown())) {
							// call to display Display placard button
							// when ERAp field enabled
							new AddPlacardDialogActivity().displayButtonViewChange();
						}
						//dismiss dialog
						dialogPackage.cancel();
					} else {
						AddPlacardDialogActivity.pkg_group = "";
								AddPlacardDialogActivity.eT_Erap
						.setVisibility(View.GONE);
						new AddPlacardDialogActivity().displayButtonViewChange();
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
