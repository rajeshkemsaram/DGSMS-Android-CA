package com.ideabytes.dgsms.ca.database;

import java.io.File;

import org.com.ca.dgsms.ca.model.DBConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/*************************************************************
 * Copy right @Ideabytes SofTABLE_LICENSE_DETAILStware India Private Limited
 * Web site : http://ideabytes.com
 * Name : DatabaseHelper
 * author:  Suman
 * Description : This class is to create database tables
 * Modified Date : 23-11-2015
 **************************************************************/
public class DatabaseHelper extends SQLiteOpenHelper implements DBConstants {
	private static String deviceId = "";
	private final String TAG = "DatabaseHelper";
	public SQLiteDatabase db;
	private static DatabaseHelper mInstance = null;
	// Constructor to create database in Phone memory
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	// Create table un_numbers
	// Modified by Suman/10-01-2015, added
	// COL_TD_ID,COL_UNTYPE,COL_UNNO_DESCRIPTION,COL_UNNO_DISPLAY_STATUS
	private static final String CREATE_TABLE_UNNUMBERS = "CREATE TABLE "
			+ TABLE_UNNUMBER_INFO + "(" + COL_ID + " INTEGER ," + COL_UN_NUMBER
			+ " TEXT ," + COL_UN_CLASS_ID + " TEXT ," + COL_UNTYPE + " TEXT ,"
			+ COL_DESCRIPTION + " TEXT ," + COL_UNNUMBER_DISPLAY_STATUS
			+ " TEXT , " + COL_STATUS + " TEXT ,	" + COL_SHIPPING_NAME
			+ " TEXT DEFAULT ' ' , " + COL_GROUP_NAME + " TEXT DEFAULT ' ' ,"
			+ COL_UPDATED_DATETIME + " TEXT  " + ")";
	// create table un_classes
	// Modified by Suman/10-01-2015 added COL_DANGEROUS_PLACARD
	private static final String CREATE_TABLE_UNCLASSES = "CREATE TABLE "
			+ TABLE_UNCLASS + "(" + COL_ID + " INTEGER PRIMARY KEY NOT NULL ,"
			+ COL_NAME + " TEXT ," + COL_PARENT_CLASS + " TEXT ,"
			+ COL_ROAD_WEIGHT_GROUP + " INTEGER , " + COL_SHIP_WEIGHT_GROUP
			+ " INTEGER ," + COL_RAIL_WEIGHT_GROUP + " INTEGER ,"
			+ COL_PRIMARY_PLACARD + " TEXT ," + COL_SECONDARY_PLACARD
			+ " TEXT ," + COL_UNNUMBER_DISPLAY_STATUS + " TEXT ,"
			+ COL_SPECIAL_PROVISION + " INTEGER ,"
			+ COL_NONEXCEMPT + " INTEGER ,"
			+ COL_UPDATED_DATETIME + " TEXT ,"
			+ COL_DANGEROUS_PLACARD + " TEXT" + ")";

	// create table weight
	private static final String CREATE_TABLE_WEIGHT = "CREATE TABLE "
			+ TABLE_WEIGHT + " (" + COL_ID + " INTEGER PRIMARY KEY NOT NULL ,"
			+ COL_NAME + " TEXT ," + COL_STATUS + " TEXT " + ")";

	// create table updated database date time
	private static final String CREATE_TABLE_DATABASEUPDATEDTIME = "CREATE TABLE "
			+ TABLE_UPDATED_AT
			+ " ("
			+ COL_TIME_ID
			+ " INTEGER PRIMARY KEY NOT NULL , "
			+ COL_UPDATED_AT
			+ " TEXT"
			+ ")";

	// create table language
	private static final String CREATE_TABLE_LANGUAGE = "CREATE TABLE "
			+ TABLE_LANGUAGE + "(" + COL_LANGUAGE_ID
			+ " INTEGER PRIMARY KEY  AUTOINCREMENT ," + COL_SELECTED_STATE
			+ " INTEGER ," + COL_LANGUAGE_NAME + " TEXT " + ")";

