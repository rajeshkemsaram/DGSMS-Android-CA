package com.ideabytes.dgsms.ca.license;

import java.util.Arrays;
import java.util.HashMap;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.Seperator;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.rulesdata.AsyncGetLicenseData;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogTechError;
//import com.ideabytes.dgsms.ca.rulesdata.AsyncGetLicenseData;
import com.ideabytes.dgsms.ca.rulesdata.AsynkActivity;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.progressdialog.CustomProgressDialog;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

/**************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : WebActivity
 * author:  Suman
 * Description : This class is to register new user or verify existing user
 * (license creation pages from server will be handled here)
 * Created Date : 20-08-2015
 * Modified Date : 17-07-2016
 * Reason : Server methods changed to POST
 ***************************************************************************/
@SuppressLint("JavascriptInterface")
@SuppressWarnings("deprecation")
public class WebActivity extends Activity implements DBConstants {
    private final String LOGTAG =WebActivity.class.getSimpleName();
	private WebView webView ;
	boolean gone = false;
	private CustomProgressDialog customdialog;
	private String companyname="";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.webview_licesnse);

		//custom title bar
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.my_custom_titlebar_liense);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		getApplicationContext().deleteDatabase(DATABASE_NAME);
		Intent i = getIntent();
		//url from main activity, based on server response to check device id exist
		String url = i.getStringExtra("url");
		//System.out.println("url "+url);
		String env = i.getStringExtra("env");
		//System.out.println("action "+env);
		//get device id
		String deviceid = i.getStringExtra(DEVICE_ID);
//		Toast.makeText(getApplicationContext(),deviceid,Toast.LENGTH_SHORT).show();
		//System.out.println("deviceid "+deviceid);
		String country = i.getStringExtra("country");

		webView = (WebView)	findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
       // webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		//to speed up loading web page
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

//		if(Seperator.getInstance().changeTo()==true){
//			companyname="generic";
//		}else {
//			companyname="landstar";
//		}
		companyname=Utils.getResString( R.string.company_name);
		String custuri = url+"?imei="+deviceid+"&env="+env+"&country="+country+"&company="+companyname+"&ostype=android";
