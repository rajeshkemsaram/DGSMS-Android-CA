package com.ideabytes.dgsms.ca;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ideabytes.dgsms.ca.adapters.CustomPsnAdapter;
import com.ideabytes.dgsms.ca.alertdialog.AlerDialogInValidUnNumber;
import com.ideabytes.dgsms.ca.alertdialog.AlertDailogPlacardAbnormalCase;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialog;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogClass2;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogClass7Catogory;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogIbc;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogOn1000Kgs;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogPackage;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogResidue;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogSp84;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogSpecialCase;
import com.ideabytes.dgsms.ca.alertdialog.AlertIsDryTrailer;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.erg.AsyncTaskErg;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.model.DataCenter;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.scan.ScannerActivity;
import com.ideabytes.dgsms.ca.utils.DGLogicInput;
import com.ideabytes.dgsms.ca.utils.DecimalInputTextWatcher;
import com.ideabytes.dgsms.ca.utils.UnNumberInfo;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.AddTransaction;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.PlacardDisplayLogic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AddPlacardDialogActivity
 * author:  Suman
 * Description : To Show placards input dialog to take from user
 * Modified Date : 02-12-2015
 * Resoan : Debuggin issues
 ****************************************************************/
public class AddPlacardDialogActivity extends Activity implements DBConstants,
        OnFocusChangeListener {
    private final String LOGTAG = AddPlacardDialogActivity.class.getSimpleName();
    public static Activity dialogActivity;//Activity reference to display dialog

    // Edit texts
    public static EditText nos_text;
    public static EditText eT_BillOfLading;//edit text for 'bill of lading'
    public static EditText eT_UnNumber;//edit text for 'un number'
    public static EditText eT_DGGrossMassPkg;//edit text for 'weight per package'
    public static EditText eT_NumberOfUnits;//edit text for 'no of units'
    public static EditText eT_Erap;//edit text for 'ERAP'
    public static EditText eT_GrossWeightTotal;//edit text for 'gross weight'
    // TextViews
    public static TextView tV_UnDesc;//text view to display 'un description'
    public static TextView tv_Ibc;//text view to display 'ibc select'
    public static TextView tv_Residue;//text view to display 'residue select'
    // button
    public static Button button_IBC;//button for 'ibc'
    public static Button button_Residue;//button for 'residue'
    public static Button button_DisplayPlacard;////button for 'Display Placard'
    public static Button button_Erg;//button for 'ERG'
    //Radio button to select weight type
    private RadioGroup Radio_Weight_Group;
    // int
    public int whoHasFocus = 0;//variable to notice exit text focus
    // String
    public static String nosdata = "";
    private String[] sellection = {Utils.getResString(R.string.Dialog_Btn_Delete_Yes),
            Utils.getResString(R.string.Dialog_Btn_Delete_No)};//array storing options 'Yes' and 'No'
    public String transaction_id_web = "";//to store transaction id from web(used in pick up orders)
    public String user_id_web = "";//to store user id from web(used in pick up orders)
    public static String description;//to display description of un number
    public static String pkg_group = "";//to display package group for a un number
    public String unnumber_display = "0";// to hold un number display status
    public static String ibc_status = "0";//to hold ibc status
    public static String ibc_residue_status = "0";//to hold residue status
    private String erap_no = "";//to store erap number for a un number
    public static int exemption = 0;//exemption for dg logic,BY DEFAULT 0
    public static Resources mResources;//get resources from drawable
//    public static String name;//name of the placard for dg logic
//    public static String pp;//primary placard for dg logic
//    public static String un_class_id;//un_class_id  for dg logic
//	public static int optimise = 1;//optimise for dg logic,BY DEFAULT 1(DISABLE)
//	public static int exemption = 0;//exemption for dg logic,BY DEFAULT 0
//    public static int ship = 0;//ship for dg logic,BY DEFAULT 0
//	public static String danger_placard = "0";//danger_placard for dg logic,BY DEFAULT 0

    private boolean flagDialogPackage = false;

    public static TableRow nosrow; //nos displaying table

    public static String nos = "";
    public static String sometext= "";//splitted psn value

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //no title bar for a activity or dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_add_item);
        //table row for nos edittext box
        nosrow = (TableRow) findViewById(R.id.nosrow);
        nosrow.setVisibility(View.GONE);
        //Edit text for nos
        nos_text = (EditText) findViewById(R.id.nos_text);
        //filter for not accepting spaces in edit text
        InputFilter filter = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (nos_text.getText().toString().equals("")) {
                    canEnterSpace = false;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }
                    if (Character.isWhitespace(currentChar) && canEnterSpace) {
                        builder.append(currentChar);
                    }
                }
                return builder.toString();
            }
        };
        nos_text.setFilters(new InputFilter[]{filter});
