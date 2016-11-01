package com.ideabytes.dgsms.ca.services;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.asynctask.AsyncTaskPing;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskPushTransactions;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/*********************************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : ServiceToPushOrders
 * author:  Suman
 * Description : This service is to check continuous net connectivity to push transactions to
 * mobile web portal, on a given time interval
 * Modified Date : 27-10-2015
 ************************************************************************************************/
//This service is to check continuous net connectivity to push transactions to 
//mobile web portal, on a given time interval
public class ServiceToPushOrders extends Service implements DBConstants {
	private final String LOGTAG = "ServiceToPushOrders";
	private Timer mTimer = null;//timer reference to start alarm time

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
        Log.v(LOGTAG,"Push orders service started");
		mTimer = new Timer();
		mTimer.scheduleAtFixedRate(new CheckForConnection(), SERVICE_START_INTERVAL, TIME_INTERVAL_TO_PUSH_TRANS);

		return START_STICKY;
	}

	private class CheckForConnection extends TimerTask {
		@Override
		public void run() {
            Log.v(LOGTAG," I am working to push shipments to web ");
            //sendMail("suman.pula@ideabytes.com","exception","dont know");
			//if network is available then get transaction from transaction_details table to
			//push transactions to mobile web portal
			try {
				boolean netStatus = new AsyncTaskPing(getApplicationContext()).execute().get();
				if(netStatus) {
					try {
						JSONObject jObjectResponse = new GetDBData(getApplicationContext()).getTransactionsToPushOrders();
						//Log.e(LOGTAG,"json in service "+jObjectResponse);
						if(jObjectResponse.length() > 0) {
							new AsyncTaskPushTransactions(getApplicationContext()).execute(jObjectResponse);
							//Log.v(LOGTAG,"push order "+jObjectResponse);
						}
					} catch (Exception e) {
//						e.printStackTrace();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
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
