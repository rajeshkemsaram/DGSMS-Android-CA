package com.ideabytes.dgsms.ca.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suman on 19/9/16.
 */
public class AsyncGetTerms extends AsyncTask<String,Void,String> implements DBConstants {
    private final String TAG = AsyncGetTerms.class.getSimpleName();
    private Activity activity;
    private ProgressDialog progressDialog;
    public AsyncGetTerms(Activity activity) {
        this.activity = activity;
    }
    // @Override
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

            URL url = new URL(SERVER_URL_GET_TERMS);
           // Log.v(TAG,"get terms url "+ SERVER_URL_GET_TERMS);
            //establish connection
            httpURLConnection = (HttpURLConnection)  url.openConnection();
            httpURLConnection.setRequestMethod("GET");

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
              //  Log.v(TAG,"get terms server response "+result);
            } else {
                Log.e(TAG,"Service response for get terms  "+httpURLConnection.getResponseMessage());
            }
        } catch (final Exception e) {
            result = "Exception";
            Log.e(TAG,"Exception for send terms check  "+result);
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
