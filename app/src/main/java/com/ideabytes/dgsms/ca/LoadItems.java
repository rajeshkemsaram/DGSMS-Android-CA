//package com.ideabytes.dgsms.ca;
//
//import android.app.Activity;
//import android.app.DialogFragment;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.text.method.ScrollingMovementMethod;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.ideabytes.dgsms.ca.alertdialog.AlertDialogIbc;
//import com.ideabytes.dgsms.ca.alertdialog.AlertDialogResidue;
//import com.ideabytes.dgsms.ca.erg.AsyncTaskErg;
//import com.ideabytes.dgsms.ca.exceptions.Exceptions;
//import com.ideabytes.dgsms.ca.localization.Localization;
//import com.ideabytes.dgsms.ca.model.DataCenter;
//import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
//import com.ideabytes.dgsms.ca.scan.ScannerActivity;
//import com.ideabytes.dgsms.ca.textwatcher.GenericTextWatcher;
//import com.ideabytes.dgsms.ca.utils.Utils;
//import com.ideabytes.dgsms.landstar.R;
//
//
//import org.com.ca.dgsms.ca.model.DBConstants;
//
//import java.util.Arrays;
//
///**
// * Created by suman on 13/5/16.
// */
//public  class LoadItems extends DialogFragment implements DBConstants, View.OnFocusChangeListener{
//    private Activity activity;//Activity reference
//    private View rootView;
//    // Edit texts
//    public static EditText eT_BillOfLading;//edit text for 'bill of lading'
//    public static EditText eT_UnNumber;//edit text for 'un number'
//    public static EditText eT_DGGrossMassPkg;//edit text for 'weight per package'
//    public static EditText eT_NumberOfUnits;//edit text for 'no of units'
//    public static EditText eT_Erap;//edit text for 'ERAP'
//    public static EditText eT_GrossWeightTotal;//edit text for 'gross weight'
//    // TextViews
//    public static TextView tV_UnDesc;//text view to display 'un description'
//    public static TextView tv_Ibc;//text view to display 'ibc select'
//    public static TextView tv_Residue;//text view to display 'residue select'
//    public static TextView tvTitle;//text view to display 'residue select'
//    // button
//    public static Button button_IBC;//button for 'ibc'
//    public static Button button_Residue;//button for 'residue'
//    public static Button button_DisplayPlacard;////button for 'Display Placard'
//    public static Button button_Erg;//button for 'ERG'
//    private int whoHasFocus = 0;//variable to notice exit text focus
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle bundle) {
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        //by default load single placard as max placard value
//        rootView = inflater.inflate(R.layout.custom_dialog_add_item, container, false);
//        activity = getActivity();
//        DataCenter dataCenter = DataCenter.getInstance();
//        tvTitle = (TextView) rootView
//                .findViewById(R.id.tvDialogTitle);//text view to display alert dialog title
//        tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));
//        // Enter Bill of Lading
//        eT_BillOfLading = (EditText) rootView. findViewById(R.id.enter_bill_of_lading);
//        dataCenter.seteTBillOfLading(eT_BillOfLading);
//        eT_BillOfLading.setHint(Utils.getResString(R.string.Dialog_Add_Item_Et_BL));
//        // Enter UN NUMBER
//        eT_UnNumber = (EditText) rootView. findViewById(R.id.enter_un_number);
//        dataCenter.seteTUnNumber(eT_UnNumber);
//        eT_UnNumber.setHint(Utils.getResString(R.string.Dialog_Add_Item_Et_Unumber));
//        eT_UnNumber.setOnFocusChangeListener(this);
//        // un number description
//        tV_UnDesc = (TextView) rootView. findViewById(R.id.TV_Un_Desc);
//        dataCenter.settVUnDesc(tV_UnDesc);
//        // Enter dg Weight
//        eT_DGGrossMassPkg = (EditText) rootView .findViewById(R.id.et_dgMassPkg);
//        dataCenter.seteTDGGrossMassPkg(eT_DGGrossMassPkg);
//        eT_DGGrossMassPkg.setHint(Utils.getResString(R.string.Et_DG_Mass_Pkg));
//        eT_DGGrossMassPkg.setOnFocusChangeListener(this);
//        tV_UnDesc.setMovementMethod(new ScrollingMovementMethod());
//        // DG Gross mass(units*dg weight)
//        eT_GrossWeightTotal = (EditText) rootView .findViewById(R.id.eTDgGrossTotal);
//        dataCenter.seteTGrossWeightTotal(eT_GrossWeightTotal);
//        eT_GrossWeightTotal.setOnFocusChangeListener(this);
//        eT_GrossWeightTotal.setHint(Utils.getResString(R.string.TV_DG_Gross_Mass));
//        // NUMBER OF UNITS
//        eT_NumberOfUnits = (EditText) rootView. findViewById(R.id.enter_Units);
//        dataCenter.seteTNumberOfUnits(eT_NumberOfUnits);
//        eT_NumberOfUnits.setOnFocusChangeListener(this);
//        eT_NumberOfUnits.setHint(Utils.getResString(R.string.Et_No_Of_Pkg));
//        // enter erap number
//        eT_Erap = (EditText) rootView. findViewById(R.id.Dialog_Add_Item_ET_Erap);
//        dataCenter.seteTErap(eT_Erap);
//        eT_Erap.setVisibility(View.GONE);
//        eT_Erap.setHint(Utils.getResString(R.string.Et_ERAP));
//        tv_Ibc = (TextView) rootView. findViewById(R.id.Dialog_Add_Item_Tv_IBC);
//        dataCenter.setTvIbc(tv_Ibc);
//        tv_Ibc.setVisibility(View.GONE);
//
//        tv_Residue = (TextView) rootView. findViewById(R.id.Dialog_Add_Item_Tv_Residue);
//        dataCenter.setTvResidue(tv_Residue);
//        tv_Residue.setVisibility(View.GONE);
//        //button ibc to select 'ibc status', on click this will show alert dialog to select ibc status
//        button_IBC = (Button) rootView. findViewById(R.id.spinnerIbc);
//        dataCenter.setBtnIBC(button_IBC);
//        button_IBC.setVisibility(View.GONE);
//        button_IBC.setText(Utils.getResString(R.string.Dialog_Btn_Delete_Yes));
//        button_IBC.setTextColor(Color.WHITE);
//        button_IBC.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                AlertDialogIbc spinner = new AlertDialogIbc(activity);
//                String[] selection = { Utils.getResString(R.string.Dialog_Btn_Delete_Yes),
//                        Utils.getResString(R.string.Dialog_Btn_Delete_No) };
//                spinner.showdialog(selection);
//            }
//        });
//        //button to select 'residue status', on click this shows a dialog to select residue status
//        button_Residue = (Button) rootView .findViewById(R.id.spinnerResidue);
//        dataCenter.setBtnResidue(button_Residue);
//        button_Residue.setVisibility(View.GONE);
//        button_Residue.setText(Utils.getResString(R.string.Dialog_Btn_Delete_Yes));
//        button_Residue.setTextColor(Color.WHITE);
//        button_Residue.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                AlertDialogResidue dialogResidue = new AlertDialogResidue(
//                        activity);
//                String[] selection = { Utils.getResString(R.string.Dialog_Btn_Delete_Yes),
//                        Utils.getResString(R.string.Dialog_Btn_Delete_No) };
//                dialogResidue.showdialog(selection);
//            }
//        });
//        // to scan Bar and QR code to get BL Number
//        // scan Bill of lading
//        Button scan_Bill_of_Lading_btn = (Button) rootView. findViewById(R.id.bill_of_lading_scan_btn);
//        scan_Bill_of_Lading_btn.setText(Utils.getResString(R.string.Dialog_Add_Item_Btn_Scan));
//        scan_Bill_of_Lading_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                if (isCameraAvailable()) {
//                    Intent intent = new Intent(activity,
//                            ScannerActivity.class);
//                    activity.startActivityForResult(intent, 0);
//                } else {
//                    Toast.makeText(activity,
//                            Utils.getResString(R.string.Rear_Camera_Unavailable),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        //button click to show 'erg' description of un number
//        Button button_Erg = (Button) rootView. findViewById(R.id.btnErg);
//        dataCenter.setBtnErg(button_Erg);
//        button_Erg.setEnabled(false);
//        button_Erg.setTextColor(Color.parseColor("#FFFFFF"));
//        button_Erg.setBackgroundColor(Color.parseColor("#452300"));
//        button_Erg.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                String[] values = { eT_UnNumber.getText().toString(),
//                        Utils.language };
//                try {
//                    if (NetworkUtil.getConnectivityStatus(activity.getApplicationContext()) != 0) {
//                        //async task to get 'ERG' details of un number from web server
//                        AsyncTaskErg erg = new AsyncTaskErg(activity);
//                        erg.execute(values).get();
//                    } else {
//                        //toast to show 'no network'
//                        new Utils(activity)
//                                .showToastMessage(Localization.Toast_No_Network);
//                    }
//                } catch (Exception e) {
//                    new Exceptions(activity.getApplicationContext(),activity
//                            .getClass().getName(), "Error::AsynkActivity" +
//                            Arrays.toString(e.getStackTrace()));
//                    e.printStackTrace();
//                }
//            }
//        });
//        Button btnLoadItem = (Button) rootView
//                .findViewById(R.id.display_btn);//button to dismiss
//
//        btnLoadItem.setText(Localization.Dialog_Add_Item_Btn_Display);
//        btnLoadItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        // Close button click event, on clicking cross mark close dialog
//        ImageView cancelDialogBox = (ImageView) rootView. findViewById(R.id.dialog2remove);
//        cancelDialogBox.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                View view = activity.getCurrentFocus();
//                if (view != null) {
//                    //to hide key board if visible
//                    InputMethodManager imm = (InputMethodManager) activity. getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//               dismiss();
//            }
//        });
//        return rootView;
//    }
//
//    /**
//     * This method checks whether camera available for device
//     *
//     * @return boolean
//     */
//    // this method used in scanning a bar/QR code
//    private boolean isCameraAvailable() {
//        PackageManager pm = activity.getPackageManager();
//        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
//    }
//    // Getting bar or QR code results from scanner activity
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 0:
//                try {
//                    if (resultCode == Activity.RESULT_OK) {
//                        String scanResult = data.getStringExtra(SCAN_RESULT);
//                        // Don't remove this static type in AlertDialogAddPlacard class
//                        eT_BillOfLading.setText(scanResult);
//                         Log.i(LoadItems.this.getClass().getSimpleName(),
//                                 "Bar Code ==: " + scanResult);
//                        break;
//                    } else if (resultCode == Activity.RESULT_CANCELED && data != null) {
//                        String error = data.getStringExtra(ERROR_INFO);
//                        if (!TextUtils.isEmpty(error)) {
//                            // Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } catch (Exception e) {
////                    new Exceptions(activity.getApplicationContext(),activity
////                            .getClass().getName(), "Error::AsynkActivity" +
////                            Arrays.toString(e.getStackTrace()));
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        switch (v.getId()) {
//            case R.id.enter_un_number:
//                eT_UnNumber.addTextChangedListener(GenericTextWatcher.intit(activity, eT_UnNumber));
//                break;
//            case R.id.et_dgMassPkg:
//                eT_DGGrossMassPkg.addTextChangedListener(GenericTextWatcher.intit(activity, eT_DGGrossMassPkg));
//                break;
//            case R.id.enter_Units:
//                eT_NumberOfUnits.addTextChangedListener(GenericTextWatcher.intit(activity, eT_NumberOfUnits));
//                break;
//            case R.id.eTDgGrossTotal:
//                eT_GrossWeightTotal.addTextChangedListener(GenericTextWatcher.intit(activity, eT_GrossWeightTotal));
//                break;
//        }
//    }
//
//    @Override
//    public Object getInstance() {
//        return null;
//    }
//}
