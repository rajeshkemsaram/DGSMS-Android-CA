package com.ideabytes.dgsms.ca.services;

import java.util.Timer;
import java.util.TimerTask;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.HomeActivity;

import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
/*****************************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : ServiceToPickupOrdersFromLocal
 * author:  Suman
 * Description : This service checks for orders in the web and gets the orders by calling
 * a web service stores into local database table
 * Modified Date : 27-10-2015
 ******************************************************************************************/
@SuppressLint("NewApi")
public class ServiceToPickupOrdersFromLocal extends Service implements
DBConstants {
	private Timer timer = new Timer();

	@Override
	public void onCreate() {
		// Log.i(WatchDogService.class.getName(), "WatchDog start");
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// show notification here
				try {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							//Log.e("UI thread", "I am the UI thread "+value);
							GetDBData get = new GetDBData(getApplicationContext());
							if (get.getPickupOrders().length() > 0) {
								showNotification();
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}, SERVICE_START_INTERVAL, PICKUP_INTERVAL_LOCAL_SYNC);
	}

	@Override
	public void onDestroy() {
		// Log.i(WatchDogService.class.getName(), "WatchDog stop");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// / Log.i(WatchDogService.class.getName(),
		// "WatchDog has just been called...");
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * To Show notification on notification bar for pickup orders
	 * 
	 * @author suman
	 * @since v.b.5.4.4
	 */
	public void showNotification() {
		try {
			// define sound URI, the sound to be played when there's a notification
			Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			// intent triggered, you can add other intent for other actions
			Intent intent = new Intent(ServiceToPickupOrdersFromLocal.this, HomeActivity.class);
			//intent.putExtra("orders", orders);
			PendingIntent pIntent = PendingIntent.getActivity(ServiceToPickupOrdersFromLocal.this, 0, intent, 0);

			// this is it, we'll build the notification!
			// in the addAction method, if you don't want any icon, just set the first param to 0
			Notification mNotification = new Notification.Builder(this)
					.setContentTitle(Utils.getResString(R.string.pickup_order))
					.setContentText(Utils.getResString(R.string.tv_notification_text))
					.setSmallIcon(R.drawable.launcher_icon)
					.setContentIntent(pIntent)
					.setSound(soundUri)
                    .build();

			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// If you want to hide the notification after it was selected, do the code below
			mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(0, mNotification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
