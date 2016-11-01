package com.ideabytes.dgsms.ca.asynctask;


import org.apache.http.params.HttpParams;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**********************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AsyncTaskPushTransactions
 * author:  Suman
 * Description : This class is to push local mobile transactions to web
 * Modified Date : 18-04-2016
 * Reason: checking server response and after succesful inserting push order then changing status to 1
 ************************************************************************/
public class AsyncTaskPushTransactions extends AsyncTask<JSONObject, Void, Void> implements DBConstants {
	private final String TAG = "AsyncTaskPushTransactions";
    private Context context;//application context
    public AsyncTaskPushTransactions(Context context) {
        this.context = context;
    }

	@Override
	protected Void doInBackground(JSONObject... params) {
		String result = null;
		HttpURLConnection httpURLConnection = null;
		try {
//			String user_name = new GetDBData(context).getConfigData().get(COL_USER_NAME);
			String user_name= MyAppData.getInstance().getUserName();
			//System.out.println("user_name "+user_name);
			params[0].put("user_name", user_name);
			Log.v(TAG,"input " +result);
			URL url = new URL(SERVER_URL_PUSH_TRANSACTIONS+".json");
			//establish connection
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");
			//prepare input
			DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
			outputStream.writeBytes(params[0].toString());
			outputStream.close();
			//get response
			StringBuilder sb = new StringBuilder();
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				//read response
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line ;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				//  System.out.println("" + sb.toString());
				result = sb.toString();
				Log.v(TAG,result);

				JSONObject jsonObject = new JSONObject(result);
				Log.v(TAG,jsonObject.toString());
				if (jsonObject.getJSONObject("results").getString("status").
						equalsIgnoreCase("00")) {

					UpdateDBData updateDBData = new UpdateDBData(context);

					updateDBData.updatePushTransStatus("1","0");

					updateDBData.updatePlacardContentPushStatus("1","0");

				} else {
					Log.e("push : ",httpURLConnection.getResponseMessage());
				}
			} else {
				Log.e("push Trams : ",httpURLConnection.getResponseMessage());
				Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, httpURLConnection.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
		}
        return null;
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
