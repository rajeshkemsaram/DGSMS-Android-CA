package org.com.ca.dgsms.ca.model;

import android.os.Environment;

import com.ideabytes.dgsms.landstar.R;

/******************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site: http://ideabytes.com
 *Name : DBConstants
 *Description : To store all constant variables
 * Modified Date : 27-10-2015
 *****************************************************/
public interface DBConstants {

//	String DATABASE_NAME = "dgsmsca";//OLD
//	int ALERT_DIALOG_THEME = android.support.design.R.style.Base_Theme_AppCompat_Light_Dialog_FixedSize;


//	String DATABASE_NAME = "dgmobica-landstar";
	 String DATABASE_NAME = "dgmobica-generic";
	int DATABASE_VERSION = 1;
//	String APP_NAME = "DGMOBI_CA_LANDSTAR";
	String APP_NAME = "DGMOBI_CA_GENERIC";
	String WELCOME_PAGE_URL = "http://www.ideabytes.com/dgforms/form.php";
	int ALERT_DIALOG_THEME = R.style.Base_Theme_AppCompat_Light_Dialog_FixedSize;
	/************************DEV***********************************/
	//public ip from out side network is 183.82.9.57
	String COUNTRY = "ca";
	//dev
	String POINTING_TO = "dev";
	String SERVER = "http://192.168.1.49:80";
	String SERVER_ERG ="http://192.168.1.49:80";
	String WEB_SERVER_URL = "http://192.168.1.49/DG_MOBI_REGISTRATION";
		String PROJECTNAME = "DGSMS_CA_WS_CLIENT";

//	DGSMS_CA_WS_CLIENT_TRIAL //present
//	DGSMS_CA_WS_CLIENT// previous


	//test


//	String POINTING_TO = "test";
//		String SERVER = "http://104.238.67.134:80";//trial
//	 String SERVER_ERG = "http://104.238.67.134:80";
//String WEB_SERVER_URL = "http://104.238.67.134/DG_MOBI_REGISTRATION";
//	String PROJECTNAME = "DGSMS_CA_WS_CLIENT";

	//production


//	String POINTING_TO = "production";
//		String SERVER = "http://104.238.87.154:80";//production
//	String SERVER_ERG = "http://104.238.87.154:80";
//String WEB_SERVER_URL = "http://104.238.79.21/DG_MOBI_REGISTRATION_US";
//	String PROJECTNAME = "DGSMS_CA_WS_CLIENT_TRIAL";


	String SERVER_URL_MAIL = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/sendmailToRegisterUser.json";
	String SERVER_URL_PUSH_TRANSACTIONS = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/transactiondatafrommobile";
	String SERVER_URL_VERIFY_LICENSE = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/verifylicense.json";
    //only for dev and test
	String SERVER_URL_PICKUP_ORDERS= SERVER+"/"+PROJECTNAME+"/api/web/transactionOnDeviceId.json";
	String SERVER_URL_ERG = SERVER_ERG+"/"+PROJECTNAME+"/api/web/erg/info/";
	String SERVER_URL_GUIDE = SERVER_ERG+"/"+PROJECTNAME+"/api/web/erg/guide/";
    String SERVER_URL_ISOLATION = SERVER_ERG +"/"+PROJECTNAME+"/api/web/erg/isolation.json";
    String SERVER_URL_WATER_REACTION = SERVER_ERG +"/"+PROJECTNAME+"/api/web/erg/waterReactive.json";
	String REGISTRATION_URL= SERVER+"/DG_MOBI_REGISTRATION/";//
//	String REGISTRATION_URL="http://104.238.79.21:80/DG_MOBI_REGISTRATION_US/";
    //only for producion
//    String SERVER_URL_PICKUP_ORDERS= SERVER+"/DGSMS_CA_WS_CLIENT_TRIAL/api/web/transactionOnDeviceId.json";
//	String SERVER_URL_ERG = SERVER_ERG+"/DGSMS_CA_WS_CLIENT_TRIAL/api/web/erg/info/";
//	String SERVER_URL_GUIDE = SERVER_ERG+"/DGSMS_CA_WS_CLIENT_TRIAL/api/web/erg/guide/";
//    String SERVER_URL_ISOLATION = SERVER_ERG +"/DGSMS_CA_WS_CLIENT_TRIAL/api/web/erg/isolation.json";
//    String SERVER_URL_WATER_REACTION = SERVER_ERG +"/DGSMS_CA_WS_CLIENT_TRIAL/api/web/erg/waterReactive.json";




