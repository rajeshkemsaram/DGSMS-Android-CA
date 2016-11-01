package com.ideabytes.dgsms.ca.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.ideabytes.dgsms.ca.database.GetDBData;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suman on 26/9/16.
 */

public class AsyncUpgradeLicense extends AsyncTask<String,Void,String> implements DBConstants {
    private final String TAG = AsyncUpgradeLicense.class.getSimpleName();
    private Context context;
    public AsyncUpgradeLicense(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        String response = "";
        try {
            //this service takes following two inputs
            GetDBData getDBData = new GetDBData(context);
            String licenseKey = getDBData.getConfigData().get(COL_LICENSE_KEY);
           // Log.v(TAG,"upgrade license licenseKey "+licenseKey);
           // Log.v(TAG,"upgrade license validTill "+params[0]);

            String input = "{\"Data\":[{\"licenseKey\":"+"\""+licenseKey+"\""+","
                    +"\"validTill\":\""+params[0]+"\""+"}]}";
           // Log.v(TAG,"upgrade license input "+input);
            URL url = new URL(SERVER_URL_UPGRADE_LICENSE);
           // Log.v(TAG,"upgrade license url  "+SERVER_URL_UPGRADE_LICENSE);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(httpURLConnection.getOutputStream()));
            outputStream.writeBytes(input);
            outputStream.close();
            //get response
            StringBuilder sb = new StringBuilder();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String data ;
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((data = reader.readLine()) != null) {
                    sb.append(data+"\n");
                }
                response = sb.toString();
               // Log.v(TAG,"upgrade license response "+sb.toString());
            } else {
                Log.e(TAG,"Service error response from upgrade license "+httpURLConnection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return response;
    }

    @Override
    public Object getInstance() {
        return null;
    }
}