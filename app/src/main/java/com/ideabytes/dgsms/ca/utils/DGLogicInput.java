package com.ideabytes.dgsms.ca.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;

import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : DGLogicInput
 * author:  Suman
 * Description : To Show placards input dialog to take from user
 * Created Date :30-11-2015
 * Modified Date : 01-12-2015
 * Reason : To change unnumber display status for dg input
 ****************************************************************/

public class DGLogicInput implements DBConstants {
    public Context context;
    public DGLogicInput() {

    }
    public DGLogicInput(Context context) {
        this.context = context;
    }
    /**
     * This method prepares input to placard selection logic, based on un number
     * and details entered by user in "Add Item" dialog
     *
     * author suman
     * @param inputToDGLogic this input can be given to DGLogic to get placards
     * @param key , key to replace for json input object
     * @param value , value to replace for json input object
     * @return JSONObject
     */
    public JSONObject inpuToPlacardSelection(final JSONObject inputToDGLogic,final String key,final String value) {
        try {
           // System.out.println("inpuToPlacardSelection before "+inputToDGLogic);
            JSONArray jsonArray = inputToDGLogic.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectinDataArray = jsonArray.getJSONObject(i);
                if (jsonObjectinDataArray.has(key)) {
                    //the key has to be replaced with value, in some of cases value of key for dg logic input to be changed
                    //and reconsrtuct the input json object
                    jsonObjectinDataArray.remove(key);
                    jsonObjectinDataArray.put(key,value);
                }
                JSONArray erapArray = jsonObjectinDataArray
                        .getJSONArray("erapdetails");
                // System.out.println("erap "+erapArray);
                for (int j = 0; j < erapArray.length(); j++) {
                    JSONObject jsonObjectin = erapArray.getJSONObject(j);
                    if (jsonObjectin.has(key)) {
                        //the key has to be replaced with value, in some of cases value of key for dg logic input to be changed
                        //and reconsrtuct the input json object
                        jsonObjectin.remove(key);
                        jsonObjectin.put(key,value);
                    }
                }
            }
            inputToDGLogic.put("Data", jsonArray);
            //System.out.println("inpuToPlacardSelection after "+inputToDGLogic);
        } catch (Exception e) {
            new Exceptions(context,DGLogicInput.this
                    .getClass().getName(), "Error::DGLogicInput::inpuToPlacardSelection()" +
                    Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return inputToDGLogic;
    }
    public JSONObject inputToDLogic(final JSONObject jsonObject,final String key,final String value) {
        JSONObject jsonResponse = null;
        try {
            Log.v("jsoncheck",jsonObject.toString());
            JSONObject inputToUtils = new JSONObject();
            UnNumberInfo unNumberInfo = new UnNumberInfo(context);
            inputToUtils.put(COL_USER_ID, jsonObject.getString(DEVICE_ID));
            inputToUtils.put(COL_TRANSACTION_ID, jsonObject.getString(DEVICE_ID));
            inputToUtils.put(COL_BL, jsonObject.getString(ET_BL));
            inputToUtils.put(COL_UN_NUMBER, jsonObject.getString(ET_UNNUMBER));
            double weight_in_kgs = 0.0;
            if (jsonObject.getInt(RADIO_GROUP_WT_TYPE) == R.id.RbKgs) {
                inputToUtils.put(COL_WEIGHT_TYPE, "1");
                weight_in_kgs = Double.parseDouble(jsonObject.getString(ET_GROSS_WEIGHT));
                inputToUtils.put(COL_WEIGHT_IN_KGS,
                        weight_in_kgs   );
            } else {
                inputToUtils.put(COL_WEIGHT_TYPE, "0");
                weight_in_kgs = Double.parseDouble(jsonObject.getString(ET_GROSS_WEIGHT));
                //if weight type is Lbs then convert gross weight from Kgs to Lbs
                inputToUtils.put(COL_WEIGHT_IN_KGS,
                        new Utils().convertWeightToKgs(weight_in_kgs));
            }
            inputToUtils.put(COL_DG_WEIGHT, new Utils().resrictDecimals(Double.parseDouble(jsonObject.getString(ET_DGWEIGHT))));
            int no_of_units = Integer.parseInt(jsonObject.getString(ET_NO_OF_UNITS));
            inputToUtils.put(COL_NO_OF_UNITS, no_of_units);
            inputToUtils.put(COL_GROSS_WEIGHT, Double.parseDouble(jsonObject.getString(ET_GROSS_WEIGHT)));
            inputToUtils.put(COL_SUBSIDARY_EXIST, Utils.subsidaryExist);
            inputToUtils.put(COL_PACKAGE_WEIGHT,
                    new Utils().resrictDecimals(Double.parseDouble(jsonObject.getString(ET_DGWEIGHT))));
            inputToUtils.put(COL_IBC_STATUS, jsonObject.getString(COL_IBC_STATUS));
            inputToUtils.put(COL_IBC_RESIDUE_STATUS,
                    jsonObject.getString(COL_IBC_RESIDUE_STATUS));
            inputToUtils.put(COL_USER_ID_WEB, "1");//TODO
            inputToUtils.put(COL_TRANSACTION_ID_WEB,
                    "1");//TODO
            inputToUtils.put(COL_OPTIMISE, jsonObject.getString(COL_OPTIMISE));
            inputToUtils.put(COL_CONSIGNEE_DANGER, jsonObject.getString(COL_CONSIGNEE_DANGER));
            inputToUtils.put(COL_UN_SYLE, "2");
            inputToUtils.put(COL_INSERTED_DATE_TIME,
                    Utils.getPresentDateTime());
            inputToUtils.put(COL_NOS_DETAILS,jsonObject.getString(COL_NOS_DETAILS));
            JSONObject unInfo = unNumberInfo.getUnNumberInfo(jsonObject.getString(ET_UNNUMBER));
            // System.out.println("unInfo "+unInfo);
            JSONArray local = unInfo.getJSONArray("result");
            for (int i = 0; i < local.length(); i++) {
                JSONObject resultObject = local.getJSONObject(i);
                JSONArray erap = resultObject.getJSONArray("erapdetails");
                // This happens for some UN Numbers, means without package group
                // and erap index
                if (erap.length() < 1) {
                    inputToUtils.put(COL_PKG_GROUP, "");
                    inputToUtils.put(COL_ERAP_INDEX, "");
                }
                for (int j = 0; j < erap.length(); j++) {
                    JSONObject innerObject = erap.getJSONObject(j);
                    inputToUtils.put(COL_ERAP_INDEX,
                            innerObject.getString(COL_ERAP_INDEX));
                    if (AddPlacardDialogActivity.button_IBC.getText().toString()
                            .equals(Utils.getResString(R.string.Dialog_Btn_Delete_Yes))) {
                        inputToUtils.put(COL_ERAP_INDEX,
                                erap.getJSONObject(0).getString(COL_ERAP_INDEX));
                    }
                    if (innerObject.getString(COL_ERAP_INDEX).startsWith("S")) {
                        inputToUtils.put(COL_ERAP_INDEX, "0");
                    }
                    inputToUtils.put(COL_PKG_GROUP, jsonObject.getString(COL_PKG_GROUP));
                }
                inputToUtils.put(COL_WEIGHT_INDEX,
                        resultObject.getString(COL_WEIGHT_INDEX));
                //for class 1.4s and 1s no need to placard, change to 9999999999
                if (jsonObject.getString(COL_NAME).equalsIgnoreCase("1.4S")) {
                    inputToUtils.put(COL_WEIGHT_INDEX,
                            "9999999999");
                }
                //handled in inpu change, actula weight_index from un tab out is 1000, but need to placard at 10,260816
                if (jsonObject.getString(ET_UNNUMBER).equalsIgnoreCase("0301")) {
                    inputToUtils.put(COL_WEIGHT_INDEX,
                            "10");
                }

                // if un type is "l", set erap index to 450
                if ((resultObject.getString(COL_UNTYPE).equals("l"))
                        && (weight_in_kgs / no_of_units) > 450) {
                    inputToUtils.put(COL_WEIGHT_INDEX, "450");
                } else if (AddPlacardDialogActivity.button_IBC.getText().toString()
                        .equals(Utils.getResString(R.string.Dialog_Btn_Delete_Yes))) {
                    if (AddPlacardDialogActivity.button_IBC.getVisibility() == View.VISIBLE) {
                        inputToUtils.put(COL_WEIGHT_INDEX, "0");
                    }
                    if (jsonObject.getString(COL_NAME).equalsIgnoreCase("7.3")) {
                        inputToUtils.put(COL_WEIGHT_INDEX, "0");
                    }
                }
                inputToUtils.put(COL_UN_CLASS_ID, jsonObject.getString(COL_UN_CLASS_ID));
                inputToUtils.put(COL_GROUP_NAME, resultObject.getString(COL_GROUP_NAME));
                inputToUtils.put(COL_SPECIAL_PROVISION,
                        resultObject.getString(COL_SPECIAL_PROVISION));
                inputToUtils.put(COL_NAME,
                        jsonObject.getString(COL_NAME));
                inputToUtils.put(COL_UNNUMBER_DISPLAY_STATUS,
                        resultObject.getString(COL_UNNUMBER_DISPLAY_STATUS));
                inputToUtils.put(COL_DESCRIPTION,
                        jsonObject.getString(COL_DESCRIPTION));
                inputToUtils.put(COL_UNTYPE,
                        resultObject.getString(COL_UNTYPE));
                inputToUtils.put(COL_PRIMARY_PLACARD,
                        jsonObject.getString(COL_PRIMARY_PLACARD));
                inputToUtils.put(COL_SECONDARY_PLACARD,
                        resultObject.getString(COL_SECONDARY_PLACARD));

                inputToUtils.put(COL_NOS_DETAILS,jsonObject.getString(COL_NOS_DETAILS));
                //1005 case, need to show secondary placard based language,added 26-08-2016
                if (jsonObject.getString(ET_UNNUMBER).equalsIgnoreCase("1005")) {
                    String sP = "";
                    if (Utils.language.equalsIgnoreCase("en") || Utils.language.equalsIgnoreCase("sp")) {
                        sP = sP+"#es";
                    } else {
                        sP = sP+"#fr";
                    }
                    inputToUtils.put(COL_SECONDARY_PLACARD,
                            sP);
                }
                inputToUtils.put(COL_DANGEROUS_PLACARD,
                        jsonObject.getString(COL_DANGEROUS_PLACARD));
                //for class 7, it should not mix with danger,added 26-08-2016
                if (jsonObject.getString(COL_NAME).startsWith("7")) {
                    inputToUtils.put(COL_DANGEROUS_PLACARD,
                            "0");
                }
                inputToUtils.put(COL_UNNUMBERDESC_ID,
                        resultObject.getString(COL_UNNUMBERDESC_ID));
                inputToUtils.put(COL_NONEXCEMPT,
                        resultObject.getString(COL_NONEXCEMPT));
                inputToUtils.put(COL_ERAP_NO, jsonObject.getString(ET_ERAP));
                inputToUtils.put(COL_UNNUMBER_DISPLAY,
                        jsonObject.getString(COL_UNNUMBER_DISPLAY));

                if (inputToUtils.has(key)) {
                    //the key has to be replaced with value, in some of cases value of key for dg logic input to be changed
                    //and reconsrtuct the input json object
                    inputToUtils.put(key,value);
                }
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(inputToUtils);
            //System.out.println("jo "+jo);
            JSONObject jsonResult = new JSONObject();
            jsonResult.put("Data", jsonArray);
            String jsonStr = jsonResult.toString();
            jsonResponse = new JSONObject(jsonStr);
           // System.out.println("jsonResponse " + jsonResponse);
            // dgLogicInput.inpuToPlacardSelection(unNumberInfo.getUnNumberInfo(un_number),key,value);
        } catch (Exception e) {
//            new Exceptions(context,DGLogicInput.this
//                    .getClass().getName(), "Error::DGLogicInput::inputToDLogic()" +
//                    Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }return jsonResponse;
    }

    /**
     * This method used to get visibility of erap, to set un number display
     * status
     *
     * author Suman
     * @since v.b.5.2.8
     */

    public String setUndisplayStatus(final EditText eT_Erap,final String ibc_status) {
        String unnumber_display = "0";
        try {
            if (eT_Erap.getVisibility() == View.VISIBLE) {
                unnumber_display = "1";
            } else if (ibc_status.equals("1")) {
                unnumber_display = "1";
            } else {
                unnumber_display = "0";
            }
        } catch (Exception e) {
            new Exceptions(context,DGLogicInput.this
                            .getClass().getName(),
                    "Error setUndisplayStatus method " + e.toString());
        }
        return unnumber_display;
    }// setUndisplayStatus()
    /**
     * This method checks residue visibility based on IBC selection
     *
     * author suman
     * @param gross_Weight gross weight(units*dg weight)
     * @since v.b.5.3.3
     */
    public String checkResidueVisibility(final Button btnIbc,final Button btnResidue,final TextView tvResidue,final double gross_Weight) {
        String ibc_residue_status = "0";
        try {
            // if IBC selection is "yes"(If it is visible) and gross weight
            // varies between 0 to 450 kgs
            // have to display Residue selection spinner
            if (btnIbc.getVisibility() == View.VISIBLE
                    && btnIbc.getText().toString()
                    .equals(Utils.getResString(R.string.Dialog_Btn_Delete_Yes))) {
                if (gross_Weight > 0 && gross_Weight < 450) {
                    btnResidue.setVisibility(View.VISIBLE);
                    tvResidue.setVisibility(View.VISIBLE);
                    ibc_residue_status = "1";
                    tvResidue.setText(Utils.getResString(R.string.Dialog_Add_Item_Tv_Residue));
                } else {
                    btnResidue.setVisibility(View.GONE);
                    ibc_residue_status = "0";
                    tvResidue.setVisibility(View.GONE);
                }
            } else {
                btnResidue.setVisibility(View.GONE);
                tvResidue.setVisibility(View.GONE);
                ibc_residue_status = "0";
            }
        } catch (Exception e) {
            new Exceptions(context,DGLogicInput.this
                            .getClass().getName(),
                    "Error checkResidueVisibility method " + e.toString());
        }
        return ibc_residue_status;
    } // checkResidueVisibility()

    @Override
    public Object getInstance() {
        return null;
    }
}
