package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ideabytes.dgsms.ca.asynctask.AsyncCheckCoupons;
import com.ideabytes.dgsms.ca.asynctask.AsyncGetTerms;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskSendPayStatus;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.license.WebActivity;
import com.ideabytes.dgsms.ca.mobilelost.MobileLostUserData;
import com.ideabytes.dgsms.ca.models.CouponDetails;
import com.ideabytes.dgsms.ca.networkcheck.DialogCheckNetConnectivity;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import billing.Billing;

/**
 * Created by suman on 7/9/16.
 */
public class AlertCouponCodes extends Dialog implements DBConstants {

    private Activity activity;
    private final String TAG = "AlertCouponCodes";
    private String couponResponse = "";
    private Button btnProceed;
    private Button btnApply;
    private EditText etCoupon;
    //to add bullet point in text view "\u2022 Bullet"+
    private String terms = "\u2022"+" This licence allows the buyer to download and use the DGMobi on a single device for a period of 1 year from the date of purchase.\n" +
            "\u2022"+" Purchaser of the licence does not give the Buyer of the licence any rights to the source code of the application.\n" +
            "\u2022"+" Buyer agrees not to decompile, disassemble, reverse-engineer DGMobi and copy any of the features for any purpose, and each attempt thereto shall constitute an infringement to this Agreement.\n" +
            "\u2022"+" The Licensed Software may not be sold, leased, assigned, loaned or otherwise transferred or provided to a third party.\n" +
            "\u2022"+" Licensee may not use the Licensed Software to provide services to third parties\n" +
            "\u2022"+" Buyer has been trained in the handling of Dangerous Goods or HAZMAT and possess a valid Certificate citing the HAZMAT Training whenever using DGMobi\n" +
            "\u2022"+" Limited liability: While DGMobi has been tested extensively, in the event of an error or an omission, the liability is limited to 6$ or 20% of the purchase price. All errors or omissions must be reported to support@dgsms.ca within 24 hours of the discovery of the error and must include a copy of the HAZMAT training certificate.\n" +
            "\u2022"+" The licence is governed by the laws of the province of Ontario, Canada and any litigation in regards to DGMobi must be filed in the provincial courts of Ottawa, Ontario Canada\n" +
            "\u2022"+" Periodically, updates to the software will be made to manage the interpretation and changes to the regulations. Buyer agrees to update the DGMobi software as needed without holding Ideabytes or its affiliates liable for revenues lost due to the upgrading of the software.\n" +
            "\u2022"+" All transactions of the software are archived and data may be used for analytics which may be shared with Landstar as needed.";
    public AlertCouponCodes(Activity activity) {
        super(activity);
        this.activity = activity ;
    }