//		nos_text.setVisibility(View.GONE);
        this.setFinishOnTouchOutside(false);

        dialogActivity = AddPlacardDialogActivity.this;
        mResources = getResources();

        // dialog box title(Add Item)
        TextView tV_DialogBoxTitle = (TextView) findViewById(R.id.tvDialogTitle);
        tV_DialogBoxTitle.setText(Utils.getResString(R.string.Dialog_Add_Item_Title));

        // Enter Bill of Lading
        eT_BillOfLading = (EditText) findViewById(R.id.enter_bill_of_lading);
        eT_BillOfLading.setHint(Utils.getResString(R.string.Dialog_Add_Item_Et_BL));
        // add on tab out to edit text
        eT_BillOfLading.setOnFocusChangeListener(this);
        eT_BillOfLading.requestFocus();
        eT_BillOfLading.addTextChangedListener(text_watcher_edittext);

        // Enter UN NUMBER
        eT_UnNumber = (EditText) findViewById(R.id.enter_un_number);
        eT_UnNumber.setHint(Utils.getResString(R.string.Dialog_Add_Item_Et_Unumber));
        eT_UnNumber.setOnFocusChangeListener(this);
        // add on tab out to edit text
        eT_UnNumber.addTextChangedListener(text_watcher_edittext);


        // un number description
        tV_UnDesc = (TextView) findViewById(R.id.TV_Un_Desc);
        tV_UnDesc.setText(Utils.getResString(R.string.TV_UN_Description));
        tV_UnDesc.setMovementMethod(new ScrollingMovementMethod());

        // Enter dg Weight
        eT_DGGrossMassPkg = (EditText) findViewById(R.id.et_dgMassPkg);
        eT_DGGrossMassPkg.setHint(Utils.getResString(R.string.Et_DG_Mass_Pkg));
        eT_DGGrossMassPkg.setOnFocusChangeListener(this);
        eT_DGGrossMassPkg.addTextChangedListener(new DecimalInputTextWatcher(
                eT_DGGrossMassPkg, 2));
        // add on tab out to edit text
        eT_DGGrossMassPkg.addTextChangedListener(text_watcher_edittext);

        // DG Gross mass(units*dg weight)
        eT_GrossWeightTotal = (EditText) findViewById(R.id.eTDgGrossTotal);
        eT_GrossWeightTotal.setHint(Utils.getResString(R.string.TV_DG_Gross_Mass));
        // this restricts decimal value upto two digits ex.123.22222 to 123.22
        eT_GrossWeightTotal.addTextChangedListener(new DecimalInputTextWatcher(
                eT_GrossWeightTotal, 2));
        // add on tab out to edit text
        eT_GrossWeightTotal.setOnFocusChangeListener(this);
        eT_GrossWeightTotal.addTextChangedListener(text_watcher_edittext);

        // NUMBER OF UNITS
        eT_NumberOfUnits = (EditText) findViewById(R.id.enter_Units);
        eT_NumberOfUnits.setHint(Utils.getResString(R.string.Et_No_Of_Pkg));
        // add on tab out to edit text
        eT_NumberOfUnits.setOnFocusChangeListener(this);
        eT_NumberOfUnits.addTextChangedListener(text_watcher_edittext);

        // enter erap number
        eT_Erap = (EditText) findViewById(R.id.Dialog_Add_Item_ET_Erap);
        eT_Erap.setVisibility(View.GONE);
        eT_Erap.setHint(Utils.getResString(R.string.Et_ERAP));
        // add on tab out to edit text
        eT_Erap.setOnFocusChangeListener(this);
        eT_Erap.addTextChangedListener(text_watcher_edittext);

        tv_Ibc = (TextView) findViewById(R.id.Dialog_Add_Item_Tv_IBC);
        tv_Ibc.setVisibility(View.GONE);

        tv_Residue = (TextView) findViewById(R.id.Dialog_Add_Item_Tv_Residue);
        tv_Residue.setVisibility(View.GONE);
        //button ibc to select 'ibc status', on click this will show alert dialog to select ibc status
        button_IBC = (Button) findViewById(R.id.spinnerIbc);
        button_IBC.setVisibility(View.GONE);
        button_IBC.setText(Utils.getResString(R.string.Dialog_Btn_Delete_Yes));
        button_IBC.setTextColor(Color.WHITE);
        button_IBC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialogIbc spinner = new AlertDialogIbc(AddPlacardDialogActivity.this);
                spinner.showdialog(sellection);
            }
        });
        //button to select 'residue status', on click this shows a dialog to select residue status
        button_Residue = (Button) findViewById(R.id.spinnerResidue);
        button_Residue.setVisibility(View.GONE);
        button_Residue.setText(Utils.getResString(R.string.Dialog_Btn_Delete_Yes));
        button_Residue.setTextColor(Color.WHITE);
        button_Residue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialogResidue dialogResidue = new AlertDialogResidue(
                        AddPlacardDialogActivity.this);
                dialogResidue.showdialog(sellection);
            }
        });
        //button click to show 'erg' description of un number
        button_Erg = (Button) findViewById(R.id.btnErg);
        setButtonViewErg(button_Erg, eT_UnNumber.getText().toString());
        button_Erg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] values = {eT_UnNumber.getText().toString(),
                        Utils.language};
                try {
                    if (NetworkUtil.getConnectivityStatus(getApplicationContext()) != 0) {
                        //async task to get 'ERG' details of un number from web server
                        AsyncTaskErg erg = new AsyncTaskErg(AddPlacardDialogActivity.this);
                        erg.execute(values).get();

                    } else {
                        //toast to show 'no network'
                        new Utils(AddPlacardDialogActivity.this)
                                .showToastMessage(Utils.getResString(R.string.Toast_No_Network));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // to scan Bar and QR code to get BL Number
        // scan Bill of lading
        Button scan_Bill_of_Lading_btn = (Button) findViewById(R.id.bill_of_lading_scan_btn);
        scan_Bill_of_Lading_btn.setText(Utils.getResString(R.string.Dialog_Add_Item_Btn_Scan));
        scan_Bill_of_Lading_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isCameraAvailable()) {
                    Intent intent = new Intent(AddPlacardDialogActivity.this,
                            ScannerActivity.class);
                    startActivityForResult(intent, 0);
                    overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(AddPlacardDialogActivity.this,
                            Utils.getResString(R.string.Rear_Camera_Unavailable),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // RADIO BUTTON WEIGHT GROUP
        Radio_Weight_Group = (RadioGroup) findViewById(R.id.radioWeightGroup);
        Radio_Weight_Group
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // On selecting radio button also we have to get values
                        // of gd weight and units to display dg gross mass

                    }
                });

        // DISPLAY BUTTON(Display Placard)
        button_DisplayPlacard = (Button) findViewById(R.id.display_btn);
        button_DisplayPlacard.setText(Utils.getResString(R.string.Dialog_Add_Item_Btn_Display));
        // By default button 'Display Placard' in disable state
        displayButtonViewChange();
        //'Display Placard' button click to show alert dialog
        //which takes input for placarding
        button_DisplayPlacard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DGLogicInput dgLogicInput = new DGLogicInput(getApplicationContext());
                //disable key board if visible
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                JSONObject response = null;//placard response from DG logic
                final String deviceId = new Utils().getDeviceId(getApplicationContext());//store device id
                // by default weight type is set to Kgs
                try {
                    // un number entered by user
                    String un_number = eT_UnNumber.getText().toString();
                    // DG weight entered by user
                    double dg_Weight = Double.parseDouble(eT_DGGrossMassPkg
                            .getText().toString());
                    // By default no of units is 1
                    int no_of_units = 1;//store 'no of units'
                    if (!eT_NumberOfUnits.getText().toString().equals("")) {
                        // no of units entered by user
                        no_of_units = Integer.parseInt(eT_NumberOfUnits
                                .getText().toString());
                    }
                    // gross weight which is used in placard selection logic
                    double gross_Weight = dg_Weight * no_of_units;//store gross weight

                    erap_no = eT_Erap.getText().toString();//to store erap number
                    // if response contains erap details array values are many,
                    // for example un: 1950 having erap details array values 15,
                    // means 15 diff types of erap details for the un Number
                    UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
                    //name = unInfo.get("name").get(0);
                    getInstance().setNonExempt(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(un_number), COL_NONEXCEMPT));
                    getInstance().setUnType(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(un_number), COL_UNTYPE));
                    getInstance().setUn_class_id(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(un_number), COL_UN_CLASS_ID));
                    getInstance().setDanger_placard(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(un_number), COL_DANGEROUS_PLACARD));
                    //oxygen placard check
                    PlacardDisplayLogic displayLogic1=new PlacardDisplayLogic();
                    String finalString1= displayLogic1.placardDisplayLogic(deviceId, deviceId);
                    checkOxygenPlacard(un_number, ibc_status, dg_Weight);
                    new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString1, "add",
                            new GetDBData(getApplicationContext()).getMaxTransId());

                    if (getInstance().getName().equalsIgnoreCase("7.3")) {
                        getInstance().setDanger_placard("1");

                        PlacardDisplayLogic displayLogic=new PlacardDisplayLogic();
                        String finalString= displayLogic.placardDisplayLogic(deviceId, deviceId);
                        new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, "add",
                                new GetDBData(getApplicationContext()).getMaxTransId());

                        //change danger_placard value to "0", because class 7,
                        //category type III, cannot be mixed with danger
                    }
                    // excemptValue(name, ship); just send non except value from on un number tab out
                    //  System.out.println("name "+name);
                    if (unNumberInfo.getUnNumberInfo(un_number).length() > 1) {
                        response = dgLogicInput.inputToDLogic(jsonEditTexts(), "key", "value");

                        PlacardDisplayLogic displayLogic=new PlacardDisplayLogic();
                        String finalString= displayLogic.placardDisplayLogic(deviceId, deviceId);
                        new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, "add",
                                new GetDBData(getApplicationContext()).getMaxTransId());
