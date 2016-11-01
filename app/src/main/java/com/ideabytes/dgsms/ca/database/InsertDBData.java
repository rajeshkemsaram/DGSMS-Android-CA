package com.ideabytes.dgsms.ca.database;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.josn.JSONParser;
import com.ideabytes.dgsms.ca.utils.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/*************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : InsertDBData
 * author:  Suman
 * Description : This class is to insert database table's data
 * Modified Date : 07-12-2015
 * Reason : Updated method insertIntoOrders(). to load placard based on new logic.
 ***************************************************************/
public class InsertDBData extends DatabaseDAO implements DBConstants {

	private Context context = null;
    private int dbCode = 0;
	/**
	 * @author Ideabytes
	 * @param context
	 */
	public InsertDBData(Context context) {
		super(context);
        this.context = context;
	}

	/**
	 * @author Ideabytes
	 * @param response

	 * @return
	 */
	public long insertIntoWeight(final String response) throws Exception {
		long tableCount = 0;
		try {
			getDatabaseConnection(dbCode);
			GetDBData getDBData = new GetDBData(context);
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
                //there is a problem from server db data, coming empty,25-08-2016
				if (!jsonObject.optString("name").isEmpty()) {
					values.put(COL_NAME, Double.parseDouble(jsonObject.optString("name")));
				} else {
                    values.put(COL_NAME, jsonObject.optString("name"));
                }
				values.put(COL_STATUS, Integer.parseInt(jsonObject.optString("status")
						.toString()));
				db.insert(TABLE_WEIGHT, null, values);
				values.clear();
			}
			tableCount = getDBData.getWeightTalbeCount();
			if (tableCount != jsonObjectCount) {
				throw new Exception("Weight table");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Weight table");
		} finally {
			closeDatabaseConnection();
		}
		return tableCount;
	}

