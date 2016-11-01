package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.InsertDBData;

import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.AddTransaction;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.PlacardDisplayLogic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
/*********************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogClass2
 * author:  Suman
 * Description : Customized dialog to show "Will consigment a ship" for class 2.1
 * Created Date : 20-10-2015
 * Modified Date : 20-04-2016
 * Reason: issue fixed in constructing response for dg logic in addding dangerour_placard key
 * This issue caused bug in danger mixing with 2.1
 *******************************************************/
public class AlertDialogClass2 extends Dialog implements DBConstants {

    private Activity activity;//activity reference
    private String name = "";
    private double gross_Weight ;
    public AlertDialogClass2(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    /**
     * Display alert dialog to accept terms and conditions to take device imei/mac address,
     * which uses in the application, this displays once in the life time of the app,
     * once use has accepted the terms and conditions
     *
     * author Suman
     * @param message message to display on alert dialog
     * @param response jsong response to dg logic to load a placard
     * @since 1.0.7
     */
    public void showDialog(final String message,final JSONObject response) {
        try {

            final Dialog alertDialogClass2 = new Dialog(activity);
            alertDialogClass2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialogClass2.setCanceledOnTouchOutside(false);
            alertDialogClass2.setContentView(R.layout.custom_dialog_two_buttons);
            alertDialogClass2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;
            TextView tvTitle = (TextView) alertDialogClass2
                    .findViewById(R.id.Dialog_Two_Title);//text view to display alert dialog title
            tvTitle.setText("Alert Message");

            TextView tvMessage = (TextView) alertDialogClass2
                    .findViewById(R.id.Dialog_Two_Message);//text view to display alert dialog message
            tvMessage.setText(message);

            Button yes = (Button) alertDialogClass2.findViewById(R.id.Dialog_Two_Yes);
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
                            name = jsonObjectinDataArray.getString(COL_NAME);
                            gross_Weight = jsonObjectinDataArray.getDouble(COL_GROSS_WEIGHT);
                            jsonObjectinDataArray.remove(COL_DANGEROUS_PLACARD);
                            jsonObjectinDataArray.put(COL_DANGEROUS_PLACARD,"0");
                            int exemptionValue = AddPlacardDialogActivity.excemptValue(name,1);
                           //  System.out.println("exemptionValue "+exemptionValue);
                            jsonObjectinDataArray.put("exemptionValue", exemptionValue);//update key value based on selection
                            //for ecxemot values 1, weight index must be '0'
                            jsonObjectinDataArray.remove("weight_index");//remove key value
                            jsonObjectinDataArray.put("weight_index", 0);//update key value based on selection
                         }
                        alertDialogClass2.dismiss();
                        String popUp = AddPlacardDialogActivity.check1000KgRules(name, 0, gross_Weight);
                        //System.out.println("popUp in yes "+popUp);
                        if(popUp.split("::")[0].equalsIgnoreCase("1")) {
                            AlertDialogOn1000Kgs alertDialogOn1000Kgs = new AlertDialogOn1000Kgs(activity);
                            alertDialogOn1000Kgs.showDialog("Adding this load - exceeds 1000 Kg for the class3\n" +
                                    "Are these loads from the same consignor?",response);
                            return;
                        }
                        callDGLogic(response);//send json reponse to dg logic to load placard
                    } catch (Exception e) {
                        new Exceptions(activity.getApplicationContext(),AlertDialogClass2.this
                                .getClass().getName(), "Error::AlertDialogClass2::showDialog::btnYes " +
                               Arrays.toString(e.getStackTrace()));
                        e.printStackTrace();
                    }
                }
            });
            Button no = (Button) alertDialogClass2.findViewById(R.id.Dialog_Two_No);
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
                            name = jsonObjectinDataArray.getString(COL_NAME);
                            gross_Weight = jsonObjectinDataArray.getDouble(COL_GROSS_WEIGHT);
                            jsonObjectinDataArray.remove(COL_DANGEROUS_PLACARD);
                            jsonObjectinDataArray.put(COL_DANGEROUS_PLACARD,"1");
                            int exemptionValue = AddPlacardDialogActivity.excemptValue(name,0);
                            // System.out.println("exemptionValue "+exemptionValue);
                            jsonObjectinDataArray.put("exemptionValue", exemptionValue);//update key value based on selection
                         }
                        alertDialogClass2.dismiss();
                        String popUp = AddPlacardDialogActivity.check1000KgRules(name,0,gross_Weight);
                       // System.out.println("popUp in no "+popUp);
                        if(popUp.split("::")[0].equalsIgnoreCase("1")) {
                            AlertDialogOn1000Kgs alertDialogOn1000Kgs = new AlertDialogOn1000Kgs(activity);
                            alertDialogOn1000Kgs.showDialog("Adding this load - exceeds 1000 Kg for the class3\n" +
                                    "Are these loads from the same consignor?",response);
                            return;
                        }
                        callDGLogic(response);//send json reponse to dg logic to load placard
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // On back/home button press dismiss dialog as well as close the
            // activity
            alertDialogClass2.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                        alertDialogClass2.dismiss();
                    }
                    return true;
                }
            });
            alertDialogClass2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to send input to dg logic on class 2.1 case
     *
     * @param response
     */
    private void callDGLogic(final JSONObject response) {
        try {
            String deviceId = new Utils().getDeviceId(activity.getApplicationContext());
            String finalvalue = null;
            // valid input from response and user values are insert into
            // transaction_details table
            AddTransaction addTransaction = new AddTransaction();
            String value = addTransaction.PrintValues(deviceId, response);
             //System.out.println("response in 2.1" + response);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }
}