//						 Log.e(LOGTAG,
//						 "response in if means special case "+response);
                    } else {
                        response = dgLogicInput.inputToDLogic(jsonEditTexts(), "key", "value");

                        PlacardDisplayLogic displayLogic=new PlacardDisplayLogic();
                        String finalString= displayLogic.placardDisplayLogic(deviceId, deviceId);
                        new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, "add",
                                new GetDBData(getApplicationContext()).getMaxTransId());
//                        Log.e(LOGTAG,
//                                "json response to placard selection response  " + response);
                    }
                    //to check class 2.1 to display "will consignment a ship"
                    if (checkClass2_1(getInstance().getName())) {
                        AlertDialogClass2 alertDialogClass2 = new AlertDialogClass2(AddPlacardDialogActivity.this);
                        alertDialogClass2.showDialog(Utils.getResString(R.string.trans_on_ship), response);


                        return;
                    }
                    String popUp = check1000KgRules(getInstance().getName(), 0, gross_Weight);
                    //System.out.println("pop up "+popUp);
                    //after calling 1000kd rule method update consignee_danger value based on

                    if (popUp.split("::")[0].equalsIgnoreCase("1")) {
                        AlertDialogOn1000Kgs alertDialogOn1000Kgs = new AlertDialogOn1000Kgs(AddPlacardDialogActivity.this);
                        alertDialogOn1000Kgs.showDialog("Adding this load - exceeds 1000 Kg for the class3\n" +
                                "Are these loads from the same consignor?", response);

                        return;
                    }
                    // valid input from response and user values are insert into
                    // transaction_details table
                    AddTransaction addTransaction = new AddTransaction();

                    String value = addTransaction.PrintValues(deviceId, response);

                    // Abnormal case
                    if (value.startsWith("99")) {
                        AlertDailogPlacardAbnormalCase dialogAbnormal = new AlertDailogPlacardAbnormalCase(
                                AddPlacardDialogActivity.this);
                        String[] reponse = value.split("::");
                        String erroCode = reponse[0];
                        String errorMessage = reponse[1];
                        dialogAbnormal.showDialog(errorMessage, -1);
                        PlacardDisplayLogic displayLogic=new PlacardDisplayLogic();
                        String finalString= displayLogic.placardDisplayLogic(deviceId, deviceId);
                        new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, "add",
                                new GetDBData(getApplicationContext()).getMaxTransId());
                        return;
                    } else {
                        // valid case with proper response, error code as '00'
                        // and erros mgs as 'Success'danger
                        new PlacardDisplayLogic().placardDisplayLogic(deviceId, deviceId);
                        PlacardDisplayLogic displayLogic=new PlacardDisplayLogic();
                        String finalString= displayLogic.placardDisplayLogic(deviceId, deviceId);
                        new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, "add",
                                new GetDBData(getApplicationContext()).getMaxTransId());
