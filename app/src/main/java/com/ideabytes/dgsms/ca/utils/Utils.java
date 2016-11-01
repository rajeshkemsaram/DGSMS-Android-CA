package com.ideabytes.dgsms.ca.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;

import com.ideabytes.dgsms.ca.adapters.CustomAdapterSpecialCase;
import com.ideabytes.dgsms.ca.database.GetDBData;

import com.ideabytes.dgsms.ca.networkcheck.DialogCheckNetConnectivity;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.UnInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/*******************************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : Utils
 * author:  Suman
 * Description : To construct placards this class is having methods to deal with Danger logic
 * Modified Date : 22-02-2016
 * Reason: getDeviceId method changed to get imei when there are two slots
 *********************************************************************************************/
public class Utils implements DBConstants {
	private final String LOGTAG = "Utils";
	private static Context context;
	private Activity activity;
	public static int subsidaryExist = 0;

	public static String language = "en";//language by default "English"
	public static String groupName = "";//groupName from DG logic

	public Utils() {

	}

	public Utils(Context context) {
		Utils.context = context;
	}

	public Utils(Activity activity) {
		this.activity = activity;
	}

	/**
	 * To restrict decimal values upto 2 decimal points, example: 123.23 if it
	 * is 123.23563
	 * 
	 * @author Suman
	 * @param value
	 * @return double value
	 */
	public double resrictDecimals(double value) {
		// To restrict decimal values upto 2, ex. 123.23
		DecimalFormat df = new DecimalFormat("#.##");
		String Swtp = df.format(value);
		value = Double.parseDouble(Swtp);
		return value;
	}// resrictDecimals()

	/**
	 * This method returns UN Number Info like Pkg Group,Erap, Description etc
	 * 
	 * @author Suman
	 * @param unnumber
	 * @param context
	 * @return UN Number info
	 */
	public HashMap<String, ArrayList<String>> unInfo(String unnumber,
			Context context) {
		String description = null;
		int length = 0;
		String sp84status = null;
		String sp84value = null;
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		ArrayList<String> descrip = new ArrayList<String>();
		ArrayList<String> responseLength = new ArrayList<String>();
		ArrayList<String> sp84Reponse = new ArrayList<String>();
		ArrayList<String> pkg = new ArrayList<String>();
		ArrayList<String> erapIndex = new ArrayList<String>();
		ArrayList<String> erapStatus = new ArrayList<String>();
		ArrayList<String> unType = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> pp = new ArrayList<String>();
        ArrayList<String> unclass_id = new ArrayList<String>();
        ArrayList<String> nonexempt = new ArrayList<String>();
		String desc = "";
		UnInfo un = new UnInfo(context);
		String result = "{\"result\" :" + un.validateUnNumber(unnumber) + "}";
		//Log.v("Utils","::result::unInfo()::===> "+result);

		try {
			if (result.length() < 50) {
				JSONObject jo = new JSONObject(result);
				descrip.add(jo.getString("result"));
				map.put("desc", descrip);
				responseLength.add("empty");
				map.put("length", responseLength);
				return map;
			} else {
				JSONObject jo = new JSONObject(result);
				JSONArray ja = jo.getJSONArray("result");
				length = ja.length();
				responseLength.add(String.valueOf(length));

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jsonObjectinDataArray = ja.getJSONObject(i);
					description = jsonObjectinDataArray
							.optString("description").toString();
					unType.add(jsonObjectinDataArray.optString("untype")
							.toString());
					name.add(jsonObjectinDataArray.optString("name")
							.toString());
                    pp.add(jsonObjectinDataArray.optString("primary_placard")
                            .toString());
                    unclass_id.add(jsonObjectinDataArray.optString("unclass_id")
                            .toString());
                    nonexempt.add(jsonObjectinDataArray.optString("nonexempt")
                            .toString());
					if (jsonObjectinDataArray.optString("secondary_placard")
							.toString().length() > 0) {
						subsidaryExist = 1;
					} else {
						subsidaryExist = 0;
					}
					JSONArray erap = jsonObjectinDataArray
							.getJSONArray("erapdetails");

					for (int j = 0; j < erap.length(); j++) {
						JSONObject jsonObjectin = erap.getJSONObject(j);
						String erap_status = jsonObjectin.optString(
								"erap_status").toString();
						erapStatus.add(erap_status);
						String pkg_group = jsonObjectin.optString("pkg_group")
								.toString();
						if (pkg_group.isEmpty()) {
							pkg_group = "empty";
						}
						pkg.add(pkg_group);
						String erap_index = jsonObjectin
								.optString("erap_index").toString();
						if (erap_status.equals("1")) {
							erapIndex.add(erap_index);
						} else if (AddPlacardDialogActivity.ibc_status.equals("1")) {
							erapIndex.add("1000");
						}
						sp84status = jsonObjectin.optString("sp84status")
								.toString();
						sp84value = jsonObjectin.optString("sp84value")
								.toString();
						sp84Reponse.add(sp84status);
						if (sp84status.equals("1")) {
							sp84Reponse.add(sp84value);
						}
					}// for
                    desc = desc + description;
				}
				descrip.add(desc);
				map.put("length", responseLength);
				map.put("desc", descrip);
				map.put("pkg_group", pkg);
				map.put("erap_index", erapIndex);
				map.put("erap_status", erapStatus);
				map.put("sp84", sp84Reponse);
				map.put("untype", unType);
				map.put("name", name);
                map.put("pp", pp);
                map.put("unclass_id", unclass_id);
                map.put("nonexempt", nonexempt);
			}
		} catch (Exception e) {
			Log.e("Utils",
					"Error in un details from validate un unmber class, on un number tab out "
							+ e.toString());
			Utils.generateNoteOnSD(TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()),FOLDER_PATH_DEBUG);
		}
		return map;
	}// unInfo()

	/**
	 * This method returns UN Number description values if length of erap
	 * details response is greater than 1
	 * 
	 * @author Suman
	 * @param unNumber
	 * @param context
	 * @return description values of UN Number
	 */
