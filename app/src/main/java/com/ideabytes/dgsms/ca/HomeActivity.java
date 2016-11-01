package com.ideabytes.dgsms.ca;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.ideabytes.dgsms.ca.alertdialog.AlertCouponCodes;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialog;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogChooseLanguage;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogLicenseExpaired;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogOnPickupOrders;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogTransHistory;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogVerifyLicense;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskCheckDevice;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskToVerifyLicense;
import com.ideabytes.dgsms.ca.customalert.ShowAlertLicenseUpgrade;
import com.ideabytes.dgsms.ca.customalert.ShowAlertVerifyLicense;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.fragments.FragmentHome;
import com.ideabytes.dgsms.ca.license.WebActivity;
import com.ideabytes.dgsms.ca.license.WebActivityBuyLicesnse;

import com.ideabytes.dgsms.ca.logs.Logger;
import com.ideabytes.dgsms.ca.networkcheck.DialogCheckNetConnectivity;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.rulesdata.AsynkActivity;
import com.ideabytes.dgsms.ca.services.ServiceToPickupOrdersFromWeb;
import com.ideabytes.dgsms.ca.services.ServiceToPushOrders;
import com.ideabytes.dgsms.ca.services.ServiceToVerifyLicense;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : HomeActivity
 * author:  Suman
 * Description : To Show navigation drawer and its menu items listener
 * Created Data : 15-10-2015
 * Modified Date : 27-10-2015
 ****************************************************************/

