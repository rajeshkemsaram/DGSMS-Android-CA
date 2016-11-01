package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.Arrays;
/*************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AlertDialog
 * Created On : 13-10-2015
 * Modified Date : 16-10-15
 * author:  Suman
 * Description : This class is to show alert messages
 ***************************************************************/
public class AlertDialog extends Dialog implements DBConstants {
    private Activity activity;//Activity reference

    public AlertDialog(Activity activity) {
        super(activity);
        this.activity = activity ;
    }

    /**
     * This method displays dialog on Invalid UN Number
     *
     * author suman
     * @param message message to display on alert dialog
     * @since 3.0
     */
    public void showDialg(final String message) {
        try {
            // populate dialog UN Number invalid case and clear focus in all fields
            final Dialog alertDialog = new Dialog(activity, R.style.PauseDialog);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setContentView(R.layout.alert_dialog);
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //dialogInvalidUn.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

            TextView tvTitle = (TextView) alertDialog
                    .findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
            tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));
            TextView tvMessage = (TextView) alertDialog
                    .findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
                tvMessage.setText(message);

            Button inunClose = (Button) alertDialog
                    .findViewById(R.id.Alert_Dialog_Btn_Ok);//button to dismiss

            inunClose.setText(Utils.getResString(R.string.btn_ok));
            inunClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                    AlertIsDryTrailer d1= new AlertIsDryTrailer(activity);
//                    if(d1.i==1) {
//                        d1.showAlertDryTrailer();
//                    }
                    alertDialog.dismiss();
                }
            });
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

