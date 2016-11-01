package com.ideabytes.dgsms.ca.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.UnInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*******************************************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : UnNumberInfo
 * author:  Suman
 * Description : To construct placards this class is having methods to deal with Danger logic
 * Created Date : 27-11-2015
 * Modified Date : 28-11-2015
 * Reason: to get un number info from databse tables
 *********************************************************************************************/
public class UnNumberInfo implements DBConstants {
    private final String TAG = "UnNumberInfo";
    public Context context;
    UnNumberInfo() {
    }
    public UnNumberInfo(Context context){
        this.context = context;
    }
    /**
     * This method is to retrieve un number info on tab out which is required for dg logic for placarding
     *
     * @param unNumber
     * @return JSONObject un number info
     */
    public JSONObject getUnNumberInfo(final String unNumber) {
        JSONObject unInfo = null;
        JSONObject response = new JSONObject();
        try {
            UnInfo un = new UnInfo(context);
            String result = "{\"result\" :" + un.validateUnNumber(unNumber) + "}";
            unInfo = new JSONObject(result);
            //System.out.println("unInfo "+unInfo);
            if (result.length() > 50) {
                //System.out.println("unInfo "+unInfo);
                JSONArray jsonArray = unInfo.getJSONArray("result");
               // System.out.println("jsonArray len "+jsonArray.length());
                JSONArray jarray = new JSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resultObject = new JSONObject();
                    JSONObject jsonObjectinDataArray = jsonArray.getJSONObject(i);
                   // System.out.println("jsonObjectinDataArray "+jsonObjectinDataArray);
                    resultObject.put(COL_GROUP_NAME, jsonObjectinDataArray
                            .optString("group_name").toString());
                    resultObject.put(COL_DANGEROUS_PLACARD, jsonObjectinDataArray
                            .optString("dangerous_placard").toString());
                    resultObject.put(COL_UN_CLASS_ID, jsonObjectinDataArray
                            .optString("unclass_id").toString());
                    resultObject.put(COL_UNNUMBER_DISPLAY_STATUS, jsonObjectinDataArray
                            .optString("unnumber_display_status").toString());
                    resultObject.put(COL_SPECIAL_PROVISION, jsonObjectinDataArray
                            .optString("specialProvision").toString());
                    resultObject.put(COL_UNTYPE, jsonObjectinDataArray
                            .optString("untype").toString());
                    resultObject.put(COL_WEIGHT_INDEX, jsonObjectinDataArray
                            .optString("weight_index").toString());
                    resultObject.put(COL_PRIMARY_PLACARD, jsonObjectinDataArray
                            .optString("primary_placard").toString());
                    resultObject.put(COL_DESCRIPTION, jsonObjectinDataArray
                            .optString("description").toString());
                    resultObject.put(COL_NAME, jsonObjectinDataArray
                            .optString("name").toString());
                    JSONArray erapArray = jsonObjectinDataArray
                            .getJSONArray("erapdetails");
                    for (int j = 0; j < erapArray.length(); j++) {
                        JSONObject jsonObjectin = erapArray.getJSONObject(j);
                        resultObject.put(COL_PKG_GROUP, jsonObjectin.optString(
                                "pkg_group").toString());
                        resultObject.put("sp84status", jsonObjectin.optString(
                                "sp84status").toString());
                        resultObject.put("erap_status", jsonObjectin.optString(
                                "erap_status").toString());
                        resultObject.put("erap_index", jsonObjectin.optString(
                                "erap_index").toString());
                        resultObject.put("erapdetails",erapArray);
                    }
                    resultObject.put(COL_SHIPPING_NAME, jsonObjectinDataArray
                            .optString("shipping_name").toString());
                    resultObject.put(COL_SECONDARY_PLACARD, jsonObjectinDataArray
                            .optString("secondary_placard").toString());
                    resultObject.put(COL_NONEXCEMPT, jsonObjectinDataArray
                            .optString(COL_NONEXCEMPT).toString());
                    resultObject.put(COL_UNNUMBERDESC_ID, jsonObjectinDataArray
                            .optString("unnumberdesc_id").toString());
                    resultObject.put("erapdetails", erapArray);
                    jarray.put(resultObject);
                }
                response.put("result",jarray);
            }//if
        } catch (Exception e){
            new Exceptions(context,UnNumberInfo.this
                    .getClass().getName(), "Error::UnNumberInfo::getUnNumberInfo()" +
                    Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
       return response;
    }
    /**
     * This method is to retrieve values from 'result' json object,mainly used to get values from unnumber info on tab out
     *
     * @param jsonObject
     * @param key
     * @return required value
     */
    public String getSingleValue(final JSONObject jsonObject, final String key) {
        String value = "";
        //System.out.println("jsonObject "+jsonObject+" key "+key);
        try {
            HashMap<String,String> result = getResponseFromJson(jsonObject, key);
           // System.out.println("result "+result);
            for (Map.Entry<String,String> e : result.entrySet()) {
                    value = e.getValue();
            }
        } catch (Exception e) {
            new Exceptions(context,UnNumberInfo.this
                    .getClass().getName(), "Error::UnNumberInfo::getSingleValue()" +
                     Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return  value;
    }
    /**
     * This method is to retrieve values from 'erapdetails' json array,mainly used to get values of 'erap_index' ,'erap_status','sp84status',
     * 'sp84value' and 'pkg_group'
     *
     * @param jsonObject
     * @param key
     * @return list
     */
    public  ArrayList<String> getMultiValues(final JSONObject jsonObject, final String key) {
        String value = "";
        ArrayList<String> list =  new ArrayList<String>();
      //  System.out.println("debug getMultiValues::jsonObject "+jsonObject+" key "+key);
        try {
            HashMap<String,String> result = getResponseFromJson(jsonObject, key);
            // System.out.println("debug getMultiValues::result "+result);
            for (Map.Entry<String,String> e : result.entrySet()) {
                value = e.getValue();
                    list.add(value);
            }
        } catch (Exception e) {
            new Exceptions(context,UnNumberInfo.this
                    .getClass().getName(), "Error::UnNumberInfo::getMultiValues()" +
                    Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return  list;
    }
    public HashMap<String,String> getResponseFromJson(final JSONObject jsonObject, final String key) {
        HashMap<String,String> result = new HashMap<String, String>();
        try {
           // System.out.println("jsonObject "+jsonObject+" key "+key);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectinDataArray = jsonArray.getJSONObject(i);
                //result.put(jsonObjectinDataArray.optString(key).toString(),jsonObjectinDataArray.optString(key).toString());
               // System.out.println("in result " + jsonObjectinDataArray.getString(key) + " key " + key);
                if (jsonObjectinDataArray.has(key)) {
                    result.put(key, jsonObjectinDataArray.getString(key));
                }
                JSONArray erapArray = jsonObjectinDataArray
                        .getJSONArray("erapdetails");
               // System.out.println("erap "+erapArray);
                for (int j = 0; j < erapArray.length(); j++) {
                    JSONObject jsonObjectin = erapArray.getJSONObject(j);
                    if (jsonObjectin.has(key)) {
                        result.put(String.valueOf(j), jsonObjectin.getString(key));
                    }
                    //System.out.println("jsonObjectin "+jsonObjectin);
                    //System.out.println("in erap details "+jsonObjectin.getString(key)+" key "+key);
                    //result.put(jsonObjectin.optString(key).toString(),jsonObjectin.optString(key).toString());
                }
            }
            //System.out.println("suman mama "+result);
        } catch (Exception e) {
            new Exceptions(context,UnNumberInfo.this
                    .getClass().getName(), "Error::UnNumberInfo::getResponseFromJson()" +
                    Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object getInstance() {
        return null;
    }




}