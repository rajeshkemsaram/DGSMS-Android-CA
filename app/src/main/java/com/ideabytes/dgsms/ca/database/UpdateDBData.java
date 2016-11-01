package com.ideabytes.dgsms.ca.database;

import java.util.Arrays;
import java.util.HashMap;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.josn.JSONParser;
import com.ideabytes.dgsms.ca.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : UpdateDBData
 * @author:  Suman
 * @Description : This class is to update database table's data
 * @Modified Date : 23-11-2015
 **************************************************************/
public class UpdateDBData extends  DatabaseDAO implements DBConstants {

	private Context context = null;
	private int dbCode = 0;
	public UpdateDBData(Context context) {
		super(context);
		this.context = context;
	}

	/**
	* @param response
	 * @author Ideabytes
	 * @return number of updated rows count
	 */
	public int updateWeight(final String response) throws Exception {
		int rows = 0;
		try {
		getDatabaseConnection(dbCode);
		JSONArray jsonArray = new JSONParser().getJsonArray(response, "weightInfo");
		//  Log.v(TAG, "weightInfo table service " + jsonArray);
		// Log.v(LOGLOGTAG,"weight table count from service "+jsonArray);
		int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
		//Log.v(TAG,"weight table count from service "+jsonObjectCount);
		// Iterate the jsonArray and print the info of JSONObjects
		for (int i = 1; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ContentValues values = new ContentValues();
			values.put(COL_ID, Integer
					.parseInt(jsonObject.optString("id").toString()));
			values.put(COL_NAME, Double.parseDouble(jsonObject.optString("name")
					.toString()));
			values.put(COL_STATUS, Integer.parseInt(jsonObject.optString("status")
					.toString()));
			rows = db.update(TABLE_WEIGHT, values, COL_ID + " = ?",
					new String[]{Integer.toString(Integer
                            .parseInt(jsonObject.optString("id").toString()))});
		}
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateWeight method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;
	}

	/**
	 * @author Ideabytes
	 * @param response
	 * @return number of updated rows count
	 */
	public int updateUNClass(final String response) throws Exception  {
		int rows = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            GetDBData getDBData = new GetDBData(context);
            JSONArray jsonArray = new JSONParser().getJsonArray(response, "unClassInfo");
            //Log.v(TAG,"unClassInfo table service "+jsonArray);
            int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
            // Log.v(TAG,"unClassInfo table count from service "+jsonObjectCount);
            // Iterate the jsonArray and print the info of JSONObjects
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_ID, Integer
                        .parseInt(jsonObject.optString("id").toString()));
                values.put(COL_NAME, jsonObject.optString("name").toString());
                values.put(COL_ROAD_WEIGHT_GROUP, Integer.parseInt(jsonObject.optString(
                        "road_weight_group").toString()));
                values.put(COL_PRIMARY_PLACARD, jsonObject.optString("primary_placard")
                        .toString());
                values.put(COL_SECONDARY_PLACARD, jsonObject.optString(
                        "secondary_placard").toString());
                values.put(COL_UNNUMBER_DISPLAY_STATUS, jsonObject.optString(
                        "unnumber_display_status").toString());
                values.put(COL_PARENT_CLASS, jsonObject.optString("parent_class")
                        .toString());
                values.put(COL_DANGEROUS_PLACARD, jsonObject.optString(
                        "dangerous_placard").toString());
                values.put(COL_SPECIAL_PROVISION, jsonObject.optString(
                        "specialProvision").toString());
                values.put(COL_NONEXCEMPT, jsonObject.optString(
                        "nonexempt").toString());

                rows = db.update(TABLE_UNCLASS, values, COL_ID + " = ?",
                        new String[]{jsonObject.optString("id").toString()});
            }
			//connection.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateUNClass method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;

	}

	/**
	 * @author Ideabytes
	 * @param response
	 * @return number of updated rows count
	 */
	// Modified by Suman/10-01-2015 added int id
	public int updateUNNumber(final String response) throws Exception  {
		int rows = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            GetDBData getDBData = new GetDBData(context);
            JSONArray jsonArray = new JSONParser().getJsonArray(response, "unNumberInfo");
            //Log.v(TAG,"unNumberInfo table service "+jsonArray);
            int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_ID, Integer
                        .parseInt(jsonObject.optString("id").toString()));
                values.put(COL_UN_NUMBER, jsonObject.optString("unnumber").toString());
                values.put(COL_UN_CLASS_ID, Integer.parseInt(jsonObject.optString(
                        "unclass_id").toString()));
                values.put(COL_UNNUMBER_DISPLAY_STATUS, Integer.parseInt(jsonObject.optString(
                        "unnumber_display_status").toString()));
                values.put(COL_UNTYPE, jsonObject.optString("untype").toString());
                values.put(COL_DESCRIPTION, jsonObject.getString("description"));
                values.put(COL_STATUS, Integer.parseInt(jsonObject.optString("status")
                        .toString()));
                values.put(COL_GROUP_NAME, jsonObject.optString("group_name").toString());
                values.put(COL_SHIPPING_NAME, jsonObject.optString("shipping_name").toString());

                rows = db.update(TABLE_UNNUMBER_INFO, values, COL_UN_NUMBER
                        + " = ?", new String[]{jsonObject.optString("unnumber").toString()});
            }
			//connection.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateUNNumber method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;

	}

    /**
     * Method to update sp84 table
     * @param response
     * @return
     * @throws Exception
     */
	public int updateSp84(final String response) throws Exception {
		int rows = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            GetDBData getDBData = new GetDBData(context);
            JSONArray jsonArray = new JSONParser().getJsonArray(response, "sp84Info");
            // Log.v(TAG,"sp84Info table service "+jsonArray);
            int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_ID, Integer
                        .parseInt(jsonObject.optString("id").toString()));
                values.put(COL_VALUE, jsonObject.optString("value").toString());
                values.put(COL_STATUS, Integer.parseInt(jsonObject.optString("status")
                        .toString()));

                rows = db.update(TABLE_SP84_INFO, values, COL_ID + " = ?",
                        new String[]{jsonObject.optString("id").toString()});
            }
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateSp84 method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;

	}
    /**
     * Method to update erap table
     * @param response
     * @return
     * @throws Exception
     */
    public int updateErap(final String response) throws Exception {
        int rows = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            GetDBData getDBData = new GetDBData(context);
            JSONArray jsonArray = new JSONParser().getJsonArray(response, "erapInfo");
            int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
            for (int i = 1; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_UNNUMBERDESC_ID, jsonObject.optString("unnumberdesc_id")
                        .toString());
                values.put(COL_PKG_GROUP, jsonObject.optString("pkg_group").toString());
                values.put(COL_ERAP_INDEX, jsonObject.optString("erap_index")
                        .toString());
                values.put(COL_ERAP_STATUS, jsonObject.optString("erap_status")
                        .toString());
                rows = db.update(TABLE_ERAP_INFO, values, COL_ID + " = ?",
                        new String[]{jsonObject.optString("id").toString()});
            }
            //db.close();
        } catch (Exception e) {
            Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateSp84 method " + e.toString());
            e.printStackTrace();
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return rows;

    }

	/**
	 * This method updates license session which will use in calling server rules data
	 * @author suman
	 * @param license_Session
	 * @return no of rows effeted
	 */
	public int updateLicenseKey(String license_Session) {
		int rows = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			ContentValues values = new ContentValues();
			values.put(COL_LICENSE_SESSION, license_Session);
			rows = db.update(TABLE_LISENCE_DETAILS, values, COL_LICENSE_SESSION + " = ?",
					new String[] { license_Session });
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateLicenseKey method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;
	}

	public void updateVerifyLicense(final HashMap<String, String> licenseData) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deviceId= licenseData.get("deviceId");
			String valid_till = licenseData.get(COL_VALID_TILL);
			String valid_from = licenseData.get(COL_VALID_FROM);
			String license_key = licenseData.get(COL_LICENSE_KEY);
			String license_session = licenseData.get(COL_LICENSE_SESSION);
			String url = licenseData.get(COL_URL);
			String max_Transaction = licenseData.get(COL_MAX_TRANSACTIONS);
			String companyName = licenseData.get(COL_COMPANY_NAME);
			String username = licenseData.get(COL_USER_NAME);

			db.execSQL("update " + TABLE_LISENCE_DETAILS + " set "
					+COL_VALID_FROM+" = "+"\""+valid_from+"\""+", "
					+COL_VALID_TILL+" = "+"\""+valid_till+"\""+", "
					+COL_LICENSE_SESSION+" = "+"\""+license_session+"\""+", "
					+COL_LICENSE_KEY+" = "+"\""+license_key+"\""+", "
					+COL_MAX_TRANSACTIONS+" = "+"\""+max_Transaction+"\""+", "
					+COL_URL+" = "+"\""+url+"\""+", "+COL_USER_NAME+" = "+"\""+username+"\""
					+", "+COL_COMPANY_NAME+" = "+"\""+companyName+"\""
					+" where "+ COL_IMEI + " = " +"\""+ deviceId+"\"" );
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateVerifyLicense method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * This updates orders table column status to 1, if order picked up
	 * @author suman
	 * @param status
	 * @param colid
	 */
	public void updateOrders(int status, int colid) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			ContentValues values = new ContentValues();
			values.put(COL_TRANSACTION_STATUS, status);
			values.put(COL_ID, colid);
			db.execSQL("update " + TABLE_ORDERS+" set "
					+COL_TRANSACTION_STATUS+" = "+"\""+status +"\""+" where "+
					COL_ID +" = "+colid );
			//	db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateOrders method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}

	}

	/**
	 * This updates verify license status to "1" in config table
	 * 
	 * @author Ssuman
	 * @param status
	*/
	public void updateVerifyLicense(final String status) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			db.execSQL("update " + TABLE_TERMS_CHECK+" set "
					+COL_VERIFY_LICENSE +" = "+"\""+status+"\"" );
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateVerifyLicense method " + e.toString());
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	/**
	 * This updates max transactions value in config table
	 * 
	 * @author Suman
	 * @param maxTrans
	 */
	public void updateMaxTransactions(int maxTrans) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			db.execSQL("update " + TABLE_LISENCE_DETAILS+" set "
					+COL_MAX_TRANSACTIONS+" = "+"\""+maxTrans+"\""  );
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updateMaxTransactions method " + e.toString());
			new Exceptions(context,UpdateDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
			e.printStackTrace();

		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	/**
	 * update push transaction status of the row once it is pushed to mobile web portal
	 * 
	 * author suman
	 * 
	 */
//	public void updatePushTransStatus() {
//		try {
//			//gets connection for SQLite database
//			getDatabaseConnection(dbCode);
//			db.execSQL("update " + TABLE_TRANSACTIONS+" set "
//					+COL_PUSH_STATUS+" = '1' where "+COL_PUSH_STATUS+" = '0'" );
//			//db.close();
//		} catch (Exception e) {
//			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updatePushTransStatus method " + e.toString());
//			e.printStackTrace();
//
//		}	finally {
//			//closes database connection
//			closeDatabaseConnection();
//		}
//	}


	public void updatePushTransStatus(final String a1, final String a2) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			db.execSQL("update " + TABLE_TRANSACTIONS+" set "
					+COL_PUSH_STATUS+" = '"+a1+"' where "+COL_PUSH_STATUS+" = '"+a2+"'" );
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updatePushTransStatus method " + e.toString());

			e.printStackTrace();

		}	finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}




	public void updatePlacardingType(final int placardingType) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String updateQuery = "update "+TABLE_UTILITIES+" set "+COL_PLACARDING_TYPE+" = "+placardingType;
			Log.v("Suman","updateQuery "+updateQuery);
			db.execSQL(updateQuery);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
	}


	public void updateDbFromServer(final String updateQuery) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			db.execSQL(updateQuery);
			Log.v("Rules data sycn up","updateQuery "+updateQuery);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
	}

	/**
	 * This updates push status value in placard_content table
	 *
	 * author Suman
	 */
	public void updatePlacardContentPushStatus(final String a1, final String a2) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			db.execSQL("update " + TABLE_PLACARDING_TYPE+" set "
					+COL_PUSH_STATUS+" = '"+a1+"' where "+COL_PUSH_STATUS+" = '"+a2+"'" );
			//db.close();
		} catch (Exception e) {
			Log.e(UpdateDBData.this.getClass().getSimpleName(), "Error updatePushTransStatus method " + e.toString());

			e.printStackTrace();

		}	finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}


	@Override
	public Object getInstance() {
		return null;
	}
}