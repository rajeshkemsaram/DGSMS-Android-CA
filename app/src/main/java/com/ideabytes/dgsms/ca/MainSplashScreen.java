package com.ideabytes.dgsms.ca;

import java.io.File;
import java.util.Arrays;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogTechError;
import com.ideabytes.dgsms.ca.rulesdata.AsynkActivity;
import com.ideabytes.dgsms.ca.database.DatabaseHelper;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/*****************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : MainSplashScreen
 * author:  Suman
 * Description : To Show splash screen for 5 seconds
 * Created Date : 04-04-2014*
 * Modified Date : 26-11-2015
 * Reason: checking terms and conditions usinf text file content
 ****************************************************/
public class MainSplashScreen extends Activity implements DBConstants {
private final String LOGTAG = "MainSplashScreen";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        Log.v("devide id", Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID.toString()));
        Log.v(LOGTAG +"Token",MyAppData.getInstance().getFcmToken());

        //custom title bar
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.my_custom_title_on_progress_bar);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DatabaseHelper.getDatabaseInstance(MainSplashScreen.this);
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
            AlertDialogTechError alertDialogTechError = new AlertDialogTechError(MainSplashScreen.this);
            alertDialogTechError.showDialog(Utils.getResString(R.string.required_os)
                    + Build.VERSION.RELEASE);
            return;

        }
    }
	// Validation for SD Card

	public static boolean isSDcardPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();

	}
	/**
	 * This method returns true if Debug file exist in sd card
	 * 
	 * @author suman
	 * @return debug file exist status
	 */
	private boolean checkDebugFile() {
		boolean exist = false;
		try {
			File f = new File(FOLDER_PATH_DEBUG);
			if(f.exists()) {
				exist = true;
			} else {
				exist = false;
			}
		} catch (Exception e) {
			Log.e(MainSplashScreen.this.getClass().getSimpleName(), "Error checkDebugFile method " + e.toString());
			Utils.generateNoteOnSD(TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()), FOLDER_PATH_DEBUG);
		}
		return exist;
	}

    /**
     * This method is to show splash screen for X seconds
     */
    private void loadSplashScreen() {
        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(SPLASH_SCREEN_SLEEP_INTERVAL);

                    // After 5 seconds redirect to main activity
					Intent intent = new Intent(MainSplashScreen.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
                    finish();

                } catch (Exception e) {
					new Exceptions(getApplicationContext(),MainSplashScreen.this
							.getClass().getName(), "Error::AsynkActivity" +
							Arrays.toString(e.getStackTrace()));
                }
            }
        };
        // start thread
        background.start();
    }
    public void showAlert() {



        AlertDialog.Builder builder =  new AlertDialog.Builder(MainSplashScreen.this);
        builder.setTitle(Utils.getResString(R.string.Dialog_Alert_Title));
        builder.setCancelable(false);
        builder.setMessage(Utils.getResString(R.string.rules_msg));
        builder.setPositiveButton(Utils.getResString(R.string.btn_continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //call async task to load rules data from server
                startActivity(new Intent(MainSplashScreen.this,AsynkActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(Utils.getResString(R.string.btn_later), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        builder.show();
    }
    @Override
    public Object getInstance() {
        return null;
    }







}