	// create table content
	private static final String CREATE_TABLE_LANGUAGE_CONTENT = "CREATE TABLE "
			+ TABLE_LANGUAGE_CONTENT + "(" + COL_LANGUAGE_CONTENT_ID
			+ " TEXT  NOT NULL ," + COL_LANGUAGE_ID + " INTEGER ,"
			+ COL_MSG_TEXT + " TEXT  NOT NULL" + ")";

	private static final String CREATE_TABLE_LICENCECHECKDATEANDTIME = "CREATE TABLE "
			+ TABLE_LICENCECHECK_UPDATED_DATE
			+ " ("
			+ COL_LICENCE_TIME_ID
			+ " INTEGER PRIMARY KEY NOT NULL, "
			+ COL_LICENCE_UPDATED_AT
			+ " TEXT" + ")";

	private static final String CREATE_TABLE_ERAP = "CREATE TABLE "
			+ TABLE_ERAP_INFO + " (" + COL_ID
			+ " INTEGER PRIMARY KEY  AUTOINCREMENT ," + COL_UNNUMBERDESC_ID
			+ " TEXT , " + COL_PKG_GROUP + " TEXT , " + COL_ERAP_INDEX
			+ " TEXT , " + COL_ERAP_STATUS + " TEXT" + ")";

	private static final String CREATE_TABLE_SP84_INFO = "CREATE TABLE "
			+ TABLE_SP84_INFO + " (" + COL_ID + " INTEGER ," + COL_STATUS + " TEXT ,"+ COL_VALUE
			+ " TEXT " + ")";

	private static final String CREATE_TABLE_EXCLUDED_UNNUMBERS = "CREATE TABLE "
			+ TABLE_EXCLUDED_UNNUMBERS + " (" + COL_UN_NUMBER + " TEXT " + ")";

	private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE "

			+ TABLE_TRANSACTIONS
			+ " ("
			+ COL_ID
			+ " INTEGER PRIMARY KEY  AUTOINCREMENT , "
			+ COL_NAME
			+ " TEXT , "
			+ COL_BL
			+ " TEXT , "
			+ COL_UN_NUMBER
			+ " TEXT , "
			+ COL_DG_WEIGHT
			+ " REAL ,"
			+ COL_PACKAGE_WEIGHT
			+ " REAL , "
			+ COL_GROSS_WEIGHT
			+ " REAL ,"
			+ COL_WEIGHT_TYPE
			+ " INTEGER , "
			+ COL_WEIGHT_IN_KGS
			+ " REAL ,"
			+ COL_ERAP_NO
			+ " TEXT ,"
			+ COL_DESCRIPTION
			+ " TEXT ,"
			+ COL_SUBSIDARY_EXIST
			+ " INTEGER , "
			+ COL_UNNUMBER_DISPLAY
			+ " INTEGER , "
			+ COL_DANGEROUS_PLACARD
			+ " INTEGER , "
			+ COL_DANGEROUS_PLACARD_ORIGINAL
			+ " INTEGER , "
			+ COL_PRIMARY_PLACARD
			+ " TEXT, "
			+ COL_SECONDARY_PLACARD
			+ " TEXT, "
			+ COL_WEIGHT_INDEX
			+ " REAL , "
			+ COL_UNTYPE
			+ " TEXT , "
			+ COL_TRANSACTION_STATUS
			+ " INTEGER DEFAULT 0 , "
			+ COL_USER_ID
			+ " TEXT DEFAULT 0 , "
			+ COL_ERAP_STATUS
			+ " INTEGER DEFAULT 0 , "
			+ COL_PKG_GROUP
			+ " TEXT , "
			+ COL_ERAP_INDEX
			+ " TEXT ,"
			+ COL_UN_CLASS_ID
			+ " INTEGER ,"
			+ COL_NO_OF_UNITS
			+ " INTEGER ,"
			+ COL_INSERTED_DATE_TIME
			+ " TEXT , "
			+ COL_MAX_PLACARD
			+ " INTEGER , "
			+ COL_IBC_STATUS
			+ " INTEGER , "
			+ COL_IBC_RESIDUE_STATUS
			+ " INTEGER , "
			+ COL_GROUP_NAME
			+ " TEXT , "
			+ COL_TRANSACTION_ID
			+ " TEXT , "
			+ COL_USER_ID_WEB
			+ " TEXT , "
			+ COL_TRANSACTION_ID_WEB + " TEXT , "
			+ COL_NONEXCEMPT + " INTEGER , "
			+ COL_OPTIMISE + " INTEGER , "
			+ COL_PUSH_STATUS + " TEXT , "
			+ COL_NON_EXEMPT + " TEXT , "
			+ COL_NOS + " TEXT , "
			+ COL_USER_NAME + " TEXT , "
            + COL_CONSIGNEE_DANGER+" INTEGER , "
            + COL_SPECIAL_PROVISION+" INTEGER , "
            + COL_UN_SYLE+" INTEGER  "
			+ ")";