//                        // Utils.finalPlacardCount = DangerousPlacardLogic.finalPlacardCount;//assign placarad count value as
                        /// max placards , so that automatic placard optimization and not enough placard works

                        Intent i = new Intent(AddPlacardDialogActivity.this,
                                HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        nosrow.setVisibility(View.GONE);
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Close button click event, on clicking cross mark close dialog
        ImageView cancelDialogBox = (ImageView) findViewById(R.id.dialog2remove);
        cancelDialogBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = AddPlacardDialogActivity.this.getCurrentFocus();
                if (view != null) {
                    //to hide key board if visible
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });

    }

    // on clicking back button, close dialog add item
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Focus change for all the edit texts
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.enter_bill_of_lading:
                whoHasFocus = 1;
                break;
            case R.id.enter_un_number:
                whoHasFocus = 2;
                break;
            case R.id.et_dgMassPkg:
                whoHasFocus = 3;
                break;
            case R.id.enter_Units:
                whoHasFocus = 4;
                break;
            case R.id.Dialog_Add_Item_ET_Erap:
                whoHasFocus = 5;
                break;
            case R.id.eTDgGrossTotal:
                whoHasFocus = 6;
                break;
        }
    }

    // Text Watcher for Five Edit Texts
    TextWatcher text_watcher_edittext = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (whoHasFocus) {
                case 1:
                    try {
                        if (!(eT_BillOfLading.getText().toString().equals(""))) {
                            eT_BillOfLading.setError(null);
                            displayButtonViewChange();
                        } else {
                            eT_BillOfLading
                                    .setError(Utils.getResString(R.string.Please_Enter_Bill_OF_lad_error));
                            displayButtonViewChange();
                        }// else
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    try {
                        UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
                        if (eT_UnNumber.getText().toString().trim().length() < 4) {
                            eT_UnNumber.setError(Utils.getResString(R.string.UN_Number_4Ditgits_error));
                            eT_Erap.setVisibility(View.GONE);
                            tV_UnDesc.setTextColor(Color.parseColor("#d3d3d3"));
                            tV_UnDesc.setText(Utils.getResString(R.string.TV_UN_Description));
                            checkIbcVisibility("");
                            setButtonViewErg(button_Erg, eT_UnNumber.getText()
                                    .toString().trim());
                            displayButtonViewChange();
                            nosrow.setVisibility(View.GONE);
                        } else {
                            eT_UnNumber.setError(null);
                            tV_UnDesc.setTextColor(Color.BLACK);
//						nosrow.setVisibility(View.VISIBLE);
                            setButtonViewErg(button_Erg, eT_UnNumber.getText()
                                    .toString());
                            Utils utils = new Utils();
                            String unnumber = eT_UnNumber.getText().toString();//store un number value from edit text
                            String units = eT_NumberOfUnits.getText().toString();//store 'no of units' from edit text
                            String weight = eT_DGGrossMassPkg.getText().toString();//store weight value from edit text
                            JSONObject jsonResponseCheckUnNumber = unNumberInfo.getUnNumberInfo(unnumber);

                            // System.out.println("lenght "+jsonResponseCheckUnNumber.length()+" suman "+jsonResponseCheckUnNumber.getJSONArray("result").length());
                            if (jsonResponseCheckUnNumber.length() == 0) {
                                // If length of response is empty then description
                                // may
                                // be Invalid un number or forbidden case
                                AlerDialogInValidUnNumber dialogInvalid = new AlerDialogInValidUnNumber(
                                        AddPlacardDialogActivity.this);
                                dialogInvalid.showDialg(Utils.getResString(R.string.Dialog_InvalidUn_Message));
                                return;
                            }// if
                            else if (jsonResponseCheckUnNumber.getJSONArray("result").length() > 1) {
                                Log.v("greater>1", jsonResponseCheckUnNumber.getJSONArray("result").toString());
                                JSONArray array = jsonResponseCheckUnNumber.getJSONArray("result");
                                ArrayList<JSONObject> description = new ArrayList<>(array.length());
                                JSONObject jobj = new JSONObject();
                                //getting descriptions from jsonarray
                                for (int i = 0; i < array.length(); i++) {
                                    jobj = array.getJSONObject(i);
                                    description.add(i, jobj);
                                    Log.v("description check", description.toString());
                                }
                                ArrayList<String> specaiCaseResponse = null;

                                specaiCaseResponse = utils.specialCase1(description, AddPlacardDialogActivity.this);
//							unnumber-->description
                                //for displaying list of description
                                AlertDialogSpecialCase dialogSpecialCase = new AlertDialogSpecialCase(
                                        AddPlacardDialogActivity.this);
                                //System.out.println(specaiCaseResponse);
                                dialogSpecialCase.showDialog1(specaiCaseResponse);
                                return;

                            } else {
                                JSONArray array = jsonResponseCheckUnNumber.getJSONArray("result");
                                Log.v("equals 1", jsonResponseCheckUnNumber.getJSONArray("result").toString());
                                String description1[] = new String[array.length()];
                                String text[] = null;
                                JSONObject obj = null;
                                JSONObject ob = array.getJSONObject(0);
                                String dry = ob.getString("name");
                                //checking for class 1 if so display alertdialog
                                if (dry.startsWith("1")) {

                                    AlertIsDryTrailer alertIsDryTrailer = new AlertIsDryTrailer(AddPlacardDialogActivity.this);
                                    alertIsDryTrailer.showAlertDryTrailer();
                                }

                                for (int i = 0; i < array.length(); i++) {
                                    obj = array.getJSONObject(i);
                                    description1[i] = obj.getString("description");
                                    Log.v(LOGTAG + "description",description1[i]);
                                }
                                String value = obj.getString("description");
                                //rajesh
                                if (value.contains("@")) {
                                    JSONArray specaiCaseResponse = utils
                                            .specialCase(obj.getString("description"), AddPlacardDialogActivity.this);
                                     Log.v(LOGTAG +"jaaray",specaiCaseResponse.toString());
                                    AlertDialogSpecialCase dialogSpecialCase = new AlertDialogSpecialCase(
                                            AddPlacardDialogActivity.this);
                                    //System.out.println(specaiCaseResponse);
                                    dialogSpecialCase.showDialog(specaiCaseResponse);
                                } else {
                                    Log.v(LOGTAG+"test123", obj.getString("description"));
                                    //rajesh
                                    text = value.split("#");
                                    Log.v(LOGTAG +"text0", text[0]);
                                    sometext= text[0];
                                    tV_UnDesc.setText(text[0].toString());
                                    if(text[0].contains("N.O.S")){
                                        Log.v("!@#",text[0]);
                                        nosrow.setVisibility(View.VISIBLE);
                                    }

                                }

                                getInstance().setName(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(unnumber), COL_NAME));
                                //Log.v(LOGTAG,"name "+name);
                                getInstance().setPp(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(unnumber), COL_PRIMARY_PLACARD));
                                // Log.v(LOGTAG,"pp "+name);
                                getInstance().setUn_class_id(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(unnumber), COL_UN_CLASS_ID));
                                //to check class7 to set name as 7.1,7.2 or 7.3 for name 7
                                checkClass7(getInstance().getName());
                                checkIbcVisibility(unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(unnumber), COL_UNTYPE));
                                eT_BillOfLading.clearFocus();//clear focus from 'bill of lading' edit text

                                // Check sp84 status to display pop up for the UN
                                // Number
                                // if sp84 status is 1 in the response show type of
                                // Virus List in pop up
                                if (unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(unnumber), "sp84status").equals("1")) {
                                    checkSp84Status(unnumber);
                                }
                                if (!units.equals("") && (!weight.equals(""))) {
                                    double gross_Weight = 0.0;
                                    gross_Weight = (Double.parseDouble(weight))
                                            * (Double.parseDouble(units));
                                    // to get net weight decimals upto 2 decimal points
                                    gross_Weight = utils
                                            .resrictDecimals((gross_Weight));
                                    checkPackage(unnumber, gross_Weight);

                                }// inner if
                                displayButtonViewChange();
                                text = value.split("#");
                                if (text[0].contains("N.O.S")) {
                                    nosrow.setVisibility(View.VISIBLE);
                                }