public class HomeActivity extends Activity implements DBConstants {
     private static String LOGTAG = "HomeActivity";
    private static Context context = null;//to store Activity context
    private DrawerLayout drawerLayout;//navigation drawer layout
    private ListView drawerList;//list view to display navigation drawer options
    private String[] listItems;//navigation drawer ist items
    private int count = 0;//this variable is to check menu button click
    // menu options
    //private static final int maxPlacards = 0;//position of 'max placard, tab in Menu items
    private static final int change_language = 0;//position of 'change language, tab in Menu items
    private static final int show_my_orders = 1;//position of 'Show Orders, tab in Menu items
    private static final int menu_history = 2;//position of 'History, tab in Menu items
    //private static final int menu_dangerplacard_optimization = 6;//position of 'dangerplacard_optimization, tab in Menu items
    private static final int menu_quit = 3;//position of 'Quit, tab in Menu items
    private static boolean isDialogShown=false;
    private boolean sameDay=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.homeactivity);
        try {
            // custom title bar
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                    R.layout.my_custom_title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this context is used in diff classes(db classes)
        HomeActivity.context = getApplicationContext();
        // calling menu items,on clicking this navigation drawer will open
        Button btnOptions = (Button) findViewById(R.id.btn_options_menu);
        btnOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onRight(v);
            }
        });

        //to close the application when user press exit button while license registration/
        //verification,don't remove return statement, otherwise it executes main activity again
        if (getIntent().getBooleanExtra("EXIT", false)) {
            HomeActivity.this.finish();
            return;
        }




        try {
            //when no license data detected call web view to registration or verification
            GetDBData get = new GetDBData(getApplicationContext());
            // JSONObject jObjectResponse = get.getPlacardingTypeTable();
            //  Log.v("Suman","placarding table data "+get.getPlacardingTypeTable());
            // get.getGroupCompatibility();
            //   get.getClass1Compatibility();
            // get.getUnNumberInfoData();
            // get.getTransactionDetails();
            // get.getUnClassesTableData();
            /// get.getSegragations();
            // System.out.println("debug size "+get.getLicenseData().size());
//           startService(new Intent(HomeActivity.this, ServiceToVerifyLicense.class));


            if (get.getConfigData().size() <= 0) {
                if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
                    //when app launching for first time, check device status, payment status and registration
                    //with service,based on status drive the flow
                    AsyncTaskCheckDevice asyncTaskCheckDevice = new AsyncTaskCheckDevice(getApplicationContext());
                    JSONArray deviceStatus = asyncTaskCheckDevice.execute(new Utils().getDeviceId(getApplicationContext())).get();
                    Log.v(LOGTAG +"array",deviceStatus.toString());
                    if (deviceStatus.length() > 0) {
                        //there is a entry with device id in server database
                        //read payment status and registration status, then drive flow
                        JSONObject status = deviceStatus.getJSONObject(0);
                                //rajesh
                        if (status.getInt(REG_STATUS) == 1 && status.getInt(PAYMENT_STATUS) == 1) {
                            //everything done, go to verification page
                            callWebPage(VERIFICATION);
//                              if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
//                                  AlertCouponCodes alertCouponCodes = new AlertCouponCodes(HomeActivity.this);
//                                  alertCouponCodes.showDialog("You need to buy license to use this app");
//                              } else {
//                                  DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(HomeActivity.this);
//                                  checkWifi.showDialog();
//                              }
                        } else if (status.getInt(REG_STATUS) == 0 && status.getInt(PAYMENT_STATUS) == 1) {
                            //payment done but not registered, go to registration page
                            callWebPage(HOME);
                        } else if (status.getInt(REG_STATUS) == 0 && status.getInt(PAYMENT_STATUS) == 0) {
                            //this block is testing purpose, make staus changes in db then it comes here
                            //to test coupons flow and paymen as well
                            if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
                                AlertCouponCodes alertCouponCodes = new AlertCouponCodes(HomeActivity.this);
                                alertCouponCodes.showDialog(Utils.getResString(getApplicationContext(),R.string.alert_msg_buy_license),"payment");
                            } else {
                                DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(HomeActivity.this);
                                checkWifi.showDialog();
                            }
                        }//else if
                    } else {
                        if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
                            AlertCouponCodes alertCouponCodes = new AlertCouponCodes(HomeActivity.this);
                            alertCouponCodes.showDialog(Utils.getResString(getApplicationContext(),R.string.alert_msg_buy_license),"payment");
                        } else {
                            DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(HomeActivity.this);
                            checkWifi.showDialog();
                        }
                        //enable this to bypass payment and disable above lines
                        // callWebPage(HOME);
                    }//inner else , check licensing and payment status
                } else {
                    DialogCheckNetConnectivity checkWifi = new DialogCheckNetConnectivity(HomeActivity.this);
                    checkWifi.showDialog();

                }//outer else to check n/w to check device status with server
            } else {
                loadNavigationDrawer(savedInstanceState);
                verifyLicense();
                checkApkUpdate();
            }//outer else, checking license status in local db

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //listening menu button click listener, this opens navigation drawer from right on the screen
    public void onRight(View view) {
        drawerLayout.openDrawer(drawerList);
        count++;
        if (count == 2) {
            //close on double click to menu button
            drawerLayout.closeDrawers();
            count = 0;
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }

    /**
     * navigation drawer item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            //close drawer on selecting an item
            drawerLayout.closeDrawers();
            // display view for selected nav drawer item
            displayView(position);
        }
    }
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
       switch (position) {
            case change_language://to change language
                try {
                    AlertDialogChooseLanguage dialogChooseLanguage = new AlertDialogChooseLanguage(
                            HomeActivity.this);
                    dialogChooseLanguage.showdialog(new Utils().getLanguagesList());
                    //Toast.makeText(getContext(),listItems[position],Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
               break;
            case show_my_orders://to show pick up orders from web
                //Toast.makeText(getContext(),listItems[position],Toast.LENGTH_LONG).show();
                try {
                    GetDBData get = new GetDBData(getApplicationContext());
                    if (get.getPickupOrders().length() > 0) {
                        JSONArray orders = new Utils(getApplicationContext())
                                .getOrdersFromDatabase();
                        try {
                            AlertDialogOnPickupOrders alert = new AlertDialogOnPickupOrders(
                                    HomeActivity.this);
                            alert.showDialog(orders);
                        } catch (Exception e) {
                            e.printStackTrace();
                       }
                    } else {
                        new Utils(HomeActivity.this)
                                .showToastMessage(Utils.getResString(R.string.Toast_Message_No_Orders));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case menu_history://tp show transaction history
                try {
                    ArrayList<String> history = new GetDBData(getApplicationContext()).getTransHistory();
                    if (history.size() > 0) {
                        AlertDialogTransHistory alert = new AlertDialogTransHistory(HomeActivity.this);
                        alert.showDialog(history);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Transaction History", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getContext(),listItems[position],Toast.LENGTH_LONG).show();
                break;
            case menu_quit://to Quit the application
               // Toast.makeText(getContext(),listItems[position],Toast.LENGTH_LONG).show();
                try {
                    HomeActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }
    /**
     * This method will return application context
     *
     * author Suman
     * @return application context
     * @since 5.1
     */
    // Using in establishing database connection, to create connection context
    // must
    public static Context getContext() {
        return HomeActivity.context;
    }// getContext()
/**
 * This method is to select default selection as home activity from navigation drawer

 */
    private void defaultSelection(final Fragment fragment,final int position) {
        try {
            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.frame_container, fragment).commit();

                // update selected item and title, then close the drawer
                drawerList.setItemChecked(position, true);
                drawerList.setSelection(position);
                setTitle(listItems[position]);
                drawerLayout.closeDrawer(drawerList);
            } else {
                // error in creating fragment
                Log.e("MainActivity", "Error in creating fragment");
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    private void loadNavigationDrawer(final Bundle savedInstanceState) {
        try {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerList = (ListView) findViewById(R.id.list_slidermenu);
            listItems = new Utils().getMenuItemsForNavigationDrawer();
            // Set the adapter for the list view
            drawerList.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.navigation_drawer, listItems));

            // making navigation drawer to react on Menu item button click
            drawerList.setOnItemClickListener(new SlideMenuClickListener());

            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    R.drawable.menu, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ) {
                public void onDrawerClosed(View view) {
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            drawerLayout.setDrawerListener(mDrawerToggle);
            //by default home activity will be selected
            if (savedInstanceState == null) {
                // on first time display view for first nav item
                FragmentHome fragment = new FragmentHome();
                defaultSelection(fragment, 0);
                verifyLicense();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void callWebPage(final String pageName) {
        Intent i = new Intent(HomeActivity.this,WebActivity.class);
        i.putExtra("url", pageName);
        i.putExtra("env", POINTING_TO);
        i.putExtra("country", COUNTRY);
        i.putExtra(DEVICE_ID, new Utils().getDeviceId(getApplicationContext()));
        Log.v(LOGTAG+"device id home : ",new Utils().getDeviceId(getApplicationContext()));
        // i.putExtra(DEVICE_ID, "123");
        startActivity(i);
        finish();
    }


    private void checkApkUpdate() {
//        MyAppData myAppData = (MyAppData) getApplicationContext();
        if (MyAppData.getInstance().isApkUpdated()) {
            if (MyAppData.getInstance().getUpdatePriority() == 1) {
                //for priority 1,show update alert and its a force update, show alert with single button
                //its mandatory
                showAlertOnUpdate(Utils.getResString(getApplicationContext(),R.string.alert_msg_apk_update),404);
            } else {
                //for priority 0,only show update alert, its optional
                showAlertOnUpdate(Utils.getResString(getApplicationContext(),R.string.alert_msg_apk_update),100);
            }
        }
    }

    /**
     * Show alert to handle apk update
     * @param message
     * @param code
     */
    private void showAlertOnUpdate(final String message, final int code) {
        android.app.AlertDialog.Builder builder;
        builder = new android.app.AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(Utils.getResString(getApplicationContext(),R.string.Dialog_Alert_Title));
        builder.setMessage(message);
        builder.setCancelable(false);
        //when 404 show only one button,else two buttons means no force update
        if (code != 404) {
            builder.setPositiveButton(Utils.getResString(getApplicationContext(),R.string.btn_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyAppData.getInstance().setApkUpdated(false);
                    dialog.cancel();
                }
            });
        }
        builder.setNegativeButton(Utils.getResString(getApplicationContext(),R.string.btn_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url="";
                if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
                        url = "https://play.google.com/store/apps/details?id=com.ideabytes.dgsms.landstar";
                    if (APP_NAME.equalsIgnoreCase("DGMOBI_US_GENERIC")) {
                        url = "https://play.google.com/store/apps/details?id=com.ideabytes.dgsms.generic";
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    MyAppData.getInstance().setApkUpdated(false);
                    dialog.cancel();
                } else {
                    //show alert to user to connect network
                    AlertDialog alertDialog =
                            new AlertDialog(HomeActivity.this);
                    alertDialog.showDialg(Utils.getResString( R.string.alert_msg_conn_nw));
                }
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void verifyLicense() {
        long diffInDays = 0;
        try {
//            final com.ideabytes.dgsms.us.MyAppData myAppData = (MyAppData) getApplicationContext();
            ShowAlertVerifyLicense showAlertVerifyLicense = new ShowAlertVerifyLicense(HomeActivity.this);
            // Log.v("Home","license "+new GetDBData(getApplicationContext()).getVerifyLicenseStatus());
            if(MyAppData.getInstance().isVerifyLicense()) {
                showAlertVerifyLicense.showDialog(Utils.getResString(getApplicationContext(),R.string.alert_verify_license)
                        ,"verify");
            } else if(MyAppData.getInstance().isLicenseExpired()) {
                showAlertVerifyLicense.showDialog(Utils.getResString(getApplicationContext(),R.string.alert_msg_license_expired),
                        "expired");
            } else {
                Log.v("Suman ", "isDialogShown " + isDialogShown);

                if (!isDialogShown && !sameDay ) {
                    //license is valid but check is in last 10 days state, then show alert to user

                    String presentDate = Utils.getPresentDateTime();
                    String validTo = new GetDBData(getApplicationContext()).getConfigData().get(COL_VALID_TILL);
                    Log.v("Suman ", "presentDate " + presentDate);
                    // validTo = "2016-09-27";// enable this to test all scenarios
                    Log.v("Suman ", "validTo " + validTo);
                    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    Date endDate = formater.parse(validTo);
                    Date startDate = formater.parse(presentDate);
                    diffInDays = new Utils().dateDifference(startDate, endDate);
                    String btnNoLater = Utils.getResString(R.string.Dialog_Btn_Delete_No) + ", " + Utils.getResString(R.string.btn_later);
                    String btnYesContinue = Utils.getResString(R.string.btn_yes) + ", " + Utils.getResString(R.string.btn_continue);
                    String btnOk = Utils.getResString(getApplicationContext(), R.string.btn_ok);
                    String msgLicenseUpgrade = Utils.getResString(R.string.alert_msg_upgrade);
                    String msgLicenseExpired = Utils.getResString(R.string.alert_msg_license_expired) + " " +
                            Utils.getResString(R.string.alert_msg_extend);
//                    diffInDays = 30;
                    Log.v("Suman ", "diffInDays " + diffInDays);
                    ShowAlertLicenseUpgrade showAlertLicenseUpgrade = new ShowAlertLicenseUpgrade(com.ideabytes.dgsms.ca.HomeActivity.this);
                    if (diffInDays > 1 && diffInDays <= 30) {
                        //start showing alert
                        showAlertLicenseUpgrade.showDialog(msgLicenseUpgrade + " " + "in " + diffInDays + " days" + " (on " + validTo + ".)", 2, btnYesContinue, btnNoLater, "upgrade");
                    } else if (diffInDays < 1) {
                        //show expired message
                        showAlertLicenseUpgrade.showDialog(msgLicenseExpired, 1, btnOk, "", "expired");
                    }
                    isDialogShown = true;
                    sameDay=true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyAppData.getInstance().getToday().equalsIgnoreCase(MyAppData.getInstance().getYesterday())){
            MyAppData.getInstance().setYesterday((MyAppData.getInstance().getToday()));
            sameDay=true;
        }
        else {
             sameDay=true;
        }

    }
}