	private static final String CREATE_TABLE_TEMP_TRANSACTIONS = "CREATE TABLE "

			+ TABLE_TEMP_TRANSACTIONS
			+ " ("
			+ "id"
			+ " INTEGER PRIMARY KEY  AUTOINCREMENT , "
			+ COL_NAME
			+ " TEXT , "
			+ COL_UN_NUMBER
			+ " TEXT , "
			+ COL_DG_WEIGHT
			+ " REAL ,"
			+ COL_TOTAL_WEIGHT
			+ " REAL ,"
			+ "erap_number"
			+ " TEXT ,"
			+ COL_TRANSACTION_STATUS
			+ " INTEGER DEFAULT 0 , "
			+ COL_UNNUMBER_DISPLAY
			+ " INTEGER , "
			+ COL_PRIMARY_PLACARD
			+ " TEXT, "
			+ COL_SECONDARY_PLACARD
			+ " TEXT, "
			+ COL_WEIGHT_INDEX
			+ " REAL , "
			+ COL_UNTYPE
			+ " TEXT , "
			+ "user_id "
			+ " TEXT DEFAULT 0 , "
			+ COL_PKG_GROUP
			+ " TEXT , "
			+ "un_class_id "
			+ " INTEGER ,"
			+ "number_of_units"
			+ " INTEGER ,"
			+ COL_IBC_STATUS
			+ " INTEGER , "
			+ COL_GROUP_NAME
			+ " TEXT , "
			+ COL_TRANSACTION_ID
			+ " TEXT , "
			+ COL_USER_ID_WEB
			+ " TEXT , "
			+ COL_TRANSACTION_ID_WEB + " TEXT , "
			+" special_provision INTEGER , "
			+" un_style INTEGER , "
			+ COL_NONEXCEMPT + " INTEGER , "
            + "non_exempt_default INTEGER , "
			+ "nonoptimise_display_mandatory INTEGER , "
			+ "non_optimise TEXT , "
			+ "display_placard_mandatory TEXT , "
			+ "display_primary_placard TEXT , "
			+ "display_secondary_placard TEXT , "
            + COL_SWAP_FOR_DANGER+" INTEGER , "
            + "consignor_1000kg INTEGER "
			+ ")";