//
                            }
                        }// else
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    try {
                        int no_of_units = 0;//'no of units'
                        eT_GrossWeightTotal
                                .removeTextChangedListener(text_watcher_edittext);
                        String weight = eT_DGGrossMassPkg.getText().toString();//weight
                        // by suman, Date 28 Oct 2014 don't accept zeros as first
                        // character
                        if ((!weight.equalsIgnoreCase("")) && (Double.parseDouble(weight.trim()) <= 0)) {
                            eT_DGGrossMassPkg
                                    .setError(Utils.getResString(R.string.Dialog_Add_Item_Et_GRWeight));
                            eT_GrossWeightTotal.setText(String.valueOf(0.0));
                            displayButtonViewChange();
                            checkResidueVisibility(0.0);
                        } else {
                            UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
                            eT_DGGrossMassPkg.setError(null);
                            // EnterGrossWeight.clearFocus();
                            double dg_Weight = 0.0;
                            if (weight.equals("")
                                    || (weight.equals("0") || (weight.equals("00")))) {
                            } else if (weight.startsWith(".")) {
                                String wt = "0" + weight;
                            } else {
                                dg_Weight = Double.parseDouble(weight);
                                if (eT_NumberOfUnits.getText().toString().length() > 0) {
                                    no_of_units = Integer.parseInt(eT_NumberOfUnits
                                            .getText().toString());
                                    checkResidueVisibility(0.0);
                                }
                                Utils utils = new Utils();
                                double gross_Weight = dg_Weight * no_of_units;//total weight
                                // to get net weight decimals upto 2 decimal points
                                gross_Weight = utils
                                        .resrictDecimals((gross_Weight));
                                if (eT_UnNumber.getText().toString().length() > 0) {
                                    String pkgGroup = unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(eT_UnNumber.getText().toString()), COL_PKG_GROUP);
                                    enableErap(pkgGroup);
                                }
                                eT_GrossWeightTotal
                                        .removeTextChangedListener(text_watcher_edittext);
                                eT_GrossWeightTotal.setText(String
                                        .valueOf(gross_Weight));
                                eT_GrossWeightTotal
                                        .addTextChangedListener(text_watcher_edittext);
                                checkResidueVisibility(gross_Weight);
                                if (eT_UnNumber.getText().toString().length() > 3) {
                                    String unNumber = eT_UnNumber.getText().toString();
                                    checkPackage(unNumber,
                                            gross_Weight);
//								if (unInfo.get("sp84").size() > 0
//										&& (unInfo.get("sp84").equals("1"))) {
//									if (unInfo.get("pkg_group").size() > 0) {
//										AlertDialogPackage dialogPackage = new AlertDialogPackage(
//												AddPlacardDialogActivity.this);
//										dialogPackage.showDialog(unInfo
//												.get("pkg_group"));
//									}// if
//								}// if
                                }
                            }// else
                            displayButtonViewChange();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    try {
                        int units4 = 0;//'no of units'
                        double dgWeight4 = 0.0;//weight
                        String weight4 = eT_GrossWeightTotal.getText().toString();//gross weight
                        String units = eT_NumberOfUnits.getText().toString();//'no of units'
                        UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
                        if (!units.equals("")) {
                            if (!units.equals("0")) {
                                int no_of_units = Integer.parseInt(units);
//							if (!eT_DGGrossMassPkg.getText().toString()
//									.equals("")) {
//								Utils utils = new Utils();
//								double gross_Weight = Double
//										.parseDouble(eT_DGGrossMassPkg
//												.getText().toString())
//												* no_of_units;
//								gross_Weight = utils
//										.resrictDecimals(gross_Weight);//total weight
//                                if (eT_UnNumber.getText().toString().length() > 0) {
//                                    String pkgGroup = unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(eT_UnNumber.getText().toString()), COL_PKG_GROUP);
//                                    enableErap(pkgGroup);
//                                }
//								eT_GrossWeightTotal
//								.removeTextChangedListener(text_watcher_edittext);
//								eT_GrossWeightTotal.setText(String
//										.valueOf(gross_Weight));
//								eT_GrossWeightTotal
//								.addTextChangedListener(text_watcher_edittext);
//								checkResidueVisibility(gross_Weight);
//								if (eT_UnNumber.getText().toString().length() > 3) {
//									checkPackage(eT_UnNumber.getText()
//											.toString(), gross_Weight);
//								}
//							}// if
                                if (eT_NumberOfUnits.getText().toString()
                                        .trim().length() > 0
                                        && weight4.length() > 0) {
                                    double total = 0.0;
                                    units4 = Integer.parseInt(eT_NumberOfUnits
                                            .getText().toString().trim());//'no of units'
                                    dgWeight4 = Double.parseDouble(eT_GrossWeightTotal.getText().toString());
                                    total = new Utils().resrictDecimals(dgWeight4
                                            / units4);///weight
                                    if (eT_UnNumber.getText().toString().length() > 0) {
                                        String pkgGroup = unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(eT_UnNumber.getText().toString()), COL_PKG_GROUP);
                                        enableErap(pkgGroup);
                                    }
                                    eT_DGGrossMassPkg
                                            .removeTextChangedListener(text_watcher_edittext);
                                    eT_DGGrossMassPkg.setText("" + total);
                                    eT_DGGrossMassPkg
                                            .addTextChangedListener(text_watcher_edittext);
                                }
                            } else {
                                eT_NumberOfUnits
                                        .setError(Utils.getResString(R.string.Dialog_Add_Item_Et_Units));
                                checkResidueVisibility(0.0);
                            }
                        } else {
                            eT_NumberOfUnits
                                    .setError(Utils.getResString(R.string.Dialog_Add_Item_Et_Units));
                            checkResidueVisibility(0.0);
                        } // else
                        displayButtonViewChange();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 5:
                    try {
                        if (eT_Erap.getText().toString().length() > 0) {
                            displayButtonViewChange();
                        } else {
                            displayButtonViewChange();
                            eT_Erap.setError(Utils.getResString(R.string.Dialog_Add_Item_Et_Erap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        int units6 = 0;
                        double dgWeight6 = 0.0;
                        String weight6 = eT_GrossWeightTotal.getText().toString();
                        if (weight6.trim().length() < 0) {
                            eT_GrossWeightTotal
                                    .setError(Utils.getResString(R.string.Please_Enter_Gross_Weight_error));
                            displayButtonViewChange();
                        } else if ((!weight6.equalsIgnoreCase("")) && (Double.parseDouble(weight6.trim()) <= 0)) {
                            eT_GrossWeightTotal
                                    .setError(Utils.getResString(R.string.Please_Enter_Gross_Weight_error));
                            displayButtonViewChange();
                            displayButtonViewChange();
                        } else if (eT_NumberOfUnits.getText().toString().trim()
                                .length() > 0
                                && weight6.length() > 0) {
                            double total = 0.0;
                            units6 = Integer.parseInt(eT_NumberOfUnits.getText()
                                    .toString().trim());
                            dgWeight6 = Double.parseDouble(weight6);
                            total = new Utils().resrictDecimals(dgWeight6 / units6);
                            eT_DGGrossMassPkg
                                    .removeTextChangedListener(text_watcher_edittext);
                            eT_DGGrossMassPkg.setText("" + total);
                            if (!eT_UnNumber.getText().toString().isEmpty()) {
                                checkPackage(eT_UnNumber.getText().toString(),
                                        total);
                            }
                            eT_DGGrossMassPkg
                                    .addTextChangedListener(text_watcher_edittext);
                            displayButtonViewChange();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * This method is to check Oxygen placard status for un numbers 1072,1073,3156 and 3157.
     * <p/>
     * <p/>
     * author suman
     *
     * @param unNumber  un number
     * @param ibcStatus ibc status for un number
     * @param dgWeight  dg weight entered by user
     * @return Oxygen placard status
     */
    private boolean checkOxygenPlacard(final String unNumber, final String ibcStatus, final double dgWeight) {
        boolean status = false;
        try {
            if (unNumber.equalsIgnoreCase("1072")
                    || unNumber.equalsIgnoreCase("1073")
                    || unNumber.equalsIgnoreCase("3156")
                    || unNumber.equalsIgnoreCase("3157")) {
                status = true;
                DGLogicInput dgLogicInput = new DGLogicInput(getApplicationContext());
                if (eT_Erap.getVisibility() == View.VISIBLE) {
                    dgLogicInput.inputToDLogic(jsonEditTexts(), COL_UNNUMBER_DISPLAY, "1");
                } else {
                    dgLogicInput.inputToDLogic(jsonEditTexts(), COL_UNNUMBER_DISPLAY, "0");
                }
                if ((ibcStatus.equalsIgnoreCase("0")
                        && dgWeight <= 450)) {
                    getInstance().setName("2.2");
                    getInstance().setPp("2.2");
                    getInstance().setUn_class_id("41");
                    dgLogicInput.inputToDLogic(jsonEditTexts(), COL_UNTYPE, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * This method checks IBC visibility based on unType
     * <p/>
     * author Suman
     *
     * @param untype type of un number ex. liquid
     * @since v.b.5.3.3
     */

    public void checkIbcVisibility(final String untype) {
        try {
            // if un type from response of an un number is gas or liquid i.e
            // untype
            // "l" or "g" then display IBC selection spinner
            if (untype.equals("l") || untype.equals("g")) {
                unnumber_display = "1";
                button_IBC.setVisibility(View.VISIBLE);
                ibc_status = "1";
                tv_Ibc.setVisibility(View.VISIBLE);
                tv_Ibc.setText(Utils.getResString(R.string.Dialog_Add_Item_Tv_IBC));
            } else {
                unnumber_display = "0";
                ibc_status = "0";
                button_IBC.setVisibility(View.GONE);
                tv_Ibc.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // checkIbcVisibility()

    /**
     * This method is to check class 7 with categories , for class7 have to display
     * 3 categories I,II and III, on selection I placard 7.1, for II placard 7.2 and for III placard 7.3
     * and without un number on the placard
     *
     * @param name class name of un number
     */
    private void checkClass7(final String name) {
        try {
            ArrayList<String> list = new ArrayList<String>();
            list.add("I");
            list.add("II");
            list.add("III");
            if (name.equalsIgnoreCase("7")) {
                AlertDialogClass7Catogory alert = new AlertDialogClass7Catogory(AddPlacardDialogActivity.this);
                alert.showDialog(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to display "Will this consignment be transported on a ship?"
     * for class 2.1 based on "Yes" or "no" selection excemption rule applied
     *
     * @param name class name of a un number
     */
    private boolean checkClass2_1(final String name) {
        if (name.equalsIgnoreCase("2.1")) {
            return true;
        }
        return false;
    }

    /**
     * This method shows Dialog with Drop Down list for related list of Virus
     * type which are belongs to the UN Number if sp84 status is 1 from service
     * <p/>
     * author Suman
     *
     * @param unNumber <for a un number,check sp84 status >
     * @since 5.0
     */

    public void checkSp84Status(final String unNumber) {
        try {
            UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
            // if sp84 status is 1 then pop up all sp84 values to into drop down
            // to select the user
            //System.out.println("debug checkSp84Status::un "+unNumber+" res "+unNumberInfo.getUnNumberInfo(unNumber));
            String sp84Value = unNumberInfo.getMultiValues(unNumberInfo.getUnNumberInfo(unNumber), "sp84value").get(0);
            String[] sp84ValueResponse = sp84Value.split(",");
            String sp84ValueArray[] = new String[sp84ValueResponse.length];
            for (int i = 0; i < sp84ValueResponse.length; i++) {
                sp84ValueArray[i] = sp84ValueResponse[i].replace("#", " ");
            }// for
            AlertDialogSp84 dialogSp84 = new AlertDialogSp84(
                    AddPlacardDialogActivity.this);
            dialogSp84.showDialog(sp84ValueArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// checkSp84VirusType()

    /**
     * This method used to enable/disable based on package selection that is
     * selected by User
     *
     * @param package_group
     * @author Suman
     * @since 5.0
     */
    public void enableErap(final String package_group) {
        Utils utils = new Utils();
        //System.out.println("debug "+package_group);
        //for some un numbers , package name will be empty but erap status is "1",
        //then also we have to enable erap
        HashMap<String, HashMap<String, String>> pkgInfo = utils.getInfo(
                eT_UnNumber.getText().toString(), dialogActivity);
        //Log.v(LOGTAG,"pkgInfo "+pkgInfo);
        try {
            String erap_status = pkgInfo.get(package_group).get(COL_ERAP_STATUS);
            //Log.v(LOGTAG,"erap_status "+erap_status);
            if (erap_status.equals("1")) {
                // if any un number is having erap status as 1 then it states
                // that it must ask for erap value
                eT_Erap.setVisibility(View.VISIBLE);
                displayButtonViewChange();
            } else {
                eT_Erap.setVisibility(View.GONE);
                displayButtonViewChange();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }// enableErap

    /**
     * This method reruns package group list for a UN Number based on DG Gross
     * Weight
     * <p/>
     * author Suman
     *
     * @param unNumber     un number
     * @param gross_Weight gross weight (units*dg weight)
     * @since 5.0
     */
    public void checkPackage(final String unNumber, final double gross_Weight) {
        try {
            UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
            ArrayList<String> erap_status = unNumberInfo.getMultiValues(unNumberInfo.getUnNumberInfo(unNumber), COL_ERAP_STATUS);
            ArrayList<String> erap_index = unNumberInfo.getMultiValues(unNumberInfo.getUnNumberInfo(unNumber), COL_ERAP_INDEX);
            //System.out.println("debug checkPackage::un "+unNumber+" res "+unNumberInfo.getUnNumberInfo(unNumber));
            Utils utils = new Utils();
            for (int i = 0; i < erap_status.size(); i++) {
                if (erap_status.get(i).equalsIgnoreCase("See SP84")) {
                    // if erap_index value is "See SP84", then no need to
                    // check for
                    // gross weight exceeded or not,whatever the weight
                    // amount placard must be displayed
                    return;
                } else if (erap_status.get(i).equalsIgnoreCase("1")) {
                    int erapIndex = Integer.parseInt(erap_index.get(i));
                    if (pkgListToDisplay((double) erapIndex, gross_Weight)) {
                        // if erap index exceeds , gross weight we have to show
                        // existing packing group
                        ArrayList<String> pkg = unNumberInfo.getMultiValues(unNumberInfo.getUnNumberInfo(unNumber), COL_PKG_GROUP);
                        pkg = utils.eliminateDuplicatesFromList(pkg);
                        //System.out.println("debug size "+pkg.size());
                        if (!pkg.get(i).equalsIgnoreCase("") && pkg.size() > 0) {
                            AlertDialogPackage dialogPackage = new AlertDialogPackage(
                                    dialogActivity);
                            if (flagDialogPackage == false) {
                                flagDialogPackage = true;
                                dialogPackage.showDialog(pkg);
                            }
                        }
                    } else {
                        // make erap edit text disable
                        // value when it was enabled
                        if (eT_Erap.getVisibility() == View.VISIBLE) {
                            eT_Erap.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// checkPackage()

    /**
     * This method returns package group display status, based on weight per
     * package value
     * <p/>
     * author Suman
     *
     * @param erapIndex ERAP index of un number
     * @param dgMass    dg weight
     * @return package group display status
     * @since 5.0
     */
    public boolean pkgListToDisplay(final double erapIndex, final double dgMass) {
        boolean status = false;
        if (erapIndex <= dgMass) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }// pkgCheck()

    /**
     * This method checks residue visibility based on IBC selection
     * <p/>
     * author suman
     *
     * @param gross_Weight gross weight(units*dg weight)
     * @since v.b.5.3.3
     */
    public void checkResidueVisibility(final double gross_Weight) {
        try {
            // if IBC selection is "yes"(If it is visible) and gross weight
            // varies between 0 to 450 kgs
            // have to display Residue selection spinner
            if (button_IBC.getVisibility() == View.VISIBLE
                    && button_IBC.getText().toString()
                    .equals(Utils.getResString(R.string.Dialog_Btn_Delete_Yes))) {
                if (gross_Weight > 0 && gross_Weight < 450) {
                    button_Residue.setVisibility(View.VISIBLE);
                    tv_Residue.setVisibility(View.VISIBLE);
                    ibc_residue_status = "1";
                    tv_Residue.setText(Utils.getResString(R.string.Dialog_Add_Item_Tv_Residue));
                } else {
                    button_Residue.setVisibility(View.GONE);
                    ibc_residue_status = "0";
                    tv_Residue.setVisibility(View.GONE);
                }
            } else {
                button_Residue.setVisibility(View.GONE);
                tv_Residue.setVisibility(View.GONE);
                ibc_residue_status = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // checkResidueVisibility()

    /**
     * This method used to get visibility of erap, to set un number display
     * status
     *
     * author Suman
     * @since v.b.5.2.8
     */

    /**
     * This method checks whether camera available for device
     *
     * @return boolean
     */
    // this method used in scanning a bar/QR code
    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    // Getting bar or QR code results from scanner activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        String scanResult = data.getStringExtra(SCAN_RESULT);
                        // Don't remove this static type in AlertDialogAddPlacard class
                        eT_BillOfLading.setText(scanResult);
                        // Log.i(DialogActivity.this.getClass().getSimpleName(),
                        // "Bar Code ==: " + scanResult);
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
                        String error = data.getStringExtra(ERROR_INFO);
                        if (!TextUtils.isEmpty(error)) {
                            // Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * This changes "Add Placard" button visibility based on validations
     * <p/>
     * author suman
     */
    @SuppressLint("ResourceAsColor")
    public static void displayButtonViewChange() {
        try {
            if ((eT_DGGrossMassPkg.getText().toString().trim().length() > 0)
                    && (eT_BillOfLading.getText().toString().trim().length() > 0)
                    && (eT_UnNumber.getText().toString().trim().length() > 3)
                    && (eT_NumberOfUnits.getText().toString().trim().length() > 0)) {
                if (eT_Erap.getVisibility() == View.VISIBLE) {
                    if (eT_Erap.getText().toString().trim().length() > 0) {
                        // When erap enable it must be filled and also three
                        // remaining
                        // edit texts must be filled so enable button
                        displayButtonTrue();
                    } else {
                        // When erap enabled, three remaining fields are filled
                        // but erap not
                        // entered so disable button
                        displayButtonFalse();
                    }
                } else {
                    // when erap is not exist, if three remaining edit texts are
                    // valid enable button
                    displayButtonTrue();
                }// inner else
            } else {
                // if any of the filed not entered disable button
                displayButtonFalse();
            }// outer else
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// displayButtonViewChange()

    /**
     * This method is used to change ERG button display, On valid un number ERG
     * button enabled
     * <p/>
     * author suman
     *
     * @param erg      ERg button to set enable/disable
     * @param unnumber un number
     */
    public void setButtonViewErg(final Button erg, final String unnumber) {
        try {
            if (unnumber.length() < 4) {
                erg.setEnabled(false);
                Drawable mDrawable = mResources.getDrawable(
                        R.drawable.button_color_disable);
                erg.setTextColor(Color.parseColor("#452300"));
                erg.setBackgroundDrawable(mDrawable);
            } else {
                erg.setEnabled(true);
                Drawable mDrawable = mResources.getDrawable(
                        R.drawable.button_color_bg_orange);
                erg.setTextColor(Color.parseColor("#FFFFFF"));
                erg.setBackgroundDrawable(mDrawable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// setButtonViewErg()

    /**
     * This changes button to enabled state
     */
    private static void displayButtonTrue() {
        Drawable mDrawable = mResources
                .getDrawable(R.drawable.button_color_bg_orange);
        button_DisplayPlacard.setTextColor(Color
                .parseColor("#FFFFFF"));
        button_DisplayPlacard.setBackgroundDrawable(mDrawable);
        button_DisplayPlacard.setEnabled(true);
    }

    /**
     * This changes button to disabled state
     */
    private static void displayButtonFalse() {
        Drawable mDrawable = mResources
                .getDrawable(R.drawable.button_color_disable);
        button_DisplayPlacard.setTextColor(Color
                .parseColor("#452300"));
        button_DisplayPlacard.setBackgroundDrawable(mDrawable);
        button_DisplayPlacard.setEnabled(false);
    }

    /**
     * Change except value for class 2.1 on user selection if "yes" value is 1 else 0
     * and also exempt vaue 1 for bewlow mentioned classes irrespective of user slection
     *
     * @param name
     * @param ship
     * @return excemptValue 0 or 1
     */
    public static int excemptValue(final String name, final int ship) {
        if (name.equals("1.1") || name.equals("1.2") || name.equals("1.3") || name.equals("1.5") || name.equals("2.3") ||
                name.equals("4.3") || name.equals("5.2") || name.equals("6.1") || name.equals("7.3") || (name.equals("2.1") && ship == 1)) {
            exemption = 1;
        }
        return exemption;
    }

    /**
     * This method is to check 1000kg rule
     *
     * @param name        class name of un number
     * @param ship        ship type like by ship or road
     * @param grossWeight gross weight(units*dg weight)
     * @return pop up display status and consinee classdanger value
     */
    public static String check1000KgRules(final String name, final int ship, final double grossWeight) {
        double cumWeight = 0;
        int consignee_danger = 0;
        int ibc = 0;
        int weight = 0;
        double finalGrossMass = 0;
        int popUpValue = 0;
        try {
            if (ibc_status.equals("0")) {
                if (grossWeight <= 1000) {
                    if (!(name.equals("1.1") || name.equals("1.2") || name.equals("1.3") ||
                            name.equals("1.4") || name.equals("1.4S") || name.equals("1.5") || name.equals("2.3") ||
                            name.equals("4.3") || name.equals("5.2") || name.equals("6.1") || name.equals("7.3") || (name.equals("2.1") && ship == 1))) {
                        ArrayList<HashMap<String, String>> bannerData = new GetDBData(
                                HomeActivity.getContext()).getBannerData();
                        for (int i = 0; i < bannerData.size(); i++) {
//                    final String completeString = bannerData.get(i);
//                    System.out.println("completeString "+completeString);
                            if (name.equals(bannerData.get(i).get(COL_NAME))) {
                                String ibcBanner = "0";
                                int grossMass = Integer.parseInt(
                                        new GetDBData(HomeActivity.getContext()).getBannerData().get(i).get(COL_GROSS_WEIGHT));
                                ibcBanner = new GetDBData(HomeActivity.getContext()).getBannerData().get(i).get(COL_IBC_STATUS);
                                cumWeight = cumWeight + grossMass;
                                //8System.out.println("cum wt "+cumWeight);
                                if (grossMass > 1000) {
                                    weight = 1;
                                }
                                if (ibcBanner.equalsIgnoreCase("1")) {
                                    ibc = 1;
                                }
                                finalGrossMass = grossWeight + cumWeight;
                            }
                        }

                        if (finalGrossMass > 1000 && ibc != 1 && weight != 1) {
                            popUpValue = 1;
                        }
                    } else {
                        consignee_danger = 0;//changed to work placard for class 1, 12-05-2016
                    }
                } else {
                    if (name.equals("1.4S")) {
                        consignee_danger = 0;
                    } else {
                        consignee_danger = 1;
                    }

                }
            } else {
                consignee_danger = 1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return popUpValue + "::" + consignee_danger;
    }

    private JSONObject jsonEditTexts() {
        JSONObject jsonObject = new JSONObject();
        DGLogicInput dgLogicInput = new DGLogicInput();
        UnNumberInfo unNumberInfo = new UnNumberInfo(getApplicationContext());
        try {
            String consignee_dangerArray[] = check1000KgRules(getInstance().getName(), 0, Double.parseDouble(eT_GrossWeightTotal.getText().toString().trim())).split("::");
            String consignee_danger = consignee_dangerArray[1];
            jsonObject.put(ET_BL, eT_BillOfLading.getText().toString().trim());
            jsonObject.put(ET_UNNUMBER, eT_UnNumber.getText().toString().trim());
            jsonObject.put(ET_DGWEIGHT, eT_DGGrossMassPkg.getText().toString().trim());
            jsonObject.put(ET_NO_OF_UNITS, eT_NumberOfUnits.getText().toString().trim());
            jsonObject.put(ET_GROSS_WEIGHT, eT_GrossWeightTotal.getText().toString().trim());
            //when erap text filed is visible get erap value
            if (eT_Erap.getVisibility() == View.VISIBLE) {
                jsonObject.put(ET_ERAP, eT_Erap.getText().toString().trim());
            } else {
                jsonObject.put(ET_ERAP, "");
            }
            jsonObject.put(RADIO_GROUP_WT_TYPE, Radio_Weight_Group.getCheckedRadioButtonId());
            jsonObject.put(DEVICE_ID, new Utils().getDeviceId(getApplicationContext()));
            jsonObject.put(COL_NAME, getInstance().getName());
            jsonObject.put(COL_PRIMARY_PLACARD, getInstance().getPp());
            jsonObject.put(COL_UN_CLASS_ID, getInstance().getUn_class_id());
            jsonObject.put(COL_CONSIGNEE_DANGER, Integer.parseInt(consignee_danger));
            jsonObject.put(COL_UNNUMBER_DISPLAY, dgLogicInput.setUndisplayStatus(eT_Erap, ibc_status));
            if (button_IBC.getVisibility() == View.VISIBLE && button_IBC.getText().toString().equalsIgnoreCase("Yes")) {
                jsonObject.put(COL_IBC_STATUS, "1");
            } else {
                jsonObject.put(COL_IBC_STATUS, "0");
            }
            jsonObject.put(COL_IBC_RESIDUE_STATUS, ibc_residue_status);
            jsonObject.put(COL_DESCRIPTION, unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(eT_UnNumber.getText().toString().trim()), COL_DESCRIPTION));
            jsonObject.put(COL_OPTIMISE, "1");
            jsonObject.put(COL_PKG_GROUP, pkg_group);
            if (nosrow.getVisibility() == View.VISIBLE) {
                if (nos_text.getText().toString().length() >= 1)
                    jsonObject.put(COL_NOS, nos_text.getText().toString().trim());
                else jsonObject.put(COL_NOS, "1");
            } else {

                jsonObject.put(COL_NOS, "1");
            }
            jsonObject.put(COL_DANGEROUS_PLACARD, getInstance().getDanger_placard());
            //Log.v(LOGTAG,"jsonObject " + jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public DataCenter getInstance() {
        return DataCenter.getInstance();
    }
}