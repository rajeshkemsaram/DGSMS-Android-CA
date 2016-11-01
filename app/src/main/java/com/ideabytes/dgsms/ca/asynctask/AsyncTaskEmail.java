package com.ideabytes.dgsms.ca.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suman on 14/7/16.
 */
public class AsyncTaskEmail extends AsyncTask<String,Void,Void> implements DBConstants {
    private final String TAG = AsyncTaskEmail.class.getSimpleName();
    @Override
    protected Void doInBackground(String... params) {
        String result ;
        String input = " {\"Data\":[{\"user_name\":"+"\""+params[0]+"\""+"}]}";
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(SERVER_URL_MAIL);
           // Log.v(TAG,"url "+SERVER_URL_MAIL);
           // Log.v(TAG,"input  "+input);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(httpURLConnection.getOutputStream()));
            outputStream.writeBytes(input);
            outputStream.close();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                String data ;
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((data = reader.readLine()) != null) {
                    builder.append(data+"\n");
                }
                result = builder.toString();
              //  Log.v(LOCAL,"response "+result);
            } else {
                Log.e(TAG," error in sending email "+httpURLConnection.getResponseMessage());
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
