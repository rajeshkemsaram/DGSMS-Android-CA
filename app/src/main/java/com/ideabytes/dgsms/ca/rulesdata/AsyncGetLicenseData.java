package com.ideabytes.dgsms.ca.rulesdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AsyncGetLicenseData
 * author:  Suman
 * Description : This is to get license data from web server
 * Modified Date : 27-10-2015
 ************************************************************/

public class AsyncGetLicenseData extends AsyncTask<String, String, JSONObject>
implements DBConstants {
	private static final String TAG = "AsyncGetLicenseData";
	private Activity parent;//Activity reference
	private String type="";

	public AsyncGetLicenseData(Activity parent,String type) {
		this.parent = parent;
		this.type=type;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected JSONObject doInBackground(String... params) {
		String result = null;
        JSONObject licenseData = null;
		HttpURLConnection httpURLConnection = null;
		try {
//			Log.v(TAG,"license data response url "+ params[0]);
			URL url = new URL(params[0]);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			StringBuffer sb = new StringBuffer();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				String data;
				while ((data = reader.readLine()) != null) {
					sb.append(data+"\n");
				}
				reader.close();
				result = sb.toString();
                licenseData = new JSONObject(result);
                 Log.v(TAG,"license data "+licenseData);
				if (result != null) {
						//store license details into license table
						InsertDBData insertData = new InsertDBData(parent.getApplicationContext());


							insertData.insertIntoLicenseTable(result,type);


					}
			} else {
				Log.e(TAG,"Service response for license data "+httpURLConnection.getResponseMessage());
				Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, httpURLConnection.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
		}

		return licenseData;
	}

	@Override
	public Object getInstance() {
		return null;
	}
}