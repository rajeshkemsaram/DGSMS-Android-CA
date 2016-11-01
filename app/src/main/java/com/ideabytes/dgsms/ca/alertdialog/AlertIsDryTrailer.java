package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;

/**
 * Created by sairam on 27/9/16.
 */

public class AlertIsDryTrailer extends Dialog implements DBConstants {
    private final static String TAG = "AlertIsDryTrailer";
    private Activity activity;
    int i=0;

    public AlertIsDryTrailer(Activity activity) {
        super(activity);
        this.activity = activity ;
    }

    public void showAlertDryTrailer() {
        final Utils utils = new Utils();
        android.app.AlertDialog.Builder builder;
        builder = new android.app.AlertDialog.Builder(activity, R.style.AlertDialogCustom);

        builder.setTitle(Utils.getResString(R.string.dry_trialer_msg));
        builder.setCancelable(false);
        builder.setPositiveButton(Utils.getResString(R.string.Dialog_Btn_Delete_Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(Utils.getResString(R.string.Dialog_Btn_Delete_No), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    AlertDialog alertDialog = new AlertDialog(activity);
                    alertDialog.showDialg(Utils.getResString(R.string.non_trialer_msg));
                    alertDialog.setCancelable(false);
                    //make un number edit text empty on this message
                    AddPlacardDialogActivity.eT_UnNumber.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public Object getInstance() {
        return null;
    }




}
