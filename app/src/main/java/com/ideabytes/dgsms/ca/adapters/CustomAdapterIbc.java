package com.ideabytes.dgsms.ca.adapters;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;
import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogIbc;
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
/******************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : CustomAdapterIbc
 * author:  Suman
 * Description : Customized dialog to show IBC status
 ******************************************************/
public class CustomAdapterIbc extends BaseAdapter implements DBConstants {

	//private final String TAG = "CustomAdapterIbc";
	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adapterIbcActivity = null;//Activity reference
	private Dialog dialogIbc;
	public CustomAdapterIbc(List<String> items,
			Activity parentActivity,Dialog dialogIbc) {
		this.listItems = items;
		this.adapterIbcActivity = parentActivity;
		this.dialogIbc =dialogIbc;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		RadioGroup radioGroup = null;//radio group of radio buttons
		RadioButton radioButton = null;//radio button to select preferred ibc status
		TextView tv1 = null;//text view to display 'ibc status'
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterIbcActivity
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
						AddPlacardDialogActivity.button_IBC.setText(listItems.get(position));
						if(listItems.get(position).equals(Utils.getResString(R.string.Dialog_Btn_Delete_Yes))) {
							//if ibc selection is "yes" and units and weight per package is between 0 to 450
							//checks for residue 
							if (AddPlacardDialogActivity.eT_NumberOfUnits.getText().toString().length() > 0 &&
									AddPlacardDialogActivity.eT_DGGrossMassPkg.getText().toString().length() > 0) {
								int	no_of_units = Integer.parseInt(AddPlacardDialogActivity.eT_NumberOfUnits
										.getText().toString());
								double dg_Weight = Double
										.parseDouble(AddPlacardDialogActivity.eT_DGGrossMassPkg.getText().toString());
								double gross_Weight = dg_Weight * no_of_units;
								new AddPlacardDialogActivity().checkResidueVisibility(gross_Weight);
								AddPlacardDialogActivity.ibc_status = "1";
							} 		
						} else {
							AddPlacardDialogActivity.ibc_status = "0";									
							new AddPlacardDialogActivity().checkResidueVisibility(0.0);
						}
						dialogIbc.cancel();

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
