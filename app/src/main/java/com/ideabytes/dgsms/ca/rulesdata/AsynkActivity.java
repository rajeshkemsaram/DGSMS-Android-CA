package com.ideabytes.dgsms.ca.rulesdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskEmail;
import com.ideabytes.dgsms.ca.database.DeleteDBData;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.networkcheck.DialogCheckNetConnectivity;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.services.ServiceToPickupOrdersFromWeb;
import com.ideabytes.dgsms.ca.services.ServiceToPushOrders;
import com.ideabytes.dgsms.ca.services.ServiceToVerifyLicense;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/*************************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AsynkActivity
 * author:  Suman
 * Description : This is to display spinner on receiving rules data from web server and
 * inserting data into respective local database tables
 * Modified on : 17-07-2016
 * Reason: Service methods changed to POST
 ****************************************************************************************/
public class AsynkActivity extends Activity implements DBConstants {

	private final String TAG = "AsynkActivity";
	private ProgressBar progressBar2;//to display progress bar
	private RelativeLayout customProgressLayout;//layout to display custom progress bar
	private TextView stayTuneText;//text view to display stay tune message
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.custom_progress);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		customProgressLayout = (RelativeLayout) findViewById(R.id.customProgressLayout);
		progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);

		//custom title bar
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.my_custom_title_on_progress_bar);
		//to display loading status message on the screen with blinck animation
		stayTuneText = (TextView) findViewById(R.id.text );
		stayTuneText.setText(getResources().getString(R.string.staytunedtext));
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(1000); //You can manage the blinking time with this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		stayTuneText.startAnimation(anim);

			//call async task to load rules data from server
			try {
				//call async task to load rules data from server
				AsyncTaskGetRulesData asyncDynamicData = new AsyncTaskGetRulesData();
				asyncDynamicData.execute();
                //when type from license data is "registration" then only send email to user but not on verification
//                if (Utils.getRulesDataUrl(getApplicationContext()).optString(TYPE).equalsIgnoreCase("registration")) {
//                    //send email
//                    AsyncTaskEmail asyncTaskEmail = new AsyncTaskEmail();
//                    //  asyncTaskEmail.execute(Utils.getRulesDataUrl(getApplicationContext()).optString("userName"));
//
//                }

			}  catch (Exception e) {
				e.printStackTrace();
			}

	}

	@Override
	public Object getInstance() {
		return null;
	}

	/**
	 * @author Suman
	 * @since 5.0 
	 */
	// To get UnNumeber associated data with Un Class
	 public class AsyncTaskGetRulesData extends AsyncTask<JSONObject, String, String> implements
			DBConstants {
		private final String LOGTAG = AsyncTaskGetRulesData.class.getSimpleName();

		@Override
		protected String doInBackground(JSONObject... params) {
            String result = null;
			HttpURLConnection httpURLConnection = null;
			try {
                JSONObject jsonObject = Utils.getRulesDataUrl(getApplicationContext());
                Log.v(LOGTAG,"server url "+jsonObject.optString("serverUrl"));
                Log.v(LOGTAG,"server input "+jsonObject.toString());
				URL url = new URL(jsonObject.optString("serverUrl"));
				//connect to server
				httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setRequestMethod("POST");
				//prepare input to service
				DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
				outputStream.writeBytes(jsonObject.toString());
				outputStream.close();
				//get response from service
				StringBuilder sb = new StringBuilder();
				if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					//read response
					BufferedReader br = new BufferedReader(
							new InputStreamReader(httpURLConnection.getInputStream()));
					String line ;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					result = sb.toString();
                    //Log.v(LOGTAG,"result "+result);
                    JSONObject resultsJson = new JSONObject(result);
                    if (resultsJson.has("results")) {
                       // Log.v(LOGTAG,"result has key "+resultsJson);
                        //int status = resultsJson.getInt("status");
                        if (resultsJson.getJSONObject("results").getString("status").equalsIgnoreCase("00")) {
                            insertWeight(result);
                            insertUnClass(result);
                            insertUnNumbers(result);
                            insertErapData(result);
                            insertSp84Info(result);
                            insertClassCompatibility(result);
                            insertGroupCompatibility(result);
                            // getting populated with localization values for the first time
//                            Localization localLanguage = new Localization(getApplicationContext());
//                            localLanguage.insertLanguageContent();
                        } else {
                            result = "Exception Invalid license ";
                        }
                    } else {
                        Log.v(LOGTAG,"result has no key ");
                        result = "Exception in json, abnormal response, status is "+resultsJson.getString("status")
                        +" and message is "+resultsJson.getString("statusMessage");
                    }
                 }
			} catch (Exception e) {
                result = e.getMessage();
				e.printStackTrace();
			}
			return result;
		}

		protected void onProgressUpdate(String... progress) {
			super.onProgressUpdate();
			//progressDialog.setMessage("" + progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			//Exception
			//due to network lost or some technical issue, handle situation like below
			if (result.startsWith("Exception")) {
				Log.e(LOGTAG, "in exception " +result);
                progressBar2.setVisibility(View.GONE);
                stayTuneText.clearAnimation();
                stayTuneText.setVisibility(View.GONE);
                Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,DEBUG,result);
				//System.out.println("suma exception async activity");
//            AlertDialogTechError dialog = new AlertDialogTechError(activity);
//            dialog.showDialog("Technical Error,Please Try Again Later");
				showAlert();
				return;
			} else if (NetworkUtil.getConnectivityStatus(getApplicationContext()) == 0) {
				Log.e(LOGTAG, "no internet");
				DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(AsynkActivity.this);
				checkWifi.showDialog();
			} else {
				try {
					String clientName = new GetDBData(getApplicationContext()).getConfigData().get(COL_COMPANY_NAME);//get client from license data
					//  System.out.println("clientName "+clientName);
					// if company license then start service to get pickup orders from web
					//for trail license no need to call pick up order service becuase trial cannot have pick up orders
					// for trial license client name will be empty.
//					if (!clientName.equalsIgnoreCase("individual")) {
//						Intent serviceIntent = new Intent(AsynkActivity.this, ServiceToPickupOrdersFromWeb.class);
//						serviceIntent.putExtra(COL_COMPANY_NAME, clientName);
//						startService(serviceIntent);
//						// System.out.println("service started");
//					}
                    new InsertDBData(getApplicationContext())
                            .insertIntoTermsCheck("0", new Utils()
                                    .getDeviceId(getApplicationContext()));
//                    MyAppData.getInstance().setUserName(new GetDBData(getApplicationContext()).getConfigData().get(COL_USER_NAME));

//                    new InsertDBData(getApplicationContext()).insertPlacardingType();
					//once license details ready and rules data setup done
					//then start service which checks net connectivity and gets transactions from
					//transaction_details table to push transactions to mobile web portal
					//enable later. now no use
					startService(new Intent(AsynkActivity.this, ServiceToPushOrders.class));

					//start a count down time once rules data set up done to verify license on every verification
					//time interval(for example 3 days)
					Intent i1 = new Intent(AsynkActivity.this, ServiceToVerifyLicense.class);
					startService(i1);
					// navigate to main activity after data loading is completed
					Intent i = new Intent(AsynkActivity.this, HomeActivity.class);
					startActivity(i);
					finish();
                    //send email
                    AsyncTaskEmail asyncTaskEmail = new AsyncTaskEmail();
                    asyncTaskEmail.execute(Utils.getRulesDataUrl(getApplicationContext()).optString("userName"));

				} catch (Exception e) {
                    showAlert();
					e.printStackTrace();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			progressBar2.setVisibility(View.VISIBLE);
		}

		@Override
		public Object getInstance() {
			return null;
		}
	}
	@Override
	public void onBackPressed() {
		// this is to disable back button on loading rules data
    }
    /**
     * Insert Weight rules data from web server into weight table
     *
     * author suman
     * @param response
     */
    public void insertWeight(final String response) throws Exception {
        try {
            new InsertDBData(getApplicationContext()).insertIntoWeight(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception Weight table");
        }
    }

    /**
     * Insert UN Classes rules data from web server into un_clases table
     *
     * author suman
     * @param response
     */
    public void insertUnClass(final String response) throws Exception {
        try {
            new InsertDBData(getApplicationContext()).insertIntoUnClass(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception unClasses table");
        }
    }

    /**
     * Insert UN Numbers rules data from web server into unnumber_info table
     *
     * author suman
     * @param response
     */
    public void insertUnNumbers(final String response) throws Exception {
        try {
            new InsertDBData(getApplicationContext()).insertIntoUNNumber(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception unNumberInfo table");
        }
    }

    // insert into special_cases table
    /**
     * Insert ERAP rules data from web server into erap table
     *
     * author suman
     * @param response
     */
    public void insertErapData(final String response) throws Exception {
        try {
            //this is to display text "Almost There" message on finishing rules data set up
            setText(stayTuneText, getResources().getString(R.string.alomstThere));
            new InsertDBData(getApplicationContext()).insertIntoErapData(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception erap table");
        }
    }
    /**
     * Insert segregation rules data from web server into segregation table
     *
     * author suman
     * @param response
     */
    public void insertClassCompatibility(final String response) throws Exception {
        try {
            new InsertDBData(getApplicationContext()).insertIntoClass1Compatibility(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception class compatibility table");
        }
    }
    /**
     * Insert segregation rules data from web server into segregation table
     *
     * author suman
     * @param response
     */
    public void insertGroupCompatibility(final String response) throws Exception {
        try {
            new InsertDBData(getApplicationContext()).insertIntoGroupCompatibility(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception group compatibility table");
        }
    }
    /**
     * Insert sp84 rules data from web server into sp84 table
     *
     * author suman
     * @param response
     */
    public void insertSp84Info(final String response) throws Exception {
        try {
            setText(stayTuneText, getResources().getString(R.string.workingonit));
            new InsertDBData(getApplicationContext()).insertIntoSp84Info(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception sp84 table");
        }
    }
    /**
     * This is to display a text view on progress dialog while loading
     * rules data set up from server, on finishing this message will displayed.
     *
     * author suman
     * @param text <text view to display message>
     * @param value < message to display on progress bar>
     */
    private void setText(final TextView text,final String value) {
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    //Log.e("UI thread", "I am the UI thread "+value);
                    text.setText(value);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showAlert() {
        AlertDialog.Builder builder =  new AlertDialog.Builder(AsynkActivity.this);
        builder.setTitle("Alert Message");
        builder.setMessage("We are facing some issue in installation, try later");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //before continuing delete rules data that partially loaded
                DeleteDBData delete = new DeleteDBData(getApplicationContext());
                delete.deleteRulesData();
                finish();
            }
        });
        builder.show();
    }
}