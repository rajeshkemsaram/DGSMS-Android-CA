package com.ideabytes.dgsms.ca.mobilelost;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ideabytes.dgsms.ca.asynctask.AsyncCheckUsername;
import com.ideabytes.dgsms.ca.asynctask.AsyncGetDeviceLostStatus;
import com.ideabytes.dgsms.ca.asynctask.AsyncGetLostDevices;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.rulesdata.AsynkActivity;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by suman on 7/10/16.
 */
public class MobileLostUserData extends Activity implements DBConstants {
    String TAG=getClass().getSimpleName().toString();
    private ArrayList<String> lostDeviceList = new ArrayList<>();
    private ArrayList<String> listOfIds = new ArrayList<>();
    private String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.splash_screen);
        showAlert();
//        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;
//        final EditText etUsername = (EditText) findViewById(R.id.etUserName);
//        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
//        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
//        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
//        btnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String userName = etUsername.getText().toString();
//                String password = etPassword.getText().toString();
//                String email  = etEmail.getText().toString();
//                //here call server to get device ids
//                showAlert(userName);
//            }
//        });
//        Button btnCancel = (Button) findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
    private void showAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MobileLostUserData.this);
        builder.setTitle(Utils.getResString(R.string.alert_title_mobile_lost));
        //builder.setMessage(Utils.getResString(R.string.alert_title_mobile_lost));
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.mobile_lost_user_data, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setNegativeButton(Utils.getResString(R.string.back), null);
        builder.setPositiveButton(Utils.getResString(R.string.btn_submit), null);
        final AlertDialog alertDialog = builder.create();
        final EditText etUsername = (EditText)dialogView. findViewById(R.id.etUserName);
        final EditText etEmail = (EditText)dialogView. findViewById(R.id.etEmail);
        final EditText etPassword = (EditText)dialogView. findViewById(R.id.etPassword);