	String SERVER_URL_SEND_PAY_STATUS = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/registration/InsertDeviceIdAndPaymentStatus.json";
	String SERVER_URL_UPGRADE_LICENSE = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/license/updatelicenseinformation.json";
	String HOME = WEB_SERVER_URL+"/home.jsp";
	String SERVER_URL_CHECK_DEVICEID = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/payment/paymentstatus.json";
	String VERIFICATION  = WEB_SERVER_URL+"/verification.jsp";
	String SERVER_URL_GET_TERMS = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/termsandconditioninformation.json";
	String SERVER_URL_VERIFY_COUPONS = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/coupon/couponcodeverification.json";



	String SERVER_URL_GET_DB_UPDATES = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/rules/rulesdatasyncup.json";

	//lostmobile services
	String SERVER_URL_CHECK_USERNAME = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/usernames.json";
	String SERVER_URL_LOST_MOBILE_RESPONSE = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/updatemobilelostdeviceidtonewdeviceid.json";
	String SERVER_URL_GET_LOST_DEVICES = SERVER+"/DGSMS_CA_WS_SERVER/api/mobile/mobilelostdeviceidinformations.json";

//	String TABLE_LICENSE_DETAILS = "license_details";



	long DELAY = 30000;
	long PERIOD = 60000;

	// Table name
	String TABLE_EXCLUDED_UNNUMBERS = "excluded_unnumbers";
	String TABLE_UNNUMBER_INFO = "unnumber_info";
	String TABLE_UNCLASS = "un_clases";
	String TABLE_WEIGHT = "weight";
	String TABLE_ERAP_INFO = "erap_info";
	String TABLE_SP84_INFO = "sp84_info";
	String TABLE_TRANSACTIONS = "transaction_details";
	String TABLE_TEMP_TRANSACTIONS = "temptransaction";
	String TABLE_ORDERS = "orders";
	String TABLE_TERMS_CHECK = "terms";
	String COL_TERMS = "terms";

	String TABLE_LISENCE_DETAILS = "license_details";

	String TABLE_LANGUAGE = "language";
	String TABLE_LANGUAGE_CONTENT = "language_content";
	String TABLE_UPDATED_AT = "last_updated_at";
	String TABLE_LICENCECHECK_UPDATED_DATE = "licencecheck_updated_date";

	String TABLE_LICENSE_CHECK = "license_check_temp";
	String COL_CHECK = "license_check";
	String COL_DATA_SYNC = "data_sync_status";

	String TABLE_LICENSE_TEMP_DETAILS = "license_temp_details";
	//String COL_VALID_TILL = "valid_till" ;
	//String COL_VALID_FROM = "valid_from" ;
	String COL_NW_TIME = "nw_time" ;
	//String COL_LICENSE_KEY = "license_key" ;
	//String COL_IMEI = "imei";

	// Constants For Swagger
	String UN_NUMBER = "UnNumber";
	String LOCAL = "local";

	/******************** Column names of table unnumber_info*********************/
	String COL_ID = "id";
	//String COL_UN_NUMBER = "un_number";
	String COL_DESCRIPTION = "description";
	String COL_UN_CLASS_ID = "un_class_id";
	String COL_UNNUMBER_DISPLAY_STATUS = "unnumber_display_status";
	String COL_UNTYPE = "untype";
	String COL_SHIPPING_NAME = "shipping_name";
	String COL_GROUP_NAME = "group_name";
	// String COL_STATUS = "status";
	//String COL_UPDATED_DATETIME = "updated_datetime";

