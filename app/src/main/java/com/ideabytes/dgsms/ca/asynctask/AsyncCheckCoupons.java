package com.ideabytes.dgsms.ca.asynctask;

import android.app.Activity;
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
 * Created by suman on 7/9/16.
 */

public class AsyncCheckCoupons extends AsyncTask<String,Void,String> implements DBConstants {
    private final String TAG = AsyncCheckCoupons.class.getSimpleName();
    private Activity activity;
    private ProgressDialog  progressDialog;
    public AsyncCheckCoupons(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
     super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Checking, Please wait...");
        progressDialog.show();
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected String doInBackground(String... params) {
        String result = null;
        HttpURLConnection httpURLConnection = null;
        try {
            String input = " {\"Data\":[{\"coupon_code\":"+"\""+params[0]+"\""+","
                    +"\"verify_status\":\""+params[1]+"\""+","+"\"imeinumber\":\""+params[2]+"\""+"}]}";
            Log.v(TAG,"coupon code input "+ input);
            URL url = new URL(SERVER_URL_VERIFY_COUPONS);
           Log.v(TAG,"coupon code url "+ SERVER_URL_VERIFY_COUPONS);
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
               // Log.v(TAG,"coupon code server response "+result);
            } else {
                Log.e(TAG,"Service response for coupon code "+httpURLConnection.getResponseMessage());
            }
        } catch (final Exception e) {
            result = "Exception";
            Log.e(TAG,"Exception for email "+result);
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
