package com.ideabytes.dgsms.ca.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GetDBData extends DatabaseDAO implements DBConstants {
	/*************************************************************
	 * Copy right @Ideabytes Software India Private Limited 
	 * Web site : http://ideabytes.com
	 * Name : GetDBData
	 * author:  Suman
	 * Description : This class is to get database table's data
	 * Modified Date : 12-01-2016
	 * Reason : To change class 6.s to 6.2 in transaction history
	 ************************************************************/
	private Context context;
	public String TAG = "getDBData";
	public static long id;
    private int dbCode = 1;
	public GetDBData(Context context) {
		super(context);
        this.context = context;
	}

	/**
	 * author Ideabytes
	 * @return void
	 */
	public String getLastUpdatedDate() {
		String lastUpdatedDate = null;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT " + COL_UPDATED_AT + " FROM "
					+ TABLE_UPDATED_AT + " where " + COL_TIME_ID
					+ " = ( select MAX(" + COL_TIME_ID + ") from "
					+ TABLE_UPDATED_AT + ")", null);
			// COL_TIME_ID
			while (c.moveToNext()) {
				lastUpdatedDate = c.getString(c.getColumnIndex(COL_UPDATED_AT));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getLastUpdatedDate method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return lastUpdatedDate;
	}

	public void getLanguageContent() {
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			// SELECT lc2.text,lc1.text as value FROM `language_content` as lc1 JOIN
			// `language_content` as lc2 ON lc1.cid = lc2.id WHERE lc1.lang_id = 2
			 c = db.rawQuery("select * from  " + TABLE_LANGUAGE_CONTENT,
					null);

			while (c.moveToNext()) {
//				 Log.v(TAG, "####### Language details table details #######");
//
//				 Log.v(TAG," CONTENT ID   "+
//				 c.getString(c.getColumnIndex(COL_LANGUAGE_CONTENT_ID)));
//
//				 Log.v(TAG," LANGUAGE ID   "+
//				 c.getString(c.getColumnIndex(COL_LANGUAGE_ID)));
//				 Log.v(TAG," TEXT "+ c.getString(c.getColumnIndex(COL_MSG_TEXT)));

			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getLanguageContent method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
	}

	public void getLanguage() {
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("select * from  " + TABLE_LANGUAGE, null);

			while (c.moveToNext()) {

				// Log.v(TAG, "####### Language details table details #######");
				//
				// Log.v(TAG," CONTENT ID   "+
				// c.getString(c.getColumnIndex(COL_SELECTED_STATE)));
				//
				// Log.v(TAG," LANGUAGE ID   "+
				// c.getString(c.getColumnIndex(COL_LANGUAGE_ID)));
				// Log.v(TAG," TEXT "+
				// c.getString(c.getColumnIndex(COL_LANGUAGE_NAME)));

			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getLanguage method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
	}

	/**
	 * author Ideabytes
	 * @return language updated date
	 */
	public String getLicenceUpdatedDate() {
		String lastUpdatedDate = null;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT " + COL_LICENCE_UPDATED_AT + " FROM "
					+ TABLE_LICENCECHECK_UPDATED_DATE + " where "
					+ COL_LICENCE_TIME_ID + " = ( select MAX("
					+ COL_LICENCE_TIME_ID + ") from "
					+ TABLE_LICENCECHECK_UPDATED_DATE + ")", null);
			// COL_TIME_ID
			while (c.moveToNext()) {
				lastUpdatedDate = c.getString(c
						.getColumnIndex(COL_LICENCE_UPDATED_AT));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getLicenceUpdatedDate method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return lastUpdatedDate;
	}

	/**
	 *This method returns unnumbers table data
	 * 
	 * author suman
	 * @return ArrayList<String> un number ifno table data
	 */
	public ArrayList<String> getUnNumberTableData() {
		ArrayList<String> selectedPlacardList = new ArrayList<String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_UNNUMBER_INFO;
			c = db.rawQuery(query, null);

			while (c.moveToNext()) {

//				Log.e(TAG, "TABLE_UN_NUMBERS data");
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_ID==>" + c.getString(c.getColumnIndex(COL_ID)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_UNNUMBER==>"
//								+ c.getString(c.getColumnIndex(COL_UN_NUMBER)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_UN_CLASS_ID==>"
//								+ c.getString(c.getColumnIndex(COL_UN_CLASS_ID)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_UNNO_DISPLAY_STATUS==>"
//								+ c.getString(c
//										.getColumnIndex(COL_UNNUMBER_DISPLAY_STATUS)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_UNTYPE==>" + c.getString(c.getColumnIndex(COL_UNTYPE)));
//				Log.v(TAG + "::getUnNumberTableData()::", "COL_UNNO_DESCRIPTION==>"
//						+ c.getString(c.getColumnIndex(COL_DESCRIPTION)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_STATUS==>" + c.getString(c.getColumnIndex(COL_STATUS)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_GROUP_NAME==>" + c.getString(c.getColumnIndex(COL_GROUP_NAME)));
//				Log.v(TAG + "::getUnNumberTableData()::",
//						"COL_SHIPPING_NAME==>" + c.getString(c.getColumnIndex(COL_SHIPPING_NAME)));

			}
			Log.e(TAG, "No of columns in table unnumbers  " + c.getCount());
	    }  catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getUnNumberTableData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		// Log.d(TAG, "DatabaseHelper::getBannerData::end");
		return selectedPlacardList;
	}

	/**
	 * this method returns weight table data
	 * 
	 * author suman
	 * @return ArrayList<String> weight table data
	 */
	public ArrayList<String> getWeightTableData() {
		ArrayList<String> result = new ArrayList<String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_WEIGHT;
			c = db.rawQuery(query, null);

			while (c.moveToNext()) {

//				Log.e(TAG + "::getWeightTableData()::", "TABLE_WEIGHT data");
//				Log.v(TAG + "::getWeightTableData()::",
//						"COL_WEIGHT_INDEX==>"
//								+ c.getString(c.getColumnIndex(COL_WEIGHT_INDEX)));
//				Log.v(TAG + "::getWeightTableData()::",
//						"COL_WEIGHT==>" + c.getString(c.getColumnIndex(COL_NAME)));
			}
			Log.e(TAG, "No of columns in table TABLE_WEIGHT  " + c.getCount());
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getWeightTableData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return result;
	}

	/**
	 * author suman
	 * @return ArrayList<String> package group of un number
	 */
	public ArrayList<String> getPckGroup(final String unNumber) {
		ArrayList<String> result = new ArrayList<String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select ui." + COL_TD_ID + ",uc." + COL_PARENT_CLASS
					+ ",ui." + COL_UN_CLASS_ID + ",uc."

				+ COL_DANGEROUS_PLACARD + ",uc." + COL_DANGEROUS_PLACARD
				+ ",ui." + COL_DESCRIPTION + ",ui." + COL_UNTYPE + ",ui."
				+ COL_UNNUMBER_DISPLAY_STATUS + ",uc." + COL_SECONDARY_PLACARD
				+ ",w." + COL_WEIGHT_INDEX + " from " + TABLE_UNNUMBER_INFO
				+ " ui," + TABLE_UNCLASS + " uc , " + TABLE_WEIGHT + " w"

				+ " where ui." + COL_UN_NUMBER + " = " + "'" + unNumber + "'"

				+ " and uc." + COL_UN_CLASS_ID + " = ui." + COL_UN_CLASS_ID

				+ " and  w." + COL_WEIGHT_INDEX + " =  uc."
				+ COL_ROAD_WEIGHT_GROUP;

			c = db.rawQuery(query, null);
			Log.e(TAG, "getPckGroup() size " + c.getCount());
			while (c.moveToNext()) {

				Log.e(TAG, "getPckGroup()");
				Log.v(TAG + "::getPckGroup()::",
						"COL_TD_ID==>" + c.getString(c.getColumnIndex(COL_TD_ID)));
				Log.v(TAG + "::getPckGroup()::",
						"COL_DANGEROUS_PLACARD==>"
								+ c.getString(c
										.getColumnIndex(COL_DANGEROUS_PLACARD)));
				Log.v(TAG + "::getPckGroup()::",
						"COL_UNNO_DESCRIPTION==>"
								+ c.getString(c.getColumnIndex(COL_DESCRIPTION)));
				Log.v(TAG + "::getPckGroup()::",
						"COL_UNNO_DISPLAY_STATUS==>"
								+ c.getString(c
										.getColumnIndex(COL_UNNUMBER_DISPLAY_STATUS)));
				Log.v(TAG + "::getPckGroup()::",
						"COL_WEIGHT_ID==>"
								+ c.getString(c.getColumnIndex(COL_WEIGHT_INDEX)));
			}
		}  catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getPckGroup method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return result;
	}

	/**
	 * Author : Suman
	 * @return HashMap<String, String> transaction details table data
	 */
	public HashMap<String, String> getTransactionDetails() {
		HashMap<String, String> reponse = new HashMap<String, String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_TRANSACTIONS, null);
			Log.d(TAG,
                    "DatabaseHelper::Transaction:::: sizes of Transaction :"
                            + c.getCount());
			while (c.moveToNext()) {

				//				Log.e(TAG + "::getTransactionDetails()::",
				//						"####### Transaction table details #######");
				//				Log.d(TAG + "::getTransactionDetails()::",
				//						"un_number== "
				//								+ c.getString(c.getColumnIndex(COL_UN_NUMBER)));
				reponse.put(COL_UN_NUMBER, c.getString(c.getColumnIndex(COL_UN_NUMBER)));
				//				Log.d(TAG + "::getTransactionDetails()::",
				//						"user id== " + c.getString(c.getColumnIndex(COL_USER_ID)));
				reponse.put(COL_USER_ID, c.getString(c.getColumnIndex(COL_USER_ID)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"bl== " + c.getString(c.getColumnIndex(COL_BL)));
				reponse.put(COL_BL, c.getString(c.getColumnIndex(COL_BL)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"weight_type== "
//								+ c.getString(c.getColumnIndex(COL_WEIGHT_TYPE)));
				reponse.put(COL_WEIGHT_TYPE, c.getString(c.getColumnIndex(COL_WEIGHT_TYPE)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"dg_weight== "
//								+ c.getString(c.getColumnIndex(COL_DG_WEIGHT)));
				reponse.put(COL_DG_WEIGHT, c.getString(c.getColumnIndex(COL_DG_WEIGHT)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"number_of_units== "
//								+ c.getString(c.getColumnIndex(COL_NO_OF_UNITS)));
				reponse.put(COL_NO_OF_UNITS, c.getString(c.getColumnIndex(COL_NO_OF_UNITS)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"gross_weight== "
//								+ c.getString(c.getColumnIndex(COL_GROSS_WEIGHT)));
				reponse.put(COL_GROSS_WEIGHT, c.getString(c.getColumnIndex(COL_GROSS_WEIGHT)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"subsidary_exist== "
//								+ c.getString(c.getColumnIndex(COL_SUBSIDARY_EXIST)));
				reponse.put(COL_SUBSIDARY_EXIST, c.getString(c.getColumnIndex(COL_SUBSIDARY_EXIST)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"untype== " + c.getString(c.getColumnIndex(COL_UNTYPE)));
				reponse.put(COL_UNTYPE, c.getString(c.getColumnIndex(COL_UNTYPE)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"weight_index== "
//								+ c.getString(c.getColumnIndex(COL_WEIGHT_INDEX)));
				reponse.put(COL_WEIGHT_INDEX, c.getString(c.getColumnIndex(COL_WEIGHT_INDEX)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"primary_placard== "
//								+ c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD)));
				reponse.put(COL_PRIMARY_PLACARD, c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"description== "
//								+ c.getString(c.getColumnIndex(COL_DESCRIPTION)));
				reponse.put(COL_DESCRIPTION, c.getString(c.getColumnIndex(COL_DESCRIPTION)));
//				Log.d(TAG + "::getTransactionDetails()::", "dangerous_placard== "
//						+ c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD)));
				reponse.put(COL_DANGEROUS_PLACARD, c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD)));
//				Log.d(TAG + "::getTransactionDetails()::", "unnumber_display== "
//						+ c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY)));
				reponse.put(COL_UNNUMBER_DISPLAY, c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY)));
//				Log.d(TAG + "::getTransactionDetails()::", "secondary_placard== "
//						+ c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD)));
				reponse.put(COL_SECONDARY_PLACARD, c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"transaction_id== "
//								+ c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
				reponse.put(COL_TRANSACTION_ID, c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
//				Log.d(TAG + "::getTransactionDetails()::", "inserted_date_time== "
//						+ c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME)));
				reponse.put(COL_INSERTED_DATE_TIME, c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"package_weight== "
//								+ c.getString(c.getColumnIndex(COL_PACKAGE_WEIGHT)));
				reponse.put(COL_PACKAGE_WEIGHT, c.getString(c.getColumnIndex(COL_PACKAGE_WEIGHT)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"weight_in_kgs== "
//								+ c.getString(c.getColumnIndex(COL_WEIGHT_IN_KGS)));
				reponse.put(COL_WEIGHT_IN_KGS, c.getString(c.getColumnIndex(COL_WEIGHT_IN_KGS)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"erap_no== " + c.getString(c.getColumnIndex(COL_ERAP_NO)));
				reponse.put(COL_ERAP_NO, c.getString(c.getColumnIndex(COL_ERAP_NO)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"pkg_group== "
//								+ c.getString(c.getColumnIndex(COL_PKG_GROUP)));
				reponse.put(COL_PKG_GROUP, c.getString(c.getColumnIndex(COL_PKG_GROUP)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"un class id== "
//								+ c.getString(c.getColumnIndex(COL_UN_CLASS_ID)));
				reponse.put(COL_UN_CLASS_ID, c.getString(c.getColumnIndex(COL_UN_CLASS_ID)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"COL_TRANSACTION_STATUS== "
//								+ c.getString(c
//										.getColumnIndex(COL_TRANSACTION_STATUS)));
				reponse.put(COL_TRANSACTION_STATUS, c.getString(c.getColumnIndex(COL_TRANSACTION_STATUS)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"COL_TRANSACTION_ID_WEB== "
//								+ c.getString(c
//										.getColumnIndex(COL_TRANSACTION_ID_WEB)));
				reponse.put(COL_TRANSACTION_ID_WEB, c.getString(c.getColumnIndex(COL_TRANSACTION_ID_WEB)));
//				Log.d(TAG + "::getTransactionDetails()::",
//						"COL_USER_ID_WEB== "
//								+ c.getString(c
//										.getColumnIndex(COL_USER_ID_WEB)));
				reponse.put(COL_USER_ID_WEB, c.getString(c.getColumnIndex(COL_USER_ID_WEB)));
				reponse.put(COL_MAX_PLACARD, c.getString(c.getColumnIndex(COL_MAX_PLACARD)));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getTransactionDetails method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return reponse;
	}

	/**
	 * This method to show TABLE_ERAP table data
	 * 
	 * author suman
	 */

	public void getErapTableData() {
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_ERAP_INFO;
			c = db.rawQuery(query, null);
			while (c.moveToNext()) {
				Log.e(TAG + "::getErapTableData()::", "Erap table data");
				Log.e(TAG + "::getErapTableData()::",
						"COL_ERAP_ID:" + c.getString(c.getColumnIndex(COL_ID)));
				Log.e(TAG + "::getErapTableData()::",
						"COL_UNNODESC_ID:"
								+ c.getString(c.getColumnIndex(COL_UNNUMBERDESC_ID)));
				Log.e(TAG + "::getErapTableData()::",
                        "COL_PKG_GROUP:"
                                + c.getString(c.getColumnIndex(COL_PKG_GROUP)));
				Log.e(TAG + "::getErapTableData()::",
                        "COL_ERAP_INDEX:"
                                + c.getString(c.getColumnIndex(COL_ERAP_INDEX)));
				Log.e(TAG + "::getErapTableData()::",
                        "COL_ERAP_STATUS:"
                                + c.getString(c.getColumnIndex(COL_ERAP_STATUS)));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getErapTableData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
	}

	/**
	 * This method to show TABLE_SP84_INFO table data
	 * 
	 * author suman
	 */

	public void getSp84TableData() {
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_SP84_INFO;
			c = db.rawQuery(query, null);
			while (c.moveToNext()) {
				Log.e(TAG + "::getSp84TableData()::", "TABLE_SP84_INFO table data");
				Log.v(TAG + "::getSp84TableData()::",
                        "COL_ID_SP84:" + c.getString(c.getColumnIndex(COL_ID)));
				Log.v(TAG + "::getSp84TableData()::",
                        "COL_VALUE_SP84:"
                                + c.getString(c.getColumnIndex(COL_VALUE)));

			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getSp84TableData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
	}

    /**
     * This method is to display shipment data on the main screen
     *
     * Author : Suman
     * @return list with shipment information
     */
	public ArrayList<HashMap<String,String>> getBannerData() {
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		Cursor c = null;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String kgs = null;
			String ibcStatus = null;
			// Log.d(TAG,
			// "DatabaseHelper::getSpecail_CaseUN_class_id::start::category: "+category);
			c = db.rawQuery("SELECT *  FROM " + TABLE_TRANSACTIONS +" where "+COL_TRANSACTION_STATUS+" = 0 ", null);
			// Log.d(TAG, "DatabaseHelper::Transaction:::: sizes of Transaction :" +
			// c.getCount());
			while (c.moveToNext()) {
                HashMap<String,String> map = new HashMap<String,String>();
                if (c.getString(c.getColumnIndex(COL_WEIGHT_TYPE)).equals("1")) {
					kgs = " Kgs";
				} else {
					kgs = " Lbs";
				}
				map.put(COL_WEIGHT_TYPE,kgs);
				String cl = c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD));
				if(c.getString(c.getColumnIndex(COL_UN_NUMBER)).equals("1005")) {
					cl = "2.3";
				} else if(cl.equalsIgnoreCase("6.s")) {
					//for class 6.s, name must be displayed 6.2
					cl = "6.2";
				} else if(cl.startsWith("7")) {
					//for class 7.1,7.2 and 7.3, need to display class name as 7
					cl = "7";
				}
				//is placards is 1s then display class change to 1.4S
				else if(cl.equalsIgnoreCase("1s")) {
					cl = "1.4S";
				} else if(cl.equalsIgnoreCase("2.4")) {
					cl = "2.2";
				}
				map.put(COL_PRIMARY_PLACARD,cl);
					map.put(COL_NAME, c.getString(c.getColumnIndex(COL_NAME)));
				if(c.getString(c.getColumnIndex(COL_IBC_STATUS)).equalsIgnoreCase("1")) {
					ibcStatus = "Yes";
				} else {
                    ibcStatus = "No";
                }
				map.put(COL_IBC_STATUS,ibcStatus);
				map.put(COL_ID,c.getString(c.getColumnIndex(COL_ID)));
				map.put(COL_USER_ID,c.getString(c.getColumnIndex(COL_USER_ID)));
				map.put(COL_MAX_PLACARD,c.getString(c.getColumnIndex(COL_MAX_PLACARD)));
				map.put(COL_TRANSACTION_ID,c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
				map.put(COL_UN_NUMBER,"UN:"+c.getString(c.getColumnIndex(COL_UN_NUMBER)));
				map.put(COL_DG_WEIGHT,c.getString(c.getColumnIndex(COL_DG_WEIGHT)));
				map.put(COL_NO_OF_UNITS,c.getString(c.getColumnIndex(COL_NO_OF_UNITS)));
				map.put(COL_BL, c.getString(c.getColumnIndex(COL_BL)));
				map.put(COL_GROUP_NAME, c.getString(c.getColumnIndex(COL_GROUP_NAME)));
                map.put(COL_GROSS_WEIGHT, c.getString(c.getColumnIndex(COL_GROSS_WEIGHT)));
				map.put(COL_NOS,c.getString(c.getColumnIndex(COL_NOS)));
                list.add(map);
            }
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getBannerData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
			if(c != null) {
				//close the cursor
				c.close();
			}
		}
		return list;
	}

    /**
     * This method is to show trasaction  history of transaction data from table
     *
     * Author : Suman
     * @return ArrayList<String> transaction history
     */
	public ArrayList<String> getTransHistory() {

		ArrayList<String> bannerData = new ArrayList<String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String kgs = null;
			// Log.d(TAG,
			// "DatabaseHelper::getSpecail_CaseUN_class_id::start::category: "+category);
			c = db.rawQuery("SELECT *  FROM " + TABLE_TRANSACTIONS , null);
			// Log.d(TAG, "DatabaseHelper::Transaction:::: sizes of Transaction :" +
			// c.getCount());
			while (c.moveToNext()) {

				//			 Log.e(TAG+"::getBannerData()::", "####### Banner Data #######");
				//			 Log.d(TAG+"::getBannerData()::","unnumber== " +
				//			 c.getString(c.getColumnIndex(COL_UN_NUMBER)));
				//			 Log.d(TAG+"::getBannerData()::","user id== "+
				//			 c.getString(c.getColumnIndex(COL_USER_ID)));
				//			 Log.d(TAG+"::getBannerData()::","bl== "+
				//			 c.getString(c.getColumnIndex(COL_BL)));
				//			 Log.d(TAG+"::getBannerData()::","dg_weight== "+
				//			 c.getString(c.getColumnIndex(COL_DG_WEIGHT)));
				//			 Log.d(TAG+"::getBannerData()::","number_of_units== "+
				//			 c.getString(c.getColumnIndex(COL_NUMBER_OF_UNITS)));
				//			 Log.d(TAG+"::getBannerData()::","gross_weight== "+
				//			 c.getString(c.getColumnIndex(COL_GROSS_WEIGHT)));
				//			 Log.d(TAG+"::getBannerData()::","weight_type== "+
				//			 c.getString(c.getColumnIndex(COL_WEIGHT_TYPE)));
				if (c.getString(c.getColumnIndex(COL_WEIGHT_TYPE)).equals("1"))
					kgs = " Kgs";
				else
					kgs = " Lbs";
				//			 Log.d(TAG+"::getBannerData()::","subsidary_exist== "+
				//			 c.getString(c.getColumnIndex(COL_SUBSIDARY_EXIST)));
				//			 Log.d(TAG+"::getBannerData()::","untype== "+
				//			 c.getString(c.getColumnIndex(COL_UNTYPE)));
				//			 Log.d(TAG+"::getBannerData()::","weight_index== "+
				//			 c.getString(c.getColumnIndex(COL_WEIGHT_INDEX)));
				//			 Log.d(TAG+"::getBannerData()::","primary_placard== "+
				//			 c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD)));
				//			 Log.d(TAG+"::getBannerData()::","description== "+
				//			 c.getString(c.getColumnIndex(COL_DESCRIPTION)));
				//			 Log.d(TAG+"::getBannerData()::","dangerous_placard== "+
				//			 c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD)));
				//		 Log.d(TAG+"::getBannerData()::","unnumber_display== "+
				//		c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY)));
				//			 Log.d(TAG+"::getBannerData()::","secondary_placard== "+
				//			 c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD)));
				//
				//			 Log.d(TAG+"::getBannerData()::","transaction_id== "+
				//			 c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
				//			 Log.d(TAG+"::getBannerData()::","inserted_date_time== "+
				//			 c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME)));
				//			 Log.d(TAG+"::getBannerData()::","package_weight== "+
				//			 c.getString(c.getColumnIndex(COL_PACKAGE_WEIGHT)));
				//			 Log.d(TAG+"::getBannerData()::","weight_in_kgs== "+
				//			 c.getString(c.getColumnIndex(COL_WEIGHT_IN_KGS)));
				//			 Log.d(TAG+"::getBannerData()::","erap_no== "+
				//			 c.getString(c.getColumnIndex(COL_ERAP_NO)));
				//			 Log.d(TAG+"::getBannerData()::","pkg_group== "+
				//			 c.getString(c.getColumnIndex(COL_PKG_GROUP)));
				//			 Log.d(TAG+"::getBannerData()::","un class id== "+
				//			 c.getString(c.getColumnIndex(COL_UN_CLASS_ID)));
				//			 Log.d(TAG+"::getBannerData()::","COL_TRANSACTION_STATUS== "+
				//			 c.getString(c.getColumnIndex(COL_TRANSACTION_STATUS)));

				String cl = c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD));
				if(c.getString(c.getColumnIndex(COL_UN_NUMBER)).equals("1005")) {
					cl = "2.3";
				} else if(cl.equalsIgnoreCase("6.s")) {
					cl = "6.2";
				}
				//is placards is 1s then display class change to 1.4S
				else if(cl.equalsIgnoreCase("1s")) {
					cl = "1.4S";
				} else if(cl.equalsIgnoreCase("2.4")) {
					cl = "2.2";
				}
                String group = c.getString(c.getColumnIndex(COL_GROUP_NAME));
				bannerData.add(" B/L:" + c.getString(c.getColumnIndex(COL_BL))
						+ " UN:" + c.getString(c.getColumnIndex(COL_UN_NUMBER))
						+ " CL:"
						+ cl+group
                        + " NetWt:" + c.getString(c.getColumnIndex(COL_GROSS_WEIGHT))
                        + kgs
						+ " DGWt:" + c.getString(c.getColumnIndex(COL_DG_WEIGHT))
						+ kgs + " Units:"
						+ c.getString(c.getColumnIndex(COL_NO_OF_UNITS)));
				//			Log.d(TAG+"::getBannerData()::","COL_MAX_PLACARD== "+
				//			c.getString(c.getColumnIndex(COL_MAX_PLACARD)));

			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getBannerData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return bannerData;
	}
	/**
	 * This method is to show from unnumber_info table
     *
	 * author suman
	 * @since v.b.5.3.2	
	 */
	public void getUnNumberInfoData() {
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_UNNUMBER_INFO;
			c = db.rawQuery(query, null);
			while (c.moveToNext()) {
				//Log.e(TAG + "::getUnNumberInfoData()::", " TABLE_UNNUMBER_INFO data ");
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_ID:" + c.getString(c.getColumnIndex(COL_ID)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_UN_CLASS_ID:"
//								+ c.getString(c.getColumnIndex(COL_UN_CLASS_ID)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_UN_NUMBER:"
//								+ c.getString(c.getColumnIndex(COL_UN_NUMBER)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_STATUS:"
//								+ c.getString(c.getColumnIndex(COL_STATUS)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_UNNUMBER_DISPLAY_STATUS:"
//								+ c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY_STATUS)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_UNTYPE:"
//								+ c.getString(c.getColumnIndex(COL_UNTYPE)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_DESCRIPTION:"
//								+ c.getString(c.getColumnIndex(COL_DESCRIPTION)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_SHIPPING_NAME:"
//								+ c.getString(c.getColumnIndex(COL_SHIPPING_NAME)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_GROUP_NAME:"
//								+ c.getString(c.getColumnIndex(COL_GROUP_NAME)));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getUnNumberInfoData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }

	}

	/**
	 * This method is to show from unclases table
     *
	 * author suman
	 * @since v.b.5.3.2	
	 */
	public void getUnClassInfoData() {
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_UNCLASS+" where "+COL_ID+" = '84'";
			c = db.rawQuery(query, null);
			while (c.moveToNext()) {
//				Log.e(TAG + "::getUnNumberInfoData()::", " TABLE_UNCLASS data ");
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_ID:" + c.getString(c.getColumnIndex(COL_ID)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_NAME:"
//								+ c.getString(c.getColumnIndex(COL_NAME)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_PARENT_CLASS:"
//								+ c.getString(c.getColumnIndex(COL_PARENT_CLASS)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_ROAD_WEIGHT_GROUP:"
//								+ c.getString(c.getColumnIndex(COL_ROAD_WEIGHT_GROUP)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_SHIP_WEIGHT_GROUP:"
//								+ c.getString(c.getColumnIndex(COL_SHIP_WEIGHT_GROUP)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_RAIL_WEIGHT_GROUP:"
//								+ c.getString(c.getColumnIndex(COL_RAIL_WEIGHT_GROUP)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_PRIMARY_PLACARD:"
//								+ c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_SECONDARY_PLACARD:"
//								+ c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_UNNUMBER_DISPLAY_STATUS:"
//								+ c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY_STATUS)));
//				Log.e(TAG + "::getUnNumberInfoData()::",
//						"COL_DANGEROUS_PLACARD:"
//								+ c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD)));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getUnNumberInfoData method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
	}

	/**
	 * This method is to show from unnumber_info table
     *
	 * author suman
	 * @since v.b.5.3.2
     * @return  HashMap<String, String>, license details from license details table
	 */
	public HashMap<String, String> getConfigData() {
		HashMap<String, String> result = new HashMap<String, String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			String query = "select * from " + TABLE_LISENCE_DETAILS;
			c = db.rawQuery(query, null);
			while (c.moveToNext()) {
				result.put(COL_IMEI, c.getString(c.getColumnIndex(COL_IMEI)));
				result.put(COL_VALID_FROM, c.getString(c.getColumnIndex(COL_VALID_FROM)));
				result.put(COL_VALID_TILL, c.getString(c.getColumnIndex(COL_VALID_TILL)));
				result.put(COL_URL, c.getString(c.getColumnIndex(COL_URL)));
				result.put(COL_LICENSE_KEY, c.getString(c.getColumnIndex(COL_LICENSE_KEY)));
				result.put(COL_LICENSE_SESSION, c.getString(c.getColumnIndex(COL_LICENSE_SESSION)));
				result.put(COL_SYNC_DATE_TIME, c.getString(c.getColumnIndex(COL_SYNC_DATE_TIME)));
				result.put(COL_MAX_TRANSACTIONS, c.getString(c.getColumnIndex(COL_MAX_TRANSACTIONS)));
				result.put(COL_COMPANY_NAME, c.getString(c.getColumnIndex(COL_COMPANY_NAME)));
				result.put(COL_USER_NAME, c.getString(c.getColumnIndex(COL_USER_NAME)));

//				result.put(SAFETY_PLACARD,c.getString(c.getColumnIndex(SAFETY_PLACARD)));
//				result.put(TYPE,c.getString(c.getColumnIndex(TYPE)));


				//			Log.e(TAG + "::getConfigData()::", " TABLE_CONFIG_DETAILS data ");
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_IMEI:" + c.getString(c.getColumnIndex(COL_IMEI)));
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_VALID_FROM:"
				//							+ c.getString(c.getColumnIndex(COL_VALID_FROM)));
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_VALID_TILL:"
				//							+ c.getString(c.getColumnIndex(COL_VALID_TILL)));
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_URL:"
				//							+ c.getString(c.getColumnIndex(COL_URL)));
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_LICENSE_KEY:"
				//							+ c.getString(c.getColumnIndex(COL_LICENSE_KEY)));
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_LICENSE_SESSION:"
				//							+ c.getString(c.getColumnIndex(COL_LICENSE_SESSION)));
				//			Log.e(TAG + "::getConfigData()::",
				//					"COL_SYNC_DATE_TIME:"
				//							+ c.getString(c.getColumnIndex(COL_SYNC_DATE_TIME)));
				//			Log.e(TAG + "::getConfigData()::",
				//								"COL_MAX_TRANS:"
				//										+ c.getString(c.getColumnIndex(COL_MAX_TRANS)));

				//			
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getConfigData method " + e.toString());
		} finally {
            //closes database connection
            closeDatabaseConnection();
        }
		return result;
	}

    /**
     * This method is to display group name of un number on the placard(mostly for class1 )
     *
     * Author : Suman
     * @return ArrayList<String> , group names of un number
     */
	public ArrayList<String> getDisplayGroup() {
		ArrayList<String> bannerData = new ArrayList<String>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_DISPLAY_GROUP, null);
			// Log.d(TAG, "DatabaseHelper::Transaction:::: sizes of Transaction :" +
			// c.getCount());
			while (c.moveToNext()) {
				bannerData.add(( c.getString(c.getColumnIndex(COL_DISPLAY_NAME))
						+c.getString(c.getColumnIndex(COL_GROUP_NAME))));
				Log.e(TAG + "::getDisplayGroup()::", " TABLE_DISPLAY_GROUP data ");
				Log.e(TAG + "::getDisplayGroup()::",
						"COL_ID:" + c.getString(c.getColumnIndex(COL_ID)));
				Log.e(TAG + "::getDisplayGroup()::",
						"COL_DISPLAY_NAME:"
								+ c.getString(c.getColumnIndex(COL_DISPLAY_NAME)));
				Log.e(TAG + "::getDisplayGroup()::",
                        "COL_GROUP_NAME:"
                                + c.getString(c.getColumnIndex(COL_GROUP_NAME)));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getDisplayGroup method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return bannerData;	
	}


	/**
	 * To verify license based on transaction count
	 * 
	 * author suman
	 * @return transaction count from transaction details table
	 */
	public HashMap<String, Integer> getTrasactionCount() {
		HashMap<String, Integer> values = new HashMap<String,Integer>();
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_TRANSACTIONS, null);
			values.put("transcount", c.getCount());
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getTrasactionCount method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return values;
	}

	/**
	 * This method gives terms check values from "terms" table
	 * 
	 * author suman
	 * @return terms check values
	 */
	public HashMap<String, String> getTermsCheck() {
		HashMap<String, String> values = new HashMap<String,String>();
		Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT * FROM " + TABLE_TERMS_CHECK, null);
			while (c.moveToNext()) {
				values.put(COL_VERIFY_LICENSE, c.getString(c.getColumnIndex(COL_VERIFY_LICENSE)));
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getTermsCheck method " + e.toString());

		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return values;
	}

	/**
	 * get placarding type from database, when there is a placarding type saved then start updating
	 * else please insert value, this is used in preferences, save placarding alert dialog
	 * @return placarding status value from database table
	 */
	public int getPlacardingType() {
		String countQuery = "SELECT  "+COL_PLACARDING_TYPE+" FROM " +TABLE_UTILITIES;
		Cursor c = null;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(countQuery, null);
			Log.v("Suman","count "+c.getCount()+" value "+c.getInt(c.getColumnIndex(COL_PLACARDING_TYPE)));
			if (c.getCount() > 0 ) {
				return c.getInt(c.getColumnIndex(COL_PLACARDING_TYPE));
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeDatabaseConnection();
		}
		return 1;
	}



	/**
	 * This method is used to get all pickup orders from 'orders' table,
	 * which loads from order service 
	 * 
	 * author suman
     * @return JSONObject, pick up oders from web which are stored in local db
	 */
	public JSONObject getPickupOrders() {
		JSONObject response = new JSONObject();
		JSONArray jArray = new JSONArray();
        //gets connection for SQLite database
        getDatabaseConnection(dbCode);
		Cursor c = db.rawQuery("SELECT *  FROM " + TABLE_ORDERS+" where "+COL_TRANSACTION_STATUS+" = "+"\""+0+"\"", null);
		//		Log.d(TAG,
		//				"DatabaseHelper::orders:::: sizes of orders :"
		//						+ c.getCount());
		while (c.moveToNext()) {
			try {
				JSONObject values = new JSONObject();			
				String dg_weight = c.getString(c.getColumnIndex(COL_DG_WEIGHT));
				values.put(COL_DG_WEIGHT, dg_weight);
				//Log.d(TAG,"DatabaseHelper::orders:::: dg_weight=="+dg_weight);
				String un_class_id = c.getString(c.getColumnIndex(COL_UN_CLASS_ID));
				values.put(COL_UN_CLASS_ID, un_class_id);
				//Log.d(TAG,"DatabaseHelper::orders:::: dg_weight=="+dg_weight);
				String un_number = c.getString(c.getColumnIndex(COL_UN_NUMBER));
				values.put(COL_UN_NUMBER, un_number);
				//Log.d(TAG,"DatabaseHelper::orders:::: un_number=="+un_number);
				String pkg_group = c.getString(c.getColumnIndex(COL_PKG_GROUP));
				values.put(COL_PKG_GROUP, pkg_group);
				//Log.d(TAG,"DatabaseHelper::orders:::: pkg_group=="+pkg_group);
				String weight_type = c.getString(c.getColumnIndex(COL_WEIGHT_TYPE));
				values.put(COL_WEIGHT_TYPE,weight_type );
				//Log.d(TAG,"DatabaseHelper::orders:::: weight_type=="+weight_type);
				String ibc_residue_status = c.getString(c.getColumnIndex(COL_IBC_RESIDUE_STATUS));
				values.put(COL_IBC_RESIDUE_STATUS,ibc_residue_status );
				//Log.d(TAG,"DatabaseHelper::orders:::: ibc_residue_status=="+ibc_residue_status);
				String weight_in_kgs = c.getString(c.getColumnIndex(COL_WEIGHT_IN_KGS));
				values.put(COL_WEIGHT_IN_KGS, weight_in_kgs);
				//Log.d(TAG,"DatabaseHelper::orders:::: weight_in_kgs=="+weight_in_kgs);
				String dangerous_placard_original = c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD));
				values.put(COL_DANGEROUS_PLACARD,dangerous_placard_original );
				//Log.d(TAG,"DatabaseHelper::orders:::: dangerous_placard=="+dangerous_placard_original);
				String unnumber_display = c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY));
				values.put(COL_UNNUMBER_DISPLAY, unnumber_display);
				//Log.d(TAG,"DatabaseHelper::orders:::: unnumber_display=="+unnumber_display);
				String description = c.getString(c.getColumnIndex(COL_DESCRIPTION));
				values.put(COL_DESCRIPTION,description );
				//Log.d(TAG,"DatabaseHelper::orders:::: description=="+description);
				String bl = c.getString(c.getColumnIndex(COL_BL));
				values.put(COL_BL,bl );
				//Log.d(TAG,"DatabaseHelper::orders:::: bl=="+bl);
				String subsidary_exist = c.getString(c.getColumnIndex(COL_SUBSIDARY_EXIST));
				values.put(COL_SUBSIDARY_EXIST,subsidary_exist );
				//Log.d(TAG,"DatabaseHelper::orders:::: subsidary_exist=="+subsidary_exist);
				String number_of_units = c.getString(c.getColumnIndex(COL_NO_OF_UNITS));
				values.put(COL_NO_OF_UNITS,number_of_units );
				//Log.d(TAG,"DatabaseHelper::orders:::: number_of_units=="+number_of_units);
				String erap_index = c.getString(c.getColumnIndex(COL_ERAP_INDEX));
				values.put(COL_ERAP_INDEX,erap_index );
				//Log.d(TAG,"DatabaseHelper::orders:::: erap_index=="+erap_index);
				String ibc_status = c.getString(c.getColumnIndex(COL_IBC_STATUS));
				values.put(COL_IBC_STATUS, ibc_status);
				//Log.d(TAG,"DatabaseHelper::orders:::: ibc_status=="+ibc_status);
				String group_name = c.getString(c.getColumnIndex(COL_GROUP_NAME));
				values.put(COL_GROUP_NAME,group_name );
				//Log.d(TAG,"DatabaseHelper::orders:::: group_name=="+group_name);
				String untype = c.getString(c.getColumnIndex(COL_UNTYPE));
				values.put(COL_UNTYPE, untype);
				//Log.d(TAG,"DatabaseHelper::orders:::: untype=="+untype);
				String weight_index = c.getString(c.getColumnIndex(COL_WEIGHT_INDEX));
				values.put(COL_WEIGHT_INDEX, weight_index);
				//	Log.d(TAG,"DatabaseHelper::orders:::: weight_index=="+weight_index);
				String max_placard = c.getString(c.getColumnIndex(COL_MAX_PLACARD));
				values.put(COL_MAX_PLACARD, max_placard);
				//Log.d(TAG,"DatabaseHelper::orders:::: max_placard=="+max_placard);
				String primary_placard = c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD));
                if(primary_placard.equalsIgnoreCase("2.4")) {
                    primary_placard = "2.2";
                }
				values.put(COL_PRIMARY_PLACARD, primary_placard);
				//Log.d(TAG,"DatabaseHelper::orders:::: dg_weight=="+dg_weight);
				String gross_weight = c.getString(c.getColumnIndex(COL_GROSS_WEIGHT));
				values.put(COL_GROSS_WEIGHT,gross_weight );
				//Log.d(TAG,"DatabaseHelper::orders:::: gross_weight=="+gross_weight);
				String secondary_placard = c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD));
				values.put(COL_SECONDARY_PLACARD, secondary_placard);
				//Log.d(TAG,"DatabaseHelper::orders:::: secondary_placard=="+secondary_placard);
				String package_weight = c.getString(c.getColumnIndex(COL_PACKAGE_WEIGHT));
				values.put(COL_PACKAGE_WEIGHT, package_weight);
				//Log.d(TAG,"DatabaseHelper::orders:::: package_weight=="+package_weight);
				String erap_status = c.getString(c.getColumnIndex(COL_ERAP_STATUS));
				values.put(COL_ERAP_STATUS, erap_status);
				//Log.d(TAG,"DatabaseHelper::orders:::: erap_status=="+erap_status);
				String erap_no = c.getString(c.getColumnIndex(COL_ERAP_NO));
				values.put(COL_ERAP_NO, erap_no);
				//Log.d(TAG,"DatabaseHelper::orders:::: erap_no=="+erap_no);
				String inserted_date_time = c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME));
				values.put(COL_INSERTED_DATE_TIME, inserted_date_time);
				//Log.d(TAG,"DatabaseHelper::orders:::: inserted_date_time=="+inserted_date_time);
				String nameInTransaction = c.getString(c.getColumnIndex(COL_NAME));
                if(nameInTransaction.equalsIgnoreCase("2.4")) {
                    nameInTransaction = "2.2";
                }
				values.put(COL_NAME, nameInTransaction);
				//Log.d(TAG,"DatabaseHelper::orders:::: name=="+nameInTransaction);
				values.put(COL_TRANSACTION_ID, c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
				values.put(COL_USER_ID, c.getString(c.getColumnIndex(COL_USER_ID)));
				//Log.d(TAG,"DatabaseHelper::orders:::: user_id=="+c.getString(c.getColumnIndex(COL_USER_ID)));
				values.put(COL_ID, c.getString(c.getColumnIndex(COL_ID)));
				values.put(COL_USER_ID_WEB, c.getString(c.getColumnIndex(COL_USER_ID_WEB)));
				values.put(COL_TRANSACTION_ID_WEB, c.getString(c.getColumnIndex(COL_TRANSACTION_ID_WEB)));
				values.put(COL_CONSIGNEE_DANGER, c.getInt(c.getColumnIndex(COL_CONSIGNEE_DANGER)));
				values.put(COL_SPECIAL_PROVISION, c.getInt(c.getColumnIndex(COL_SPECIAL_PROVISION)));
                values.put(COL_UN_SYLE, c.getInt(c.getColumnIndex(COL_UN_SYLE)));
//                values.put(COL_NONEXCEMPT, c.getInt(c.getColumnIndex(COL_NONEXCEMPT)));
                values.put(COL_OPTIMISE, c.getInt(c.getColumnIndex(COL_OPTIMISE)));
				values.put(COL_NUMBER_OF_UNITS,c.getString(c.getColumnIndex(COL_NUMBER_OF_UNITS)));
				values.put(COL_TRANSACTION_VALUE, c.getString(c.getColumnIndex((COL_TRANSACTION_VALUE).toString())));
				values.put(COL_OVERRIDEN, c.getString(c.getColumnIndex((COL_OVERRIDEN).toString())));
				values.put(COL_CHECKSTATUS,c.getString(c.getColumnIndex((COL_CHECKSTATUS).toString())));
				values.put(COL_NONEXCEMPT,c.getString(c.getColumnIndex((COL_NONEXCEMPT).toString())));
				values.put(COL_NOS,"");

				values.put("count", String.valueOf(c.getCount()));
				jArray.put(values);
				response.put("getOrders", jArray);
			} catch (Exception e) {
					e.printStackTrace();
					new Exceptions(context,GetDBData.this
						.getClass().getName(), Arrays.toString(e.getStackTrace()));

			} finally {
                //closes database connection
                closeDatabaseConnection();
                if(c != null) {
                    //close the cursor
                 //   c.close();
                }
            }
		}
		return response;
	}
	/**
	 * This method is to get transactions done by user from transaction_details table
	 * for push orders to mobile web portal
	 * 
	 * author suman
	 * @return json Array, transactions done by user to push to mobile portal
	 */
	public JSONObject getTransactionsToPushOrders() {
        JSONObject response = new JSONObject();
		JSONArray jArray = new JSONArray();
        //gets connection for SQLite database
        getDatabaseConnection(dbCode);
		Cursor c = db.rawQuery("SELECT *  FROM " + TABLE_TRANSACTIONS+" where "+COL_PUSH_STATUS+" != '1' ", null);
//				Log.d(TAG,
//						"DatabaseHelper::push orders:::: sizes of push orders orders :"
//								+ c.getCount());
		while (c.moveToNext()) {
			try {
                JSONObject values = new JSONObject();
				String push_status = c.getString(c.getColumnIndex(COL_PUSH_STATUS));
				values.put(COL_PUSH_STATUS, push_status);
				//Log.d(TAG, "DatabaseHelper::getTransactionsToPushOrders:::: COL_PUSH_STATUS==" + push_status);
				String dg_weight = c.getString(c.getColumnIndex(COL_DG_WEIGHT));
				values.put(COL_DG_WEIGHT, dg_weight);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dg_weight=="+dg_weight);
				String un_class_id = c.getString(c.getColumnIndex(COL_UN_CLASS_ID));
				values.put("unclass_id", un_class_id);//dont change this key, its matching with server key
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dg_weight=="+dg_weight);
				String un_number = c.getString(c.getColumnIndex(COL_UN_NUMBER));
				values.put(COL_UN_NUMBER, un_number);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: un_number=="+un_number);
				String pkg_group = c.getString(c.getColumnIndex(COL_PKG_GROUP));
				values.put(COL_PKG_GROUP, pkg_group);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: pkg_group=="+pkg_group);
				String weight_type = c.getString(c.getColumnIndex(COL_WEIGHT_TYPE));
				values.put(COL_WEIGHT_TYPE,weight_type );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: weight_type=="+weight_type);
				String ibc_residue_status = c.getString(c.getColumnIndex(COL_IBC_RESIDUE_STATUS));
				values.put(COL_IBC_RESIDUE_STATUS,ibc_residue_status );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: ibc_residue_status=="+ibc_residue_status);
				String weight_in_kgs = c.getString(c.getColumnIndex(COL_WEIGHT_IN_KGS));
				values.put(COL_WEIGHT_IN_KGS, weight_in_kgs);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: weight_in_kgs=="+weight_in_kgs);
				String dangerous_placard_original = c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD));
				values.put(COL_DANGEROUS_PLACARD,dangerous_placard_original );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dangerous_placard=="+dangerous_placard_original);
				String unnumber_display = c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY));
				values.put(COL_UNNUMBER_DISPLAY, unnumber_display);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: unnumber_display=="+unnumber_display);
				String description = c.getString(c.getColumnIndex(COL_DESCRIPTION));
				values.put(COL_DESCRIPTION,description );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: description=="+description);
				String bl = c.getString(c.getColumnIndex(COL_BL));
				values.put(COL_BL,bl );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: bl=="+bl);
				String subsidary_exist = c.getString(c.getColumnIndex(COL_SUBSIDARY_EXIST));
				values.put(COL_SUBSIDARY_EXIST,subsidary_exist );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: subsidary_exist=="+subsidary_exist);
				String number_of_units = c.getString(c.getColumnIndex(COL_NO_OF_UNITS));
				values.put("no_of_units",number_of_units );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: number_of_units=="+number_of_units);
				String erap_index = c.getString(c.getColumnIndex(COL_ERAP_INDEX));
				values.put(COL_ERAP_INDEX,erap_index );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: erap_index=="+erap_index);
				String ibc_status = c.getString(c.getColumnIndex(COL_IBC_STATUS));
				values.put(COL_IBC_STATUS, ibc_status);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: ibc_status=="+ibc_status);
				String group_name = c.getString(c.getColumnIndex(COL_GROUP_NAME));
				values.put(COL_GROUP_NAME,group_name );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: group_name=="+group_name);
				String untype = c.getString(c.getColumnIndex(COL_UNTYPE));
				values.put(COL_UNTYPE, untype);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: untype=="+untype);
				String weight_index = c.getString(c.getColumnIndex(COL_WEIGHT_INDEX));
				values.put(COL_WEIGHT_INDEX, weight_index);
				//	Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: weight_index=="+weight_index);
				String max_placard = c.getString(c.getColumnIndex(COL_MAX_PLACARD));
				values.put(COL_MAX_PLACARD, max_placard);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: max_placard=="+max_placard);
				String primary_placard = c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD));
                if(primary_placard.equalsIgnoreCase("2.4")) {
                    primary_placard = "2.2";
                }
				values.put(COL_PRIMARY_PLACARD, primary_placard);
				String date_time= c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME));
				values.put(COL_INSERTED_DATE_TIME,date_time);
				values.put(COL_TRANSACTION_STATUS, c.getString(c.getColumnIndex(COL_TRANSACTION_STATUS)));
//				values.put(COL_INSERTED_DATE_TIME, c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME)));

				values.put(COL_NON_EXEMPT, c.getString(c.getColumnIndex(COL_NONEXCEMPT)));
				values.put(COL_CONSIGNEE_DANGER, c.getString(c.getColumnIndex(COL_CONSIGNEE_DANGER)));
				values.put(COL_UN_SYLE, c.getString(c.getColumnIndex(COL_UN_SYLE)));
				values.put(COL_SPECIAL_PROVISION, c.getString(c.getColumnIndex(COL_SPECIAL_PROVISION)));

				values.put("nos", "");

				values.put(COL_USER_NAME,c.getString(c.getColumnIndex(COL_USER_NAME)));
				values.put("weight_in_lbs","");
				values.put("username",MyAppData.getInstance().getUserName());
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dg_weight=="+dg_weight);
				String gross_weight = c.getString(c.getColumnIndex(COL_GROSS_WEIGHT));
				values.put(COL_GROSS_WEIGHT,gross_weight );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: gross_weight=="+gross_weight);
				String secondary_placard = c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD));
				values.put(COL_SECONDARY_PLACARD, secondary_placard);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: secondary_placard=="+secondary_placard);
				String package_weight = c.getString(c.getColumnIndex(COL_PACKAGE_WEIGHT));
				values.put(COL_PACKAGE_WEIGHT, package_weight);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: package_weight=="+package_weight);
				String erap_status = c.getString(c.getColumnIndex(COL_ERAP_STATUS));
				values.put(COL_ERAP_STATUS, erap_status);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: erap_status=="+erap_status);
				String erap_no = c.getString(c.getColumnIndex(COL_ERAP_NO));
				values.put(COL_ERAP_NO, erap_no);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: erap_no=="+erap_no);

				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: inserted_date_time=="+inserted_date_time);
				String nameInTransaction = c.getString(c.getColumnIndex(COL_NAME));
                if(nameInTransaction.equalsIgnoreCase("2.4")) {
                    nameInTransaction = "2.2";
                }
				values.put(COL_NAME, nameInTransaction);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: name=="+nameInTransaction);
				values.put(COL_TRANSACTION_ID, c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
				values.put(COL_DANGEROUS_PLACARD_ORIGINAL, c.getString(c.getColumnIndex(COL_DANGEROUS_PLACARD_ORIGINAL)));
				values.put(COL_USER_ID, c.getString(c.getColumnIndex(COL_USER_ID)));
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: user_id=="+c.getString(c.getColumnIndex(COL_USER_ID)));
				values.put(COL_ID, c.getString(c.getColumnIndex(COL_ID)));
				//values.put(COL_USER_ID_WEB, c.getString(c.getColumnIndex(COL_USER_ID_WEB)));
                values.put(COL_USER_ID_WEB, "123");
                //Log.d(TAG, "DatabaseHelper::getTransactionsToPushOrders:::: COL_USER_ID_WEB==" + c.getString(c.getColumnIndex(COL_USER_ID_WEB)));
               // values.put(COL_TRANSACTION_ID_WEB, c.getString(c.getColumnIndex(COL_TRANSACTION_ID_WEB)));
                values.put(COL_TRANSACTION_ID_WEB, "123");
               // Log.d(TAG, "DatabaseHelper::getTransactionsToPushOrders:::: COL_TRANSACTION_ID_WEB==" + c.getString(c.getColumnIndex(COL_TRANSACTION_ID_WEB)));
				//values.put(COL_PUSH_STATUS, c.getString(c.getColumnIndex(COL_PUSH_STATUS)));
				//System.out.println(COL_PUSH_STATUS+" = "+c.getString(c.getColumnIndex(COL_PUSH_STATUS)));
				jArray.put(values);
               // Log.e("Suman", "" + values);
				response.put("getPushOrders", jArray);//dont change key
				response.put("placard_content",getPlacardingTypeTable());
				response.put("user_name",MyAppData.getInstance().getUserName());

				Log.e(TAG+"non_exempt_check ",c.getString(c.getColumnIndex(COL_NONEXCEMPT))+"");
				Log.e(TAG+"response : ", ""+response);

			} catch (Exception e) {
				new Exceptions(context,GetDBData.this
						.getClass().getName(), Arrays.toString(e.getStackTrace()));
			} finally {
                //closes database connection
                closeDatabaseConnection();
                if(c != null) {
                    //close the cursor
                   // c.close(); dont open close cursor, mustple values are not adding if cursor cloded
                }
            }
		}
		return response;
	}
	/**
	 * This method is to get  transactions done by user from temptransactions table
	 * for push orders to mobile web portal
	 *
	 * author suman
	 * @return json Array, transactions done by user to push to mobile portal
	 */
	public JSONObject getTempTransactionDetails() {
		JSONObject response = new JSONObject();
		JSONArray jArray = new JSONArray();
		//gets connection for SQLite database
		getDatabaseConnection(dbCode);
		Cursor c = db.rawQuery("SELECT *  FROM " + TABLE_TEMP_TRANSACTIONS, null);
//				Log.e(TAG,
//						"DatabaseHelper:getTempTransactionDetails:::: sizes of push orders orders :"
//								+ c.getCount());
		while (c.moveToNext()) {
			try {
				JSONObject values = new JSONObject();
				String id = c.getString(c.getColumnIndex("id"));
				values.put("id", id);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dg_weight=="+dg_weight);
				String name = c.getString(c.getColumnIndex("name"));
				values.put("name", name);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dg_weight=="+dg_weight);
				String un_number = c.getString(c.getColumnIndex(COL_UN_NUMBER));
				values.put(COL_UN_NUMBER, un_number);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: un_number=="+un_number);
				String dg_weight = c.getString(c.getColumnIndex(COL_DG_WEIGHT));
				values.put(COL_DG_WEIGHT, dg_weight);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: pkg_group=="+pkg_group);
				//String weight_type = c.getString(c.getColumnIndex(COL_WEIGHT_TYPE));
				//values.put(COL_WEIGHT_TYPE,weight_type );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: weight_type=="+weight_type);
				String total_weight = c.getString(c.getColumnIndex(COL_TOTAL_WEIGHT));
				values.put(COL_TOTAL_WEIGHT,total_weight );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: ibc_residue_status=="+ibc_residue_status);
				String erap_number = c.getString(c.getColumnIndex("erap_number"));
				values.put("erap_number", erap_number);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: weight_in_kgs=="+weight_in_kgs);
				String unnumber_display = c.getString(c.getColumnIndex(COL_UNNUMBER_DISPLAY));
				values.put(COL_UNNUMBER_DISPLAY,unnumber_display );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dangerous_placard=="+dangerous_placard_original);

				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: subsidary_exist=="+subsidary_exist);
				String number_of_units = c.getString(c.getColumnIndex(COL_NO_OF_UNITS));
				values.put(COL_NO_OF_UNITS,number_of_units );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: number_of_units=="+number_of_units);
				String ibc_status = c.getString(c.getColumnIndex(COL_IBC_STATUS));
				values.put(COL_IBC_STATUS, ibc_status);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: ibc_status=="+ibc_status);
				String group_name = c.getString(c.getColumnIndex(COL_GROUP_NAME));
				values.put(COL_GROUP_NAME,group_name );
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: group_name=="+group_name);
				String untype = c.getString(c.getColumnIndex(COL_UNTYPE));
				values.put(COL_UNTYPE, untype);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: untype=="+untype);
				String weight_index = c.getString(c.getColumnIndex(COL_WEIGHT_INDEX));
				values.put(COL_WEIGHT_INDEX, weight_index);
				//	Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: weight_index=="+weight_index);
				//String max_placard = c.getString(c.getColumnIndex(COL_MAX_PLACARD));
				//values.put(COL_MAX_PLACARD, max_placard);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: max_placard=="+max_placard);
				String primary_placard = c.getString(c.getColumnIndex(COL_PRIMARY_PLACARD));
				values.put(COL_PRIMARY_PLACARD, primary_placard);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: dg_weight=="+dg_weight);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: gross_weight=="+gross_weight);
				String secondary_placard = c.getString(c.getColumnIndex(COL_SECONDARY_PLACARD));
				values.put(COL_SECONDARY_PLACARD, secondary_placard);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: secondary_placard=="+secondary_placard);
				String un_style = c.getString(c.getColumnIndex("un_style"));
				values.put("un_style", un_style);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: package_weight=="+package_weight);
				String special_provision = c.getString(c.getColumnIndex("special_provision"));
				values.put("special_provision", special_provision);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: erap_status=="+erap_status);
				//Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: name=="+nameInTransaction);
				values.put(COL_TRANSACTION_ID, c.getString(c.getColumnIndex(COL_TRANSACTION_ID)));
				values.put(COL_USER_ID, c.getString(c.getColumnIndex(COL_USER_ID)));
                //Log.d(TAG,"DatabaseHelper::getTransactionsToPushOrders:::: user_id=="+c.getString(c.getColumnIndex(COL_USER_ID)));
				values.put(COL_ID, c.getString(c.getColumnIndex(COL_ID)));
				values.put(COL_USER_ID_WEB, c.getString(c.getColumnIndex(COL_USER_ID_WEB)));
				values.put(COL_TRANSACTION_ID_WEB, c.getString(c.getColumnIndex(COL_TRANSACTION_ID_WEB)));
				String non_exempt = c.getString(c.getColumnIndex(COL_NONEXCEMPT));
				values.put(COL_NONEXCEMPT, non_exempt);
				String non_optimise = c.getString(c.getColumnIndex("non_optimise"));
				values.put("non_optimise", non_optimise);
				String non_exempt_default = c.getString(c.getColumnIndex("non_exempt_default"));
				values.put("non_exempt_default", non_exempt_default);
				String nonoptimise_display_mandatory = c.getString(c.getColumnIndex("nonoptimise_display_mandatory"));
				values.put("nonoptimise_display_mandatory", nonoptimise_display_mandatory);
				String display_placard_mandatory = c.getString(c.getColumnIndex("display_placard_mandatory"));
				values.put("display_placard_mandatory", display_placard_mandatory);
				String display_primary_placard = c.getString(c.getColumnIndex("display_primary_placard"));
				values.put("display_primary_placard", display_primary_placard);
				String display_secondary_placard = c.getString(c.getColumnIndex("display_secondary_placard"));
				values.put("display_secondary_placard", display_secondary_placard);
				String swap_for_danger = c.getString(c.getColumnIndex("swap_for_danger"));
				values.put("swap_for_danger", swap_for_danger);
				String consignor_1000kg = c.getString(c.getColumnIndex("consignor_1000kg"));
				values.put("consignor_1000kg", consignor_1000kg);

				jArray.put(values);
				response.put("getTempDetails", jArray);

			} catch (Exception e) {
				new Exceptions(context,GetDBData.this
						.getClass().getName(), Arrays.toString(e.getStackTrace()));
			} finally {
				//closes database connection
				closeDatabaseConnection();
			}
		}
		return response;
	}

	/**
	 * This cheks rules data status from database tables "un number info","unclasses","sp84","erap" and
	 * "weight" tables
	 *
	 * author suman
	 * @return rules data count
	 */
	//This method is used in license verification process, while verifying license if rules
	//data present in the database just get only license details
	public boolean getRulesDataCount() {
		boolean status = false;

		try {
            if(getWeightTableCount() && getUNNumberTableCount() && getUnClassInfoCount()
					&& getSp84Count() && getErapCount()) {
				status = true;
			} 
		} catch (Exception e) {
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		}
		return status;
	}
	/**
	 * This method returns "true" if weight table filled with rules data
	 * 
	 * author suman
	 * @return weight table data status
	 */
	private boolean getWeightTableCount() {
		boolean status = false;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_WEIGHT, null);
			if(c.getCount() > 0) {
				status = true;
			}
		} catch (Exception e) {
            Log.e(GetDBData.this.getClass().getSimpleName(), "Error getWeightTableCount() method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
        }   finally {
            //closes database connection
            closeDatabaseConnection();

        }
        return status;
	}//getWeightTableCount()
	/**
	 * This method returns "true" if un number table filled with rules data
	 *
	 * author suman
	 * @return un number table data status
	 */
	private boolean getUNNumberTableCount() {
		boolean status = false;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_UNNUMBER_INFO, null);
			if(c.getCount() > 0) {
				status = true;
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getUNNumberTableCount() method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return status;
	}//getUNNumberTableCount()
	/**
	 * This method returns "true" if unclasses table filled with rules data
	 * 
	 * author suman
	 * @return unclasses table data status
	 */
	private boolean getUnClassInfoCount() {
		boolean status = false;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_UNCLASS, null);
			if(c.getCount() > 0) {
				status = true;
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getUnClassInfoCount() method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return status;
	}//getUnClassInfoCount()
	/**
	 * This method returns "true" if sp84 table filled with rules data
	 * 
	 * author suman
	 * @return sp84 table data status
	 */
	private boolean getSp84Count() {
		boolean status = false;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_SP84_INFO, null);
			if(c.getCount() > 0) {
				status = true;
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getSp84Count() method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return status;
	}//getSp84Count()
	/**
	 * This method returns "true" if erap table filled with rules data
	 * 
	 * author suman
	 * @return erap table data status
	 */
	private boolean getErapCount() {
		boolean status = false;
        Cursor c = null;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			c = db.rawQuery("SELECT *  FROM " + TABLE_ERAP_INFO, null);
			if(c.getCount() > 0) {
				status = true;
			}
		} catch (Exception e) {
			Log.e(GetDBData.this.getClass().getSimpleName(), "Error getErapCount() method " + e.toString());
			new Exceptions(context,GetDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
            //closes database connection
            closeDatabaseConnection();
            if(c != null) {
                //close the cursor
                c.close();
            }
        }
		return status;
	}//getErapCount()
	/**
	 * This method is to return weight table rows count that were inserted on rules data loading
	 * this is to check, are weight table data completely loaded or not.Will receive a count value from service,
	 * have to check this with inserted count.
	 * @return count
	 */
	public long getWeightTalbeCount() {
		String countQuery = "SELECT  * FROM " + TABLE_WEIGHT;
		Cursor c = null;
		long count = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(countQuery, null);
			if (c.getCount() > 0 ) {
				count = c.getCount();
			}
			//Log.v(LOGTAG,"weight table rows count "+count);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeDatabaseConnection();
		}
		return count;
	}
	/**
	 * This method is to return unnumber_info table rows count that were inserted on rules data loading
	 * this is to check, are unnumber_info table data completely loaded or not.Will receive a count value from service,
	 * have to check this with inserted count.
	 * @return count
	 */
	public long getUnNumberTalbeCount() {
		String countQuery = "SELECT  * FROM " +TABLE_UNNUMBER_INFO;
		Cursor c = null;
		long count = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(countQuery, null);
			if (c.getCount() > 0 ) {
				count = c.getCount();
			}
			//Log.v(LOGTAG,"unnumber_info table rows count "+count);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeDatabaseConnection();
		}
		return count;
	}
	/**
	 * This method is to return un_classes table rows count that were inserted on rules data loading
	 * this is to check, are un_classes table data completely loaded or not.Will receive a count value from service,
	 * have to check this with inserted count.
	 * @return count
	 */
	public long getUnClassesTalbeCount() {
		String countQuery = "SELECT  * FROM " +TABLE_UNCLASS;
		Cursor c = null;
		long count = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(countQuery, null);
			if (c.getCount() > 0 ) {
				count = c.getCount();
			}
			//Log.v(LOGTAG,"un_classes table rows count "+count);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeDatabaseConnection();
		}
		return count;
	}
	/**
	 * This method is to return erap_info table rows count that were inserted on rules data loading
	 * this is to check, are erap_info table data completely loaded or not.Will receive a count value from service,
	 * have to check this with inserted count.
	 * @return count
	 */
	public long getErapTalbeCount() {
		String countQuery = "SELECT  * FROM " +TABLE_ERAP_INFO;
		Cursor c = null;
		long count = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(countQuery, null);
			if (c.getCount() > 0 ) {
				count = c.getCount();
			}
			//Log.v(LOGTAG,"erap_info table rows count "+count);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeDatabaseConnection();
		}
		return count;
	}
	/**
	 * This method is to return sp84_info table rows count that were inserted on rules data loading
	 * this is to check, are sp84_info table data completely loaded or not.Will receive a count value from service,
	 * have to check this with inserted count.
	 * @return count
	 */
	public long getSp84TalbeCount() {
		String countQuery = "SELECT  * FROM " +TABLE_SP84_INFO;
		Cursor c = null;
		long count = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(countQuery, null);
			if (c.getCount() > 0 ) {
				count = c.getCount();
			}
			//Log.v(LOGTAG,"sp84_info table rows count "+count);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeDatabaseConnection();
		}
		return count;
	}
    /**
     * This method is to return segregation table rows count that were inserted on rules data loading
     * this is to check, are segregation table data completely loaded or not.Will receive a count value from service,
     * have to check this with inserted count.
     * @return count
     */
    public long getGroupCompatibilityTableCount() {
        String countQuery = "SELECT  * FROM " +TABLE_GROUP_COMPITABILITY;
        Cursor c = null;
        long count = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            c = db.rawQuery(countQuery, null);
            if (c.getCount() > 0 ) {
                count = c.getCount();
            }
            //Log.v(LOGTAG,"getGroupCompatibilityTableCount table rows count "+count);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            closeDatabaseConnection();
        }
        return count;
    }
    /**
     * This method is to return segregation table rows count that were inserted on rules data loading
     * this is to check, are segregation table data completely loaded or not.Will receive a count value from service,
     * have to check this with inserted count.
     * @return count
     */
    public long getClassCompatibilityTableCount() {
        String countQuery = "SELECT  * FROM " +TABLE_CLASS_COMITABILITY;
        Cursor c = null;
        long count = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            c = db.rawQuery(countQuery, null);
            if (c.getCount() > 0 ) {
                count = c.getCount();
            }
            //Log.v(LOGTAG,"getClassCompatibilityTableCount table rows count "+count);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            closeDatabaseConnection();
        }
        return count;
    }


	public JSONArray getPlacardingTypeTable() {
		Cursor c = null;
		JSONArray jArray = new JSONArray();
		try {
			String selectQuery = "SELECT * FROM " + TABLE_PLACARDING_TYPE +" where "+COL_PUSH_STATUS+" != '1' ";
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			c = db.rawQuery(selectQuery, null);
			Log.v("Suman","selectQuery "+selectQuery);
			while (c.moveToNext()) {
				JSONObject values = new JSONObject();
				values.put(COL_ID,c.getString(c.getColumnIndex(COL_ID)));
				values.put(COL_ACTION,c.getString(c.getColumnIndex(COL_ACTION)));
				values.put(COL_TRANS_ID,c.getString(c.getColumnIndex(COL_TRANS_ID)));
				values.put(COL_NON_OPTIMISE,c.getString(c.getColumnIndex(COL_NON_OPTIMISE)));
				values.put(COL_SEMI_OPTIMISE,c.getString(c.getColumnIndex(COL_SEMI_OPTIMISE)));
				values.put(COL_OPTIMISE,c.getString(c.getColumnIndex(COL_OPTIMISE)));
				values.put(COL_PUSH_STATUS,c.getString(c.getColumnIndex(COL_PUSH_STATUS)));
				//send device id explicitly as a input
				values.put("device_id",new Utils().getDeviceId(context));
				//send user name in every json array
				values.put("username", MyAppData.getInstance().getUserName());
				values.put(COL_INSERTED_DATE_TIME,c.getString(c.getColumnIndex(COL_INSERTED_DATE_TIME)));
				jArray.put(values);
				Log.v("Suman","jArray "+jArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jArray;
	}



	public String getMaxTransId() {
		Cursor c = null;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String selectQuery = "SELECT "+COL_ID+"  FROM " + TABLE_TRANSACTIONS + " where " + COL_ID + " = "+"(SELECT MAX("+COL_ID+") as id FROM "+ TABLE_TRANSACTIONS+")";
			c = db.rawQuery(selectQuery, null);
			while (c.moveToNext()) {
				return c.getString(c.getColumnIndex(COL_ID));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}



    @Override
    public Object getInstance() {
        return null;
    }
}