package com.ideabytes.dgsms.ca.services;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.asynctask.AsyncToVerifyLicense;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.utils.Utils;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
//This service is to change license verification status to un done
// means setting verification status to "1" on every three days of time interval
/*********************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : ServiceToVerifyLicense
 * author:  Suman
 * Description : his service is to change license verification status to un done
 * means setting verification status to "1" on every three days of time interval
 * Modified Date : 27-10-2015
 ********************************************************************************/
public class ServiceToVerifyLicense extends Service implements DBConstants {
			String TAG="ServiceToVerifyLicense";
	private Timer mTimer = null;

	ConnectionServiceCallback mConnectionServiceCallback;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	public interface ConnectionServiceCallback {
		void hasInternetConnection();
		void hasNoInternetConnection();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			mTimer = new Timer();
			//System.out.println("licenseTime "+licenseTime);
			mTimer.scheduleAtFixedRate(new CheckForConnection(), TIMER_START_INTERVAL, TIMER_REPEAT_INTERVAL);
//			Log.v(TAG, "license verify service started");
		} catch (Exception e) {
			e.printStackTrace();
		}	

		return super.onStartCommand(intent, flags, startId);
	}

	class CheckForConnection extends TimerTask {
		@Override
		public void run() {
			if(isNetworkAvailable()) {
				Log.v(TAG,"Working on to check verify license");
				try {
//					MyAppData myAppData = (MyAppData) getApplicationContext();
					String deviceId = new Utils().getDeviceId(getApplicationContext());
					GetDBData getDBData = new GetDBData(getApplicationContext());
					String response  = new AsyncToVerifyLicense().execute(deviceId,
							getDBData.getConfigData().get(COL_IMEI)).get();
					//Log.v(TAG,"licenseData "+new JSONObject(response).getJSONObject(RESULTS).getJSONArray("licenseData"));
					//Log.v(TAG,"licenseData size "+new JSONObject(response).getJSONObject(RESULTS).getJSONArray("licenseData").length());
					if (new JSONObject(response).getJSONObject(RESULTS).getJSONArray("licenseData").length() < 1) {
						MyAppData.getInstance().setLicenseExpired(true);
						//after setting flag , stop service no need to run
						stopSelf();
					} else {
                        MyAppData.getInstance().setLicenseExpired(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		mTimer.cancel();
		super.onDestroy();
	}
    private boolean isNetworkAvailable(){
        try {
            if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0)
                return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
