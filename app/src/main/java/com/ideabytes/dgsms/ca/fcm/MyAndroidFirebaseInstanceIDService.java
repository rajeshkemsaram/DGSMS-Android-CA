package com.ideabytes.dgsms.ca.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskCheckDevice;
import com.ideabytes.dgsms.ca.utils.Utils;

import java.util.concurrent.ExecutionException;

/**
 * Created by sairam on 5/10/16.
 */

public class MyAndroidFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyAndroidFCMIIDService";
    public static String fcmToken = "";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        MyAppData.getInstance().setFcmToken(refreshedToken);
//        MyAppData myAppData = (MyAppData) getApplicationContext();
//        myAppData.setFcmToken(refreshedToken);
        fcmToken = refreshedToken;
        sendToken();

    }

    private void sendToken() {
        AsyncTaskCheckDevice asyncTaskCheckDevice = new AsyncTaskCheckDevice(getApplicationContext());
        try {
            asyncTaskCheckDevice.execute(new Utils().getDeviceId(getApplicationContext())).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
