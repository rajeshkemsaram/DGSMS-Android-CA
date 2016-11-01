package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.content.DialogInterface;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.Arrays;

import static org.com.ca.dgsms.ca.model.DBConstants.ALERT_DIALOG_THEME;
import static org.com.ca.dgsms.ca.model.DBConstants.FOLDER_PATH_DEBUG;
import static org.com.ca.dgsms.ca.model.DBConstants.TEXT_FILE_NAME;

/**
 * Created by sairam on 22/10/16.
 */


public class VerifyLicenseDialog implements DBConstants {
    private Activity activity;//Activity reference

    public VerifyLicenseDialog(Activity activity) {

        this.activity = activity ;
    }

    /**
     * This method displays dialog on Invalid UN Number
     *
     * author suman
     * @param message message to display on alert dialog
     * @since 3.0
     */
    public void showDialog(final String message) {
        try {
            Utils utils = new Utils();
            android.app.AlertDialog.Builder builder;
            builder = new android.app.AlertDialog.Builder(activity );
            builder.setTitle(utils.getResString(activity.getApplicationContext(), R.string.alert_title));
            builder.setMessage(message);
            builder.setCancelable(false);
            //when 404 show only one button,else two buttons means no force update
            builder.setPositiveButton(utils.getResString(activity.getApplicationContext(),R.string.btn_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }
}