    /**
     * This method is used to show "Is there any other consignment on the truck?"
     *
     * @author Suman
     * @since 5.2
     */
    public void showDialog(String message, final String action) {
        try {
            final CouponDetails couponDetails = CouponDetails.getInstance();
            final Dialog dialogCouponCodes = new Dialog(activity);
            dialogCouponCodes.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCouponCodes.setCanceledOnTouchOutside(false);
            dialogCouponCodes
                    .setContentView(R.layout.dialog_coupons);
            dialogCouponCodes.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;
            TextView tvTitle = (TextView) dialogCouponCodes
                    .findViewById(R.id.Dialog_Two_Title);
            tvTitle.setText("Welcome to DGMobi");

            TextView tvMessage = (TextView) dialogCouponCodes
                    .findViewById(R.id.Dialog_Two_Message);
            tvMessage.setText(message);




            final LinearLayout llCouponCodes = (LinearLayout) dialogCouponCodes.findViewById(R.id.llCoupons);
            llCouponCodes.setVisibility(View.GONE);


            final CheckBox chHaveCoupon = (CheckBox) dialogCouponCodes.findViewById(R.id.chHaveCoupon);
            chHaveCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if (isChecked) {
                      llCouponCodes.setVisibility(View.VISIBLE);
                  } else {
                      llCouponCodes.setVisibility(View.GONE);
                      displayButtonFalse(btnProceed);
                  }
                }
            });
            final LinearLayout llTvResponse = (LinearLayout) dialogCouponCodes.findViewById(R.id.tvText);
            llTvResponse.setVisibility(View.GONE);
            final TextView textView = (TextView) dialogCouponCodes.findViewById(R.id.tvCouponText);

            final CheckBox chTerms = (CheckBox) dialogCouponCodes.findViewById(R.id.chTerms);
            chTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (llTvResponse.getVisibility() == View.VISIBLE) {
                       llTvResponse.setVisibility(View.GONE);
                   }
                    if (isChecked) {
                        //on checking terms and conditions and there is no coupon code then
                        //user can process, so enable continue button
                        if (!chHaveCoupon.isChecked()) {
                            displayButtonTrue(btnProceed);
                        }
                    } else {
//                        displayButtonFalse(btnProceed);
                    }
                }
            });
            final TextView tvClickHere = (TextView) dialogCouponCodes.findViewById(R.id.tvMobileLostClick);
            tvClickHere.setPaintFlags(tvClickHere.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            tvClickHere.setText(Utils.getResString(R.string.tv_mobile_lost_click));

            tvClickHere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.v(TAG +"onclick : ","inclick");
                        Intent intent = new Intent(activity, MobileLostUserData.class);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });



            final TextView tvTerms = (TextView) dialogCouponCodes.findViewById(R.id.tvTermsCilck);
            tvTerms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // AlertTermsCheck alertDialog = new AlertTermsCheck(activity);
                   // alertDialog.showDialog(terms);
                    AsyncGetTerms  asyncGetTerms = new AsyncGetTerms(activity);
                    try {
                        String termsAndConds = asyncGetTerms.execute().get();
                        showAlert(termsAndConds);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });



            etCoupon = (EditText) dialogCouponCodes.findViewById(R.id.etCoupon);
            //with text watcher on et coupon, manage error message on invalid coupon codes
            etCoupon.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (etCoupon.getText().toString().length() > 0) {
                        if (llTvResponse.getVisibility() == View.VISIBLE) {
                            llTvResponse.setVisibility(View.GONE);
                            btnApply.setText(Utils.getResString(R.string.btn_check));
                            updateContinueDisplay(btnApply);
                        }
                    } else {
                        displayButtonTrue(btnProceed);
                    }
                }
            });
            btnApply  = (Button) dialogCouponCodes .findViewById(R.id.btnApply);
            btnApply.setText(Utils.getResString(R.string.btn_check));
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (etCoupon.getText().toString().length() > 0) {
                            //get the coupon code from edit text
                            couponDetails.setCouponCode(etCoupon.getText().toString());
                            if (NetworkUtil.getConnectivityStatus(activity.getApplicationContext()) != 0) {
                                if (btnApply.getText().toString().equalsIgnoreCase(Utils.getResString(R.string.btn_check))) {
                                    if(checkCoupon("1")) {
                                        //when check clicked send input as "1" and update button name based on response
                                        updateContinueDisplay(btnApply);
                                        btnApply.setText(Utils.getResString(R.string.btn_apply));
                                    }
                                } else {
                                    //to change 'Continue' button display change
                                    updateContinueDisplay(btnApply);
                                    //send applied status to server when button name is "Apply"
                                    checkCoupon("2");
                                }
                                llTvResponse.setVisibility(View.VISIBLE);
                                if (couponResponse.startsWith("C")) {
                                    //on success message show message in green
                                    textView.setTextColor(Color
                                            .parseColor("#27AE60"));
                                   // textView.setTextColor(Color.GREEN);
                                } else {
                                    //on success message show message in green
                                    textView.setTextColor(Color.RED);
                                }
                                textView.setText(couponResponse);
                            } else {
                                DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(activity);
                                checkWifi.showDialog();
                            }
                        } else {
                            llTvResponse.setVisibility(View.VISIBLE);
                            textView.setText("Enter Coupon Code");
                            //Toast.makeText(activity.getApplicationContext(),"Enter Coupon Code",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btnProceed = (Button) dialogCouponCodes
                    .findViewById(R.id.Dialog_Two_Yes);
            btnProceed.setText(Utils.getResString(R.string.btn_continue));
            updateContinueDisplay(btnProceed);
            btnProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (chTerms.isChecked()) {
                            //call Billing activity to proceed with payment
                            //when discount is 100% then by pass payment
                           // Log.v(TAG,"coupon code on proceed "+couponDetails.getDiscountPercentage());
                            if (!couponDetails.getDiscountPercentage().isEmpty() && couponDetails.getDiscountPercentage().equalsIgnoreCase("100")) {
                                //payment not required when discount is 100 but need to update payment status to service
                                AsyncTaskSendPayStatus asyncTaskSendPayStatus = new AsyncTaskSendPayStatus(activity.getApplicationContext());
                                asyncTaskSendPayStatus.execute("1",couponDetails.getCouponCode());
                                //then call home page so that user can register
                                callWebPage(HOME);
                            } else {
                                //get product id dynamic from sever
                                if(!couponDetails.getProductId().equalsIgnoreCase("")) {
                                    couponDetails.setProductId(couponDetails.getProductId());
                                } else {
                                    if (APP_NAME.equalsIgnoreCase("DGMOBI_US_GENERIC")) {
                                        couponDetails.setProductId("dgmobicagen1");
                                    } else {
                                        couponDetails.setProductId("dgmobicals1");
                                    }
                                }

                                //based on coupon,service sends percentage and product id
                                Intent intent = new Intent(activity, Billing.class);
                                intent.putExtra("action",action);
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        } else {
                            //if terms not accepted , show error message
                            llTvResponse.setVisibility(View.VISIBLE);
                            textView.setTextColor(Color.RED);
                            textView.setText("Terms and Conditions shall be accepted to continue further");
                            //don't remove return otherwise it navigates to next
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialogCouponCodes.dismiss();
                }
            });
            Button btnQuit = (Button) dialogCouponCodes
                    .findViewById(R.id.Dialog_Two_No);
            btnQuit.setText(Utils.getResString(R.string.btn_quit));
            btnQuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //set coupon details to empty when app 'Quit' so that stored value never be miss leaded
                        couponDetails.setDiscountPercentage("");
                       //stop payment and exit from app
                        activity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialogCouponCodes.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    //to disable back button
                    return false;
                }
            });
            dialogCouponCodes.show();
        } catch (Exception e) {
            Log.e(AlertCouponCodes.this.getClass().getSimpleName(), "Error showDialog method " + e.toString());
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * This method is to call web page
     * @param pageName
     */
    private void callWebPage(final String pageName) {
        Intent i = new Intent(activity,WebActivity.class);
        i.putExtra("url", pageName);
        i.putExtra("env", POINTING_TO);
        i.putExtra("country", COUNTRY);
        i.putExtra(DEVICE_ID, new Utils().getDeviceId(activity));
        activity.startActivity(i);
        activity.finish();
    }

    public boolean checkCoupon(final String status) {
        boolean isCouponValid = false;
        try {
            final CouponDetails couponDetails = CouponDetails.getInstance();
            String deviceId = new Utils().getDeviceId(activity.getApplicationContext());
            //call service to check coupon code availability
            AsyncCheckCoupons asyncCheckCoupons = new AsyncCheckCoupons(activity);
            //if status is 1 then it verifies else it applies
            String result = asyncCheckCoupons.execute(couponDetails.getCouponCode(),status,deviceId).get();
            JSONObject response = new JSONObject(result).getJSONObject(RESULTS);
            if (response.getString(STATUS).equalsIgnoreCase("99")) {
                couponResponse = response.getString("message");
                //on invalid coupon code clear edit text
                etCoupon.setText("");
                displayButtonFalse(btnProceed);
                isCouponValid = false;
            } else if (response.getString("status").equalsIgnoreCase("77")) {
                couponResponse = response.getString("message");
                //on invalid coupon code clear edit text
                etCoupon.setText("");
                displayButtonFalse(btnProceed);
                isCouponValid = false;
                // Toast.makeText(activity,"Coupon Code Count Has Been Exceeded",Toast.LENGTH_SHORT).show();
            } else if (response.getString("status").equalsIgnoreCase("00")) {
                //Toast.makeText(activity,"Valid Coupon Code\"",Toast.LENGTH_SHORT).show();
                couponResponse = "";
                couponDetails.setProductId(response.getString(PRODUCT_ID));
                couponDetails.setDiscountPercentage(response.getString(DISCOUNT_PERCENTAGE));
                if (couponDetails.getDiscountPercentage().equalsIgnoreCase("100")) {
                    couponResponse = "Congratulations! Full amount is waivered!!";
                } else {
                    couponResponse = "Congratulations! you will get " + couponDetails.getDiscountPercentage()
                            + "% on actual price";
                }
                if (btnApply.getText().toString().equalsIgnoreCase(Utils.getResString(R.string.btn_apply))) {
                    //on valid coupon code applied just disable edit text
                    etCoupon.setEnabled(false);
                    //on valid coupon code applied just disable apply button
                    btnApply.setEnabled(false);
                }
                isCouponValid = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCouponValid;
    }
    private void updateContinueDisplay(final Button button) {
       // Log.v("Suman ","button "+button.getText().toString());
        if (button.getText().toString().equalsIgnoreCase(Utils.getResString(R.string.btn_apply))) {
            displayButtonTrue(btnProceed);
        } else {
            displayButtonFalse(btnProceed);
        }
    }
    private void displayButtonTrue(final Button button) {
        Drawable mDrawable = activity.getResources()
                .getDrawable(R.drawable.button_color_bg_orange);
        button.setTextColor(Color
                .parseColor("#FFFFFF"));
        button.setBackgroundDrawable(mDrawable);
        button.setEnabled(true);
    }

    @SuppressWarnings("deprecation")
    private void displayButtonFalse(final Button button) {
        Drawable mDrawable = activity.getResources()
                .getDrawable(R.drawable.button_color_disable);
        button.setTextColor(Color
                .parseColor("#452300"));
        button.setBackgroundDrawable(mDrawable);
        button.setEnabled(false);
    }
    private void showAlert(final String message) {
        try {
            final Utils utils = new Utils();
            JSONObject  jsonObject = new JSONObject(message).getJSONObject(RESULTS);
            StringBuilder sb = new StringBuilder();
            if (jsonObject.getString(STATUS).equalsIgnoreCase("00")) {
                JSONArray  jsonArray = jsonObject.getJSONArray("userDetails");
                android.app.AlertDialog.Builder builder;
                builder = new android.app.AlertDialog.Builder(activity);
                builder.setTitle(utils.getResString( R.string.alert_title_terms));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerJsonObject = jsonArray.getJSONObject(i);
                   // Log.v("Suman", "desc "+innerJsonObject.getString("description"));
                    sb.append("\u2022"+innerJsonObject.getString("description")+"\n");
                }
                builder.setMessage(sb.toString());
                builder.setPositiveButton(utils.getResString(
                        R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save terms status and date time
                        new InsertDBData(activity.getApplicationContext()).saveTermsCheck(1,
                                utils.getPresentDateTime());
                        dialog.cancel();
                    }
                });
                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
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