	/******************* Column names of table un_clases********************/
	// String COL_ID = "id";
	String COL_NAME = "name";
	String COL_PARENT_CLASS = "parent_class";
	String COL_ROAD_WEIGHT_GROUP = "road_weight_group";
	String COL_SHIP_WEIGHT_GROUP = "ship_weight_group";
	String COL_RAIL_WEIGHT_GROUP = "rail_weight_group";
	String COL_PRIMARY_PLACARD = "primary_placard";
	String COL_SECONDARY_PLACARD = "secondary_placard";
	// String COL_UNNUMBER_DISPLAY_STATUS = "unnumber_display_status";
	// String COL_STATUS = "status";
	String COL_DANGEROUS_PLACARD = "dangerous_placard";
	String COL_UPDATED_DATETIME = "updated_datetime";
	//COL_SPECIAL_PROVISION
	//COL_NONEXCEMPT

	/************ Column names of table weight********************/
	// String COL_ID = "id";
	// String COL_NAME = "name";
	// String COL_STATUS = "status";
	// String COL_UPDATED_DATETIME = "updated_datetime";

	/************** Column names of table excluded_unnumbers****************/
	String COL_UNNUMBER_EXCLUDED = "un_number";

	/*************** Column names of table transaction_details_lookup****************/
	String COL_TD_ID = "td_id";
	String COL_BL = "bl";
	//String COL_UN_NUMBER = "un_number";
	// String COL_UN_CLASS_ID = "unclass_id";
	String COL_DG_WEIGHT = "dg_weight";
	String COL_NO_OF_UNITS = "number_of_units";
	String COL_GROSS_WEIGHT = "gross_weight";
	String COL_SHIPPING_TYPE = "shipping_type";
	String COL_TRUCK_NUMBER = "truck_number";
	String COL_WEIGHT_TYPE = "weight_type";
	String COL_WEIGHT_KGS = "weight_in_kgs";
	String COL_STATUS = "status";
	String COL_USER_ID = "user_id";

	/***************** Column names of table erap_info ****************/
	// String COL_ID = "id";
	String COL_UNNUMBERDESC_ID = "unnumberdesc_id";
	String COL_PKG_GROUP = "pkg_group";
	String COL_ERAP_INDEX = "erap_index";
	String COL_ERAP_STATUS = "erap_status";
	// String COL_UPDATED_DATETIME = "updated_datetime";

	/*********** Column names of table sp84_info *************/
	// String COL_ID = "id";
	String COL_VALUE = "value";
	// String COL_STATUS = "status";
	// String COL_UPDATED_DATETIME = "updated_datetime";

	/************ Column names of table transaction_details **************/
	// String COL_ID = "id";
	// String COL_BL = "bl";
	String COL_NOS_DETAILS="nos";
	String COL_UN_NUMBER = "un_number";
	// String COL_DG_WEIGHT = "dg_weight";
	String COL_PACKAGE_WEIGHT = "package_weight";
	// String COL_GROSS_WEIGHT = "gross_weight";
	// String COL_WEIGHT_TYPE = "weight_type";
	String COL_WEIGHT_IN_KGS = "weight_in_kgs";
	String COL_ERAP_NO = "erap_no";
	String COL_SUBSIDARY_EXIST = "subsidary_exist";
	String COL_UNNUMBER_DISPLAY = "unnumber_display";
	// String COL_DANGEROUS_PLACARD = "dangerous_placard";
	String COL_DANGEROUS_PLACARD_ORIGINAL = "dangerous_placard_original";
	// String COL_PRIMARY_PLACARD = "primary_placard";
	// String COL_SECONDARY_PLACARD = "secondary_placard";
	String COL_WEIGHT_INDEX = "weight_index";
	// String COL_UNTYPE = "untype";
	String COL_TRANSACTION_STATUS = "transaction_status";
	// String COL_USER_ID = "user_id";
	// String COL_ERAP_STATUS = "erap_status";
	String COL_DRIVER_ID = "driver_id";
	String COL_SYNC_STATUS = "sync_status";
	// String COL_PKG_GROUP = "pkg_group";
	// String COL_ERAP_INDEX = "erap_index";