//	public ArrayList<JSONObject> specialCase(String unNumber, Context context) {
	public static JSONArray specialCase(String unNumber, Context context) {
			String data =	unNumber.replaceAll(" ", " ");
		String search = " or ";
		JSONArray splitResponse = new JSONArray();

		if(data.contains("@")) {
			String [] outerSplit = data.split("@");
			for(int i = 0; i<outerSplit.length; i++){
				String saparatePsn[] = outerSplit[i].split("#");
				if(saparatePsn[0].contains(search)){
					String [] innerSplit = saparatePsn[0].split(search);
					for(int j =0; j< innerSplit.length; j++){
						splitResponse.put(innerSplit[j]+"#"+(saparatePsn.length == 2?saparatePsn[1]:""));
					}
				}else{
					splitResponse.put(saparatePsn[0]+"#"+(saparatePsn.length == 2?saparatePsn[1]:""));
				}
			}
		} else if(data.split("#")[0].contains(search)) {
			String saparatePsn[] = data.split("#");
			String [] innerSplit = saparatePsn[0].split(search);
			for(int k = 0; k< innerSplit.length; k++){
				splitResponse.put(innerSplit[k]+"#"+(saparatePsn.length == 2?saparatePsn[1]:""));
			}
		} else {
			splitResponse.put(data);
		}
		return splitResponse;

	}// specialCase()

	/**
	 * method for multiple description if length  >1
	 * @param unNumber for collection of description arraylist
	 * @param context
     * @return
     */
	public ArrayList<String> specialCase1(ArrayList<JSONObject> unNumber, Context context) {

		UnInfo un = new UnInfo(context);
		ArrayList<String> specialCase = new ArrayList<String>();

		for(int i=0;i<unNumber.size();i++){
			try {
				specialCase.add(i,unNumber.get(i).getString("description"));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}


		return specialCase;
	}
	/**
	 * This method is used to make package group list
	 * 
	 * @author Suman
	 * @param unnumber
	 * @param context
	 * @return package group for a un number information as Hash Map data
	 */
	public HashMap<String, HashMap<String, String>> getInfo(String unnumber,
			Context context) {
		HashMap<String, HashMap<String, String>> resultMap = new HashMap<String, HashMap<String, String>>(
				3);
		UnInfo un = new UnInfo(context);
		String result = "{\"result\" :" + un.validateUnNumber(unnumber) + "}";
		try {
			JSONObject jo = new JSONObject(result);
			JSONArray ja = jo.getJSONArray("result");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jsonObjectinDataArray = ja.getJSONObject(i);
				JSONArray erap = jsonObjectinDataArray
						.getJSONArray("erapdetails");
				for (int j = 0; j < erap.length(); j++) {
					JSONObject jsonObjectin = erap.getJSONObject(j);
					String erap_status = jsonObjectin.optString("erap_status")
							.toString();
					String pkg_group = jsonObjectin.optString("pkg_group")
							.toString();
					String erap_index = jsonObjectin.optString("erap_index")
							.toString();
					HashMap<String, String> erapdetals = new HashMap<String, String>();
					erapdetals.put("erap_index", erap_index);
					erapdetals.put("erap_status", erap_status);
					resultMap.put(pkg_group, erapdetals);
				}// for
			}// for
		} catch (ArrayIndexOutOfBoundsException oiobe) {
			oiobe.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (JSONException e1) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}// getInfo()

	/**
	 * This method makes input for Danger Placard Logic based on User input and
	 * response from first service based UN Number as JSONObject which contains
	 * all input parameters that is used in placard selection
	 * 
	 * @author Suman
	 * @param  inputToUtils
	 * @return JSONObject
	 */
	public static JSONObject processTransactionJsonInput(
			final JSONObject inputToUtils) {
		JSONObject jsonResponse = null;
		try {
			JSONObject jo = new JSONObject();
           // System.out.println(inputToUtils);
            UnInfo un = new UnInfo();
			jo.put(COL_USER_ID, inputToUtils.getString(DEVICE_ID));
			jo.put(COL_BL, inputToUtils.getString(COL_BL));
			jo.put(COL_UN_NUMBER, inputToUtils.getString("un_number"));
			jo.put(COL_WEIGHT_TYPE, inputToUtils.getString("weight_type"));
			jo.put(COL_DG_WEIGHT, inputToUtils.getDouble("dg_weight"));
			jo.put(COL_NO_OF_UNITS, inputToUtils.getString("number_of_units"));
			jo.put(COL_GROSS_WEIGHT, ((double) inputToUtils
					.getDouble("gross_weight") ));
			jo.put(COL_SUBSIDARY_EXIST, Utils.subsidaryExist);
			JSONArray local = new JSONArray(un.validateUnNumber(inputToUtils
					.getString(COL_UN_NUMBER)));

			for (int i = 0; i < local.length(); i++) {
				JSONObject erapdetails = local.getJSONObject(i);
				JSONArray erap = erapdetails.getJSONArray("erapdetails");
				// This happens for some UN Numbers, means without package group
				// and erap index
				if (erap.length() < 1) {
					jo.put(COL_PKG_GROUP, "");
					jo.put(COL_ERAP_INDEX, "");
				}
				for (int j = 0; j < erap.length(); j++) {
					JSONObject innerObject = erap.getJSONObject(j);
					jo.put(COL_ERAP_INDEX,
							innerObject.getString(COL_ERAP_INDEX));
					if (AddPlacardDialogActivity.ibc_status.equals("1")) {
						jo.put(COL_ERAP_INDEX,
								erap.getJSONObject(0).getString(COL_ERAP_INDEX));
					}
					if (innerObject.getString(COL_ERAP_INDEX).startsWith("S")) {
						jo.put(COL_ERAP_INDEX, "0");
					}
					jo.put(COL_PKG_GROUP, inputToUtils.getString(COL_PKG_GROUP));
				}
				jo.put(COL_UNTYPE, inputToUtils.getString(COL_UNTYPE).trim());
				jo.put(COL_NAME, inputToUtils.getString(COL_NAME).trim());
				jo.put(COL_WEIGHT_INDEX,
						erapdetails.getString(COL_WEIGHT_INDEX));
				// if un type is "l", set erap index to 450
				if (erapdetails.getString(COL_UNTYPE).equals("l")
						&& (double) (inputToUtils.getDouble("converted_Weight") / (double) inputToUtils
								.getInt("number_of_units")) > 450) {
					jo.put(COL_WEIGHT_INDEX, "450");
				} else if (AddPlacardDialogActivity.button_IBC.getText().toString()
						.equals(Utils.getResString(R.string.Dialog_Btn_Delete_Yes))) {
					if (AddPlacardDialogActivity.button_IBC.getVisibility() == View.VISIBLE) {
						jo.put(COL_WEIGHT_INDEX, "0");
					}
				}
				jo.put(COL_PRIMARY_PLACARD,
                        inputToUtils.getString(COL_PRIMARY_PLACARD));

				jo.put(COL_DESCRIPTION, inputToUtils.getString(COL_DESCRIPTION));
				// jo.put("description",erapdetails.getString("description"));
				jo.put(COL_DANGEROUS_PLACARD,
						erapdetails.getString(COL_DANGEROUS_PLACARD));
				// Based on un type, erap status set un number display status
				// on erap, untype = "l" or "g" un number must on placard
				jo.put(COL_UNNUMBER_DISPLAY,
						inputToUtils.getString(COL_UNNUMBER_DISPLAY));
				jo.put(COL_SECONDARY_PLACARD,
						erapdetails.getString(COL_SECONDARY_PLACARD));
				jo.put(COL_TRANSACTION_ID, inputToUtils.getString(DEVICE_ID));
				jo.put(COL_INSERTED_DATE_TIME,
						inputToUtils.getString(COL_INSERTED_DATE_TIME));
				jo.put(COL_PACKAGE_WEIGHT,
						inputToUtils.getDouble(COL_DG_WEIGHT));
				jo.put(COL_WEIGHT_IN_KGS,
						inputToUtils.getDouble("converted_Weight"));
				jo.put(COL_ERAP_NO, inputToUtils.getString(COL_ERAP_NO));
				jo.put(COL_UN_CLASS_ID, inputToUtils.getInt(COL_UN_CLASS_ID));
				jo.put(COL_GROUP_NAME, erapdetails.getString(COL_GROUP_NAME));
				jo.put(COL_IBC_STATUS, inputToUtils.getString(COL_IBC_STATUS));
				jo.put(COL_IBC_RESIDUE_STATUS,
						inputToUtils.getString(COL_IBC_RESIDUE_STATUS));
				jo.put(COL_USER_ID_WEB, inputToUtils.getString(COL_USER_ID_WEB));
				jo.put(COL_TRANSACTION_ID_WEB,
						inputToUtils.getString(COL_TRANSACTION_ID_WEB));
                jo.put(COL_OPTIMISE, inputToUtils.getString(COL_OPTIMISE));
				jo.put(COL_SPECIAL_PROVISION,
						erapdetails.getString("specialProvision"));
                jo.put("consignee_danger", inputToUtils.getString("consignee_danger"));
				jo.put("nonexempt", inputToUtils.getString("nonexempt"));
				jo.put("un_style", inputToUtils.getString("un_style"));
			}
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(jo);
			//System.out.println("jo "+jo);
			JSONObject jsonResult = new JSONObject();
			jsonResult.put("Data", jsonArray);
			String jsonStr = jsonResult.toString();
			jsonResponse = new JSONObject(jsonStr);
		} catch (Exception e) {
			Log.e("Utils",
					"Error in json construction for input to placard selection "
							+ e.toString());
			Utils.generateNoteOnSD(TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()), FOLDER_PATH_DEBUG);
		}
		return jsonResponse;
	}// processTransactionJsonInput()


	/**
	 * This method returns present Date Time in format (yyyy-MM-dd HH:mm:ss)
	 * 
	 * @author Suman
	 * @return Date Time
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getPresentDateTime() {
		String presentDate = "";
		try {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			presentDate = sdf.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
            generateNoteOnSD(FOLDER_PATH_DEBUG,"debug",e.toString());
		}
		return presentDate;
	}// getPresentDateTime()

	/***
	 * This method stores debug text in specified location if any exception
	 * caught of File (Bug report)
	 *
	 * @author Suman
	 * @param fileName
	 * @param body
	 * @since 5.2.0
	 */
	public static void generateNoteOnSD(final String path,final String fileName,final String body) {
		try {
			File root = new File(path);
			if (!root.exists()) {
				root.mkdirs();
			}
            FileOutputStream fOut = new FileOutputStream(root+"/"+getPresentDateTime()+".txt");
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
           // System.out.println(body);
            myOutWriter.append(body);
            myOutWriter.close();
            fOut.close();
			// Toast.makeText(MainActivity.getContext(), "Bug Report generated",
			// Toast.LENGTH_SHORT).show();
			// if(new
			// ConnectionDetector(MainActivity.getContext()).isConnectingToInternet())
			// {
			// new SendBugReportAsyncTask(context).execute();
			// }
		} catch (Exception e) {
			Log.e("Utils",
                    "Error in writing debug note to folder path" + e.toString());
		}
	}// generateDebugFile()

	/**
	 * This method is to read text file content
	 *
	 * @param path
	 * @param fileName
	 * @return file content
	 */
	public String readTextFile(final String path,final String fileName) {
		String aBuffer = "";
		try {
			File myFile = new File(path+"/"+fileName+".txt");
			FileInputStream fIn = new FileInputStream(myFile);
			BufferedReader myReader = new BufferedReader(
					new InputStreamReader(fIn));
			String aDataRow = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
			//System.out.println("file content "+aBuffer);
			myReader.close();

		} catch (Exception e) {
			Log.e("Utils", "Error in creating folder DGSMS" + e.toString());
			Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
		}
		return  aBuffer.trim();
	}

	/**
	 * This method creates "/DGSMS/Apk/" folder in sd card to store updated apk
	 * downloaded from server
	 * 
	 * @author suman
	 * @param folderPath
	 * @return folder creation status
	 */
	public static boolean createDGSMSFolderIfRequired(String folderPath) {
		try {
			File directory = new File(folderPath);
			// Log.v(TAG, "Folder path=>"+directory.getAbsolutePath());
			if (!directory.isDirectory()) {
				if (!directory.mkdirs()) {
					return false;
				}
			}// try
		} catch (Exception e) {
			Log.e("Utils", "Error in creating folder DGSMS" + e.toString());
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
		}// catch
		return true;
	}// CreateIBPlayerDirectoryIfRequired()

	/**
	 * This method deletes file
	 * 
	 * @author Suman
	 * @param path
	 * @return folder deletion status
	 */
	public static boolean deleteDirectory(File path) {
		try {
			if (path.exists()) {
				File[] files = path.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		} catch (Exception e) {
			Log.e("Utils", "Error in deleting folder path" + e.toString());
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
		}
		return (path.delete());
	}

	/**
	 * This method returns device id (imei number) If device is without imei number
	 * then it will return mac address of the device as device id when nework present
	 *
	 * @author suman
	 * @return device id
	 */
	public String getDeviceId(final Context context) {
		// Log.v(LOGLOGTAG,"device id "+id);
		try {
			Log.v("devide id",Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID.toString()));
				return Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.ANDROID_ID);

		} catch (Exception e) {
			return "12345";
		}
	}






	/**
	 * This method removes duplicate entries from a array list
	 * 
	 * @author suman
	 * @param list
	 * @return Array list
	 */
	public ArrayList<String> eliminateDuplicatesFromList(ArrayList<String> list) {
		Set se = new HashSet(list);
		list.clear();
		list = new ArrayList<>(se);
		Collections.sort(list);
		return list;
	}

	/**
	 * Returns application package name
	 * 
	 * @author Suman
	 * @since v.b.5.2.6
	 */
	public String getPackageName() {
		return context.getApplicationContext().getPackageName();
	}

	/**
	 * Returns application name
	 * 
	 * @author Suman
	 * @return app name
	 */
	public String getAppName() {
		return context.getResources().getString(R.string.app_name);
	}

	/**
	 * Returns application version
	 * 
	 * @author Suman
	 * @return version number
	 */
	public String getAppVersion() {
		return context.getResources().getString(R.string.version);
	}

	/**
	 * This method is to give  optimization result from dg final string
	 *
	 * @param finalValue
	 * @return placard information hash map
	 */
    public String getResult(final String finalValue,final String key) {
        String imageString = "";
        if (finalValue.equals("")) {
            imageString = "No placard Added";
        } else {
           // System.out.println("final value in Utils::getResult():: " + finalValue);
            if (key.startsWith("Basic")) {
                imageString = finalValue.split("@@@")[0].substring(3);
               // System.out.println("Basic optimize ===> "+imageString);
            } else if (key.startsWith("Semi")) {
                imageString = finalValue.split("@@@")[1].substring(3);
               // System.out.println("semi optimize ===> "+imageString);
            } else {
                imageString = finalValue.split("@@@")[2].substring(3);
               // System.out.println("optimize ===> "+imageString);
            }
        }
        return  imageString;
    }

    /**
     * This method is to give basic otpimization result from dg final string
     *
     * @param finalValue
     * @return placard information hash map
     */
    public HashMap<String,String> basicOptimize(final String finalValue) {
		String imageString = "";
		HashMap<String,String> resultHashMap = null;
		String unNumber = "none";
		try {
			if (finalValue.equals("")) {
				imageString = "No placard Added";
			} else {
				Log.v(LOGTAG, "final value in Utils::basicOptimize():: " + finalValue);
				imageString = finalValue.split("@@@")[0].substring(3);
			//	Log.v(LOGTAG, "basic optimize ===> " + imageString);
			}
			if (imageString.startsWith("~")) {
				imageString = "No placard Added";
			}
			//if LinkedHashMap then all class 1 will be added as first placard this will use
			//in displaying group name on class 1 placard
			resultHashMap = new LinkedHashMap<String, String>();
			// response split based on comma(,), ex: response is
			// 1::3~~pp~~2772,6_1~~pp~~2777,
			String firstArray[] = imageString.split(",");
			firstArray = remove1S(firstArray);
			//Log.v(LOGTAG,"first array size "+firstArray.length);
			if (firstArray.length > 1) {
				for (int i = 0; i < firstArray.length; i++) {
					//Log.v(LOGTAG," firstArray values " +firstArray[i]);
					String secondArray[] = firstArray[i].split("~~");
					//System.out.println("secondary array size " + secondArray.length);
					for (int j = 0; j < secondArray.length; j++) {
						//System.out.println("secondary array values " + secondArray[j]);
						if (secondArray[j].startsWith("-GROUP")) {
							Utils.groupName = getGroupName(secondArray[j]);
						}
						if (secondArray.length > 2) {
							unNumber = secondArray[2];
							//System.out.println("debug i am in else  class "+secondArray[0]+" un "+unNumber+" pos "+i+" gr "+getGroupName(secondArray[j]));
							resultHashMap.put(String.valueOf(i), secondArray[0] + ":" + unNumber);
						} else {
							unNumber = "none";
							//System.out.println("debug i am in else  class "+secondArray[0]+" un "+unNumber+" pos "+i+" gr "+getGroupName(secondArray[j]));
							resultHashMap.put(String.valueOf(i), secondArray[0] + ":" + unNumber);
						}//else
					}//for
				}//for
			}//if
			else {
				Utils.groupName = "";
				resultHashMap.put("result", "sempty" + ":" + unNumber);
			}//else
		} catch (Exception e){
			Log.e(LOGTAG, "error in basic otpimize method ");
			e.printStackTrace();
		}
		resultHashMap = getHashMap(resultHashMap);
		return resultHashMap;
    }
    /**
     * This method is to give semi otpimization result from dg final string
     *
     * @param finalValue
     * @return placard information hash map
     */
    public HashMap<String,String> semiOptimize(final String finalValue) {
        String imageString = "";
        HashMap<String,String> resultHashMap = null;
        String unNumber = "none";
        try {
            if (finalValue.equals("")) {
                imageString = "No placard Added";
            } else {
				//Log.v(LOGTAG, "final value in Utils::semiOptimize():: " + finalValue);
                imageString = finalValue.split("@@@")[1].substring(3);
				Log.v(LOGTAG, "semi optimize ===> " + imageString);
            }
			if (imageString.startsWith("~")) {
				imageString = "No placard Added";
			}
            //if LinkedHashMap then all class 1 will be added as first placard this will use
                    //in displaying group name on class 1 placard
                    resultHashMap = new LinkedHashMap<String, String>();
                // response split based on comma(,), ex: response is
                // 1::3~~pp~~2772,6_1~~pp~~2777,
                String firstArray[] = imageString.split(",");
                firstArray = remove1S(firstArray);
				//Log.v(LOGTAG,"first array size "+firstArray.length);
                if (firstArray.length > 1) {
                    for (int i = 0; i < firstArray.length; i++) {
						//Log.v(LOGTAG," firstArray values " +firstArray[i]);
                        String secondArray[] = firstArray[i].split("~~");
                        //System.out.println("secondary array size " + secondArray.length);
                        for (int j = 0; j < secondArray.length; j++) {
                            //System.out.println("secondary array values " + secondArray[j]);
                            if (secondArray[j].startsWith("-GROUP")) {
                                Utils.groupName = getGroupName(secondArray[j]);
                            }
                            if (secondArray.length > 2) {
                                unNumber = secondArray[2];
                                //System.out.println("debug i am in else  class "+secondArray[0]+" un "+unNumber+" pos "+i+" gr "+getGroupName(secondArray[j]));
                                resultHashMap.put(String.valueOf(i), secondArray[0] + ":" + unNumber);
                            } else {
                                unNumber = "none";
                                //System.out.println("debug i am in else  class "+secondArray[0]+" un "+unNumber+" pos "+i+" gr "+getGroupName(secondArray[j]));
                                resultHashMap.put(String.valueOf(i), secondArray[0] + ":" + unNumber);
                            }//else
                        }//for
                    }//for
                }//if
                else {
                    Utils.groupName = "";
                    resultHashMap.put("result", "sempty" + ":" + unNumber);
                }//else
        } catch (Exception e){
			Log.e(LOGTAG, "error in basic otpimize method ");
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
            e.printStackTrace();
        }
        resultHashMap = getHashMap(resultHashMap);
        return resultHashMap;
    }

    /**
	 * This method is to give  optimization result from dg final string
	 *
	 * @param finalValue
	 * @return placard information hash map
	 */
    public HashMap<String,String> optimize(final String finalValue) {
        String imageString = "";
        HashMap<String,String> resultHashMap = null;
        String unNumber = "none";
        try {
            if (finalValue.equals("")) {
                imageString = "No placard Added";
            } else {
              //  Log.v(LOGTAG,"final value in Utils::optimize():: " + finalValue);
                imageString = finalValue.split("@@@")[2].substring(3);
                Log.v(LOGTAG, "optimize ===> " + imageString);
            }
			if (imageString.startsWith("~")) {
				imageString = "No placard Added";
			}
            //if LinkedHashMap then all class 1 will be added as first placard this will use
                    //in displaying group name on class 1 placard
                    resultHashMap = new LinkedHashMap<String, String>();
                // response split based on comma(,), ex: response is
                // 1::3~~pp~~2772,6_1~~pp~~2777,
                String firstArray[] = imageString.split(",");
                //System.out.println("first array size "+firstArray.length);
                if (firstArray.length > 1) {
                    for (int i = 0; i < firstArray.length; i++) {
						//System.out.println(" firstArray values " +firstArray[i]);
						String secondArray[] = firstArray[i].split("~~");
						//System.out.println("secondary array size " + secondArray.length);
                        for (int j = 0; j < secondArray.length; j++) {
                             //System.out.println("secondary array values " + secondArray[j]);
                            if (secondArray[j].startsWith("-GROUP")) {
                                Utils.groupName = getGroupName(secondArray[j]);
                            }
                                if (secondArray.length > 2) {
                                    unNumber = secondArray[2];
									//System.out.println("debug i am in else  class "+secondArray[0]+" un "+unNumber+" pos "+i+" gr "+getGroupName(secondArray[j]));
									resultHashMap.put(String.valueOf(i), secondArray[0] + ":" + unNumber);
                                } else {
									unNumber = "none";
									//System.out.println("debug i am in else  class "+secondArray[0]+" un "+unNumber+" pos "+i+" gr "+getGroupName(secondArray[j]));
                                    resultHashMap.put(String.valueOf(i), secondArray[0] + ":" + unNumber);
                                }//else
                        }//for
                    }//for
                }//if
                else {
                    Utils.groupName = "";
                    resultHashMap.put("result", "sempty" + ":" + unNumber);
                }//else
        } catch (Exception e){
			Log.e(LOGTAG, "error in basic otpimize method ");
            //Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
            e.printStackTrace();
        }
		resultHashMap = getHashMap(resultHashMap);
        return resultHashMap;
    }

    /**
     * This method is to return group name and display name of the placards
     * @param groupNameString
     * @return group name+display name
     */
    public String getGroupName(final String groupNameString) {
        String groupName = "";
       if (groupNameString.startsWith("-GROUP")) {
			if (!groupNameString.contains("null")) {
				String array[] = groupNameString.split("-");
				//String check = array[i];
				String group = array[2];
				String displayName = array[4];
				//Log.v(LOGTAG, "displayName " + displayName);
				groupName = displayName + group;
				//Log.v(LOGTAG, "groupName " + groupName);
				//result.put("gname", displayName + group);
			}
		}
        return groupName;
    }

    /**
     * This method is to return result hash map to show placards by removing group name string
     * from final dg result string
     * @param response
     * @return
     */
	private HashMap<String,String> getHashMap(final HashMap<String,String> response) {
		for(Map.Entry<String,String> e : response.entrySet()) {
			String value = e.getValue();
			//System.out.println("debug value "+value);
			if(value.trim().startsWith("-GROUP")) {
				String key = e.getKey();
				String groupName = getGroupName(response.get(key));
				response.remove(key);
            }
		}
		//System.out.println("debug result "+response);
		return response;
	}
    /**
     * This method is to remove 1s from final dg string , for 1s placard required
     * @param values
     * @return final string array
     */
    public String[] remove1S(final String[] values) {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; i++) {
            if(!values[i].contains("1s")) {
                list.add(values[i]);
               }
        }
        return list.toArray(values);
    }

	/**
	 * This method sets params to view that you provide
	 * 
	 * @author suman
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return margin space
	 */
	public LinearLayout.LayoutParams setTextViewParams(int left, int top,
			int right, int bottom) {
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.setMargins(left, top, right, bottom); // llp.setMargins(left, top,
		// right, bottom);
		return llp;
	}

	/**
	 * Shows a toast message
	 * 
	 * @author suman
	 * @since v.b.5.4.2
	 */
	public void showToastMessage(String message) {
		try {
			// Create layout inflater object to inflate toast.xml file
			LayoutInflater inflater = activity.getLayoutInflater();

			// Call toast.xml file for toast layout
			View toastRoot = inflater.inflate(R.layout.custom_toast, null);
			TextView tvToast = (TextView) toastRoot.findViewById(R.id.tvToast);
			tvToast.setText(message);

			Toast toast = new Toast(activity);

			// Set layout to toast
			toast.setView(toastRoot);
			toast.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.show();
		} catch (Exception e) {
			Log.e(Utils.this.getClass().getSimpleName(),
					"Error in custom toast message display " + e.toString());
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
		}
	}

	public JSONArray getOrdersFromDatabase() {
		GetDBData get = new GetDBData(context);
		JSONObject response = new JSONObject();
		JSONArray jArray = new JSONArray();
		try {
		response = get.getPickupOrders();
			System.out.println("debug Response from db in utils "+response);
			if (response.length() > 0) {
				jArray = response.getJSONArray("getOrders");
			}
		} catch (Exception e) {
			Log.e(Utils.this.getClass().getSimpleName(),
					"Error in getting data from local databse for pick up orders "
							+ e.toString());
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, Arrays.toString(e.getStackTrace()), TEXT_FILE_NAME);
		}
		return jArray;
	}
	/**
	 * This method used to get emergency phone numbers
	 * 
	 * @param countryName
	 * @return emergency phone numbers
	 */
	public String getEmergencyNumbers(String countryName) {
		HashMap<String, String> details = new HashMap<String, String>();
		try {
			details.put(
					"CANADA",
					"1.<b>CANUTEC</b>, provides a 24 hour national bilingual (French and English) emergency response advisory service:<b>613-996-6666</b>(Collect calls are accepted)<b>*666 (STAR 666)</b> cellular (in Canada only)");

			details.put(
					"UNITED STATES",
					"1. <b>CHEMTREC</b>® , a 24 hour emergency response communication service, can be reached as follows: <b>1-800-424-9300</b> (Toll-free in the U.S., Canada and the U.S. Virgin Islands) <b>703-527-3887</b> For calls originating elsewhere \n "
							+ "(Collect calls are accepted) "
							+ "<br><br>"
							+ "2.<b>CHEMTEL, INC</b>., a 24 hour emergency response communication service, can be reached as follows: "
							+ "<b>1-888-255-3924</b> "
							+ "(Toll-free in the U.S., Canada, Puerto Rico and the U.S. Virgin Islands) "
							+ "<b>813-248-0585</b> For calls originating elsewhere "
							+ "(Collect calls are accepted) "
							+ "<br><br>"
							+ "3.<b>INFOTRAC</b>, a 24 hour emergency response communication service, can be reached as follows: "
							+ "<b>1-800-535-5053</b> "
							+ "(Toll-free in the U.S., Canada and the U.S. Virgin Islands) "
							+ "<b>352-323-3500</b> For calls originating elsewhere "
							+ "(Collect calls are accepted) "
							+ "<br><br>"
							+ "4.<b>3E COMPANY</b>, a 24 hour emergency response communication service, can be reached as follows: "
							+ "<b>1-800-451-8346</b>(Toll-free in the U.S., Canada and the U.S. Virgin Islands)<b>760-602-8703</b> For calls originating elsewhere(Collect calls are accepted) "
							+ "<br><br>"
							+ ""
							+ "The emergency response information services shown above have requested to be listed as providers of emergency response information and have agreed to provide emergency response information to all callers. They maintain periodically updated lists of state and Federal radiation authorities who provide information and technical assistance on handling incidents involving radioactive materials."
							+ "\n\n"
							+ "5.<b>MILITARY SHIPMENTS</b> For assistance at incidents involving materials being shipped by, for, or to the Department of Defense (DOD), call one of the following numbers (24 hours"
							+ "<b>703-697-0218</b> - Explosives/ammunition incidents"
							+ "(U.S. Army Operations Center)"
							+ "(Collect calls are accepted)"
							+ "<b>1-800-851-8061</b> - All other dangerous goods incidents"
							+ "(Defense Logistics Agency)"
							+ "(Toll-free in the U.S.)"
							+ "<br><br>"
							+ "6.<b>NATIONWIDE POISON CONTROL CENTER (United States only)</b>"
							+

							"<b>1-800-222-1222</b>" + "(Toll-free in the U.S.)");

			details.put(
					"MEXICO",
					"1.<b>SETIQ</b> (Emergency Transportation System for the Chemical Industry), a service of the National Association of Chemical Industries (ANIQ), can be reached as follows: "
							+ "<b>01-800-00-214-00</b> in the Mexican Republic (24 hours) "
							+ "For calls originating in Mexico City and the Metropolitan Area "
							+ "<b>5559-1588</b> "
							+ "For calls originating elsewhere, call "
							+ "<b>+52-55-5559-1588</b> "
							+ "<br><br>"
							+ "2.<b>CENACOM</b>, the National Center for Communications of the Civil Protection Agency, can be reached as follows: "
							+ "<b>01-800-00-413-00</b> in the Mexican Republic (24 hours) "
							+ "For calls originating in Mexico City and the Metropolitan Area "
							+ "<b>5128-0000 exts. 11470, 11471, 11472, 11473, 11474, 11475 and 11476</b> "
							+ "For calls originating elsewhere, call "
							+ "<b>+52-55-5128-0000 exts. 11470, 11471, 11472, 11474, 11475 and 11476</b>");

			details.put(
					"ARGENTINA",
					"1.<b>CIQUIME</b>, Chemistry Information Center for Emergencies, can be reached as follows: "
							+ "<b>0-800-222-2933</b> in the Republic of Argentina (24 hours "
							+ "For calls originating elsewhere, call"
							+ "<b>+54-11-4613-1100</b>");

			details.put(
					"BRAZIL",
					"1.<b>PRÓ-QUÍMICA</b> a 24-hour emergency response information service, can be reached as follows:"
							+ "<b>0-800-118270</b> "
							+ "(Toll-free in Brazil) "
							+ "For calls originating elsewhere, call"
							+ "<b>+55-11-232-1144</b> (Collect calls are accepted)");

			details.put(
					"COLOMBIA",
					"1.<b>CISPROQUIM</b> a 24 hour emergency response information service, can be reached as follows:"
							+ "<b>01-800-091-6012</b> in Colombia"
							+ "For calls originating in Bogotá, Colombia call"
							+ "<b>288-6012</b> For calls originating elsewhere call <b>+57-1-288-6012</b>");
		} catch (Exception e) {
			Log.e(Utils.this.getClass().getSimpleName(),
					"Error in emergenty data "
							+ Arrays.toString(e.getStackTrace()));
		}
		return details.get(countryName);
	}

    public String[] getMenuItemsForNavigationDrawer() {
        final String[] listItems = { Utils.getResString(R.string.Menu_change_locale),
                Utils.getResString(R.string.Menu_Show_Orders),  Utils.getResString(R.string.Menu_History),
				Utils.getResString(R.string.Menu_Items_Quit) };
        return listItems;
    }

	public String[] getLanguagesList() {
		final String[] languages = {"English", "Spanish", "French"};
		return languages;
	}
