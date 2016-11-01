package com.ideabytes.dgsms.ca.customalert;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import com.ideabytes.dgsms.ca.alertdialog.AlertCouponCodes;
import com.ideabytes.dgsms.ca.database.DeleteDBData;
import com.ideabytes.dgsms.ca.networkcheck.DialogCheckNetConnectivity;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;

import billing.Billing;


/**
 * Created by suman on 24/9/16.
 */

public class ShowAlertLicenseUpgrade implements DBConstants {
        private final String TAG = "ShowAlertLicenseUpgrade";
        private Activity activity;
        public ShowAlertLicenseUpgrade(Activity activity) {
            this.activity = activity ;
        }
        /**
         * This method is used to show in Combustibles
         *
         * @author Suman
         * @since 5.2
         */
        public void showDialog(String message, int buttons, String btnOne, String btnTwo, final String action) {
            try {
                final Utils utils = new Utils();
                final DeleteDBData deleteDBData = new DeleteDBData(activity.getApplicationContext());
                android.app.AlertDialog.Builder builder;
                builder = new android.app.AlertDialog.Builder(activity);
                builder.setTitle(utils.getResString(activity.getApplicationContext(), R.string.Dialog_Alert_Title));
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton(btnOne, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (action.equalsIgnoreCase("upgrade")) {
                                //here we have to upgrade license
                                //call payment , once payment is done then upgrade license
//                                Intent intent = new Intent(activity, Billing.class);
//                                intent.putExtra("action","upgrade");
//                                activity.startActivity(intent);
                                if (NetworkUtil.getConnectivityStatus(activity.getApplicationContext()) != 0) {
                                    AlertCouponCodes alertCouponCodes = new AlertCouponCodes(activity);
                                    alertCouponCodes.showDialog(Utils.getResString(activity.getApplicationContext(), R.string.alert_msg_buy_license),"upgrade");
                                } else {
                                    DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(activity);
                                    checkWifi.showDialog();
                                }
                            } else if (action.equalsIgnoreCase("expired")) {
                                //here we have to truncate license and rules data
                                deleteDBData.truncateLicenseData();
                                deleteDBData.deleteRulesData();
                            }
                            dialog.cancel();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                //when 1 show only one button,else two buttons
                if (buttons != 1) {
                    builder.setNegativeButton(btnTwo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dialog.cancel();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    @Override
    public Object getInstance() {
        return null;
    }
}