	/*****table temp transaction details column names***/
// String COL_ID = "id";
	//String COL_UN_NUMBER = "un_number";
	// String COL_DG_WEIGHT = "dg_weight";
	 String COL_TOTAL_WEIGHT = "total_weight";
	// String COL_WEIGHT_TYPE = "weight_type";
	//String COL_ERAP_NO = "erap_no";
	//String COL_SUBSIDARY_EXIST = "subsidary_exist";
	//String COL_UNNUMBER_DISPLAY = "unnumber_display";
	// String COL_DANGEROUS_PLACARD = "dangerous_placard";
	//String COL_DANGEROUS_PLACARD_ORIGINAL = "dangerous_placard_original";
	// String COL_PRIMARY_PLACARD = "primary_placard";
	// String COL_SECONDARY_PLACARD = "secondary_placard";
	//String COL_WEIGHT_INDEX = "weight_index";
	// String COL_UNTYPE = "untype";
	//String COL_TRANSACTION_STATUS = "transaction_status";
	// String COL_USER_ID = "user_id";
	// String COL_ERAP_STATUS = "erap_status";
	//String COL_SYNC_STATUS = "sync_status";
	// String COL_PKG_GROUP = "pkg_group";
	// String COL_ERAP_INDEX = "erap_index";
	//String COL_CONSIGNEE_DANGER = "consignee_danger"; 1000kg_consigner from web service

	/************ TABLE_LANGUAGE *************/
	String COL_SELECTED_STATE = "selected_state";
	String COL_LANGUAGE_NAME = "language_name";
	// COMMON COLUMNS FOR TABLE_LANGUAGE AND TABLE_LANGUAGE_CONTENT
	String COL_LANGUAGE_ID = "language_id";
	/************* TABLE_LANGUAGE_CONTENT **************/
	String COL_LANGUAGE_CONTENT_ID = "language_content_id";
	String COL_MSG_TEXT = "msg_text";
	// last_updated_at COLUMNS
	String COL_TIME_ID = "id";
	String COL_UPDATED_AT = "updated_at";
	/************ TABLE LICENCECHECK COLUMN NAMES *****************/
	String COL_LICENCE_TIME_ID = "id";
	String COL_LICENCE_UPDATED_AT = "licence_updated_at";


