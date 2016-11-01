package com.ideabytes.dgsms.ca.database;

import android.content.Context;
import android.util.Log;

import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.InsertIntoTransctn;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : DeleteDBData
 * @author:  Suman
 * @Description : This class is to delete database table's data
 ***************************************************************/
public class DeleteDBData extends  DatabaseDAO implements DBConstants {
	Context context = null;
	private int dbCode = 0;
	// private final String TAG = "DeleteDBData";
	public DeleteDBData(Context context) {
		super(context);
		this.context = context;
	}
	/**
	 * @param weight_id
	 * @author ideabytes
	 * @return void
	 * @category method is used in server sync process , to delete the records
	 *           from un_weight table
	 */
	public void deleteFromWeight(int weight_id) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_WEIGHT + " where " + COL_ID
					+ " ='" + weight_id + "'";
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromWeight method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * @param un_class_id
	 * @author ideabytes
	 * @return void
	 * @category method is used in server sync process , to delete the records
	 *           from un classes table
	 */
	public void deleteFromUNClass(int un_class_id) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_UNCLASS + " where "
					+ COL_ID + " = '" + un_class_id + "'";
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromUNClass method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * @param un_number
	 * @author Ideabytes
	 * @return void
	 * @category method is used in server sync process , to delete the records
	 *           from un numbers table
	 */
	public void deletefromUNNumbers(String un_number) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_UNNUMBER_INFO + " where "
					+ COL_UN_NUMBER + " = '" + un_number + "'";
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deletefromUNNumbers method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	//delete the transaction that is already added from transaction_details table
	// on clicking delete button from banner

	//this method not in use from 23/07/15, delete driven to directly model package
	// DeleteTransaction class method
	public void deleteFromTransactionTableLookup(String colid, String userid,
			String maxplacard, String transactionid) {
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("user_id", userid);
			jObject.put("maxPlacard", maxplacard);
			jObject.put("transaction_id", colid);
			String tosp = jObject.toString();
			InsertIntoTransctn insertIntoTransctn = new InsertIntoTransctn();
			insertIntoTransctn.deleteFromTransaction(tosp);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromTransactionTableLookup method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * @author Ajay
	 * @param id
	 */
	public void deleteFromSp84(int id) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_SP84_INFO + " where "
					+ COL_ID + " = '" + id + "'";
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromSp84 method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * This method deletes latest entry into trnsaction_details table
	 * @author Suman
	 */
	public void deleteFromTransLatestEntry() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_TRANSACTIONS + " where id = " + "(select max("+COL_ID+") from " +
					TABLE_TRANSACTIONS+")";
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromTransLatestEntry method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	/**
	 * This method deletes all transactions from transaction_details,
	 * this is called when max transactions are exceeded the limit to verify license
	 * 
	 * @author Suman
	 */
	public void deleteFromTrasactions() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_TRANSACTIONS ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromTrasactions method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	/**
	 * once picking up an order is done, remove particular entry from 'orders' table
	 *  
	 * @author Suman
	 * @param id
	 * 
	 */
	public void deleteFromOrders(int id) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_ORDERS + " where "
					+ COL_ID + " = '" + id + "'";
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		}  catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteFromOrders method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * This method is to delete complete license information from databse table
	 */
	public void deleteConfigDetails() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_LISENCE_DETAILS ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteConfigDetails method " + e.toString());
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	/**
	 * This method is to kill complete databse table data on license expiry case except terms check status
	 */
	public void deleteRulesData() {
		//deleteConfigDetails();
		deleteWeightTable();
		deleteUnNumberTable();
		deleteUnClassTable();
		deleteErapTable();
		deleteSp84Table();
		deleteTransactionTable();
	}

	public void deleteWeightTable() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_WEIGHT ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteConfigDetails method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	public void deleteUnNumberTable() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_UNNUMBER_INFO ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteUnNumberTable method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	public void deleteUnClassTable() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_UNCLASS ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteUnClassTable method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	public void deleteSp84Table() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_SP84_INFO ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteSp84Table method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}
	public void deleteErapTable() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_ERAP_INFO ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteErapTable method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	public void deleteTransactionTable() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_TRANSACTIONS ;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error deleteTransactionTable method " + e.toString());
			new Exceptions(context,DeleteDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}



	/**
	 * This method is used in 'Delete All' case to delete all shipments at one click
	 * @param ids
	 */
	public void deleteAllShipments(final ArrayList<String> ids) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			for (int i = 0; i < ids.size(); i++) {
				String deleteQuery = "UPDATE " + TABLE_TRANSACTIONS + " set "+COL_TRANSACTION_STATUS+" = 1 where id = " + ids.get(i);
				Log.d("deleteQuery",deleteQuery);
				db.execSQL(deleteQuery);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}

	public void truncateLicenseData() {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			String deleteQuery = "DELETE FROM " + TABLE_LISENCE_DETAILS;
			// Log.d("query", deleteQuery);
			db.execSQL(deleteQuery);
		} catch (Exception e) {
			Log.e(DeleteDBData.this.getClass().getSimpleName(), "Error truncateLicenseData method " + e.toString());
			Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
	}



}