	/**
	 * author Ideabytes
	 * @param response
	 * @return
	 */
	public long insertIntoUnClass(final String response) throws Exception {
		long tableCount = 0;
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
				values.put(COL_PRIMARY_PLACARD,  jsonObject.optString("primary_placard")
						.toString());
				values.put(COL_SECONDARY_PLACARD,  jsonObject.optString(
						"secondary_placard").toString());
				values.put(COL_UNNUMBER_DISPLAY_STATUS, jsonObject.optString(
						"unnumber_display_status").toString());
				values.put(COL_PARENT_CLASS, jsonObject.optString("parent_class")
						.toString());
				values.put(COL_DANGEROUS_PLACARD,  jsonObject.optString(
						"dangerous_placard").toString());
				values.put(COL_SPECIAL_PROVISION,  jsonObject.optString(
						"specialProvision").toString());
				values.put(COL_NONEXCEMPT,  jsonObject.optString(
						"nonexempt").toString());

				db.insert(TABLE_UNCLASS, null, values);
				values.clear();
			}
			tableCount = getDBData.getUnClassesTalbeCount();
			// Log.v(TAG,"unClassInfo table count "+tableCount);
			if (tableCount != jsonObjectCount) {
				throw new Exception("unClasses table");
			}
		} catch (Exception e) {
			Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
			throw new Exception("unClasses table");
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return tableCount;
	}

    /**
     * author Ideabytes
     * @param response
     * @return
     */
    // Modified by Suman/10-01-2015 added id
    public long insertIntoUNNumber(final String response) throws Exception {
        long tableCount = 0;
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

                db.insert(TABLE_UNNUMBER_INFO, null, values);
                values.clear();
            }
            tableCount = getDBData.getUnNumberTalbeCount();
            // Log.v(TAG,"unNumberInfo table count "+tableCount);
            if (tableCount != jsonObjectCount) {
                if (tableCount != jsonObjectCount) {
                    throw new Exception("unNumberInfo table");
                }
            }
            //db.close();
        } catch (Exception e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            throw new Exception("unNumberInfo table");
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return tableCount;

    }














	/**
	 * author Ideabytes
	* @return rows affected
	 */
	public long insertIntoUpdatedtime() {
		long rows = 0;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String datetime = sdf.format(c.getTime());
			ContentValues values = new ContentValues();
			values.put(COL_UPDATED_AT, datetime);
			rows = db.insert(TABLE_UPDATED_AT, null, values);
		} catch (Exception e) {
			Log.e(InsertDBData.this.getClass().getSimpleName(), "Error insertIntoUpdatedtime method " + e.toString());
			e.printStackTrace();
		} finally {
            closeDatabaseConnection();
        }
		return rows;
	}

	public long insertIntoLanguageTable(int selected_state,
			String language_name) {
		long rows = 0;
		try {
          //  System.out.println("insert language table");
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			ContentValues values = new ContentValues();
			values.put(COL_SELECTED_STATE, selected_state);
			values.put(COL_LANGUAGE_NAME, language_name);
			// insert row
			rows = db.insert(TABLE_LANGUAGE, null, values);
		} catch (Exception e) {
			Log.e(InsertDBData.this.getClass().getSimpleName(), "Error insertIntoLanguageTable method " + e.toString());
			e.printStackTrace();
		} finally {
            //closes database connection
            closeDatabaseConnection();
        }
		return rows;
	}
	/**
	 * author suman
	 * @param language_content_id id
	 * @param language_id language id
	 * @param msg_text text for localization
	 * @return language content rows count
	 */
	public long insertIntoLanguageContent(String language_content_id,
			int language_id, String msg_text) {
		long rows = 0;
		try {
           // System.out.println("insett language content");
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
			ContentValues values = new ContentValues();
			values.put(COL_LANGUAGE_CONTENT_ID, language_content_id);

			values.put(COL_LANGUAGE_ID, language_id);
			values.put(COL_MSG_TEXT, msg_text);

			// insert row
			rows = db.insert(TABLE_LANGUAGE_CONTENT, null, values);
		} catch (Exception e) {
			Log.e(InsertDBData.this.getClass().getSimpleName(), "Error insertIntoLanguageContent method " + e.toString());
			new Exceptions(context,InsertDBData.this
					.getClass().getName(), Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		} finally {
            //closes database connection
            closeDatabaseConnection();
        }
		return rows;
	}
	/**
	 * This method is used to insert license details into "license" talbe 
	 * 
	 * licenseData licesne data to store into database
	 * @return count of license table rows inserted
	 */ 
	public long insertIntoLicenseTable(final String result, String type) {
		long rows = 0;
		try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            JSONObject jsnobject1 = new JSONObject(result);
           // Log.v(LOCAL,"server response license data "+result);
            JSONObject jsnobject2 = jsnobject1.getJSONObject("results");
            String status = jsnobject2.getString("reponse");
            //System.out.println("status "+status);
            JSONArray jArray = jsnobject2.getJSONArray("LicenseData");
           // System.out.println("jArray "+jArray);
            for (int i = 0; i < jArray.length(); i++) {
                final JSONObject jsonObject = jArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_IMEI, new Utils().getDeviceId(context));
                values.put(COL_VALID_TILL, jsonObject
                        .optString(COL_VALID_TILL).toString().trim());
                values.put(COL_VALID_FROM, jsonObject
                        .optString(COL_VALID_FROM).toString().trim());
                values.put(COL_LICENSE_KEY, jsonObject
                        .optString(COL_LICENSE_KEY).toString().trim());
                values.put(COL_LICENSE_SESSION, jsonObject
                        .optString(COL_LICENSE_SESSION).toString()
                        .trim());
                values.put(COL_URL, jsonObject.optString("url").toString()
                        .trim());
                values.put(COL_SYNC_DATE_TIME, "empty");
                values.put(COL_MAX_TRANSACTIONS, jsonObject
                        .optString(COL_MAX_TRANSACTIONS).toString().trim());
                values.put(COL_COMPANY_NAME, jsonObject.optString(COL_COMPANY_NAME)
                        .toString());
                values.put(COL_USER_NAME, jsonObject.optString(COL_USER_NAME)
                        .toString());
				MyAppData.getInstance().setUserName(jsonObject.optString(COL_USER_NAME)
						.toString());
				Log.v("username123",jsonObject.optString(COL_USER_NAME)
						.toString());
                // insert row
//				values.put(SAFETY_PLACARD,jsonObject.optString(SAFETY_PLACARD).toString());
//				values.put(TYPE,jsonObject.optString(TYPE));
                rows = db.insert(TABLE_LISENCE_DETAILS, null, values);
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            //closes database connection
            closeDatabaseConnection();
        }
		return rows;
	}

    //caled after license is transfered
	public long insertIntoLicenseTable(final String result) {
		long rows = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			JSONObject jsnobject1 = new JSONObject(result);
			//Log.v(LOCAL,"server response license data "+result);
			JSONObject jsnobject2 = jsnobject1.getJSONObject("results");
			//System.out.println("status "+status);
			JSONArray jArray = jsnobject2.getJSONArray("LicenseData");
			// System.out.println("jArray "+jArray);
			for (int i = 0; i < jArray.length(); i++) {
				final JSONObject jsonObject = jArray.getJSONObject(i);
				ContentValues values = new ContentValues();
				values.put(COL_IMEI, new Utils().getDeviceId(context));
				values.put(COL_VALID_TILL, jsonObject
						.optString(COL_VALID_TILL).toString().trim());
				values.put(COL_VALID_FROM, jsonObject
						.optString(COL_VALID_FROM).toString().trim());
				values.put(COL_LICENSE_KEY, jsonObject
						.optString(COL_LICENSE_KEY).toString().trim());
				values.put(COL_LICENSE_SESSION, jsonObject
						.optString(COL_LICENSE_SESSION).toString()
						.trim());
				values.put(COL_URL, jsonObject.optString("url").toString()
						.trim());
				values.put(COL_SYNC_DATE_TIME, "empty");
				values.put(COL_MAX_TRANSACTIONS, jsonObject
						.optString(COL_MAX_TRANSACTIONS).toString().trim());
				values.put(COL_COMPANY_NAME, jsonObject.optString(COL_COMPANY_NAME)
						.toString());
				values.put(COL_USER_NAME, jsonObject.optString(COL_USER_NAME)
						.toString());
				MyAppData.getInstance().setUserName(jsonObject.optString(COL_USER_NAME)
						.toString());
				Log.v("username123",jsonObject.optString(COL_USER_NAME)
						.toString());
				// insert row
//				values.put(SAFETY_PLACARD,jsonObject.optString(SAFETY_PLACARD).toString());
//				values.put(TYPE,jsonObject.optString(TYPE));
				rows = db.insert(TABLE_LISENCE_DETAILS, null, values);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;
	}

	/**
	 * Save terms and conditions acceptance status and date time, this is required even
	 * need to send to service
	 * @param status
	 * @param datetime
	 */
	public void saveTermsCheck(final int status, final String datetime) {
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			ContentValues values = new ContentValues();
			values.put(COL_TERMS_CHECK_STATUS,status);
			values.put(COL_TERMS_DATE_TIME,datetime);
			db.insert(TABLE_UTILITIES,null,values);
			values.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
	}

	// this method is not used , use this only for testing purpose
	/**
	 * author Ideabytes
	 */
	public void insertTestData() {


		try {
           // System.out.println("i am in insert test");
            insertIntoLanguageTable(0, "en");
			insertIntoLanguageTable(0, "es");
			insertIntoLanguageTable(0, "fr");
			//app name
			insertIntoLanguageContent("dgsme", 1, "DGSMS");
			insertIntoLanguageContent("dgsme", 2, "DGSMS");
			insertIntoLanguageContent("dgsme", 3, "DGSMS");
			//button name
			insertIntoLanguageContent("Dialog_Btn_Delete_No", 1, "No");
			insertIntoLanguageContent("Dialog_Btn_Delete_No", 2, "Si");
			insertIntoLanguageContent("Dialog_Btn_Delete_No", 3, "Non");
			//button name
			insertIntoLanguageContent("Dialog_Btn_Delete_Yes", 1, "Yes");
			insertIntoLanguageContent("Dialog_Btn_Delete_Yes", 2, "No");
			insertIntoLanguageContent("Dialog_Btn_Delete_Yes", 3, "Oui");
			//button name
			insertIntoLanguageContent("Dialog_Btn_Ok", 1, "OK");
			insertIntoLanguageContent("Dialog_Btn_Ok", 2, "OK");
			insertIntoLanguageContent("Dialog_Btn_Ok", 3, "OK");

            //button name
            insertIntoLanguageContent("Dialog_Btn_Save", 1, "Save");
            insertIntoLanguageContent("Dialog_Btn_Save", 2, "Save");
            insertIntoLanguageContent("Dialog_Btn_Save", 3, "Save");

			insertIntoLanguageContent("Dialog_Btn_Exit", 1, "Exit");
			insertIntoLanguageContent("Dialog_Btn_Exit", 2, "OK");
			insertIntoLanguageContent("Dialog_Btn_Exit", 3, "Dáccord");
			//Dialog message
			insertIntoLanguageContent("Dialog_Alert_Title", 1, "Alert Message");
			insertIntoLanguageContent("Dialog_Alert_Title", 2,
					"Mensaje de aviso");
			insertIntoLanguageContent("Dialog_Alert_Title", 3,
					"Message d'alerte");

			//Dialog message
			insertIntoLanguageContent("Dialog_InvalidUn_Message", 1,
					"Invalid UN Number");
			insertIntoLanguageContent("Dialog_InvalidUn_Message", 2,
					"Número  ONU");
			insertIntoLanguageContent("Dialog_InvalidUn_Message", 3,
					"Numéro UN non valide");
			//Dialog message
			insertIntoLanguageContent("Dialog_Forbidden_Message", 1,
					"Forbidden UN Number");
			insertIntoLanguageContent("Dialog_Forbidden_Message", 2,
					"DGSMS Número de ONU restringido");
			insertIntoLanguageContent("Dialog_Forbidden_Message", 3,
					"Numéro UN interdit");
			//Dialog message
			insertIntoLanguageContent("Dialog_Load_Item_Title", 1, "Load Item");
			insertIntoLanguageContent("Dialog_Load_Item_Title", 2,
					"Articulos de carga");
			insertIntoLanguageContent("Dialog_Load_Item_Title", 3,
					"Articles du chargement");
			//Dialog message
			insertIntoLanguageContent("Dialog_Add_Item_Title", 1, "Add Item");
			insertIntoLanguageContent("Dialog_Add_Item_Title", 2,
					"Añadir artículo");
			insertIntoLanguageContent("Dialog_Add_Item_Title", 3,
					"Ajouter un item");
			//Edit text Hint
			insertIntoLanguageContent("Dialog_Add_Item_Et_BL", 1,
					"Bill of Lading");
			insertIntoLanguageContent("Dialog_Add_Item_Et_BL", 2,
					"Conocimiento de embarque");
			insertIntoLanguageContent("Dialog_Add_Item_Et_BL", 3,
					"Connaissement");
			//button name
			insertIntoLanguageContent("Dialog_Add_Item_Btn_Scan", 1, "Scan");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_Scan", 2,
					"Scan");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_Scan", 3,
					"Scan");
			//Edit text Hint
			insertIntoLanguageContent("Dialog_Add_Item_Et_GRWeight", 1,
					"Enter Weight");
			insertIntoLanguageContent("Dialog_Add_Item_Et_GRWeight", 2,
					"Ingresar peso");
			insertIntoLanguageContent("Dialog_Add_Item_Et_GRWeight", 3,
					"Entrez le poids");
			
			insertIntoLanguageContent("Dialog_Add_Item_Et_Unumber", 1,
					"UN Number");
			insertIntoLanguageContent("Dialog_Add_Item_Et_Unumber", 2,
					"Número OUN");
			insertIntoLanguageContent("Dialog_Add_Item_Et_Unumber", 3,
					"Numéro UN");
			
			//Edit text Hint
			insertIntoLanguageContent("Dialog_Add_Item_Et_Units", 1,
					"Enter No Of Units");
			insertIntoLanguageContent("Dialog_Add_Item_Et_Units", 2,
					"Ingresar No de unidades");
			insertIntoLanguageContent("Dialog_Add_Item_Et_Units", 3,
					"Entrez No des unités");
			//Edit text Hint
			insertIntoLanguageContent("Dialog_Add_Item_Et_Erap", 1,
					"Enter ERAP");
			insertIntoLanguageContent("Dialog_Add_Item_Et_Erap", 2,
					"n/a");
			insertIntoLanguageContent("Dialog_Add_Item_Et_Erap", 3,
					"Entrez PIU");
			//Button name
			insertIntoLanguageContent("Dialog_Add_Item_Btn_Display", 1,
					"Display Placard");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_Display", 2,
					"Mostrar placas");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_Display", 3,
					"Montrer les plaques");
			//Button name
			insertIntoLanguageContent("Dialog_Add_Item_Btn_RbPounds", 1, "Lbs");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_RbPounds", 2,
					"Lb");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_RbPounds", 3,
					"Lb");
			//Button name
			insertIntoLanguageContent("Dialog_Add_Item_Btn_RbKgs", 1, "Kgs");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_RbKgs", 2, "KG");
			insertIntoLanguageContent("Dialog_Add_Item_Btn_RbKgs", 3, "KG");
			//Dialog message
			insertIntoLanguageContent("Dialog_Add_Item_Tv_IBC", 1, "Is package type an IBC?");
			insertIntoLanguageContent("Dialog_Add_Item_Tv_IBC", 2, "El envase es un Recipiente Intermedio para Graneles (RIG)?");
			insertIntoLanguageContent("Dialog_Add_Item_Tv_IBC", 3, "Est-ce que le contenant un Grand récipient pour vrac (GRV)?");
			//Dialog message
			insertIntoLanguageContent("Dialog_Add_Item_Tv_Residue", 1, "Is there residue in IBC ?");
			insertIntoLanguageContent("Dialog_Add_Item_Tv_Residue", 2, "Hay residuo en el RIG ?");
			insertIntoLanguageContent("Dialog_Add_Item_Tv_Residue", 3, "Est-ce qu'il y a des résidus dans le GRV?");

			//Error messages
			insertIntoLanguageContent("Please_Enter_Gross_Weight_error", 1,
					"Enter DG Gross Mass");
			insertIntoLanguageContent("Please_Enter_Gross_Weight_error", 2,
					"Ingresar el peso bruto");
			insertIntoLanguageContent("Please_Enter_Gross_Weight_error", 3,
					"Entrez la masse brute du MD");
			//Error messages
			insertIntoLanguageContent("UN_Number_4Ditgits_error", 1,
					"UN Number must be 4 digits ");
			insertIntoLanguageContent("UN_Number_4Ditgits_error", 2,
					"Número  ONU debe tener 4 cifras");
			insertIntoLanguageContent("UN_Number_4Ditgits_error", 3,
					"Numéro UN doivent avoir 4 chiffres");
			//Error messages
			insertIntoLanguageContent("Units_Zero_error", 1,
					"Units cannot be 0");
			insertIntoLanguageContent("Units_Zero_error", 2,
					"Unidades no pueded ser O");
			insertIntoLanguageContent("Units_Zero_error", 3,
					"Unités ne peut pas être 0");
			//Error messages
			insertIntoLanguageContent("Enter Valid UN Number", 1,
					"Enter Valid UN Number");
			insertIntoLanguageContent("Enter Valid UN Number", 2,
					"Ingresar el número  ONU validp");
			insertIntoLanguageContent("Enter Valid UN Number", 3,
					"Entrez le numéro UN valide");
			//Error messages
			insertIntoLanguageContent("Please_Enter_Bill_OF_lad_error", 1,
					"Enter Bill of Lading");
			insertIntoLanguageContent("Please_Enter_Bill_OF_lad_error", 2,
					"Ingresar el Conocimiento de embarque");
			insertIntoLanguageContent("Please_Enter_Bill_OF_lad_error", 3,
					"Entrez le connaissement");
			//Error messages
			insertIntoLanguageContent("Rear_Camera_Unavailable", 1,
					"Rear Camera Unavailable");
			insertIntoLanguageContent("Rear_Camera_Unavailable", 2,
					"Camara trasera no disponible");
			insertIntoLanguageContent("Rear_Camera_Unavailable", 3,
					"Caméra arrière pas disponible");
			//Dialog message
			insertIntoLanguageContent("Sorry_Try_Again_Message", 1,
					"Error!  Try Again");
			insertIntoLanguageContent("Sorry_Try_Again_Message", 2,
					"Error! Prube otra vez");
			insertIntoLanguageContent("Sorry_Try_Again_Message", 3,
					"Erreur ! Réessayez");

			//Dialog message
			insertIntoLanguageContent("Mutual_Exclusion_Message", 1,
					"Class 1 cannot be mixed with Class 7");
			insertIntoLanguageContent("Mutual_Exclusion_Message", 2,
					"Clase 1 no puede estar mezclada con clase 7");
			insertIntoLanguageContent("Mutual_Exclusion_Message", 3,
					"Classe 1 ne peut pas être mélangé avec la classe 7");

			//Dialog message
			insertIntoLanguageContent("Dialog_License_Expaired", 1,
					"License Expired. Contact admin");
			insertIntoLanguageContent("Dialog_License_Expaired", 2,
					"Licencia expirada. Contacte administrador");
			insertIntoLanguageContent("Dialog_License_Expaired", 3,
					"Le permis a expiré. Contactez admin");

			// menu items
			insertIntoLanguageContent("Menu_change_locale", 1, "Change Language");
			insertIntoLanguageContent("Menu_change_locale", 2,"Cambiar idioma");
			insertIntoLanguageContent("Menu_change_locale", 3,"Changer de langue");
			// menu items
			insertIntoLanguageContent("Menu_Show_Orders", 1, "Show Orders");
			insertIntoLanguageContent("Menu_Show_Orders", 2,"Ver las ordenes");
			insertIntoLanguageContent("Menu_Show_Orders", 3,"Voir les commandes");

			insertIntoLanguageContent("Menu_Items_Quit", 1, "Quit");
			insertIntoLanguageContent("Menu_Items_Quit", 2, "Salir");
			insertIntoLanguageContent("Menu_Items_Quit", 3, "Quitter");
			// menu items
			insertIntoLanguageContent("Menu_Max_Placards", 1, "Max Placards");
			insertIntoLanguageContent("Menu_Max_Placards", 2, "Max. placas");
			insertIntoLanguageContent("Menu_Max_Placards", 3, "Max. plaques");
			// menu items
			insertIntoLanguageContent("Menu_History", 1, "History");
			insertIntoLanguageContent("Menu_History", 2, "Historia");
			insertIntoLanguageContent("Menu_History", 3, "Histoire");
			// menu items
			insertIntoLanguageContent("Menu_Buy_Renew_License", 1, "Buy/Renew License");
			insertIntoLanguageContent("Menu_Buy_Renew_License", 2, "Comprar or renovar la licencia");
			insertIntoLanguageContent("Menu_Buy_Renew_License", 3, "Acheter ou renouveler la licence");

			// menu items
			insertIntoLanguageContent("Menu_Upgrade_License", 1, "Upgrade License");
			insertIntoLanguageContent("Menu_Upgrade_License", 2, "Licencia de actualización");
			insertIntoLanguageContent("Menu_Upgrade_License", 3, "Licence de mise à niveau");
			//Dialog message
			insertIntoLanguageContent("Dialog_Max_Plcard_Message", 1,
					"Max Placard Holders on Truck");
			insertIntoLanguageContent("Dialog_Max_Plcard_Message", 2,
					"Max. soporte de placs en el camion");
			insertIntoLanguageContent("Dialog_Max_Plcard_Message", 3,
					"Max supporte de plaques  sur le camion");
			//Dialog message
			insertIntoLanguageContent("Select_Description", 1,
					"Description");
			insertIntoLanguageContent("Select_Description", 2,
					"Descripcion");
			insertIntoLanguageContent("Select_Description", 3,
					"Description");
			//Dialog message
			insertIntoLanguageContent("Dialog_Sp84_Title", 1,
					"Select Type of Virus");
			insertIntoLanguageContent("Dialog_Sp84_Title", 2,
					"Seleccionar tipo de Virus");
			insertIntoLanguageContent("Dialog_Sp84_Title", 3,
					"Sélectionner le type de virus");
			//Dialog message
			insertIntoLanguageContent("Dialog_Package_Group", 1,
					"Packing Group");
			insertIntoLanguageContent("Dialog_Package_Group", 2,
					"Grupo de embalaje");
			insertIntoLanguageContent("Dialog_Package_Group", 3,
					"Groupe d'emballage");
            //Dialog message
            insertIntoLanguageContent("Dialog_Category", 1,
                    "Select Category");
            insertIntoLanguageContent("Dialog_Category", 2,
                    "selecciona una categoría");
            insertIntoLanguageContent("Dialog_Category", 3,
                    "Choisir une catégorie");
			//Dialog message
			insertIntoLanguageContent("Dialog_Cont_Be_Loaded", 1,
					"Can't be loaded on truck");
			insertIntoLanguageContent("Dialog_Cont_Be_Loaded", 2,
					"No puede ser cargado en el camion");
			insertIntoLanguageContent("Dialog_Cont_Be_Loaded", 3,
					"Ne peut pas être chargé sur camion");
			//Dialog message
			insertIntoLanguageContent("Dialog_Do_Not_Accept_Load", 1,
					"Not Enough Placard Holders on the truck Do NOT accept load");
			insertIntoLanguageContent("Dialog_Do_Not_Accept_Load", 2,
					"No acepte carga");
			insertIntoLanguageContent("Dialog_Do_Not_Accept_Load", 3,
					"Pas assez titulaires Pancarte sur le camion Ne pas accepter la charge");
			//Text view message
			insertIntoLanguageContent("TV_UN_Description", 1, "Description");
			insertIntoLanguageContent("TV_UN_Description", 2,
					"Descripcion");
			insertIntoLanguageContent("TV_UN_Description", 3,
					"Description");

			//Edit text hint messages
			insertIntoLanguageContent("Et_DG_Mass_Pkg", 1, "DG mass/pkg");
			insertIntoLanguageContent("Et_DG_Mass_Pkg", 2, "MP peso/paq.");
			insertIntoLanguageContent("Et_DG_Mass_Pkg", 3, "MD masse/pqt");
			//Edit text hint messages
			insertIntoLanguageContent("Et_No_Of_Pkg", 1, "No. of Pkgs");
			insertIntoLanguageContent("Et_No_Of_Pkg", 2, "No. de paquetes");
			insertIntoLanguageContent("Et_No_Of_Pkg", 3, "Nombre de paquets");
			//Edit text hint messages
			insertIntoLanguageContent("Et_ERAP", 1, "ERAP Number");
			insertIntoLanguageContent("Et_ERAP", 2,
					"n/a");
			insertIntoLanguageContent("Et_ERAP", 3, "Numero de PIU");
			//Edit text hint messages
			insertIntoLanguageContent("No_Placards_added_Message", 1,
					"No Placards Added");
			insertIntoLanguageContent("No_Placards_added_Message", 2,
					"Placas no agregadas");
			insertIntoLanguageContent("No_Placards_added_Message", 3,
					"Ne plaques pas ajouté");
			//Text view message
			insertIntoLanguageContent("Placards_On_Truck_Message", 1,
					"These Placard(s) Must be on the Truck");
			insertIntoLanguageContent("Placards_On_Truck_Message", 2,
					"Estos Letrero(s) deben estar en el camión");
			insertIntoLanguageContent("Placards_On_Truck_Message", 3,
					"Ces Placard(s) doit être sur le camion");
			//Text view message
			insertIntoLanguageContent("No_Placards_Required_Message", 1,
					"Placard Not Required");
			insertIntoLanguageContent("No_Placards_Required_Message", 2,
					"Cartel No Requerido");
			insertIntoLanguageContent("No_Placards_Required_Message", 3,
					"Placard Non requis");
			//Dialog message
			insertIntoLanguageContent("Dialog_Version_Check", 1,
					"Android OS must be 2.3 or newer");
			insertIntoLanguageContent("Dialog_Version_Check", 2,
					"Android OS debe ser  2.3 or nuevo");
			insertIntoLanguageContent("Dialog_Version_Check", 3,
					"Android OS doit être 2.3 ou plus récent");
			//Dialog message
			insertIntoLanguageContent("Dialog_Remove_Entry", 1,
					"Unload Item ?");
			insertIntoLanguageContent("Dialog_Remove_Entry", 2,
					"Descagar el articulo?");
			insertIntoLanguageContent("Dialog_Remove_Entry", 3,
					"Déchargement l’article?");
			//Toast message
			insertIntoLanguageContent("Toast_Max_Placards", 1,
					"Max Placard holders set to ");
			insertIntoLanguageContent("Toast_Max_Placards", 2,
					"Fijar maximo numero de soporte de placas");
			insertIntoLanguageContent("Toast_Max_Placards", 3,
					"Fixer numéro des supporte de plaque maxime");
			//Toast message
			insertIntoLanguageContent("Toast_Selected_Language", 1,
					"Language set to English ");
			insertIntoLanguageContent("Toast_Selected_Language", 2,
					"El idioma escogido es Español");
			insertIntoLanguageContent("Toast_Selected_Language", 3,
					"Langage choisi est le Français");
			//Toast message
			insertIntoLanguageContent("Toast_No_Network", 1,
					"Connect to Internet");
			insertIntoLanguageContent("Toast_No_Network", 2,
					"Conectar a la internet");
			insertIntoLanguageContent("Toast_No_Network", 3,
					"Connecter à Internet");
			//Toast message
			insertIntoLanguageContent("Toast_No_Fhash_Light", 1,
					"Device does not have a flash");
			insertIntoLanguageContent("Toast_No_Fhash_Light", 2,
					"Dispositivo no tiene flash");
			insertIntoLanguageContent("Toast_No_Fhash_Light", 3,
					"Appareil ne dispose pas d'un flash");

			//Toast message
			insertIntoLanguageContent("Toast_Message_No_Orders", 1, "No orders for pick up");
			insertIntoLanguageContent("Toast_Message_No_Orders", 2, "No pedidos para recoger");
			insertIntoLanguageContent("Toast_Message_No_Orders", 3, "aucun ordre de ramasser");

			//Text view message
			insertIntoLanguageContent("TV_DG_Gross_Mass", 1, "DG Gross Mass");
			insertIntoLanguageContent("TV_DG_Gross_Mass", 2, "MP peso bruto");
			insertIntoLanguageContent("TV_DG_Gross_Mass", 3, "MD masse brute");
			//Dialog message
			insertIntoLanguageContent("Dialog_LICENSE_VERIFY", 1, "Validate License");
			insertIntoLanguageContent("Dialog_LICENSE_VERIFY", 2, "Validar la licencia");
			insertIntoLanguageContent("Dialog_LICENSE_VERIFY", 3, "Valider la licence");
			//Dialog message
			insertIntoLanguageContent("Dialog_Message_Select_Orders", 1, "Select Order");
			insertIntoLanguageContent("Dialog_Message_Select_Orders", 2, "Escoger el pedido");
			insertIntoLanguageContent("Dialog_Message_Select_Orders", 3, "choisir l'ordre");
			//Dialog message
			insertIntoLanguageContent("Dialog_Message_No_change", 1, "Cannot change No of Placards");
			insertIntoLanguageContent("Dialog_Message_No_change", 2, "No se puede cambiar n de carteles");
			insertIntoLanguageContent("Dialog_Message_No_change", 3, "Vous ne pouvez pas changer Pas de Placards");
			//Dialog message
			insertIntoLanguageContent("Title_Transaction_History", 1, "Transaction History");
			insertIntoLanguageContent("Title_Transaction_History", 2, "Historial de transacciones");
			insertIntoLanguageContent("Title_Transaction_History", 3, "Record des transactions");
            //Danger placard optimization enable
            insertIntoLanguageContent("Enable", 1, "Enable");
            insertIntoLanguageContent("Enable", 2, "Active");
            insertIntoLanguageContent("Enable", 3, "Enable");
            //Danger placard optimization disable
            insertIntoLanguageContent("Disable", 1, "Disable");
            insertIntoLanguageContent("Disable", 2, "Disable");
            insertIntoLanguageContent("Disable", 3, "Disable");
			//Show Placarding Options btn name
			insertIntoLanguageContent("Btn_Show_All", 1, "Show Placarding Options");
			insertIntoLanguageContent("Btn_Show_All", 2, "Mostrar opciones de placa");
			insertIntoLanguageContent("Btn_Show_All", 3, "showAll");
			//button name optimized
			insertIntoLanguageContent("Btn_Optimized", 1, "Optimized");
			insertIntoLanguageContent("Btn_Optimized", 2, "Optimizar");
			insertIntoLanguageContent("Btn_Optimized", 3, "september");
			//button name semi optmized
			insertIntoLanguageContent("Btn_SemiOptimized", 1, "Semi Optimized");
			insertIntoLanguageContent("Btn_SemiOptimized", 2, "Semi optimizado");
			insertIntoLanguageContent("Btn_SemiOptimized", 3, "showSemiOptimize");
			//button name no optimized
			insertIntoLanguageContent("Btn_Non_Optimized", 1, "Non Optimized");
			insertIntoLanguageContent("Btn_Non_Optimized", 2, "No optimizado");
			insertIntoLanguageContent("Btn_Non_Optimized", 3, "showNonOptimize");

			//view name placards in all scenarios
			insertIntoLanguageContent("All_Scenarios", 1, "Placards in all three scenarios");
			insertIntoLanguageContent("All_Scenarios", 2, "Placas en los tres escenarios");
			insertIntoLanguageContent("All_Scenarios", 3, "placardingStyles");

			//no erg guide message
			insertIntoLanguageContent("No_ERG_Guide", 1, "Guide for the UN Number is not found");
			insertIntoLanguageContent("No_ERG_Guide", 2, "Guide pour le numéro ONU est introuvable");
			insertIntoLanguageContent("No_ERG_Guide", 3, "Guía para el Número de la ONU no se encuentra");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * author Ideabytes
     * @param response
     * @return inserted rows count
     */
    public long insertIntoErapData(final String response) throws Exception {
        long tableCount = 0;
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
                db.insert(TABLE_ERAP_INFO, null, values);
                values.clear();
            }
            tableCount = getDBData.getErapTalbeCount();
            // Log.v(TAG,"erapInfo table count "+tableCount);
            if (tableCount != jsonObjectCount) {
                if (tableCount != jsonObjectCount) {
                    throw new Exception("erap table");
                }
            }
        } catch (Exception e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            throw new Exception("erap table");
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return tableCount;

    }

    /**
     * author suman
     * @param response
     * @return no of rows inserted
     */
    public long insertIntoSp84Info(final String response) throws Exception {
        long tableCount = 0;
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
                db.insert(TABLE_SP84_INFO, null, values);
                values.clear();
            }
            tableCount = getDBData.getSp84TalbeCount();
            // Log.v(TAG,"sp84Info table count "+tableCount);
            if (tableCount != jsonObjectCount) {
                if (tableCount != jsonObjectCount) {
                    throw new Exception("sp84 table");
                }
            }
            //db.close();
        } catch (Exception e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            throw new Exception("sp84 table");
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return tableCount;

    }
    /**
     * Author : Suman
     * This method is to insert segregation rules data into segregation table
     * @param response
     * @return
     */
    public long insertIntoClass1Compatibility(final String response) throws Exception {
        long tableCount = 1;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            GetDBData getDBData = new GetDBData(context);
            JSONArray jsonArray = new JSONParser().getJsonArray(response, "class1Compatability");
            //Log.v(TAG,"class1Compatability table service "+jsonArray);
            int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
            //Log.v(TAG,"class1Compatability table count from service "+jsonObjectCount);
            // Iterate the jsonArray and print the info of JSONObjects
            for (int i = 1; i < jsonArray.length(); i++) {
                //Log.v(TAG,"class1Compatability table data "+response);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_1_1, jsonObject.optString("1.1")
                        .toString());
                values.put(COL_1_2, jsonObject.optString("1.2")
                        .toString());
                values.put(COL_1_3, jsonObject.optString("1.3")
                        .toString());
                values.put(COL_1_4, jsonObject.optString("1.4")
                        .toString());
                values.put(COL_1_5, jsonObject.optString("1.5")
                        .toString());
                values.put(COL_1_6, jsonObject.optString("1.6")
                        .toString());
                values.put(COL_CLASS1, jsonObject.optString("classname")
                        .toString());

                db.insert(TABLE_CLASS_COMITABILITY, null, values);
                //Log.v("InsertDBData","segregation values "+values);
                values.clear();
            }
            tableCount = getDBData.getClassCompatibilityTableCount();
            //Log.v(TAG, "class1Compatability table count " + tableCount);
            if (tableCount != jsonObjectCount) {
                if (tableCount != jsonObjectCount) {
                    throw new Exception("class compatibility table");
                }
            }
        } catch (Exception e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            throw new Exception("class compatibility table");
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return tableCount;
    }
    /**
     * Author : Suman
     * This method is to insert segregation rules data into segregation table
     * @param response
     * @return
     */
    public long insertIntoGroupCompatibility(final String response) throws Exception {
        long tableCount = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            GetDBData getDBData = new GetDBData(context);
            JSONArray jsonArray = new JSONParser().getJsonArray(response, "class1GroupNames");
            //Log.v(TAG,"class1GroupNames table service "+jsonArray);
            int jsonObjectCount = Integer.parseInt(jsonArray.getJSONObject(0).getString("count"));
            //Log.v(TAG,"class1GroupNames table count from service "+jsonObjectCount);
            // Iterate the jsonArray and print the info of JSONObjects
            for (int i = 1; i < jsonArray.length(); i++) {
                //Log.v(TAG,"class1GroupNames table data "+response);
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(COL_A, jsonObject.optString(COL_A)
                        .toString());
                values.put(COL_B, jsonObject.optString(COL_B)
                        .toString());
                values.put(COL_C, jsonObject.optString(COL_C)
                        .toString());
                values.put(COL_D, jsonObject.optString(COL_D)
                        .toString());
                values.put(COL_E, jsonObject.optString(COL_E)
                        .toString());
                values.put(COL_F, jsonObject.optString(COL_F)
                        .toString());
                values.put(COL_G, jsonObject.optString(COL_G)
                        .toString());
                values.put(COL_H, jsonObject.optString(COL_H)
                        .toString());
                values.put(COL_J, jsonObject.optString(COL_J)
                        .toString());
                values.put(COL_K, jsonObject.optString(COL_K)
                        .toString());
                values.put(COL_L, jsonObject.optString(COL_L)
                        .toString());
                values.put(COL_N, jsonObject.optString(COL_N)
                        .toString());
                values.put(COL_S, jsonObject.optString(COL_S)
                        .toString());
                values.put(COL_GROUP, jsonObject.optString("class_group")
                        .toString());

                db.insert(TABLE_GROUP_COMPITABILITY, null, values);
                //Log.v("InsertDBData","segregation values "+values);
                values.clear();
            }
            tableCount = getDBData.getGroupCompatibilityTableCount();
            //Log.v(TAG, "class1GroupNames table count " + tableCount);
            if (tableCount != jsonObjectCount) {
                if (tableCount != jsonObjectCount) {
                    throw new Exception("group compatibility table");
                }
            }

        } catch (Exception e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            throw new Exception("group compatibility table");
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return tableCount;
    }
	/**
	 * To insert pickup orders from web into 'orders' table 
	 * 
	 * author suman
	 * @param ordersData pick up order from web application
	 * @param status status of transaction
	 * @return rows count
	 */