	/*********** Column names of table transaction_details *************/
	// String COL_ID = "id";
	// String COL_BL = "bl";
	// String COL_UN_NUMBER = "un_number";
	// String COL_UN_CLASS_ID = "unclass_id";
	// String COL_DG_WEIGHT = "dg_weight";
	// String COL_PACKAGE_WEIGHT = "package_weight";
	// String COL_GROSS_WEIGHT = "gross_weight";
	// String COL_NUMBER_OF_UNITS = "number_of_units";
	// String COL_WEIGHT_TYPE = "weight_type";
	// String COL_WEIGHT_IN_KGS = "weight_in_kgs";
	// String COL_ERAP_NO = "erap_no";
	// String COL_SUBSIDARY_EXIST = "subsidary_exist";
	// String COL_UNNUMBER_DISPLAY = "unnumber_display";
	// String COL_DANGEROUS_PLACARD = "dangerous_placard";
	// String COL_DANGEROUS_PLACARD_ORIGINAL = "dangerous_placard_original";
	// String COL_primary_placard = "primary_placard";
	// String COL_SECONDARY_PLACARD = "secondary_placard";
	// String COL_WEIGHT_INDEX = "weight_index";
	// String COL_UNTYPE = "untype";
	// String COL_TRANSACTION_STATUS = "transaction_status";
	// String COL_USER_ID = "user_id";
	// String COL_ERAP_STATUS = "erap_status";
	// String COL_DRIVER_ID = "driver_id";
	// String COL_SYNC_STATUS = "sync_status";
	//String COL_GROUP_NAME = "group_name";
	String COL_TRANSACTION_ID = "transaction_id";
	String COL_INSERTED_DATE_TIME = "inserted_date_time";
	String COL_MAX_PLACARD = "max_placard";
	String COL_IBC_STATUS = "ibc_status";
	String COL_IBC_RESIDUE_STATUS = "ibc_residue_status";
	// String COL_PKG_GROUP = "pkg_group";
	// String COL_ERAP_INDEX = "erap_index";
    String COL_NONEXCEMPT = "non_exempt";
	String COL_SWAP_FOR_DANGER = "swap_for_danger";
    String COL_OPTIMISE = "optimise";
    String COL_PUSH_STATUS = "push_status";
	String COL_CONSIGNEE_DANGER = "consignee_danger"; //1000kg_consigner from web service
	String COL_SPECIAL_PROVISION = "special_provision";
    String COL_UN_SYLE= "un_style";

	// Status codes
	long CODE_REPRESENTATION_CONVERTER_NOT_FOUND = 400;
	String DEBUG = "debug";

	// String DATE = "date";
	//
	// String UN_NUMBER_DGLOGIC = "un_number";
	// String AMOUNT = "amount";
	// String REASON= "reason";
	// String TYPE= "type";
	//
	//
	// String CONTENT="content";
	// String LANGUAGE="language";
	// String LANGUAGE_CONTENT="language_content";
	//
	// String UNNUMBER="un_numbers";
	// String SPECIALCASE="unnumber_special_cases";
	// String LICENSE="customer_vs_mobile";
	//
	// String SHIPPING_COMPANY="company";
	//
	// String PICKUP_POINT="pickup_point_list";
	// String PERSONS="authorised_persons";
	// String TRANSACTIONS="transaction_details";
	// String DRIVER="driver_info";
	// String DELIVERY_ADDRESS="delivery_address";
	// String TRUCKS="truck_info";
	// String WEIGHT_TYPE="weight_type1";
	// String NO_OF_UNITS ="no_of_units";
	// String UNNUMBER_INSERT="unumber";
	// String BL="bl";
	//
	// String DG_WEIGHT="dg_weight";
	// String UNCLASS_ID="unclass_id";
	// String LAST_SYNC_DATE="lastsyncdate";
	// String UNNUMBER_LOOKUP ="UnNumberLookup";
	// String UNNUMBERFORDESC ="UnNumber";
	// String DANGEROUS_GOODS ="dangerous_goods";
	// String ERAP ="erap";
	//
	// String ERAP_NO ="erap_no";
	// String IMEI ="imeinumber";
	// String TRUCKDRIVER = "truck_vs_driver";
	// String DB_NAME = "cmadmin_dgsms_web";
	//
	// String USER_ID = "User_id";
	// String PRIMARY_PLACARD ="primary_placard";
	// String SECONDARY_PLACARD ="secondary_placard";
	// String DANGEROUS_PLACARD ="dangerous_placard";
	// String PLACARD_REQUIRED ="placard required";
	// String PLACARD ="placard";
	String DANGER = "classdanger";
	String PP = "pp";
	String PKG_GROUP = "pkg_group";
	String E_T = "e.t";
	String SP = "sp";
	String CW = "cw";
	String ERROR = "error";

	String IF_CONDITION = "CASE WHEN";
	String AND_CONDITION = "and";

