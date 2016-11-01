package com.ideabytes.dgsms.ca.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suman on 7/10/16.
 */

public class AsyncGetLostDevices extends AsyncTask<String,Void,String> implements DBConstants {
    private final String TAG = AsyncGetLostDevices.class.getSimpleName();
    private Context context;
    public AsyncGetLostDevices(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection httpURLConnection = null;
        String response = "";
        try {
            // Log.v(TAG,"fcmToken "+fcmToken);
            String input = " {\"Data\":[{\"user_name\":" + "\"" + params[0] + "\"" +","+
                    "\"password\":"+"\""+params[1]+"\""+"," +
                    "\"email_id\":"+"\""+ params[2]+"\""+"}]}";
//            Log.v(TAG,"input "+input);
            // Log.v(TAG,"device model "+ Utils.getDeviceModel());
            URL url = new URL(SERVER_URL_GET_LOST_DEVICES);
           // Log.v(TAG,"url "+SERVER_URL_GET_LOST_DEVICES);
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
                Log.v(TAG,"devices list response  "+response);
            } else {
                Log.e(TAG,"Service response from devices list "+httpURLConnection.getResponseMessage());
                response = "";
            }
        } catch (Exception e) {
             Toast.makeText(context,"Server not responding", Toast.LENGTH_SHORT).show();
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
