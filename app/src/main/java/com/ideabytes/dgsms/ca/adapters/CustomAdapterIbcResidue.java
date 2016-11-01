package com.ideabytes.dgsms.ca.adapters;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;
import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogResidue;
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
 *Name : CustomAdapterIbcResidue
 * author:  Suman
 * Description : Customized dialog to show residue status
 *********************************************************/
public class CustomAdapterIbcResidue extends BaseAdapter implements DBConstants {
	
	//private final String TAG = "CustomAdapterIbcResidue";
	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adapterResidueActivity = null;//Activity reference
	private Dialog dialogRecidue;
	public CustomAdapterIbcResidue(List<String> items,
								   Activity parentActivity,Dialog dialogRecidue) {
		this.listItems = items;
		this.adapterResidueActivity = parentActivity;
		this.dialogRecidue =dialogRecidue;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		RadioGroup radioGroup = null;//radio group of radio buttons
		RadioButton radioButton = null;//radio button to select preferred residue status
		TextView tv1 = null;//text view to display 'residue status'
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterResidueActivity
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
						AddPlacardDialogActivity.button_Residue.setText(listItems.get(position));
						if(AddPlacardDialogActivity.button_IBC.getVisibility() == View.VISIBLE &&
								AddPlacardDialogActivity.ibc_status.
							equals("1")) {
							AddPlacardDialogActivity.ibc_residue_status = "1";
						} else {
							AddPlacardDialogActivity.ibc_residue_status = "0";
						}
						dialogRecidue.cancel();

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
