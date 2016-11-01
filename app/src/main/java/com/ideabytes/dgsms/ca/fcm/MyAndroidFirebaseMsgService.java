package com.ideabytes.dgsms.ca.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.asynctask.AsyncGetDbUpdates;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.TaskStackBuilder;
import java.text.DecimalFormat;


/**
 * Created by sairam on 5/10/16.
 */

 public class MyAndroidFirebaseMsgService extends FirebaseMessagingService implements DBConstants{
      String TAG = "MyAndroidFCMService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Log data to Log Cat
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//
//        createNotification(remoteMessage.getNotification().getBody());

        try {
            //0 means notification
            //1 means data
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
                readFcmData(remoteMessage.getData().toString());
            } else {
                Log.d(TAG, "Message notification: " + remoteMessage.getNotification().getBody());
                readFcmData(remoteMessage.getNotification().getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

   public void createNotification(final String body) {

        Intent intent = new Intent( this , ResultActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                 .setSmallIcon(R.drawable.logo)
                .setContentTitle("Android Tutorial Point FCM Tutorial")
                .setContentText(body)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());

        Log.v(TAG,"message"+body);
    }



    private void readFcmData(final String remoteMessage) {
        //0 means database update
        //1 means apk update
        try {
            JSONObject messageResponse = new JSONObject(remoteMessage);
            String service_type = "";
            Log.d(TAG, "messageResponse: " + messageResponse);
            if(messageResponse.has("Data")){
                 service_type = messageResponse.getJSONArray("Data").getJSONObject(0).getJSONObject("data").getString("service_type");
            }else{
                service_type = messageResponse.getString("service_type");
            }
//            String service_type = messageResponse.getJSONArray("Data").getJSONObject(0).getJSONObject("data").getString("service_type");
            //String Data=messageResponse.getJSONObject("");
            Log.d(TAG, "service_type: " + service_type);
            //service_type 'Release' means update related
            if (service_type.equalsIgnoreCase("Release")) {
                String update_type = messageResponse.getJSONArray("Data").getJSONObject(0).getJSONObject("data").getString("update_type");
                String version = messageResponse.getJSONArray("Data").getJSONObject(0).getJSONObject("data").getString("version");
                String priority = messageResponse.getJSONArray("Data").getJSONObject(0).getJSONObject("data").getString("priority");
                Log.d(TAG, "update_type: " + update_type);
                Log.d(TAG, "version: " + version);
                Log.d(TAG, "priority: " + priority);
                if (update_type.equalsIgnoreCase("1")) {
                    updateApk(version, priority);
                } else {
                    updateDatabase(version);
                }
            } else if (service_type.equalsIgnoreCase("pickuporders")) {
                sendNotification(remoteMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateApk(final String serverApkVersion, final String priority) {
        String currentApkVersion = getResources().getString(R.string.version);
        Log.v("Suman","currentApkVersion "+currentApkVersion);
        Log.v("Suman","serverApkVersion "+serverApkVersion);
        Log.v("Suman","priority "+priority);
        if (!currentApkVersion.equalsIgnoreCase(serverApkVersion)) {

            //set apk update status
            MyAppData.getInstance().setApkUpdated(true);
            //set apk update priority
            MyAppData.getInstance().setUpdatePriority(Integer.parseInt(priority));


//            MyAppData myAppData = (MyAppData) getApplicationContext();
            //set apk update status
//            myAppData.setApkUpdated(true);
            //set apk update priority
//            myAppData.setUpdatePriority(Integer.parseInt(priority));
        }
    }


    private void updateDatabase(final String serverApkVersion) {
        Double array[] ;
        String input = "";
        String query = "";
        String arrayOfQueries[] = null;
        UpdateDBData updateDBData = new UpdateDBData(getApplicationContext());
        try {
            double currentDbVersion = Double.parseDouble(getResources().getString(R.string.db_version));
//              double currentApkVersion = 1.01;
            Log.d(TAG, "currentApkVersion: " + currentDbVersion);
            double updateDbVersion = Double.parseDouble(serverApkVersion);
            Log.d(TAG, "updateApkVersion: " + updateDbVersion);
            if (updateDbVersion > currentDbVersion) {
                double count = (updateDbVersion - currentDbVersion);
                //restrict decimals to two digits
                DecimalFormat precision = new DecimalFormat("0.00");
                count = Double.parseDouble(precision.format(count * 100));
                Log.d(TAG, "count: " + count);
                array = new Double[(int) count];
                //multiply with 100 to make it numeric
                for (int i = 0; i < count; i++) {
                    array[i] = currentDbVersion + 0.01;
                    currentDbVersion = array[i];
                    Log.d(TAG, "array values: " + array[i]);
                    if (i < count - 1) {
                        input = input + array[i] + ",";
                    } else {
                        input = input + array[i];
                    }
                }
                Log.d(TAG, "input: " + "\""+input+ "\"");
                //send input to service to get query information,call async here
                AsyncGetDbUpdates asyncGetDbUpdates = new AsyncGetDbUpdates();
                JSONObject dbResponse = new JSONObject(asyncGetDbUpdates.execute(input).get()).getJSONObject(RESULTS);
                JSONArray jsonArray = dbResponse.getJSONArray("rulesDataSyncUp");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has(String.valueOf(array[i]))) {
                            query = jsonObject.getString(String.valueOf(array[i])) ;
                        }
                    }
                }
                arrayOfQueries = query.split(";");
                for (int i = 0; i < arrayOfQueries.length; i++) {
                    // Log.d(TAG, "arrayOfQueries: " + arrayOfQueries[i]);
                    updateDBData.updateDbFromServer(arrayOfQueries[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendNotification(String messageBody) {
        try {
            NotificationCompat.Builder notificationBuilder=null;
            if(APP_NAME.equalsIgnoreCase("DGMOBI_CA_GENERIC")) {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                 notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("DGMobi Generic")
                        .setContentText(Utils.getResString(R.string.tv_notification_text))
                        .setSmallIcon(R.drawable.launcher_icon_ca)
                        .setSound(defaultSoundUri);
            }else{
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle("DGMobi Landstar")
                        .setContentText(Utils.getResString(R.string.tv_notification_text))
                        .setSmallIcon(R.drawable.launcher_icon_ls)
                        .setSound(defaultSoundUri);
            }



//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setContentTitle("DGMobi Landstar")
//                    .setContentText(Utils.getResString(R.string.tv_notification_text))
//                    .setSmallIcon(R.drawable.launcher_icon_ca)
//                    .setSound(defaultSoundUri);
            //there is pick up order, user came here by selecting notification
            InsertDBData insertDBData = new InsertDBData(getApplicationContext());
            //service_type 'pickuporders' means pick up orders related related
            JSONArray results = new JSONObject(messageBody).getJSONArray("result");
            //Logger.logVerbose(TAG,"getOrdersFromWeb", "results "+results);
            Log.v(TAG + "notification",""+results);
            insertDBData.insertIntoOrders(results, 0);
        /* Creates an explicit intent for an Activity in your app */
            Intent resultIntent = new Intent(this, HomeActivity.class);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            resultIntent.putExtra("action", "fromNotification");
            resultIntent.putExtra("pick", messageBody);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(HomeActivity.class);

   /* Adds the Intent that starts the Activity to the top of the stack */
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //to close notification after click
            notificationBuilder.setAutoCancel(true);
            /* notificationID allows you to update the notification later on. */
            mNotificationManager.notify(123, notificationBuilder.build());
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),"Notification",Toast.LENGTH_SHORT).show();
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Object getInstance() {
        return null;
    }
}

