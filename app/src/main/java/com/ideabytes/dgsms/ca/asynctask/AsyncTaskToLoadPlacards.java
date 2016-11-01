package com.ideabytes.dgsms.ca.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ideabytes.dgsms.ca.model.DataCenter;

import org.com.ca.dgsms.ca.model.PlacardDisplayLogic;

/**
 * Created by suman on 27/5/16.
 */
public class AsyncTaskToLoadPlacards extends AsyncTask<String,Void,String> {
    private final String LOGTAG = "AsyncTaskToLoadPlacards";
    private ProgressDialog progressDialog = null;//to display loading status
    private Activity activity = null;//application context
    public AsyncTaskToLoadPlacards(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(String... params) {
        String finalValue = "No Placard Added";
        try {
            String deviceId = params[0];
            PlacardDisplayLogic logic = new PlacardDisplayLogic();
            finalValue = logic.placardDisplayLogic(deviceId,deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        super.onPostExecute(result);
        Log.e(LOGTAG, "onPostExecute "+result);
        DataCenter.getInstance().setFinalValue(result);
        if (progressDialog != null)
            progressDialog.cancel();
    }

    @Override
    protected void onPreExecute() {
        //show progress dialog on connecting to server
        Log.e(LOGTAG,"onPreExecute");
        progressDialog = ProgressDialog.show(activity, "Loading placard...",
                "Please Wait..");
    }
}
