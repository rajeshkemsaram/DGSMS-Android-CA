package com.ideabytes.dgsms.ca.networkcheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.HomeActivity;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/***************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : DialogCheckNetConnectivity
 * author:  Suman
 * Description : This class is to show dialog to check network connectivity
 * Modified Date : 29-10-2015
 ****************************************************************************/
public class DialogCheckNetConnectivity extends Dialog implements DBConstants {
	/**
	 * This dialog appears on No Network case
	 * @author suman
	 */
	private Activity dialogNetActivity;
	public static Button btnExit;

	public DialogCheckNetConnectivity(Activity dialogWifiActivity) {
		super(dialogWifiActivity);
		this.dialogNetActivity = dialogWifiActivity;
	}

	public void showDialog() {
		try {
			final Dialog dialogNoNetwork = new Dialog(dialogNetActivity, R.style.PauseDialog);
			dialogNoNetwork.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogNoNetwork.setContentView(R.layout.custom_dialog_add_network);

			TextView tvTitle = (TextView) dialogNoNetwork.findViewById(R.id.Dialog_Title);
			tvTitle.setText(Utils.getResString(R.string.connect_network));

			//			TextView tvMessage = (TextView) dialogNoNetwork.findViewById(R.id.Dialog_Two_Message);
			//			tvMessage.setText("Please connect to Working Internet");

			btnExit = (Button) dialogNoNetwork.findViewById(R.id.btnOk);
			btnExit.setText(Utils.getResString(R.string.Dialog_Btn_Exit));

			btnExit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(btnExit.getText().toString().equalsIgnoreCase("Exit")) {
						((Activity) dialogNetActivity).finish();
					} else {
						Intent i = new Intent(dialogNetActivity,HomeActivity.class);
						dialogNetActivity.startActivity(i);
						dialogNetActivity.finish();
					}
					dialogNoNetwork.cancel();
				}
			});

			final ToggleButton togleWifi = (ToggleButton) dialogNoNetwork.findViewById(R.id.togleWifi);
			togleWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					WifiManager wifiManager = (WifiManager)dialogNetActivity.getSystemService(Context.WIFI_SERVICE);				
					wifiManager.setWifiEnabled(isChecked);
					if(isChecked) {
						btnExit.setText(Utils.getResString(R.string.btn_ok));
					} else {
						btnExit.setText(Utils.getResString(R.string.Dialog_Btn_Exit));
					}
				}
			});

			final ToggleButton togleMobile = (ToggleButton) dialogNoNetwork.findViewById(R.id.togleMobile);
			togleMobile.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					enableMobileData(isChecked);
					if(isChecked) {
						btnExit.setText(Utils.getResString(R.string.btn_ok));
					} else {
						btnExit.setText(Utils.getResString(R.string.Dialog_Btn_Exit));
					}
				}
			});

			//On back/home button press dismiss dialog as well as close the activity
			dialogNoNetwork.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						((Activity) dialogNetActivity).finish();
						dialog.dismiss();
					} else if ( keyCode == KeyEvent.KEYCODE_HOME) {
						((Activity) dialogNetActivity).finish();
					}
					return true;
				}
			});

			dialogNoNetwork.show();
		} catch (Exception e) {
			Log.e(DialogCheckNetConnectivity.this.getClass().getSimpleName(), "Error showDialog method " + e.toString());
			Utils.generateNoteOnSD(TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()),FOLDER_PATH_DEBUG);
		}
	}
	/**
	 * This method is to enable/disable mobile data
	 * 
	 * @author suman
	 * @param status
	 */
	private void enableMobileData(final boolean status) {
		ConnectivityManager dataManager;
		dataManager  = (ConnectivityManager)dialogNetActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		Method dataMtd = null;
		try {
			dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataMtd.setAccessible(status);
		try {
			dataMtd.invoke(dataManager, status);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}

	@Override
	public Object getInstance() {
		return null;
	}
}

