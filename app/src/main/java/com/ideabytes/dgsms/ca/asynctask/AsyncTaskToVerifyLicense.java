package com.ideabytes.dgsms.ca.asynctask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialog;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogLicenseExpaired;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.logs.Logger;
import com.ideabytes.dgsms.ca.progressdialog.CustomProgressDialog;
import com.ideabytes.dgsms.ca.services.ServiceToPickupOrdersFromWeb;
import com.ideabytes.dgsms.ca.utils.Utils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AsyncGetLicenseData
 * author:  Suman
 * Description : This is to get license data from web server
 * Modified Date : 17-07-2016
 * Reason : Server methods changed to POST
 ************************************************************/

public class AsyncTaskToVerifyLicense extends AsyncTask<String, String, HashMap<String, String>>
implements DBConstants {
	private static final String TAG = "AsyncTaskToVerifyLicense";
	private Activity parent;//Activity reference
    private int code ;//based code value, can be decided to verify license or upgrade,verify = 0,upgrade = 1
    private CustomProgressDialog customdialog;
	public AsyncTaskToVerifyLicense(Activity parent,int code) {
		this.parent = parent;
        this.code = code;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected HashMap<String, String> doInBackground(String... params) {
        String result = null;
        String userName = new GetDBData(parent.getApplicationContext()).getConfigData().get(COL_USER_NAME);
        HashMap<String, String> licenseData =null;
        HttpURLConnection httpURLConnection = null;
        try {
            String input = " {\"Data\":[{\"user_name\":"+"\""+userName+"\""+"}]}";
            URL url = new URL(SERVER_URL_VERIFY_LICENSE);
          //     Log.v(LOGTAG,"verify license input data "+input);
          //  Log.v(LOGTAG,"verify license input user name "+new GetDBData(parent.getApplicationContext()).getConfigData());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(httpURLConnection.getOutputStream()));
            outputStream.writeBytes(input);
            outputStream.close();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                String data;
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((data = reader.readLine()) != null) {
                   builder.append(data+"\n");
                }
                result = builder.toString();
                Log.v(TAG,"response "+result);
                JSONObject jsnobject1 = new JSONObject(result);
                //System.out.println("result "+result);
                JSONObject jsnobject2 = jsnobject1.getJSONObject("results");
                // System.out.println("jsnobject2 "+jsnobject2);
                JSONArray jArray = jsnobject2.getJSONArray("licenseData");
                licenseData = new HashMap<String, String>(jArray.length());
                String license = jsnobject2.optString("license")
                        .toString();
                licenseData.put("license", license);
                //System.out.println("jArray "+jArray);
                for (int i = 0; i < jArray.length(); i++) {
                    final JSONObject jsonObject = jArray.getJSONObject(i);
                    String valid_from = jsonObject
                            .optString(COL_VALID_FROM).toString().trim();
                    licenseData.put(COL_VALID_FROM, valid_from);
                    String valid_till = jsonObject
                            .optString(COL_VALID_TILL).toString().trim();
                    licenseData.put(COL_VALID_TILL, valid_till);
                    String license_session = jsonObject
                            .optString(COL_LICENSE_SESSION).toString()
                            .trim();
                    licenseData.put(COL_LICENSE_SESSION, license_session);
                    String license_key = jsonObject
                            .optString(COL_LICENSE_KEY).toString().trim();
                    licenseData.put(COL_LICENSE_KEY, license_key);
                    String serverUrl = jsonObject.optString("url").toString()
                            .trim();
                    licenseData.put("url", serverUrl);
                    String max_Transaction = jsonObject
                            .optString(COL_MAX_TRANSACTIONS).toString().trim();
                    licenseData.put(COL_MAX_TRANSACTIONS, max_Transaction);
                    String companyName = jsonObject.optString(COL_COMPANY_NAME)
                            .toString();
                    licenseData.put(COL_COMPANY_NAME, companyName);
                    String user_name = jsonObject.optString(COL_USER_NAME)
                            .toString();
                    licenseData.put(COL_USER_NAME, user_name);

                    licenseData.put("deviceId", new Utils().getDeviceId(parent.getApplicationContext()));
                    licenseData.put(COL_MAX_TRANSACTIONS, max_Transaction);

                    //System.out.println(" licenseData on verification " + licenseData);
                    //update latest license details into application database table license table
                    new UpdateDBData(parent.getApplicationContext()).updateVerifyLicense(licenseData);

                }
            } else {
                Log.e(TAG,"error in verify license "+httpURLConnection.getResponseMessage());
            }

		} catch (Exception e) {
            e.printStackTrace();
		} finally {
            httpURLConnection.disconnect();
        }
        return licenseData;
	}
    @Override
    protected void onPreExecute() {
        customdialog = new CustomProgressDialog(parent);
        customdialog.setTitle("Verifying..Please Wait...");
        customdialog.setCanceledOnTouchOutside(false);
        customdialog.setCancelable(false);
        customdialog.show();
    }

    protected void onPostExecute(HashMap<String, String> result) {
        // execution of result of Long time consuming operation
        super.onPostExecute(result);
        if(customdialog.isShowing() || customdialog != null) {
            customdialog.dismiss();
            customdialog = null;
        }
        GetDBData getDetails = new GetDBData(parent);
        int localTranCount = new GetDBData(parent)
                .getTrasactionCount().get("transcount");
        String max_Transaction = getDetails.getConfigData().get(COL_MAX_TRANSACTIONS);
        int maxTrans = Integer.parseInt(max_Transaction);
        int total = localTranCount + maxTrans;
        new UpdateDBData(parent.getApplicationContext()).updateMaxTransactions(total);
        if(result.size() > 1) {
            if (code == 0) {
                Toast.makeText(parent, "Your License has been Verified", Toast.LENGTH_LONG).show();
                new UpdateDBData(parent.getApplicationContext()).updateVerifyLicense("0");
                //to dismiss dialog and refresh with banner data
                Intent intent = new Intent(parent, HomeActivity.class);
                parent.startActivity(intent);
                parent.finish();
            } else {
                AlertDialog alertDialog = new AlertDialog(parent);
                alertDialog.showDialg("Your license Upgraded Successfully \nValid From:" + result.get(COL_VALID_FROM) + "\n" +
                        "To:" + result.get(COL_VALID_TILL));
                String clientName = new GetDBData(parent.getApplicationContext()).getConfigData().get(COL_COMPANY_NAME);//get client from license data
                Logger.logVerbose(TAG,"upgrade license","clientName "+clientName);
                // if company license then start service to get pickup orders from web
                //for trail license no need to call pick up order service becuase trial cannot have pick up orders
                // for trial license client name will be empty.
                if (!clientName.equalsIgnoreCase("individual")) {
                    Intent serviceIntent = new Intent(parent, ServiceToPickupOrdersFromWeb.class);
                    parent.startService(serviceIntent);
                    // System.out.println("service started");
                }
            }
        } else {
            AlertDialogLicenseExpaired dialogLicenseExpaired = new AlertDialogLicenseExpaired(parent);
            dialogLicenseExpaired.showdialog(result.get("license"));
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }
}