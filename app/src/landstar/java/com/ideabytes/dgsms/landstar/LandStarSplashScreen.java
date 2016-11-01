package com.ideabytes.dgsms.landstar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.MainSplashScreen;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogTechError;
import com.ideabytes.dgsms.ca.database.DatabaseHelper;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.rulesdata.AsynkActivity;


import java.util.Arrays;

public class LandStarSplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_land_star_splash_screen);
        setContentView(R.layout.splash_screen);
        LinearLayout llSplash  = (LinearLayout) findViewById(R.id.mainlinear);
        //change splash screen fron constants based on app type
        llSplash.setBackgroundResource(R.drawable.dgmobi_ca_landstar_splash);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.my_custom_title_on_progress_bar);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DatabaseHelper.getDatabaseInstance(this);
            GetDBData getDBData = new GetDBData(getApplicationContext());
            if (getDBData.getConfigData().size() > 0 && !getDBData.getRulesDataCount()) {
                //may be there was a problem in downloading rules data, now we have license data but not rules data so,
                //show alert to download only rules data, because licensing already done
                showAlert();
            } else {
                loadSplashScreen();
            }
        } else {
            // do something for phones running an SDK before 17
            // For gingerbread but lesser than HONEYCOMB, show a dialog or something and close the app
            AlertDialogTechError alertDialogTechError = new AlertDialogTechError(LandStarSplashScreen.this);
            alertDialogTechError.showDialog("Required OS version is 4.2 and greater,Your current version is "
                    + Build.VERSION.RELEASE);
            return;

        }
    }



    /**
     * This method is to show splash screen for X seconds
     */
    private void loadSplashScreen() {
        /****** Create Thread that will sleep for 5 seconds *************/

//        Intent intent = new Intent(LandStarSplashScreen.this,HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5000);

                    // After 5 seconds redirect to main activity
                    Intent intent = new Intent(LandStarSplashScreen.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // start thread
        background.start();
   }

    public void showAlert() {

        AlertDialog.Builder builder =  new AlertDialog.Builder(LandStarSplashScreen.this);
        builder.setTitle("Alert Message");
        builder.setCancelable(false);
        builder.setMessage("Rules data update is incomplete and it is mandatory. Would you like to update?");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //call async task to load rules data from server
                startActivity(new Intent(LandStarSplashScreen.this,AsynkActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }


}