//		webView.loadUrl("http://192.168.1.49/DG_MOBI_REGISTRATION/?imei="+deviceid+"&env=dev&country=ca&company=generic&ostype=android");
//		webView.loadUrl(custuri);
		startWebView(custuri);
		Log.v(LOGTAG,"custuri "+custuri);
		//webView.loadUrl("http://www.ideabytes.com/dgforms/testpop.php");
		final Button btnBack = (Button) findViewById(R.id.btn_Back);
		btnBack.setText(Utils.getResString(R.string.back));
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backButtonListener();
			}
		});

		final Button btnExit = (Button) findViewById(R.id.btn_Exit);
		btnExit.setText(Utils.getResString(R.string.Exit));
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				quitApplication();
			}
		});

		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if(motionEvent.getAction() == MotionEvent.ACTION_UP) {

					if(gone == false){
						btnExit.setVisibility(View.GONE);
						btnBack.setVisibility(View.GONE);
						gone = true;
					}else{
						btnExit.setVisibility(View.VISIBLE);
						btnBack.setVisibility(View.VISIBLE);
						gone = false;
					}
				}
				return false;
			}
		});
		//this block is to execute alert in web page
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,JsResult result) {
				//Required functionality here
				return super.onJsAlert(view, url, message, result);
			}

		});
	}

	@Override
	public Object getInstance() {
		return null;
	}

	//Class to be injected in Web page
	public class WebAppInterface {
		Context mContext;

		/** This method is called from web page js, to listen button click on page to handle actions */
		WebAppInterface(Context c) {
			mContext = c;
		}

		@JavascriptInterface
		public void showAlert(final String status, String type, String response) {
            Log.v(LOGTAG,"license response "+response);
			try {
				String deviceId = new Utils(WebActivity.this)
						.getDeviceId(mContext);
				if(status.trim().equalsIgnoreCase("Done")) {

					JSONObject licenseData = new JSONObject(response);
					InsertDBData insertData = new InsertDBData(getApplicationContext());
					insertData.insertIntoLicenseTable(response,type);
					Log.v(LOGTAG,"Licesse reponse :" +response );
                    //call aync activity to get rules data to be loaded into the app
					if(licenseData.length() > 0) {
						boolean isRuleataPresent = new GetDBData(getApplicationContext()).getRulesDataCount();
						//System.out.println("isRuleataPresent "+isRuleataPresent);
						if(!isRuleataPresent) {
							//after verification if no rules data found then call this
							//block to get rules data from web service
							Intent intent = new Intent(WebActivity.this,
									AsynkActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						} else {
							//rules data present in the database, no need to get again after successful
							//verification, just move to main activity
							Intent intent = new Intent(WebActivity.this,
									HomeActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						}
					} else {
						//even if message is success but no license details from
						//service then simply exit from application
						//System.out.println("suma exception web activity");
						AlertDialogTechError alert = new AlertDialogTechError(WebActivity.this);
						alert.showDialog(Utils.getResString(R.string.try_later));
						return;
					}

				} else if(status.trim().equalsIgnoreCase("Ok") || status.trim().equalsIgnoreCase("Verify")) {
					//based on service response button name changes to "ok",
					// then call web activity to navigate user to verification page
					Intent intent = new Intent(WebActivity.this,
							WebActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("url", WELCOME_PAGE_URL);
					intent.putExtra("env", POINTING_TO);
					intent.putExtra("country",COUNTRY);
					intent.putExtra("action", "verification");
					intent.putExtra(DEVICE_ID, deviceId);
					startActivity(intent);
					finish();
				} else {
					quitApplication();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
	//To handle web page loading status
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			Log.v(LOGTAG,"on url "+url);
			if (!customdialog.isShowing()) {
				customdialog.show();
			}
			return false;
		}
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			//Log.e(LOGLOGTAG,"on receive Error ");
			try {
				webView.stopLoading();
			} catch (Exception e) {
			}
			try {
				webView.clearView();
			} catch (Exception e) {
			}
			//dismiss cutom dialog
			customdialog.dismiss();
			//load error html from local res folder on page not found case
			webView.loadUrl("file:///android_asset/error.html");
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			try{
					if (customdialog.isShowing() || customdialog != null) {
						customdialog.dismiss();
						customdialog = null;
					}
			} catch(Exception e){
				Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, TEXT_FILE_NAME, "Error::WebActivity::onPageFinished()" +
						Arrays.toString(e.getStackTrace()));
				e.printStackTrace();
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			//call progress bar here, this is to display page loading until it loads
			//registration or verification page
			//System.out.println("url "+url);
//			progressDialog = new ProgressDialog(WebActivity.this);
//			progressDialog.setMessage("Loading. Please wait...");
//			progressDialog.setCanceledOnTouchOutside(false);
//			progressDialog.setCancelable(false);
//			progressDialog.show();
			customdialog = new CustomProgressDialog(WebActivity.this);
			customdialog.setTitle("Loading..Please Wait...");
            customdialog.setCanceledOnTouchOutside(false);
            customdialog.setCancelable(false);
            customdialog.show();

			super.onPageStarted(view, url, favicon);
		}
	}
	//press back button to go back to next page in the web page
	@Override
	public void onBackPressed() {
		backButtonListener();
	}


	/**
	 * This method is to listen back button click on web pages if control in web pages,
	 * if not then application back listener
	 * 
	 * @author suman
	 * 
	 */

	//press back button to go back to next page in the web page
	public void backButtonListener() {
		if (webView.canGoBack()) {
			webView.clearCache(true);
			webView.goBack();
		} else {
			super.onBackPressed();
		}	
	}//backButtonListener()
	/**
	 * This method is to Quit the application
	 * 
	 * @author suman
	 * 
	 */
	private void quitApplication() {
		Intent intent = new Intent(WebActivity.this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
		finish();
	}//quitApplication()   


	private void startWebView(String url) {

		WebSettings settings = webView.getSettings();

		settings.setJavaScriptEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setWebViewClient(new MyWebViewClient());
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");
		webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		//to speed up loading web page
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

		showProgress();

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (customdialog.isShowing()) {
					customdialog.dismiss();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(WebActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

			}
		});
		webView.loadUrl(url);
	}





	private void showProgress() {
		customdialog = new CustomProgressDialog(WebActivity.this);
		customdialog.setTitle("Loading..Please Wait...");
		customdialog.setCanceledOnTouchOutside(false);
		customdialog.setCancelable(false);
		customdialog.show();
	}









}
