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
import java.net.HttpURLConnection;
import java.net.URL;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AsyncTaskWaterReaction
 * author:  Suman
 * Description : This is to get units
 * Created Date : 02-08-2016
 ************************************************************/
public class AsyncTaskWaterReaction extends
        AsyncTask<JSONObject, String, String> implements
        DBConstants {
    private String TAG = "AsyncTaskWaterReaction";
    private ProgressDialog progressDialog = null;
    private Context context = null;

    public AsyncTaskWaterReaction(Context a) {
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
        progressDialog = ProgressDialog.show(context, "Getting Water Reaction...",
                "Please Wait..");
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        String result = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(SERVER_URL_WATER_REACTION);
           // Log.v(TAG,"url water reaction "+url);
            //prepare connection
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("content-type", "application/json");
            Log.v(TAG,"input to water reaction service "+params[0].toString());
            //prepare input to service
            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            byte[] data=String.valueOf(params[0]).getBytes("UTF-8");
            outputStream.write(data);
            outputStream.close();
            //get response
            StringBuilder sb = new StringBuilder();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //read response
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                //  System.out.println("" + sb.toString());
                result = sb.toString();
                Log.v(TAG,"water reaction  response "+result);
            } else {
                Log.e(TAG, "error in water reaction guide " + httpURLConnection.getResponseMessage());
            }
            //
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

