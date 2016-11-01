package com.ideabytes.dgsms.ca.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.logs.Logger;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.utils.Utils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/********************************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : ServiceToPickupOrdersFromWeb
 * author:  Suman
 * Description : This service checks for orders local database table, if orders exist displays
 * notification for the order
 * Modified Date : 07-12-2015
 * Reason : To update client name (compnay name) to service link
 **********************************************************************************************/

public class ServiceToPickupOrdersFromWeb extends Service implements DBConstants {
	private final String TAG = "WatchDogPickupOrdersFromWeb";
	private Timer mTimer ;
	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }

	@Override
	public Object getInstance() {
		return null;
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		try {
            Log.v(TAG,"get orders from web");
			mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new GetPuckUpOrders(), 10*1000, TIME_INTERVAL_TO_PUSH_TRANS);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return START_STICKY;
    }

    class GetPuckUpOrders extends TimerTask {
        @Override
        public void run() {
			try {
				//if network is available then get transaction from transaction_details table to
				//push transactions to mobile web portal
				if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
					// get pick up orders from web in time interval and insert
					// data into local table
					getOrdersFromWeb();
				}
				GetDBData get = new GetDBData(getApplicationContext());
				// if pick up orders data available with local table then call
				// service to show notification on pick up orders
				if (get.getPickupOrders().length() > 0) {
					// start picup orders local service to show notification to
					// select
					// pick up orders from web
					startService(new Intent(ServiceToPickupOrdersFromWeb.this,
							ServiceToPickupOrdersFromLocal.class));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }



	private ArrayList<HashMap<String, String>> getOrdersFromWeb() {
		String result = null;
		ArrayList<HashMap<String, String>> listResponse =  new ArrayList<HashMap<String, String>>();
		HttpURLConnection httpURLConnection = null;
		try {
			HashMap<String, String> values = new HashMap<String, String>();
			String userName = new GetDBData(getApplicationContext()).getConfigData().get(COL_USER_NAME);
			// Log.v(TAG,"pick up orders license input user name "+userName);
			String input = " {\"Data\":[{\"user_name\":"+"\""+userName+"\""+"}]}";
			// Log.v(TAG,"pick up orders  input data "+input);
			URL url = new URL(SERVER_URL_PICKUP_ORDERS);
			// Log.v(TAG,"url "+SERVER_URL_PICKUP_ORDERS);
			//establish connection with server
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			//prepare input
			DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outputStream.writeBytes(input);
			outputStream.close();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//read response
				StringBuilder builder = new StringBuilder();
                String data;
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				while ((data = reader.readLine()) != null) {
					builder.append(data+"\n");
				}
                reader.close();
				result = builder.toString();
				JSONObject jsonObject1 = new JSONObject(result);
				JSONObject results = jsonObject1.getJSONObject("results");
				//Logger.logVerbose(TAG,"getOrdersFromWeb", "results "+results);
				JSONArray jArray = results.getJSONArray("result");
					if (jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jsonObject = jArray.getJSONObject(i);
							String dg_weight = jsonObject.optString("dg_weight");
							values.put(COL_DG_WEIGHT, dg_weight);
							String un_class_id = jsonObject
									.optString("un_class_id").toString();
							values.put(COL_UN_CLASS_ID, un_class_id);
							String un_number = jsonObject.optString("un_number")
									.toString();
							values.put(COL_UN_NUMBER, un_number);
							String pkg_group = jsonObject.optString("pkg_group")
									.toString();
							values.put(COL_PKG_GROUP, pkg_group);
							String weight_type = jsonObject
									.optString("weight_type").toString();
							values.put(COL_WEIGHT_TYPE, weight_type);
							String ibc_residue_status = jsonObject.optString(
									"ibc_residue_status").toString();
							values.put(COL_IBC_RESIDUE_STATUS, ibc_residue_status);
							String weight_in_kgs = jsonObject.optString(
									"weight_in_kgs").toString();
							values.put(COL_WEIGHT_IN_KGS, weight_in_kgs);
							String dangerous_placard_original = jsonObject
									.optString("dangerous_placard_original")
									.toString();
							values.put(COL_DANGEROUS_PLACARD,
									dangerous_placard_original);
							String unnumber_display = jsonObject.optString(
									"unnumber_display").toString();
							values.put(COL_UNNUMBER_DISPLAY, unnumber_display);
							String description = jsonObject
									.optString("description").toString();
							values.put(COL_DESCRIPTION, description);
							String bl = jsonObject.optString("bl").toString();
							values.put(COL_BL, bl);
							String subsidary_exist = jsonObject.optString(
									"subsidary_exist").toString();
							values.put(COL_SUBSIDARY_EXIST, subsidary_exist);
							String number_of_units = jsonObject.optString(
									"number_of_units").toString();
							values.put(COL_NO_OF_UNITS, number_of_units);
							String erap_index = jsonObject.optString("erap_index")
									.toString();
							values.put(COL_ERAP_INDEX, erap_index);
							String ibc_status = jsonObject.optString("ibc_status")
									.toString();
							values.put(COL_IBC_STATUS, ibc_status);
							String group_name = jsonObject.optString("group_name")
									.toString();
							values.put(COL_GROUP_NAME, group_name);
							String untype = jsonObject.optString("untype")
									.toString();
							values.put(COL_UNTYPE, untype);
							String weight_index = jsonObject.optString(
									"weight_index").toString();
							values.put(COL_WEIGHT_INDEX, weight_index);
							String max_placard = jsonObject.optString("max_placar")
									.toString();
							values.put(COL_MAX_PLACARD, max_placard);
							String primary_placard = jsonObject.optString(
									"primary_placard").toString();
							values.put(COL_PRIMARY_PLACARD, primary_placard);
							String gross_weight = jsonObject.optString(
									"gross_weight").toString();
							values.put(COL_GROSS_WEIGHT, gross_weight);
							String secondary_placard = jsonObject.optString(
									"secondary_placard").toString();
							values.put(COL_SECONDARY_PLACARD, secondary_placard);
							String package_weight = jsonObject.optString(
									"package_weight").toString();
							values.put(COL_PACKAGE_WEIGHT, package_weight);
							String erap_status = jsonObject
									.optString("erap_status").toString();
							values.put(COL_ERAP_STATUS, erap_status);
							String erap_no = jsonObject.optString("erap_no")
									.toString();
							values.put(COL_ERAP_NO, erap_no);
							String inserted_date_time = jsonObject.optString(
									"inserted_date_time").toString();
							values.put(COL_INSERTED_DATE_TIME, inserted_date_time);
							String nameInTransaction = jsonObject.optString("name")
									.toString();
							values.put(COL_NAME, nameInTransaction);
							values.put(COL_USER_ID_WEB,
									jsonObject.optString(COL_USER_ID_WEB)
											.toString());
							values.put(COL_TRANSACTION_ID_WEB, jsonObject
									.optString(COL_TRANSACTION_ID_WEB).toString());
							values.put(COL_TRANSACTION_ID, new GetDBData(getApplicationContext())
									.getConfigData().get(COL_IMEI));
							values.put(COL_USER_ID, new GetDBData(getApplicationContext())
									.getConfigData().get(COL_IMEI));
							//newly added keys 07-12-2015
							String un_style = jsonObject.optString(COL_UN_SYLE)
									.toString();
							String special_provision = jsonObject.optString(COL_SPECIAL_PROVISION)
									.toString();
							String non_excepmt = jsonObject.optString(COL_NONEXCEMPT)
									.toString();
							String consignee_danger = jsonObject.optString(COL_CONSIGNEE_DANGER)
									.toString();
							String optimize = jsonObject.optString(COL_OPTIMISE)
									.toString();

							//newly added keys 07-12-2015
							values.put(COL_UN_SYLE, un_style);
							values.put(COL_OPTIMISE, optimize);
							values.put(COL_SPECIAL_PROVISION, special_provision);
							values.put(COL_NONEXCEMPT, non_excepmt);
							values.put(COL_CONSIGNEE_DANGER, consignee_danger);
//							new InsertDBData(getApplicationContext()).insertIntoOrders(values, 0);
							new InsertDBData(getApplicationContext()).insertIntoOrders(jArray, 0);
							//Logger.logVerbose(LOGTAG,"getOrdersFromWeb", "debug pick up orders " + values);
						}
						listResponse.add(values);
					}
				} else {
				Log.e(TAG,"Service response "+httpURLConnection.getResponseMessage());
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return listResponse;
	}
}
