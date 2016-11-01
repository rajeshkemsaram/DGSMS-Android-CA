package com.ideabytes.dgsms.ca.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;


import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.utils.Utils;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suman on 27/8/16.
 */
public class AsyncTaskSendPayStatus extends AsyncTask<String,Void,Void> implements DBConstants {
    private final String TAG = AsyncTaskSendPayStatus.class.getSimpleName();
    private Context context;
    public AsyncTaskSendPayStatus(Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        try {
//            String fcmToken = ((MyAppData) context).getFcmToken();
           String fcmToken=MyAppData.getInstance().getFcmToken();
            // Log.v(TAG,"fcmToken " +fcmToken);
            String deviceId = new Utils().getDeviceId(context);
            String inserted_date_time = Utils.getPresentDateTime();
            //Log.v(TAG,"deviceId "+deviceId);
            String model        = Build.MODEL;
            Log.v("model",model);
            //rajesh
            String input = "{\"Data\":[{\"imeinumber\":"+"\""+deviceId+"\""+","
                    +"\"payment_status\":\""+params[0]+"\""+","
                    +"\"coupon_code\":\""+ params[1]+"\""+","
                    +"\"device_token\":\""+ fcmToken+"\""+","+
                    "\"mobile_app_name\":"+"\""+APP_NAME+"\""+","
                    +"\"inserted_date_time\":\""+ inserted_date_time+"\""
                    +"," +
                    "\"mobile_model\":"+"\""+model+"\""+"}]}";
            Log.v(TAG,"input "+input);
            URL url = new URL(SERVER_URL_SEND_PAY_STATUS);
           // Log.v(TAG,"url  "+SERVER_URL_SEND_PAY_STATUS);
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
            } else {
                Log.e(TAG,"Service response from send pay status "+httpURLConnection.getResponseMessage());
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