	//folder path constants
	String FOLDER_PATH_UPDATED_APK =  Environment.getExternalStorageDirectory() + "/DGMobi-CA/Apk/";
	String FOLDER_PATH_DEBUG =  Environment.getExternalStorageDirectory() + "/DGMobi-CA/Debug/";
	String TEXT_FILE_NAME = "Debug.txt";

	//Scan constants
	String SCAN_MODES = "SCAN_MODES";
	String SCAN_RESULT = "SCAN_RESULT";
	String SCAN_RESULT_TYPE = "SCAN_RESULT_TYPE";
	String ERROR_INFO = "ERROR_INFO";

	/************** table columns TABLE_CONFIG_DETAILS ***************/
	String COL_IMEI = "imei";
	String COL_VALID_TILL = "valid_till" ;
	String COL_VALID_FROM = "valid_from" ;
	String COL_VERIFIED_DATE = "varified_date" ;
	String COL_LICENSE_KEY = "license_key" ;
	String COL_LICENSE_SESSION = "license_session";
	String COL_SYNC_DATE_TIME = "lastsyncdate" ;
	String COL_URL = "url";
	String COL_MAX_TRANSACTIONS = "max_Transaction";
	String COL_COMPANY_NAME = "company_name";
	String COL_VERIFY_LICENSE = "verify_license";
	String COL_USER_NAME = "userName";
	String SAFETY_PLACARD="safetyPlacard";
	String TYPE="type";

	String TABLE_DISPLAY_GROUP = "display_group";
	//String COL_ID = "id";
	//String COL_GROUP_NAME = "group_name";
	String COL_DISPLAY_NAME = "display_name";

	int PICKUP_INTERVAL_WEB_SYNC = 120*1000; //five minutes
	int PICKUP_INTERVAL_LOCAL_SYNC = 60*1000;//30 seconds
	int SERVICE_START_INTERVAL = 20*1000; // 60 seconds
	int TIMER_START_INTERVAL = 60*1000;//3 days (3 days value 259200*1000)
	int TIMER_REPEAT_INTERVAL = 86400*1000;//one day, this service has to check in every 24 hours

	int SPLASH_SCREEN_SLEEP_INTERVAL = 5*1000;//5 seconds
	int TIME_INTERVAL_TO_PUSH_TRANS = 60*1000; //60 seconds


	String TABLE_PLACARDING_TYPE = "placarding_type";
	String COL_NON_OPTIMISE = "non_optimise";
	String COL_SEMI_OPTIMISE = "semi_optimise";
	String COL_TRANS_ID = "trans_id";
	String COL_ACTION = "action";
//	String COL_OPTIMISE = "optimise";

	/*********** order table columns as it is transaction_details ***************/
	String COL_TRANSACTION_ID_WEB = "transaction_id_web";
	String COL_USER_ID_WEB = "user_id_web";

	String TABLE_CLASS_COMITABILITY = "class1_compatibility";
	String COL_CLASS1 = "class1";
	String COL_1_1 = "C1_1";
	String COL_1_2 = "C1_2";
	String COL_1_3 = "C1_3";
	String COL_1_4 = "C1_4";
	String COL_1_5 = "C1_5";
	String COL_1_6 = "C1_6";
	String COL_1_7 = "C1_7";

	String TABLE_GROUP_COMPITABILITY = "class1_group_compatibility";
	String COL_GROUP = "group1";
	String COL_A = "A";
	String COL_B = "B";
	String COL_C = "C";
	String COL_D = "D";
	String COL_E = "E";
	String COL_F = "F";
	String COL_G = "G";
	String COL_H = "H";
	String COL_J = "J";
	String COL_K = "K";
	String COL_L = "L";
	String COL_N = "N";
	String COL_S = "S";

	String DEVICE_ID = "deviceId";
	//get license data response from web as json response
	String SERVER_LINK_LICENSE_DATA = "http://www.ideabytes.com/dgforms/getResponse.php";
	String SERVER_LINK_TO_BUY_LICENSE = "http://www.dgsms.ca/cart-loose/checkout/index.php";

