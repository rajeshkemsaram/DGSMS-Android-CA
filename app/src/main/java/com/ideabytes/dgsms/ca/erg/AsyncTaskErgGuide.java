package com.ideabytes.dgsms.ca.erg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AsyncTaskErgGuide
 * author:  Suman
 * Description : This is to get ERG data from web server
 * Created Date : 02-08-2016
 ************************************************************/
public class AsyncTaskErgGuide extends
        AsyncTask<String, String, String> implements
        DBConstants {
    private String TAG = "AsyncTaskErgGuide";
    private ProgressDialog progressDialog = null;
    private Context context = null;
    private String language = null;
    private String unNumber = null;
    private String material;

    public AsyncTaskErgGuide(Context a) {
        context = a;
    }
    public static String result1,isolationresponse1,waterresponse1,language1;
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
        super.onPostExecute(result);
        if (progressDialog != null)
            progressDialog.cancel();
        try {
            //isolation table data
            AsyncTaskIsolation asyncTaskIsolation = new AsyncTaskIsolation(context);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("unNumber", unNumber);
            jsonObject.put("materialName", material);
            jsonObject.put("lang", language);

            String isolationResponse = asyncTaskIsolation.execute(jsonObject).get();

//                    //water reaction table data
                    AsyncTaskWaterReaction asyncTaskWaterReaction = new AsyncTaskWaterReaction(context);
            String waterResponse = asyncTaskWaterReaction.execute(jsonObject).get();
            Log.v("waterresponse",waterResponse.toString());
            //Note: Same input to both services , isolation and water reaction
            Intent intent = new Intent(context, FragActivity.class);
            intent.putExtra("value", result.toString());
            result1=result.toString();
            intent.putExtra("isolationResponse", isolationResponse);
            isolationresponse1=isolationResponse;
            intent.putExtra("waterResponse", waterResponse);
            waterresponse1=waterResponse;
            intent.putExtra("language",language);
            intent.putExtra("zero","0");
            language1=language;
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, "Getting ERG...",
                "Please Wait..");
    }

    @Override
    protected String doInBackground(String... params) {
        String result = null;
        String jsonResponse = params[0];
        language = params[1];
        unNumber = params[2];
        material = params[3];
        HttpURLConnection guideConnect = null;
        try {
            //Log.v(TAG,"erg response "+jsonResponse);
            JSONObject jsnobject = new JSONObject(jsonResponse);
           // Log.v(TAG, " erg jsnobject " + jsnobject);
            JSONObject results = jsnobject.getJSONObject("results");
           // Log.v(TAG," results responseLength "+results.length());
            if (results.length() > 2) {
                JSONObject data = results.getJSONObject("data");
                JSONArray jArray = data.getJSONArray("unnumberArray");
                // Log.v(TAG, "results " + results);
                // System.out.println("responseLength "+responseLength);
                for (int i = 0; i < 1; i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String guide_id = jsonObject.optString("guide_id")
                            .toString();
                    // String unnumber =
                    // jsonObject.optString("unnumber").toString();
                    // String serialId =
                    // jsonObject.optString("serialId").toString();
                    // String material =
                    // jsonObject.optString("material").toString();
                    URL guideUrl = new URL(SERVER_URL_GUIDE + guide_id + "/" + language
                            + ".json");
                    guideConnect = (HttpURLConnection) guideUrl.openConnection();
                    // Log.v(TAG, "guideUrl " + guideUrl);
                    StringBuilder sb = new StringBuilder();
                    if (guideConnect.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        //read response
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(guideConnect.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        //  System.out.println("" + sb.toString());
                        result = sb.toString();
                        JSONObject jsnobject1 = new JSONObject(result);
                        // Log.v(TAG, " guide response jsnobject1 " + jsnobject1);
                        JSONObject results1 = jsnobject1
                                .getJSONObject("results");
                        JSONObject data1 = results1.getJSONObject("data");
                        result = data1.toString();
                        Log.v(TAG,result);


                    } else {
                        Log.e(TAG, "error in erg guide " + guideConnect.getResponseMessage());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            guideConnect.disconnect();
        }
        return result;
    }

    @Override
    public Object getInstance() {
        return null;
    }
}
