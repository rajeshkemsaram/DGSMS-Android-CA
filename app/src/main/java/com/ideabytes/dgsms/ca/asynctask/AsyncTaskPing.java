package com.ideabytes.dgsms.ca.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;


/************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AsyncTaskPing
 * author:  Suman
 * Created Date : 18-04-2016
 * Description : Async Task to get Internet status by pinging google
 * Modified Date : 18-04-2016
 * Reason: --
 *************************************************************/
public class AsyncTaskPing extends AsyncTask<Void,Void,Boolean> {
    private final String LOGTAG = "AsyncTaskPing";
    private Context context;
    private ProgressDialog progressDialog = null;//to display loading status
    public AsyncTaskPing(Context context) {
      this.context = context;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        boolean pingStatus = false;
        try {
            if (checkNetworkOn()) {
                URL url = new URL("https://www.google.co.in/");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//                urlc.setRequestProperty("User-Agent", "Android Application:");
//                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000 * 5); // mTimeout is in seconds
                urlc.connect();
                pingStatus = true;
            }
        } catch (Exception e) {
           // Log.v(LOGTAG, " Exception ");
           return false;
            // showToast(e.getMessage());
           // e.printStackTrace();
        }
        return pingStatus;
    }
    public boolean checkNetworkOn() {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
//    protected void onPostExecute(Boolean result) {
//        // execution of result of Long time consuming operation
//        super.onPostExecute(result);
//        //Log.e(LOGTAG, "onPostExecute "+result);
//    }
//
//    @Override
//    protected void onPreExecute() {
//        //show progress dialog on connecting to server
//        //Log.e(LOGTAG,"onPreExecute");
//    }
}