//	public String returnNonExcempt(final String unNumber,final Context context) {
////		final String name,final String secondary_placard,final String pkgGroup,
////		final String specialProvision
//		unInfo(unNumber,context);
//		String nonexcempt = "0";
//		if(nonexcempt.equalsIgnoreCase("0")){
//			switch (name){
//				case  "5.2":
//					if(name.equals("TYPE B")){//TODO un number description contains
//						nonexcempt = "1";
//					} else{
//						nonexcempt = "0";
//					}
//					break;
//				case  "6.1":
//					if(specialProvision.equals("23")){
//						nonexcempt = "1";
//					}else{
//						nonexcempt = "0";
//					}
//					break;
//				case  "7.3":
//					nonexcempt = "1";
//					break;
//				default :
//					nonexcempt = "0";
//					switch (secondary_placard){
//						//       case  '4.3':
//						//         nonexempt = "1";
//						//        break;
//						case  "6.1":
//							if(pkgGroup.equals("I")){
//								nonexcempt = "1";
//							}else{
//								nonexcempt = "0";
//							}
//							break;
//						default :
//							nonexcempt = "0";
//					}
//			}
//		}
//		return nonexcempt;
//	}

	/**
	 * This method is to convert weight from Kgs to Lbs
	 * @param grossWeight
	 * @return
	 */
	public double convertWeightToKgs(final double grossWeight) {
		double convertedWeight = 0.0;
		convertedWeight = (grossWeight / 2.2);
		resrictDecimals(convertedWeight);
		return convertedWeight;
	}

	@Override
	public Object getInstance() {
		return null;
	}
	public static JSONObject getRulesDataUrl(final Context context) {
		JSONObject inputJsonObject = null;
		try {
			GetDBData getDBData = new GetDBData(context);
			JSONObject inputToRulesData = new JSONObject();
			inputToRulesData.put("url",getDBData.getConfigData().get(COL_URL));
			inputToRulesData.put("lastsyncdate","");
			inputToRulesData.put("imeinumber",getDBData.getConfigData().get(COL_IMEI));
			inputToRulesData.put("license_session",getDBData.getConfigData().get(COL_LICENSE_SESSION));
			inputToRulesData.put("license_key",getDBData.getConfigData().get(COL_LICENSE_KEY));
			inputToRulesData.put("userName",getDBData.getConfigData().get(COL_USER_NAME));
			inputJsonObject = new JSONObject("{\"Data\":["+inputToRulesData .toString()+"]}");
			//this two need separately to access, so that parsing not required
			inputJsonObject.put("serverUrl",getDBData.getConfigData().get(COL_URL));
			inputJsonObject.put("userName",getDBData.getConfigData().get(COL_USER_NAME));

			//Log.v("jsoncheck",inputJsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputJsonObject;
	}







	public static  String getResString(final int id) {
		return HomeActivity.getContext().getResources().getString(id);
	}


	public static  String getResString(final Context context, final int id) {
		return context.getResources().getString(id);
	}


//1 minute = 60 seconds
	//1 hour = 60 x 60 = 3600
	//1 day = 3600 x 24 = 86400

	/**
	 * Return difference in days
	 * @param startDate
	 * @param endDate
	 * @return days diff
	 */
	public long dateDifference(Date startDate, Date endDate){

		//milliseconds
		long different = endDate.getTime() - startDate.getTime();

//		System.out.println("startDate : " + startDate);
//		System.out.println("endDate : "+ endDate);
//		System.out.println("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;
//
//		System.out.printf(
//				"%d days, %d hours, %d minutes, %d seconds%n",
//				elapsedDays,
//				elapsedHours, elapsedMinutes, elapsedSeconds);
		return elapsedDays;

	}


	/**
	 * To get final placarding strings
	 * 0 means non optimise
	 * 1 means semi optimise
	 * 2 means optimise
	 * @param finalValue
	 * @param type
	 * @return
	 */
	public String getPlacardingType(final String finalValue, final int type) {
		String imageString = "";
		if (finalValue.equals("")) {
			imageString = "No placard Added";
		} else {
			//Log.v(TAG,"final value for optimize " + finalValue);
			imageString = finalValue.split("@@@")[type];
			//Log.v(TAG, "optimize ===> " + imageString);
		}
		return imageString;
	}















	public static JSONArray getPsn(final String input) {
		String data =	input.replaceAll(" ", " ");
		String search = " or ";
		JSONArray splitResponse = new JSONArray();
		if(data.contains("@")) {
			String [] outerSplit = data.split("@");
			for(int i = 0; i<outerSplit.length; i++){
				String saparatePsn[] = outerSplit[i].split("#");
				if(saparatePsn[0].contains(search)){
					String [] innerSplit = saparatePsn[0].split(search);
					for(int j =0; j< innerSplit.length; j++){
						splitResponse.put(innerSplit[j]+"#"+(saparatePsn.length == 2?saparatePsn[1]:""));
					}
				}else{
					splitResponse.put(saparatePsn[0]+"#"+(saparatePsn.length == 2?saparatePsn[1]:""));
				}
			}
		} else if(data.split("#")[0].contains(search)) {
			String saparatePsn[] = data.split("#");
			String [] innerSplit = saparatePsn[0].split(search);
			for(int k = 0; k< innerSplit.length; k++){
				splitResponse.put(innerSplit[k]+"#"+(saparatePsn.length == 2?saparatePsn[1]:""));
			}
		} else {
			splitResponse.put(data);
		}
		return splitResponse;
	}








}
