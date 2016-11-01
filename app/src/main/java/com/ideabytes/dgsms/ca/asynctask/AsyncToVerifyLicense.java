package com.ideabytes.dgsms.ca.asynctask;

/**
 * Created by sairam on 22/10/16.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.com.ca.dgsms.ca.model.DBConstants.SERVER_URL_VERIFY_LICENSE;

/**
 * Created by suman on 16/9/16.
 */
public class AsyncToVerifyLicense extends AsyncTask<String,Void,String> implements DBConstants {
    private final String TAG = AsyncToVerifyLicense.class.getSimpleName();
    @Override
    protected String doInBackground(String... params) {
        String result = null;
        HttpURLConnection httpURLConnection = null;
        try {
            String input = " {\"Data\":[{\"imeinumber\":"+"\""+params[0]+"\""+"}]}";
            Log.v(TAG,"verify license input "+ input);
            URL url = new URL(SERVER_URL_VERIFY_LICENSE);
            // Log.v(TAG,"verify license  url "+ SERVER_URL_VERIFY_LICENSE);
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
                 Log.v(TAG,"verify license server response "+result);
            } else {
                Log.e(TAG,"Service response for verify license  "+httpURLConnection.getResponseMessage());
            }
        } catch (final Exception e) {
            result = "Exception";
            Log.e(TAG,"Exception for verify license "+result);
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