//	public long insertIntoOrders(HashMap<String, String> ordersData ,int status) {
//		long rows = 0;
//		try {
//            //gets connection for SQLite database
//            getDatabaseConnection(dbCode);
//			ContentValues values = new ContentValues();
//			values.put(COL_DG_WEIGHT, ordersData.get(COL_DG_WEIGHT));
//			values.put(COL_UN_CLASS_ID, ordersData.get(COL_UN_CLASS_ID));
//			values.put(COL_UN_NUMBER, ordersData.get(COL_UN_NUMBER));
//			values.put(COL_PKG_GROUP, ordersData.get(COL_PKG_GROUP));
//			values.put(COL_WEIGHT_TYPE, ordersData.get(COL_WEIGHT_TYPE));
//			values.put(COL_IBC_RESIDUE_STATUS, ordersData.get(COL_IBC_RESIDUE_STATUS));
//			values.put(COL_WEIGHT_IN_KGS, ordersData.get(COL_WEIGHT_IN_KGS));
//			values.put(COL_DANGEROUS_PLACARD, ordersData.get(COL_DANGEROUS_PLACARD));
//			values.put(COL_UNNUMBER_DISPLAY, ordersData.get(COL_UNNUMBER_DISPLAY));
//			values.put(COL_DESCRIPTION, ordersData.get(COL_DESCRIPTION));
//			values.put(COL_BL, ordersData.get(COL_BL));
//			values.put(COL_SUBSIDARY_EXIST, ordersData.get(COL_SUBSIDARY_EXIST));
//			values.put(COL_NO_OF_UNITS, ordersData.get(COL_NO_OF_UNITS));
//			values.put(COL_ERAP_INDEX, ordersData.get(COL_ERAP_INDEX));
//			values.put(COL_IBC_STATUS, ordersData.get(COL_IBC_STATUS));
//			values.put(COL_GROUP_NAME, ordersData.get(COL_GROUP_NAME));
//			values.put(COL_UNTYPE, ordersData.get(COL_UNTYPE));
//			values.put(COL_WEIGHT_INDEX, ordersData.get(COL_WEIGHT_INDEX));
//			values.put(COL_MAX_PLACARD, ordersData.get(COL_MAX_PLACARD));
//			values.put(COL_PRIMARY_PLACARD, ordersData.get(COL_PRIMARY_PLACARD));
//			values.put(COL_GROSS_WEIGHT, ordersData.get(COL_GROSS_WEIGHT));
//			values.put(COL_SECONDARY_PLACARD, ordersData.get(COL_SECONDARY_PLACARD));
//			values.put(COL_PACKAGE_WEIGHT, ordersData.get(COL_PACKAGE_WEIGHT));
//			values.put(COL_ERAP_STATUS, ordersData.get(COL_ERAP_STATUS));
//			values.put(COL_ERAP_NO, ordersData.get(COL_ERAP_NO));
//			values.put(COL_INSERTED_DATE_TIME, ordersData.get(COL_INSERTED_DATE_TIME));
//			values.put(COL_NAME, ordersData.get(COL_NAME));
//			values.put(COL_TRANSACTION_ID, ordersData.get(COL_TRANSACTION_ID));
//			values.put(COL_USER_ID, ordersData.get(COL_USER_ID));
//			values.put(COL_USER_ID_WEB, ordersData.get(COL_USER_ID_WEB));
//			values.put(COL_TRANSACTION_ID_WEB, ordersData.get(COL_TRANSACTION_ID_WEB));
//            values.put(COL_TRANSACTION_STATUS, status);
//            //newly added keys 07-12-2015
//			values.put(COL_NONEXCEMPT, ordersData.get(COL_NONEXCEMPT));
//			values.put(COL_SPECIAL_PROVISION, ordersData.get(COL_SPECIAL_PROVISION));
//			values.put(COL_UN_SYLE, ordersData.get(COL_UN_SYLE));
//            values.put(COL_CONSIGNEE_DANGER, ordersData.get(COL_CONSIGNEE_DANGER));
//			values.put(COL_OPTIMISE, ordersData.get(COL_OPTIMISE));
//
//			rows = db.insert(TABLE_ORDERS, null, values);
//			values.clear();
//		} catch (Exception e) {
//			Log.e(InsertDBData.this.getClass().getSimpleName(), "Error insertIntoOrders method " + e.toString());
//			e.printStackTrace();
//		} finally {
//            //closes database connection
//            closeDatabaseConnection();
//        }
//		return rows;
//	}



	public long insertIntoOrders(final JSONArray response ,int pushTransStatus) {
		//TODO need to change input and outputs
		long rows = 0;
		try {
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			if (response.length() > 0) {
				for (int i = 0; i < response.length(); i++) {
					JSONObject jsonObject = null;
					ContentValues values = new ContentValues();
					jsonObject = response.getJSONObject(i);
					values.put(COL_BL, jsonObject.optString(COL_BL).toString());
					values.put(COL_TRANSACTION_ID_WEB, jsonObject.optString(COL_TRANSACTION_ID_WEB).toString());
					values.put(COL_USER_ID_WEB, jsonObject.optString(COL_USER_ID_WEB).toString());
					values.put(COL_UN_NUMBER, jsonObject.optString(COL_UN_NUMBER).toString());
					values.put(COL_DESCRIPTION, jsonObject.optString(COL_DESCRIPTION).toString());
					values.put(COL_DG_WEIGHT, jsonObject.optString(COL_DG_WEIGHT).toString());
					values.put(COL_GROSS_WEIGHT, jsonObject.optString(COL_GROSS_WEIGHT).toString());
					values.put(COL_NUMBER_OF_UNITS, jsonObject.optString(COL_NUMBER_OF_UNITS).toString());
					values.put(COL_WEIGHT_TYPE, jsonObject.optString(COL_WEIGHT_TYPE).toString());
					values.put(COL_WEIGHT_IN_KGS, jsonObject.optString(COL_WEIGHT_IN_KGS).toString());
					values.put(COL_GROUP_NAME, jsonObject.optString(COL_GROUP_NAME).toString());
					values.put(COL_NAME, jsonObject.optString(COL_NAME).toString());
					values.put(COL_DANGEROUS_PLACARD, jsonObject.optString(COL_DANGEROUS_PLACARD).toString());
					values.put(COL_DANGEROUS_PLACARD_ORIGINAL, jsonObject.optString(COL_DANGEROUS_PLACARD_ORIGINAL).toString());
					values.put(COL_PRIMARY_PLACARD, jsonObject.optString(COL_PRIMARY_PLACARD).toString());
					values.put(COL_SECONDARY_PLACARD, jsonObject.optString(COL_SECONDARY_PLACARD).toString());
					values.put(COL_WEIGHT_INDEX, jsonObject.optString(COL_WEIGHT_INDEX).toString());
					values.put(COL_TRANSACTION_STATUS, jsonObject.optString(COL_TRANSACTION_STATUS).toString());
					values.put(COL_TRANSACTION_VALUE, jsonObject.optString(COL_TRANSACTION_VALUE).toString());
					values.put(COL_DRIVER_ID, jsonObject.optString(COL_DRIVER_ID).toString());
					values.put(COL_PKG_GROUP, jsonObject.optString(COL_PKG_GROUP).toString());
					values.put(COL_INSERTED_DATE_TIME, jsonObject.optString(COL_INSERTED_DATE_TIME).toString());
//					values.put(COL_ISBULK, jsonObject.optString(COL_ISBULK).toString());
					values.put(COL_OVERRIDEN, jsonObject.optString(COL_OVERRIDEN).toString());
//					values.put(COL_ZONE, jsonObject.optString(COL_ZONE).toString());
					values.put(COL_CHECKSTATUS, jsonObject.optString(COL_CHECKSTATUS).toString());
					values.put("non_exempt", jsonObject.optString(COL_NON_EXEMPT).toString());
					values.put(COL_CONSIGNEE_DANGER, jsonObject.optString(COL_CONSIGNEE_DANGER).toString());
					values.put(COL_UN_SYLE, jsonObject.optString(COL_UN_SYLE).toString());
					values.put(COL_SPECIAL_PROVISION, jsonObject.optString(COL_SPECIAL_PROVISION).toString());
//					values.put(COL_SAFETY_PERMIT, jsonObject.optString(COL_SAFETY_PERMIT).toString());
//					values.put(COL_DETONATOR_TYPE, jsonObject.optString(COL_DETONATOR_TYPE).toString());
					values.put(COL_NOS, jsonObject.optString(COL_NOS).toString());
					values.put(COL_PUSH_STATUS, pushTransStatus);
					values.put(COL_IBC_STATUS,jsonObject.optString(COL_IBC_STATUS).toString());
					values.put(COL_UN_CLASS_ID,jsonObject.optString(COL_UN_CLASS_ID).toString());
					values.put(COL_PACKAGE_WEIGHT,jsonObject.optString(COL_PACKAGE_WEIGHT).toString());
					values.put(COL_ERAP_STATUS, jsonObject.get(COL_ERAP_STATUS).toString());
					values.put(COL_UNTYPE, jsonObject.get(COL_UNTYPE).toString());
					values.put(COL_SUBSIDARY_EXIST, jsonObject.get(COL_SUBSIDARY_EXIST).toString());
					values.put(COL_UNNUMBER_DISPLAY,jsonObject.get(COL_UNNUMBER_DISPLAY).toString());
					values.put(COL_SYNC_STATUS,jsonObject.get(COL_SYNC_STATUS).toString());
					values.put(COL_IBC_RESIDUE_STATUS, jsonObject.get(COL_IBC_RESIDUE_STATUS).toString());
					values.put(COL_ERAP_INDEX,jsonObject.get(COL_ERAP_INDEX).toString());
					values.put(COL_ERAP_NO,jsonObject.get(COL_ERAP_NO).toString());
					values.put( COL_INSERTED_DATE_TIME,jsonObject.get(COL_ERAP_NO).toString());
					values.put( COL_MAX_PLACARD,jsonObject.get(COL_MAX_PLACARD).toString());
					values.put(COL_TRANSACTION_ID, jsonObject.optString(COL_TRANSACTION_ID).toString());
					values.put(COL_OPTIMISE, jsonObject.get(COL_OPTIMISE).toString());

					//Log.v(LOGTAG,"values "+values);
					rows = db.insert(TABLE_ORDERS, null, values);
					values.clear();
				}
			}
		} catch (Exception e) {
			Log.e(InsertDBData.this.getClass().getSimpleName(), "Error insertIntoOrders method " + e.toString());
		} finally {
			//closes database connection
			closeDatabaseConnection();
		}
		return rows;
	}


















    /**
     * author suman
     * @param verifyLicenseStatus license verification status
     * @param deviceId device id
     * @return no of rows inserted
     */
    public long insertIntoTermsCheck(final String verifyLicenseStatus,final String deviceId) {
        long rows = 0;
        try {
            //gets connection for SQLite database
            getDatabaseConnection(dbCode);
            ContentValues values = new ContentValues();
            values.put(COL_IMEI, deviceId);
            values.put(COL_VERIFY_LICENSE, verifyLicenseStatus);
            rows = db.insert(TABLE_TERMS_CHECK, null, values);
            values.clear();
        } catch (Exception e) {
            Log.e(InsertDBData.this.getClass().getSimpleName(), "Error insertIntoTermsCheck method " + e.toString());
            e.printStackTrace();
        } finally {
            //closes database connection
            closeDatabaseConnection();
        }
        return rows;
    }



	/**
	 * Save placarding type in database from preferred options from menu items
	 * @param finalString
	 */
	public void savePlacardingType(final String finalString,final String action, final String transId) {
		try {
			//0 means non optimise
			//1 means semi optimise
			//2 means optimise
			Utils utils = new Utils();
			//gets connection for SQLite database
			getDatabaseConnection(dbCode);
			ContentValues values = new ContentValues();
			values.put(COL_NON_OPTIMISE,utils.getPlacardingType(finalString,0));
			values.put(COL_SEMI_OPTIMISE,utils.getPlacardingType(finalString,1));
			values.put(COL_OPTIMISE,utils.getPlacardingType(finalString,2));
			values.put(COL_INSERTED_DATE_TIME,utils.getPresentDateTime());
			values.put(COL_PUSH_STATUS,"0");
			values.put(COL_ACTION,action);
			values.put(COL_TRANS_ID,transId);
			db.insert(TABLE_PLACARDING_TYPE,null,values);
			values.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabaseConnection();
		}
	}


//	public void insertPlacardingType() {
//		try {
//			//gets connection for SQLite database
//			getDatabaseConnection(dbCode);
//			ContentValues values = new ContentValues();
//			values.put(COL_PLACARDING_TYPE,1);
//			db.insert(TABLE_UTILITIES, null, values);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			//closes database connection
//			closeDatabaseConnection();
//		}
//	}


    @Override
    public Object getInstance() {
        return null;
    }
}