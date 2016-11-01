package com.ideabytes.dgsms.ca.purchase;

import android.os.Bundle;
import android.app.Activity;

import com.ideabytes.dgsms.landstar.R;


//import inapp.ideabytes.util.IabHelper;

public class InAppBillingActivity extends Activity {
    private static final String TAG = "com.example.inappbilling";
//    IabHelper mHelper;
    static final String ITEM_SKU = "android.test.purchased";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_billing);


    }

}