//
        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    try {
                        String userName = etUsername.getText().toString();
                        if (userName.length() > 0) {
                            AsyncCheckUsername asyncCheckUsername = new AsyncCheckUsername(getApplicationContext());
                            String responseUserName = asyncCheckUsername.execute(userName, new Utils().getDeviceId(getApplicationContext()), "").get();
                            if (!new JSONObject(responseUserName).getJSONObject(RESULTS).getString("userNameStatus").equalsIgnoreCase("99")) {
                               // Log.v("suman", "user name exist");
                                etUsername.setText(Utils.getResString(R.string.no_user_name));
                                etUsername.setTextColor(Color.RED);
                                //etUsername.requestFocus();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //make error text empty when focus changed
                    etUsername.setText("");
                    etUsername.setTextColor(Color.BLACK);
                }
            }
        });
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                final Button btnSubmit = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnSubmit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                    if (etUsername.getText().toString().length() <= 0) {
                        Toast.makeText(getApplicationContext(),"Please enter user name",Toast.LENGTH_SHORT).show();
                    } else if (etPassword.getText().toString().length() <= 0) {
                        Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                    } else if (etEmail.getText().toString().length() <= 0) {
                        Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_SHORT).show();
                    } else if (!isValidEmail(etEmail.getText().toString())) {
                        Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    } else {
                        String userName = etUsername.getText().toString();
                        String password = etPassword.getText().toString();
                        String email = etEmail.getText().toString();
                        AsyncGetLostDevices asyncGetLostDevices = new AsyncGetLostDevices(getApplicationContext());
                        JSONObject response = new JSONObject(asyncGetLostDevices.execute(userName, password, email).get());
                        if (response.getJSONObject(RESULTS).getString(STATUS).equalsIgnoreCase("00")) {
                            for (int i = 0; i < response.getJSONObject(RESULTS).getJSONArray("paymentStatus").length();
                                 i++) {
                                JSONObject jsonObject = response.getJSONObject(RESULTS).getJSONArray("paymentStatus").getJSONObject(i);
                                String mobile_model = jsonObject.getString("mobile_model");
                                String id = jsonObject.getString("id");
                                //add all the device models associated with user name,
                                //so that user can transfer based on device model
                                lostDeviceList.add(mobile_model);
                                listOfIds.add(id);
                            }

                           // lostDeviceList.add("Samsung"); // dummy param
                          //  lostDeviceList.add("Sony"); // dummy param
                            showAlert(lostDeviceList,userName);
                        } else {
                            //call alert
                            showAlertTransLicenseDone("License Transfer Failed", "Invalid credentials", 404);
                        }
                        alertDialog.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                });
                final Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnCancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                       // Log.v("Suman","text "+btnCancel.getText().toString());
                        MobileLostUserData.this.finish();
                        alertDialog.cancel();
                    }
                });
            }
        });
        builder.create();
        alertDialog.show();
    }
    private void showAlert(final ArrayList<String> mobile_model,final String userName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MobileLostUserData.this);
        builder.setTitle(Utils.getResString(R.string.alert_title));
        LayoutInflater layoutInflater =this.getLayoutInflater();
        builder.setCancelable(false);
        View dialog = layoutInflater.inflate(R.layout.alert_dialog_lost_devices_list,null);
        builder.setView(dialog);
        final RadioGroup radioGrpDeviceList = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0; i<mobile_model.size();i++){
            final RadioButton rb = new RadioButton(MobileLostUserData.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setTextColor(Color.WHITE);
            rb.setText(mobile_model.get(i));
            radioGrpDeviceList.addView(rb);
            Log.v("Suman"," id "+""+" text "+lostDeviceList.get(i));
            radioGrpDeviceList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                   Log.v("Suman"," id "+checkedId+" text "+lostDeviceList.get(checkedId-1));

                    id = listOfIds.get(checkedId-1);
                }
            });
        }
        builder.setMessage(Utils.getResString(R.string.alert_msg_device_verification));
        builder.setPositiveButton(Utils.getResString(R.string.btn_assign), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    AsyncGetDeviceLostStatus asyncGetLostDevices = new AsyncGetDeviceLostStatus(getApplicationContext());
                    String serviceResponse = asyncGetLostDevices.execute(id, new Utils().getDeviceId(getApplicationContext()),userName).get();
                    JSONObject response  = new JSONObject(serviceResponse);
                    if (response.getJSONObject(RESULTS).getString(STATUS).equalsIgnoreCase("00")) {
                        String type="";
                        InsertDBData insertData = new InsertDBData(getApplicationContext());
                        insertData.insertIntoLicenseTable(serviceResponse);
                        for (int i = 0; i < response.getJSONObject(RESULTS).getJSONArray("LicenseData").length();
                                i++) {
                            JSONObject  jsonObject = response.getJSONObject(RESULTS).getJSONArray("LicenseData").getJSONObject(i);
                            String validTill = jsonObject.getString("valid_till");
                            String validFrom = jsonObject.getString("valid_from");
                            showAlertTransLicenseDone("License Transfer Successful", "Congratulations!!" + "\n"
                                    + "Your license is transferred successfully to this device. License is valid from " +validFrom+
                                    " to "+validTill+". For further information, please contact " + Utils.getResString(R.string.tv_web_link) + ".", 100);
                        }
                    } else {
                        showAlertTransLicenseDone(Utils.getResString(R.string.license_trans_fail), response.getJSONObject(RESULTS).getString("message"), 404);
                    }
                    dialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(Utils.getResString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAlertTransLicenseDone(final String title,final String message,final int code) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(MobileLostUserData.this);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(Utils.getResString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code == 404) {
                    MobileLostUserData.this.finish();
                } else {
                    //call activty which starts loading rules data
                    Intent intent = new Intent(MobileLostUserData.this,
                            AsynkActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }
//    @Override
//    public void onBackPressed() {
//
//    }
}
