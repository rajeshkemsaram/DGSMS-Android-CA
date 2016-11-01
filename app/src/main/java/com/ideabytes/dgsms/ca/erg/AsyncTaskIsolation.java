package com.ideabytes.dgsms.ca.erg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AsyncTaskErgGuide
 * author:  Suman
 * Description : This is to get units
 * Created Date : 02-08-2016
 ************************************************************/
public class AsyncTaskIsolation extends
        AsyncTask<JSONObject, String, String> implements
        DBConstants {
    private String TAG = "AsyncTaskIsolation";
    private ProgressDialog progressDialog = null;
    private Context context = null;
    private String language = null;

    public AsyncTaskIsolation(Context a) {
        context = a;
    }

    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        super.onPostExecute(result);
        if (progressDialog != null)
            progressDialog.cancel();
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, "Getting Isolation...",
                "Please Wait..");
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        String result = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(SERVER_URL_ISOLATION);
            Log.v(TAG,"url isolation "+url);
            //prepare connection

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("content-type", "application/json");
//            httpURLConnection.setRequestProperty("Content-Type","application/json");
             Log.v(TAG,"input to isolation service "+params[0].toString());
            //prepare input to service

            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            byte[] data=String.valueOf (params[0]).getBytes("UTF-8");
            outputStream.write(data);
            Log.v(TAG,"input "+params[0].toString());
            outputStream.close();
            //get response
            StringBuffer  sb = new StringBuffer();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //read response
                String line;
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                //  System.out.println("" + sb.toString());
                result = sb.toString();
                Log.v(TAG,"isolation response "+result);
                Log.v(TAG,"input1324 "+params[0].toString());
            } else {
                Log.e(TAG, "error in isolation guide " + httpURLConnection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
