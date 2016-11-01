package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;

import org.com.ca.dgsms.ca.model.AddTransaction;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.PlacardDisplayLogic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.InsertDBData;

import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/*********************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AlertDialogOn1000Kgs
 * author:  Suman
 * Description : Customized dialog to shows 1000kg alert dialog
 * Created Date : 26-10-2015
 * Modified Date : 26-10-2015
 *******************************************************/

public class AlertDialogOn1000Kgs extends Dialog implements DBConstants {
    public Activity activity;//activity reference
   public AlertDialogOn1000Kgs(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    /**
     * Display alert dialog to check 1000gk rule

     * author Suman
     * @param message message to display on alert dialog
     * @param response json response to add placard
     * @since 1.0.8
     */
    public void showDialog(final String message,final JSONObject response) {
        try {
            final Dialog alertDialog1000Kgs = new Dialog(activity);
            alertDialog1000Kgs.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog1000Kgs.setCanceledOnTouchOutside(false);
            alertDialog1000Kgs.setContentView(R.layout.custom_dialog_two_buttons);
            alertDialog1000Kgs.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;
            TextView tvTitle = (TextView) alertDialog1000Kgs
                    .findViewById(R.id.Dialog_Two_Title);//text view to display alert dialog title
            tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));

            TextView tvMessage = (TextView) alertDialog1000Kgs
                    .findViewById(R.id.Dialog_Two_Message);//text view to display alert dialog message
            tvMessage.setText(message);

            Button yes = (Button) alertDialog1000Kgs.findViewById(R.id.Dialog_Two_Yes);
            yes.setText(Utils.getResString(R.string.Dialog_Btn_Delete_Yes));
            // If user selection is "Yes" install the apk from /DGSMS/Apk
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call home activity on slecting "yes" to load placard
                    try {
                        // JSONObject jsnobject = new JSONObject(array);
                        JSONArray dataArray = response.getJSONArray("Data");
                        for (int j = 0; j < dataArray.length(); j++) {
                            JSONObject jsonObjectinDataArray = dataArray.getJSONObject(j);
                            jsonObjectinDataArray.remove("consignee_danger");//remove key value
                            jsonObjectinDataArray.put("consignee_danger", 1);//update key value based on selection
                        }
                        callDGLogic(response);//send json reponse to dg logic to load placard
                        alertDialog1000Kgs.dismiss();
                    } catch (Exception e) {
                        new Exceptions(activity.getApplicationContext(),AlertDialogOn1000Kgs.this
                                .getClass().getName(), "Error::AlertDialogOn1000Kgs::showDialog() " +
                                Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                    }
                }
            });
            Button no = (Button) alertDialog1000Kgs.findViewById(R.id.Dialog_Two_No);
            no.setText(Utils.getResString(R.string.Dialog_Btn_Delete_No));
            // If user selection "no" just continue with old apk, skips installation
            // process
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call home activity on slecting "no" to load placard
                    try {
                        // JSONObject jsnobject = new JSONObject(array);
                        JSONArray dataArray = response.getJSONArray("Data");
                        for (int j = 0; j < dataArray.length(); j++) {
                            JSONObject jsonObjectinDataArray = dataArray.getJSONObject(j);
                            jsonObjectinDataArray.remove("consignee_danger");//remove key value
                            jsonObjectinDataArray.put("consignee_danger", 0);//update key value based on selection
                        }
                        callDGLogic(response);//send json reponse to dg logic to load placard
                        alertDialog1000Kgs.dismiss();
                    } catch (Exception e) {
                        new Exceptions(activity.getApplicationContext(),AlertDialogOn1000Kgs.this
                                .getClass().getName(), "Error::AlertDialogOn1000Kgs::showDialog() " +
                                Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                    }
                }
            });

            // On back/home button press dismiss dialog as well as close the
            // activity
            alertDialog1000Kgs.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                    } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                        alertDialog1000Kgs.dismiss();
                    }
                    return true;
                }
            });
            alertDialog1000Kgs.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to send input to dg logic on class 2.1 case
     *  Author : Suman
     * @param response json response to add placard
     */
    private void callDGLogic(final JSONObject response) {
        try {
            String deviceId = new Utils().getDeviceId(activity.getApplicationContext());
            String finalvalue = null;
            // valid input from response and user values are insert into
            // transaction_details table
            AddTransaction addTransaction = new AddTransaction();
            String value = addTransaction.PrintValues(deviceId, response);
            // System.out.println("response " + response);
            // Abnormal case
            if (value.startsWith("99")) {
                AlertDailogPlacardAbnormalCase dialogAbnormal = new AlertDailogPlacardAbnormalCase(
                        activity);
                String[] reponse = value.split("::");
                String erroCode = reponse[0];
                String errorMessage = reponse[1];
                dialogAbnormal.showDialog(errorMessage, -1);
                return;
            } else {
                // valid case with proper response, error code as '00'
                // and erros mgs as 'Success'
                finalvalue = new PlacardDisplayLogic().placardDisplayLogic(deviceId, deviceId);
                finalvalue = finalvalue.substring(3);
                // Log.v(TAG,"finalvalue placard button-------> "+finalvalue);
                if (finalvalue
                        .startsWith("Not Enough Placard Holders on the truck Do NOT accept load")) {
                    // if placard holder size of truck exceeds the limit
                    AlertDialogNoEnoughPlacards dialogAbnormal = new AlertDialogNoEnoughPlacards(
                            activity);
                    dialogAbnormal.showDailog(
                            Utils.getResString(R.string.Dialog_Do_Not_Accept_Load), -1);
                    return;
                } else {
                    PlacardDisplayLogic displayLogic=new PlacardDisplayLogic();
                    String finalString= displayLogic.placardDisplayLogic(deviceId, deviceId);
                    new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, "add",
                            new GetDBData(activity.getApplicationContext()).getMaxTransId());
                    Intent i = new Intent(activity,
                            HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }
}
