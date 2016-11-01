package billing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;


import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskSendPayStatus;
import com.ideabytes.dgsms.ca.asynctask.AsyncUpgradeLicense;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.license.WebActivity;
import com.ideabytes.dgsms.ca.models.CouponDetails;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import constants.Constants;

/**
 * Created by suman on 23/8/16.
 */
public class Billing extends Activity implements DBConstants, Constants,IabBroadcastReceiver.IabBroadcastListener {
    private String TAG = Billing.class.getSimpleName();
    final CouponDetails couponDetails = CouponDetails.getInstance();
    // The helper object
   private IabHelper mHelper;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    //String COMPANY_LEVEL_SUBSCRIPTION = "product10";
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.splash_screen);
        //custom title bar
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.my_custom_title_for_erg);
       // setContentView(R.layout.billing);
        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper...");
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);
       // Log.v(TAG," productId "+couponDetails.getProductId());
      //  Log.v(TAG," discountPercentage "+couponDetails.getDiscountPercentage());
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup...");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished...");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.e(TAG, "Problem setting up in-app billing: " + result);
                    alert("Problem setting up in-app billing: " + result,404,"",Utils.getResString(R.string.btn_quit));
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(Billing.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    //alert("Error querying inventory. Another async operation in progress.",404);
                }
            }
        });
    }
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                alert("Failed to query inventory: " + result,404,"", Utils.getResString(R.string.btn_quit));
                return;
            }
            Purchase gasPurchase = inventory.getPurchase(couponDetails.getProductId());
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                try {
                    mHelper.consumeAsync(inventory.getPurchase(couponDetails.getProductId()), mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    alert("Query inventory failed",404,"",Utils.getResString(R.string.btn_quit));
                }
            }
            Log.d(TAG, "Query inventory was successful.");
            try {
                mHelper.launchSubscriptionPurchaseFlow(Billing.this, couponDetails.getProductId(), RC_REQUEST,
                        mPurchaseFinishedListener, "");
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
            } else {
                alert("1: " + result,404,"",Utils.getResString(R.string.btn_quit));
            }
            Log.d(TAG, "End consumption flow.");
        }
    };
    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            alert("Error querying inventory. Another async operation in progress.",404,"",Utils.getResString(R.string.btn_quit));
        }
    }
    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                Log.d(TAG, "Purchase successful." + mHelper);
                return;
            }
            if (result.isSuccess()) {
                Log.d(TAG, "Purchase successful.");
                Toast.makeText(getApplicationContext(), Utils.getResString(R.string.toast_payment_successful), Toast.LENGTH_SHORT).show();
                //on initial payment call this method
                //call upgrade service here
                if (getIntent().getStringExtra("action").equalsIgnoreCase("payment")) {
                    //on successful payment , update payment status to server with device id and
                    //navigate to registration page
                    AsyncTaskSendPayStatus asyncTaskSendPayStatus = new AsyncTaskSendPayStatus(getApplicationContext());
                    try {
                        //send date and time when accepted terms
                        asyncTaskSendPayStatus.execute("1", couponDetails.getCouponCode()).get();
                        callWebPage(HOME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else if (getIntent().getStringExtra("action").equalsIgnoreCase("upgrade")) {
                    //call upgrade service here
                    AsyncUpgradeLicense asyncUpgradeLicense = new AsyncUpgradeLicense(getApplicationContext());
                    try {
                        //when response from upgrade license is 00 then extend license validy and update in db
                        //no need to get latest info from service
                        GetDBData getDBData = new GetDBData(getApplicationContext());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String beforeValidTill = getDBData.getConfigData().get(COL_VALID_TILL);
                        Date myDate = sdf.parse(beforeValidTill);
                        // Log.v(TAG,"validTill before update "+myDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(myDate);
                        cal.add(Calendar.YEAR, 1);//add one year to valid till date and update
                        String afterValidTill = sdf.format(cal.getTime());
                        //  Log.v(TAG,"validTill after update "+validTill);
                        //send extended value to service, it will be updated in server database
                        JSONObject licenseUpgrade = new JSONObject(asyncUpgradeLicense.execute(afterValidTill).get());
                        if (licenseUpgrade.getJSONObject("results").getString("status").equalsIgnoreCase("00")) {
                            //update new expiry date
                            new UpdateDBData(getApplicationContext()).updateVerifyLicense(afterValidTill);
                            //after upgrade move to home Activity
                            Toast.makeText(getApplicationContext(), Utils.getResString(R.string.toast_license_upgrade), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Billing.this, HomeActivity.class).putExtra("action", "fromActivity"));
                            finish();
                        } else {
                            Log.e(TAG, "upgrade license status 99");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            return;
            } else if (result.isFailure()) {
                Log.e(TAG,"Error purchasing: " + result);
                //payment failed, alert the user, to continue or quit
                alert(Utils.getResString(R.string.alert_msg_payment_failed),100,
                        Utils.getResString(R.string.btn_proceed_payment),Utils.getResString(R.string.btn_later));
                return;
            } else if (!verifyDeveloperPayload(purchase)) {
                Log.e(TAG,"Error purchasing. Authenticity verification failed.");
                //authentication failed, alert the user, to continue or quit
                alert("Authentication failed, do you want to continue payment?",100,
                        Utils.getResString(R.string.btn_proceed_payment),Utils.getResString(R.string.btn_later));
                return;
            }
        }
    };

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    void alert(final String message , final int code,String btnP,String btnN) {
        //if code 404 there is error from google cannot be handled in code so that only quit has to enable
        //remaining cases can be handled in code, so for 404 only enable 'Quit'
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle(Utils.getResString(R.string.alert_title));
        bld.setMessage(message);
        //not to dismiss on back button press
        bld.setCancelable(false);
        if (code != 404) {
            bld.setPositiveButton(btnP, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        mHelper.launchSubscriptionPurchaseFlow(Billing.this, couponDetails.getProductId(), RC_REQUEST,
                                mPurchaseFinishedListener, "");
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                    dialog.cancel();
                }
            });
        }
            bld.setNegativeButton(btnN, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    quitApplication();
                    finish();
                }
            });
        bld.create().show();
    }
    private void callWebPage(final String pageName) {
        Intent i = new Intent(Billing.this,WebActivity.class);
        i.putExtra("url", pageName);
        startActivity(i);
        finish();
    }
    /**
     * This method is to Quit the application
     *
     * @author suman
     *
     */
    private void quitApplication() {
        Intent intent = new Intent(Billing.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }//quitApplication()

    @Override
    public Object getInstance() {
        return null;
    }
}
