package com.ideabytes.dgsms.ca.adapters;

import org.com.ca.dgsms.ca.model.AddTransaction;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.PlacardDisplayLogic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.HomeActivity;

import com.ideabytes.dgsms.ca.alertdialog.AlertDailogPlacardAbnormalCase;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogNoEnoughPlacards;
import com.ideabytes.dgsms.ca.database.UpdateDBData;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
/******************************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : CustomAdapterOnPickupOrders
 * author:  Suman
 * Description : Customized dialog to select pick up order from web
 *******************************************************************/
@SuppressLint("InflateParams")
public class CustomAdapterOnPickupOrders extends BaseAdapter implements DBConstants {
	String TAG=getClass().getSimpleName().toString();
	// private final String TAG="CustomAdapterOnPickupOrders";
	private JSONArray listItems = null;//items to be displayed in the alert dialog
	private Activity adapterPickUpOrdersActivity = null;//Activity reference
	private Dialog dialogOrders;

	public CustomAdapterOnPickupOrders(JSONArray items, Activity parentActivity,Dialog dialogOrders) {
		this.listItems = items;
		this.adapterPickUpOrdersActivity = parentActivity;
		this.dialogOrders = dialogOrders;
	}

	public void updateResults(JSONArray results) {
		listItems = results;
		if (listItems.length() == 0) {
			dialogOrders.cancel();
		}
		//Triggers the list update
		notifyDataSetChanged();
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		Button btnAdd,btnDecline;
		TextView tv1 = null;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adapterPickUpOrdersActivity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.pickupxml, null);
			holder = new ViewHolder();
			holder.btnAdd = (Button) convertView
					.findViewById(R.id.btnAdd);

			holder.btnDecline = (Button) convertView
					.findViewById(R.id.btnDecline);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.tvOrders);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					Log.v(TAG + "tick :","tick clicked");
					String deviceId = new Utils().getDeviceId(adapterPickUpOrdersActivity.getApplicationContext());
					JSONObject jsonResponse = null;//response from DG logic
					String finalvalue = null;//placarding data from DG logic

					
					JSONArray jsonArray = new JSONArray();

					jsonArray.put(listItems.getJSONObject(position));
					JSONObject jsonResult = new JSONObject();
					jsonResult.put("Data", jsonArray);
					String jsonStr = jsonResult.toString();
					jsonResponse = new JSONObject(jsonStr);
					Log.v(TAG+"jresponse :",jsonResponse.toString());
					//System.out.println("debug jsonResponse "+jsonResponse);
					AddTransaction addTransaction = new AddTransaction();
					String value = addTransaction.PrintValues(deviceId, jsonResponse);
					// System.out.println("response " + response);
					// Abnormal case
					if (value.startsWith("99")) {
						AlertDailogPlacardAbnormalCase dialogAbnormal = new AlertDailogPlacardAbnormalCase(
								adapterPickUpOrdersActivity);
						String[] reponse = value.split("::");
						String erroCode = reponse[0];
						String errorMessage = reponse[1];
						dialogAbnormal.showDialog(errorMessage, -1);
						return;
					} else {

						//once order is selected then change the status to 1,
						//then it wont  display in notification
						//for pickup orders
						new UpdateDBData(adapterPickUpOrdersActivity.getApplicationContext()).updateOrders(1, Integer.parseInt(listItems.getJSONObject(position).getString(COL_ID)));
						updateAdapter();
						Intent i22 = new Intent(adapterPickUpOrdersActivity,HomeActivity.class);
						adapterPickUpOrdersActivity.startActivity(i22);
						adapterPickUpOrdersActivity.finish();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		holder.btnDecline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Log.v(TAG + "cross :","cross clicked");
					//when they cross not add load to trans, just change the status, so that
					//it wont display in list
					new UpdateDBData(adapterPickUpOrdersActivity.getApplicationContext()).updateOrders(1, Integer.parseInt(listItems.getJSONObject(position).getString(COL_ID)));
					updateAdapter();
					// Log.v(LOGTAG,"final value placard button-------> "+finalvalue);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});



		try {
			String weightType = null;
			if(listItems.getJSONObject(position).getString(COL_WEIGHT_TYPE).equals("1")) {
				weightType = "Kgs";
			} else {
				weightType = "Lbs";
			}
			String pkg = "; PKG: "+listItems.getJSONObject(position).getString(COL_PKG_GROUP);
			if (listItems.getJSONObject(position).getString(COL_PKG_GROUP).equalsIgnoreCase("")) {
				//when there is no package group then display nothing
				pkg = "";
			}
//			String nos = listItems.getJSONObject(position).getString(COL_NOS);
//			//when nos enabled display data on banner, if not nothing to display
//			if (!nos.equalsIgnoreCase("")) {
//				nos = "NOS:" + nos;
//			}
			String nos="";
			String ibc = "";
			if (listItems.getJSONObject(position).getString(COL_IBC_STATUS).equalsIgnoreCase("0")) {
				ibc = "YES";
			} else {
				ibc = "NO";
			}
			String name = listItems.getJSONObject(position).getString(COL_NAME);
			if (listItems.getJSONObject(position).getString(COL_NAME).startsWith("7")) {
				name = "7";//added on 180516, to show 7 class in pick up orders
			}
			String displayString = "<b>"+listItems.getJSONObject(position).getString(COL_UN_NUMBER)+"</b>"
					+"; <b>"+listItems.getJSONObject(position).getString(COL_DESCRIPTION)+"</b>"
					+"; Cl: <b>" +listItems.getJSONObject(position).getString(COL_NAME)+"</b>"
					+   "<b>"+pkg+"</b>"
					+"; Units: <b>"+listItems.getJSONObject(position).getString(COL_NUMBER_OF_UNITS)+"</b>"
					+"; Dgwt: <b>"+listItems.getJSONObject(position).getString(COL_DG_WEIGHT)+"</b> "+weightType
					+"; Gross wt: <b>"+listItems.getJSONObject(position).getString(COL_GROSS_WEIGHT)+"</b> "+weightType
					+nos+ "; <b>"+ibc+"</b>"
					+"; BL: <b>"+listItems.getJSONObject(position).getString(COL_BL)+"</b>";
			holder.tv1.setText(Html.fromHtml(displayString));


//
//			holder.tv1.setText("B/L: "+listItems.getJSONObject(position).getString(COL_BL)
//					+" UN: "+listItems.getJSONObject(position).getString(COL_UN_NUMBER)
//					+" CL: "+name
//					+" DGWT: "+listItems.getJSONObject(position).getString(COL_DG_WEIGHT)
//					+" "+weightType
//					+nos+ "; <b>"+ibc+"</b>"
//					+" Units: "+listItems.getJSONObject(position).getString(COL_NO_OF_UNITS));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return listItems.length();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private void updateAdapter() {
		if (listItems.length() > 0) {
			listItems = new Utils(adapterPickUpOrdersActivity.getApplicationContext())
					.getOrdersFromDatabase();
			;
			updateResults(listItems);
		}
	}
}
