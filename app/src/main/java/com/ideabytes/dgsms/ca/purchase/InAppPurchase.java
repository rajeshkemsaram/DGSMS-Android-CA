package com.ideabytes.dgsms.ca.purchase;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



//import inapp.ideabytes.util.IabHelper;
import com.ideabytes.dgsms.landstar.R;

import inapp.ideabytes.util.IabHelper;
import inapp.ideabytes.util.IabResult;
import inapp.ideabytes.util.Inventory;
import inapp.ideabytes.util.Purchase;

public class InAppPurchase extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    IabHelper mHelper;
    static final String ITEM_SKU = "dgmobi1";
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxsLisr4ATHCUvtpGz1xNAO9yd" +
                "+VWdnSif7jI/IusGdIUw9GnFDuj2g2BZDtTKJ1UminzFOi4zPr3WaBglYq/ZLJFGGQarXXtXLUXdOQSviTP2lnWw7MyOXJA8" +
                "Nz7i3rm3gxp/geyH0nlIAP6Ll2/QJdbQqH3I6tBYbkOJz/rIrEUcAqXXeuXmGz/LXszsJ1IpYrgFZIgfuGm17I74Qdjr38jJYT5" +
                "iTFV/ezrytO/xr54xaxlFOzUURJVYbsLuUGgi846Jzl9mHGBd8jZsu+XNdDkd0OFU0aoIocR9k4s+Pd1nVQGQwXHgVgAms+OzT7hTo7" +
                "WLp/LSrdOmWJGWj3E1QIDAQAB";
        setContentView(R.layout.activity_in_app_purchase);
        button = (Button) findViewById(R.id.purchase);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
       //setup operation
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
           public void onIabSetupFinished(IabResult result) {
         if (!result.isSuccess()) {


             Log.d(TAG, "In-app Billing setup failed: " + result);

        } else {

             Log.d(TAG, "In-app Billing is set up OK");

          }
         }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.launchPurchaseFlow(InAppPurchase.this, ITEM_SKU, 10001,
                        mPurchaseFinishedListener, "mypurchasetoken");
                Log.v(TAG + "in flow","purchase flow started");

            }
        });




    }

        //purchase is completed then this method will be called
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                Log.v(TAG ,"purchase failed");
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();

            }

        }
    };


    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);


    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };


            // what to be done on ui if succecced
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                            Log.v(TAG,"handle ui here");
                    } else {
                        // handle error
                    }
                }
            };



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }



    // this is called after

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}