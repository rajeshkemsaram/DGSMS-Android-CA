package com.ideabytes.dgsms.ca.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suman on 8/9/16.
 */

public class AsyncGetDbUpdates extends AsyncTask<String,Void,String> implements DBConstants {
    private final String TAG = AsyncGetDbUpdates.class.getSimpleName();
    private ProgressDialog progressDialog;
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        progressDialog = new ProgressDialog(activity);
//        progressDialog.setMessage("Checking, Please wait...");
//        progressDialog.show();
//    }
//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        try {
//            if (progressDialog.isShowing()) {
//                progressDialog.cancel();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    protected String doInBackground(String... params) {
        String result = null;
        HttpURLConnection httpURLConnection = null;
        try {
            String input = " {\"Data\":[{\"version\":"+"\""+params[0]+"\""+"}]}";
            Log.v(TAG, " updates :  "+ input);
            URL url = new URL(SERVER_URL_GET_DB_UPDATES);
           // Log.v(TAG,"get db updates input url "+ SERVER_URL_GET_DB_UPDATES);
            //establish connection
            httpURLConnection = (HttpURLConnection)  url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            //prepare input
            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            outputStream.writeBytes(input);
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
                Log.v(TAG,"get db updates input server response "+result);
            } else {
                Log.e(TAG,"Service response for get db updates input "+httpURLConnection.getResponseMessage());
             }
        } catch (final Exception e) {
            result = "Exception";
            Log.e(TAG,"Exception for get db updates input "+result);
        } finally {
            httpURLConnection.disconnect();
        }
        return result;
    }

    @Override
    public Object getInstance() {
        return null;
    }
}