	public static final String CREATE_TABLE_CONFIG = "CREATE TABLE "
			+ TABLE_LISENCE_DETAILS + "(" + COL_IMEI + " TEXT , "
			+ COL_VALID_FROM + " TEXT , " + COL_VALID_TILL + " TEXT , "
			+ COL_LICENSE_KEY + " TEXT , " + COL_LICENSE_SESSION + " TEXT , "
			+ COL_URL + " TEXT , " + COL_SYNC_DATE_TIME + " TEXT , "
			+ COL_MAX_TRANSACTIONS + " INTEGER , " + COL_COMPANY_NAME + " TEXT " 
			+" , " + COL_USER_NAME + " TEXT "
			+")";

	
	public static final String CREATE_TABLE_ORDERS = "CREATE TABLE "
			+ TABLE_ORDERS + "(" + COL_ID
			+ " INTEGER PRIMARY KEY  AUTOINCREMENT , " + COL_NAME + " TEXT , "
			+ COL_BL + " TEXT , " + COL_UN_NUMBER + " TEXT , " + COL_DG_WEIGHT
			+ " REAL ," + COL_PACKAGE_WEIGHT + " REAL , " + COL_GROSS_WEIGHT
			+ " REAL ," + COL_WEIGHT_TYPE + " INTEGER , " + COL_WEIGHT_IN_KGS
			+ " REAL ," + COL_ERAP_NO + " TEXT ," + COL_DESCRIPTION + " TEXT ,"
			+ COL_SUBSIDARY_EXIST + " INTEGER , " + COL_UNNUMBER_DISPLAY
			+ " INTEGER , " + COL_DANGEROUS_PLACARD + " INTEGER , "
			+ COL_PRIMARY_PLACARD + " TEXT, " + COL_SECONDARY_PLACARD
			+ " TEXT, " + COL_WEIGHT_INDEX + " REAL , " + COL_UNTYPE
			+ " TEXT , " + COL_TRANSACTION_STATUS + " INTEGER DEFAULT 0 , "
			+ COL_USER_ID + " TEXT DEFAULT 0 , " + COL_ERAP_STATUS
			+ " INTEGER DEFAULT 0 , " + COL_PKG_GROUP + " TEXT , "
			+ COL_ERAP_INDEX + " TEXT ," + COL_UN_CLASS_ID + " INTEGER ,"
			+ COL_NO_OF_UNITS + " INTEGER ," + COL_INSERTED_DATE_TIME
			+ " TEXT , " + COL_MAX_PLACARD + " INTEGER , " + COL_IBC_STATUS
			+ " INTEGER , " + COL_IBC_RESIDUE_STATUS + " INTEGER , "
			+ COL_GROUP_NAME + " TEXT , " + COL_TRANSACTION_ID + " INTEGER , "
			+ COL_USER_ID_WEB + " TEXT , " + COL_TRANSACTION_ID_WEB + " TEXT , "
			+ COL_CONSIGNEE_DANGER+" INTEGER , "
			+ COL_SPECIAL_PROVISION+" INTEGER , "
			+ COL_UN_SYLE+" INTEGER , "
			+ COL_OPTIMISE + " INTEGER , "
			+ COL_TRANSACTION_VALUE + " TEXT , "
			+ COL_DANGEROUS_PLACARD_ORIGINAL + " TEXT , "
			+ COL_DRIVER_ID + " TEXT , "
			+ COL_NONEXCEMPT+ " TEXT , "
			+ COL_OVERRIDEN + " TEXT , "
			+ COL_NOS + " TEXT , "
			+ COL_SYNC_STATUS + " TEXT , "
			+ COL_PUSH_STATUS + " TEXT , "
			+ COL_check3Status + " TEXT  )";

	public static final String CREATE_TABLE_TERMS = "CREATE TABLE "
			+ TABLE_TERMS_CHECK + "(" + COL_VERIFY_LICENSE + " TEXT , " + COL_IMEI + " TEXT "+")";

	public static final String CREATE_TABLE_CLASS_COMPITABILITY	 = "CREATE TABLE "+TABLE_CLASS_COMITABILITY+"("
			+COL_CLASS1 +" TEXT , "+COL_1_1+" TEXT , "+COL_1_2+" TEXT , "+COL_1_3+"  TEXT,  "+COL_1_4+" TEXT , "+
			COL_1_5+" TEXT , "+COL_1_6+" TEXT  )";

	public static final String CREATE_TABLE_GROUP_COMPITABILITY = "CREATE TABLE "+DBConstants.TABLE_GROUP_COMPITABILITY+"("
			+COL_GROUP +" TEXT , "+COL_A+" TEXT , "+COL_B+" TEXT , "+COL_C+"  TEXT , "+COL_D+" TEXT , "+
			COL_E+" TEXT , "+COL_F+" TEXT , "+COL_G+" TEXT , "+COL_H+" TEXT , "+COL_J+" TEXT , "+COL_K+" TEXT , "
			+COL_L+" TEXT , "+COL_N+" TEXT , "+COL_S+" TEXT  "+")";


