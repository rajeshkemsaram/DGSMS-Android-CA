package com.ideabytes.dgsms.ca.customalert;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.alertdialog.VerifyLicenseDialog;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import billing.Billing;


/**
 * Created by suman on 24/9/16.
 */

public class ShowAlertVerifyLicense implements DBConstants {
    private final String TAG = "ShowAlertVerifyLicense";
    private Activity activity;
    public ShowAlertVerifyLicense(Activity activity) {
        this.activity = activity ;
    }
    public void showDialog(String message, final String action) {
        final Utils utils = new Utils();
//        final MyAppData myAppData = (MyAppData) activity.getApplicationContext();
            //show alert to user to verify license status
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(activity);
            builder.setTitle(utils.getResString(R.string.Dialog_Alert_Title));
            builder.setMessage(message);
            builder.setCancelable(false);
        if (action.equalsIgnoreCase("expired")) {
            builder.setPositiveButton(utils.getResString(R.string.btn_renew_now), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (NetworkUtil.isNetworkAvailable(activity.getApplicationContext())) {
                            Intent intent = new Intent(activity, Billing.class);
                            intent.putExtra("action", "upgrade");
                            activity.startActivity(intent);
                        } else {
                            //show alert to user to connect network
                            VerifyLicenseDialog alertDialog =
                                   new VerifyLicenseDialog(activity);
                            alertDialog.showDialog(utils.getResString(activity.getApplicationContext(), R.string.alert_msg_conn_nw));
                        }
                        dialog.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (action.equalsIgnoreCase("verify")) {
            builder.setNegativeButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        quitApplication();
                        dialog.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }
    /**
     * This method is to Quit the application
     *
     * @author suman
     *
     */
    private void quitApplication() {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        activity.startActivity(intent);
        activity.finish();
    }//quitApplication()

    @Override
    public Object getInstance() {
        return null;
    }
}
