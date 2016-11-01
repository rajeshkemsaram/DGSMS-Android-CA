package com.ideabytes.dgsms.ca.license;

import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.HomeActivity;

import com.ideabytes.dgsms.ca.progressdialog.CustomProgressDialog;
import com.ideabytes.dgsms.landstar.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
/******************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : WebActivityBuyLicesnse
 * author:  Suman
 * Description : This class is to handle buy/renew license flow from web pages
 * (license creation pages from server will be handled here)
 * Modified Date : 27-10-2015
 *******************************************************************************/
@SuppressLint("JavascriptInterface")
@SuppressWarnings("deprecation")
public class WebActivityBuyLicesnse extends Activity implements DBConstants {
	private String url ;
	private String deviceid;
	private WebView webView ;
	private CustomProgressDialog customdialog;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.webview_licesnse);

		//custom title bar
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.my_custom_title_bar_payment);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 

		Intent i = getIntent();
		//url from main activity, based on server response to check device id exist
		url = i.getStringExtra("url");
		//System.out.println("url "+url);
		//get device id
		deviceid = i.getStringExtra("deviceid");
		//System.out.println("deviceid "+deviceid);
		String country = i.getStringExtra("country");
		String env = i.getStringExtra("env");

		webView = (WebView)	findViewById(R.id.webview);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.addJavascriptInterface(new WebAppInterface(this), "Android");
		webView.setWebChromeClient(new WebChromeClient());
		//loads web page url to buy/renew license
		webView.loadUrl(url+"?imei="+deviceid+"&env="+env+"&country="+country);



		webView.clearCache(true);
        final Button btnBack = (Button) findViewById(R.id.btn_Back);
        btnBack.setText("Back");
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                backButtonListener();
            }
        });

        final Button btnExit = (Button) findViewById(R.id.btn_Exit);
        btnExit.setText("Exit");
        btnExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                quitApplication();
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

	//To handle web page loading status
	private class MyWebViewClient extends WebViewClient {	
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			try{
				if(customdialog.isShowing() || customdialog != null) {
					customdialog.dismiss();
					customdialog = null;
				}

			}catch(Exception exception){
				exception.printStackTrace();
			}			
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			//call progress bar here, this is to display page loading until it loads
			//registration or verification page
			customdialog = new CustomProgressDialog(WebActivityBuyLicesnse.this);
			customdialog.setTitle("Loading..Please Wait...");
			customdialog.setCanceledOnTouchOutside(false);
			customdialog.setCancelable(false);
			customdialog.show();
			super.onPageStarted(view, url, favicon);
		}
	}
	//Class to be injected in Web page
	public class WebAppInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		WebAppInterface(Context c) {
			mContext = c;
		}

		/**
		 * Show Toast Message
		 * @param status
		 */
		@JavascriptInterface
		public void paymentCompleted(String status) {
			//System.out.println("status "+status);
			//Toast.makeText(mContext, status, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(WebActivityBuyLicesnse.this, HomeActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();

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
        Intent intent = new Intent(WebActivityBuyLicesnse.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }//quitApplication()
}