	private static final String CREATE_TABLE_UTILITIES = "CREATE TABLE "+TABLE_UTILITIES+
			"("
			+ COL_TERMS_CHECK_STATUS+" INTEGER , "
			+ COL_TERMS_DATE_TIME+" TEXT , "
			+ COL_IMEI+" TEXT DEFAULT '"+deviceId+"' , "
			+ COL_LANGUAGE_LOCALE+" TEXT , "
			+ COL_PLACARDING_TYPE+ " INTEGER DEFAULT 1 , "
			+ COL_VERIFY_LICENSE_STATUS + " INTEGER DEFAULT 0 "+")";

	private static final String CREATE_TABLE_PLACARDING_TYPE = "CREATE TABLE "+TABLE_PLACARDING_TYPE+
			"("
			+ COL_ID+" INTEGER PRIMARY KEY  AUTOINCREMENT , "
			+ COL_NON_OPTIMISE+" TEXT , "
			+ COL_SEMI_OPTIMISE+" TEXT , "
			+ COL_INSERTED_DATE_TIME+" TEXT ,"
			+ COL_TRANS_ID+" TEXT , "
			+ COL_ACTION+" TEXT , "
			+COL_PUSH_STATUS+" TEXT , "
			+COL_OPTIMISE+" TEXT "+")";

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_UNNUMBERS);
		db.execSQL(CREATE_TABLE_UNCLASSES);
		db.execSQL(CREATE_TABLE_WEIGHT);
		db.execSQL(CREATE_TABLE_LANGUAGE);
		db.execSQL(CREATE_TABLE_LANGUAGE_CONTENT);
		db.execSQL(CREATE_TABLE_DATABASEUPDATEDTIME);
		db.execSQL(CREATE_TABLE_LICENCECHECKDATEANDTIME);
		db.execSQL(CREATE_TABLE_ERAP);
		db.execSQL(CREATE_TABLE_SP84_INFO);
		db.execSQL(CREATE_TABLE_EXCLUDED_UNNUMBERS);
		db.execSQL(CREATE_TABLE_TRANSACTIONS);
		db.execSQL(CREATE_TABLE_TEMP_TRANSACTIONS);
		db.execSQL(CREATE_TABLE_CONFIG);
		db.execSQL(CREATE_TABLE_ORDERS);
		db.execSQL(CREATE_TABLE_TERMS);
		db.execSQL(CREATE_TABLE_CLASS_COMPITABILITY);
		db.execSQL(CREATE_TABLE_GROUP_COMPITABILITY);
		db.execSQL(CREATE_TABLE_UTILITIES);
		db.execSQL(CREATE_TABLE_PLACARDING_TYPE);

		// Alter Tables for adding columns
		// db.execSQL("ALTER TABLE "+TABLE_UN_CLASSES+" ADD COLUMN "+COL_DANGEROUS_PLACARD+" TEXT");
		// db.execSQL("ALTER TABLE "+TABLE_UN_NUMBERS+" ADD COLUMN "+COL_UNTYPE+" TEXT");
		// db.execSQL("ALTER TABLE "+TABLE_UN_NUMBERS+" ADD COLUMN "+COL_UNNO_DESCRIPTION+" TEXT");
		// db.execSQL("ALTER TABLE "+TABLE_UN_NUMBERS+" ADD COLUMN "+COL_UNNO_DISPLAY_STATUS+" TEXT");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_UNNUMBERS);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_UNCLASSES);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_WEIGHT);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TEMP_TRANSACTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LANGUAGE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LANGUAGE_CONTENT);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_DATABASEUPDATEDTIME);

		db.execSQL("DROP TABLE IF EXISTS "
				+ CREATE_TABLE_LICENCECHECKDATEANDTIME);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ERAP);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SP84_INFO);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_EXCLUDED_UNNUMBERS);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TRANSACTIONS);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CONFIG);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ORDERS);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TERMS);
		db.execSQL("DROP TABLE IF EXISTS " +CREATE_TABLE_CLASS_COMPITABILITY);
		db.execSQL("DROP TABLE IF EXISTS " +CREATE_TABLE_GROUP_COMPITABILITY);

		onCreate(db);
	}








	public static synchronized DatabaseHelper getDatabaseInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(ctx);
		}
		return mInstance;
	}
	@Override
	public Object getInstance() {
		return null;
	}
}