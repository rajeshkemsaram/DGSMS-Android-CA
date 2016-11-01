package com.ideabytes.dgsms.ca.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.fcm.MyAndroidFirebaseInstanceIDService;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by suman on 27/8/16.
 */
public class AsyncTaskCheckDevice extends AsyncTask<String,Void,JSONArray> implements DBConstants {
    private final String TAG = AsyncTaskCheckDevice.class.getSimpleName();
    private Context context;
    public AsyncTaskCheckDevice(Context context) {
        this.context = context;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        Log.v("Asycheck",params.toString());
        JSONArray response = null;
     HttpURLConnection httpURLConnection    = null;
        try {
//            String fcmToken = ((MyAppData) context).getFcmToken();
            Log.i(TAG,"fcmToken "+ MyAndroidFirebaseInstanceIDService.fcmToken);
            String model        = Build.MODEL;
            Log.v("model",model);
//            String input = " {\"Data\":[{\"imeinumber\":" + "\"" + params[0] + "\"" +","+
//                    "\"device_token\":"+"\""+MyAppData.getInstance().getFcmToken()+"\""+","+"\"mobile_model\":"+"\""+model+"\""+"}]}";
            String input = " {\"Data\":[{\"imeinumber\":" + "\"" + params[0] + "\"" +","+
                    "\"device_token\":"+"\""+MyAppData.getInstance().getFcmToken()+"\""+"," +
                    "\"mobile_app_name\":"+"\""+APP_NAME+"\""+"," +
                    "\"mobile_model\":"+"\""+model+"\""+"}]}";
//            String input = " {\"Data\":[{\"imeinumber\":" + "\"" + params[0] + "\"" +","+
//                   "\"device_token\":"+"\""+MyAndroidFirebaseInstanceIDService.fcmToken+"\""+"}]}";
            Log.v(TAG,"input "+input);
            URL url = new URL(SERVER_URL_CHECK_DEVICEID);
            Log.v(TAG,"url "+SERVER_URL_CHECK_DEVICEID);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("content-type", "application/json");

            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(httpURLConnection.getOutputStream()));
            byte[] data1=String.valueOf(input).getBytes("UTF-8");
            outputStream.write(data1);
            outputStream.close();
            //get response
            StringBuilder sb = new StringBuilder();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String data;
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((data = reader.readLine()) != null) {
                    sb.append(data+"\n");
                }
                response = new JSONObject(sb.toString()).getJSONObject(RESULTS).getJSONArray("paymentStatus");
                Log.v(TAG,"device id status response  "+sb);
            } else {
                Log.e(TAG,"Service response from check device id "+httpURLConnection.getResponseMessage());
                response = new JSONArray();
            }
        } catch (Exception e) {
//            Toast.makeText(context,"Server not responding",Toast.LENGTH_SHORT).show();
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