	public static String DATA = "Data";
	public static String CLASS_ONE = "1";
	public static final int ZERO = 0;
	public static final int ONE = 1;
	public static final int SP_TWNTY_THREE = 23;
	public static final int DSPLY_PRMY_PLCRD = 0;
	public static final int DSPLY_SCNDRY_PLCRD = 0;
	public static final int DSPLY_PLCRD_MNDTRY = 0;
	public static final int OPTIMISE_ENABLE = 1;
	public static final int OPTIMISE_DISABLE = 0;
	public static final int MANDATORY_ERAP = 1;
	//server urls
	//dev http://192.168.1.49:80
	//uat http://104.238.67.134:80
	//trial http://104.238.78.199:80

	//CANADA
	//Server DGSMS_CA_WS_SERVER
	// Client DGSMS_CA_WS_CLIENT
	//US 
	//Server DGSMS_US_WS_SERVER
	// Client DGSMS_US_WS_CLIENT
	//production http://104.238.87.154:80
	//production server DGSMS_SERVER_CANADA
	//production day and ross client DGSMS_DAYANDROSS_CLIENT
	//production mid land client  DGSMS_MIDLAND_CLIENT

	//Edit text constants to use in constructing json object
	String ET_BL = "etBl";
	String ET_UNNUMBER = "etUn";
	String ET_DGWEIGHT = "etDgWeight";
	String ET_GROSS_WEIGHT= "etGrossWeight";
	String ET_NO_OF_UNITS = "etUnits";
	String ET_ERAP = "etErap";
	String RADIO_GROUP_WT_TYPE = "rgWeightType";
	//keys used in Fragments to display placards
	String KEY = "key";
	String RESULT = "result";
	String OPTIMIZED = "Optimized";
	String NON_OPTIMIZED = "Basic";
	String SEMI_OPTIMIZED = "Semi";
	String COUNT = "count";
	//Spill Isolation column names
	String small_Isolation="Small Isolation";
	String small_Daypad	="Small DayPAD";
	String small_Night_Pad="Small Night PAD";
	String large_lsolation="Large Isolation";
	String large_Day_Low_Pad="Large Day Low PAD";
	String large_Day_Moderate_Pad="Large Day Moderate PAD";
	String large_Day_High_Pad ="Large Day High PAD";
	String large_Night_Low_Pad="Large Night Low PAD";
	String large_night_Moderate_Pad="Large Night Moderate PAD";
	String large_Night_High_Pad="Large Night High PAD";
	//spill Isolation set6 changed column names
	String large_Day_PAD="Large Day PAD";
	String large_Night_Pad="Large Night PAD";


	String RESULTS = "results";

	String PAYMENT_STATUS = "payment_status";
	String REG_STATUS = "reg_status";

	String STATUS ="status";
	String PRODUCT_ID = "productId";
	String DISCOUNT_PERCENTAGE = "discountPercentage";

	//================TABLE UTILITIES=======================================//
	String TABLE_UTILITIES = "utilities";
	String COL_TERMS_CHECK_STATUS = "terms_check_status";
	String COL_TERMS_DATE_TIME = "terms_check_date_time";
	String COL_LANGUAGE_LOCALE = "language_locale";
	String COL_PLACARDING_TYPE = "placarding_type";
	String COL_VERIFY_LICENSE_STATUS = "verify_license_status";




	//===
	String COL_NUMBER_OF_UNITS = "number_of_units";
	String COL_TRANSACTION_VALUE = "transaction_value";
	String COL_OVERRIDEN = "overridden";
	String COL_CHECKSTATUS = "check3Status";
	String COL_NON_EXEMPT = "nonexempt";
	String COL_NOS = "nos";
	String COL_check3Status="check3Status";
	public Object getInstance();
}	
