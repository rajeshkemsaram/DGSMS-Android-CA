package org.com.ca.dgsms.ca.model;

import com.ideabytes.dgsms.ca.HomeActivity;

import org.com.ca.ca.exceptions.DGSMSNestableException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : HandleValidateUnNumber
 * author:  Suman
 * Description : this class is to get un number details from databse on un number tab out
 * Created Date : 23-11-2015
 * Modified Date : 24-11-2015
 ****************************************************************/
public class HandleValidateUnNumber implements DBConstants {
	private static int dbCodeRead = 1;
	private static int dbCodeInsert = 0;
	DatabaseConf databaseConf = new DatabaseConf(UnInfo.getDatabaseContext());

	/**
	 * Description : To get Excluded Count Of UnNumber
	 * 
	 * @param unNumber
	 * @return String
	 */
	public int getExcludedCountOfUnNumber(String unNumber) {
		DBConnection dBConnection = new DBConnection(HomeActivity.getContext());
		int excludedCount = 0;
		try {
			String selectTableSQL = "SELECT count(*) as count FROM "
					+ DBConstants.TABLE_EXCLUDED_UNNUMBERS + " WHERE "
					+ COL_UNNUMBER_EXCLUDED + " = " + unNumber + "";
			// dBConnection.displayLog("selectTableSQL: " +
			// selectTableSQL,"debug");
			dBConnection.resultSet = dBConnection.getResultSetForSelect(
					selectTableSQL, dbCodeRead);

			while (dBConnection.hasNextElement(dBConnection.resultSet)) {
				int i = dBConnection.getInt(dBConnection.resultSet, "count");
				excludedCount = i;
			}
		} catch (Exception se) {
			se.printStackTrace();
			dBConnection.displayLog(
					"Exception caught while executing validate exclude UN Number query"
							+ " message: " + se.getMessage(), "error");
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
					"Exception caught while executing validate exclude UN Number query");
		} finally {
			try {
				dBConnection.closeConnections();
			} catch (Exception e) {
				e.printStackTrace();
				dBConnection.displayLog(
						"Exception caught while closing the connection"
								+ " message: " + e.getMessage(), "error");
				throw new DGSMSNestableException(
						DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
						"Exception caught while closing the connection");
			}
		}
		return excludedCount;
	}

	/**
	 * Description : To get UnNumber Lookup Details
	 * 
	 * @param unNumber
	 * @return String
	 */
	public String getUnNumberLookupDetails(String unNumber) {
		DBConnection dBConnection = new DBConnection(HomeActivity.getContext());
		String result = null;
		try {
			String SqlSelectQuery = "select un." + COL_ID + ",cl." + COL_NAME
					+ ",un." + COL_UN_CLASS_ID +",un."+COL_GROUP_NAME+",un."+COL_SHIPPING_NAME +",cl." + COL_DANGEROUS_PLACARD
					+ ",cl." + COL_PRIMARY_PLACARD + ",un." + COL_DESCRIPTION
					+ ",un." + COL_UNTYPE + ",un."
					+ COL_UNNUMBER_DISPLAY_STATUS + ",cl."
					+ COL_SECONDARY_PLACARD + ",cl." + COL_ROAD_WEIGHT_GROUP
					+ ",cl." + COL_SPECIAL_PROVISION
					+ ",cl." + COL_NONEXCEMPT
					+ " from " + TABLE_UNNUMBER_INFO + " as un JOIN "
					+ TABLE_UNCLASS + " as cl ON cl." + COL_ID + " = un ."
					+ COL_UN_CLASS_ID + " WHERE un." + COL_UN_NUMBER + " ="
					+ "\"" + unNumber + "\"";
			// dBConnection.displayLog("selectTableSQL: " +
			// SqlSelectQuery,"debug");
			dBConnection.resultSet = dBConnection.getResultSetForSelect(
					SqlSelectQuery, dbCodeRead);
			JSONArray jArray1 = new JSONArray();
			while (dBConnection.hasNextElement(dBConnection.resultSet)) {
				JSONObject jObject = new JSONObject();
				String untype = dBConnection.getString(dBConnection.resultSet,
						COL_UNTYPE);
				String description = dBConnection.getString(
						dBConnection.resultSet, COL_DESCRIPTION);
				int unclass_id = dBConnection.getInt(dBConnection.resultSet,
						COL_UN_CLASS_ID);
				int unnumber_display_status = dBConnection.getInt(
						dBConnection.resultSet, COL_UNNUMBER_DISPLAY_STATUS);
				String primary_placard = dBConnection.getString(
						dBConnection.resultSet, COL_PRIMARY_PLACARD);
				String group_name = dBConnection.getString(
						dBConnection.resultSet, COL_GROUP_NAME);
				String secondary_placard = dBConnection.getString(
						dBConnection.resultSet, COL_SECONDARY_PLACARD);
				String dangerous_placard = dBConnection.getString(
						dBConnection.resultSet, COL_DANGEROUS_PLACARD);
				String name = dBConnection.getString(dBConnection.resultSet,
						COL_NAME);

				String shipping_name = dBConnection.getString(dBConnection.resultSet,
						COL_SHIPPING_NAME);
				int specialProvision = dBConnection.getInt(dBConnection.resultSet, "special_provision");
				int nonexempt = dBConnection.getInt(dBConnection.resultSet, COL_NONEXCEMPT);
				jObject.put("unnumberdesc_id",
						dBConnection.getInt(dBConnection.resultSet, COL_ID));
				jObject.put("description", description);
				Object erapdetails = databaseConf
						.getErapDetailsForWeb(dBConnection.getInt(
								dBConnection.resultSet, COL_ID));
				jObject.put("erapdetails", erapdetails);
				jObject.put("untype", untype);
				jObject.put("unclass_id", unclass_id);
				jObject.put("specialProvision", specialProvision);
				jObject.put(COL_NONEXCEMPT, nonexempt);
				if(!shipping_name.equals("")){
				    jObject.put("shipping_name", shipping_name);
				    } else if(shipping_name.equals("")) {
				     jObject.put("shipping_name", description);
				    }
				 if(!group_name.equals("")){
				     jObject.put("group_name", group_name);
				    } else{
				    jObject.put("group_name", "");
				    }
				jObject.put("unnumber_display_status", unnumber_display_status);
				jObject.put("primary_placard", primary_placard);
				jObject.put("secondary_placard", secondary_placard);
				jObject.put("name", name);
				jObject.put("dangerous_placard", dangerous_placard);
				String weight_index = getWeightIndex(dBConnection.getInt(
						dBConnection.resultSet, "road_weight_group"));
				jObject.put("weight_index", weight_index);
				jArray1.put(jObject);
			}
			result = jArray1.toString();
		} catch (JSONException jexc) {
			jexc.printStackTrace();
			dBConnection.displayLog(
					"JSON Exception caught while executing getUNNumberInfo query"
							+ " message: " + jexc.getMessage(), "error");
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
					"JSON Exception caught while executing getUNNumberInfo query");
		} catch (Exception se) {
			se.printStackTrace();
			dBConnection.displayLog(
					"Exception caught while executing validate exclude UN Number query"
							+ " message: " + se.getMessage(), "error");
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
					"Exception caught while executing validate exclude UN Number query");
		} finally {
			try {
				dBConnection.closeConnections();
			} catch (Exception e) {
				e.printStackTrace();
				dBConnection.displayLog(
						"Exception caught while closing the connection"
								+ " message: " + e.getMessage(), "error");
				throw new DGSMSNestableException(
						DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
						"Exception caught while closing the connection");
			}
		}
		return result;
	}

	/**
	 * Description : To get the class id of UN number
	 * 
	 * @param unNumber
	 * @return String
	 */
	public String getUNNumberClassId(String unNumber) {
		DBConnection dBConnection = new DBConnection(HomeActivity.getContext());
		String excludedCount = null;
		String data = null;
		try {
			String selectTableSQL = "SELECT " + COL_UNTYPE + ","
					+ COL_UN_CLASS_ID + "," + COL_UNNUMBER_DISPLAY_STATUS
					+ " FROM " + DBConstants.TABLE_UNNUMBER_INFO
					+ " WHERE (status = 0 OR status = 1) AND " + COL_UN_NUMBER
					+ " = " + "\"" + unNumber + "\"" + "";

			// dBConnection.displayLog("selectTableSQL: " +
			// selectTableSQL,"debug");
			dBConnection.resultSet = dBConnection.getResultSetForSelect(
					selectTableSQL, dbCodeRead);
			JSONArray jarray = new JSONArray();
			while (dBConnection.hasNextElement(dBConnection.resultSet)) {
				JSONObject jobject = new JSONObject();
				int unclass_id = dBConnection.getInt(dBConnection.resultSet,
						COL_UN_CLASS_ID);
				int unnumber_display_status = dBConnection.getInt(
						dBConnection.resultSet, COL_UNNUMBER_DISPLAY_STATUS);
				String unType = dBConnection.getString(dBConnection.resultSet,
						COL_UNTYPE);
				jobject.put("unType", unType);
				jobject.put("unclass_id", unclass_id);
				jobject.put("unnumber_display_status", unnumber_display_status);
				jarray.put(jobject);
			}
			excludedCount = jarray.toString();
			data = "{Data:" + excludedCount + "}";
		} catch (JSONException jexc) {
			jexc.printStackTrace();
			dBConnection.displayLog(
					"JSON Exception caught while executing getUNNumberInfo query"
							+ " message: " + jexc.getMessage(), "error");
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
					"JSON Exception caught while executing getUNNumberInfo query");
		} catch (Exception se) {
			se.printStackTrace();
			dBConnection.displayLog(
					"SQL Exception caught while executing getUNNumberInfo query"
							+ " message: " + se.getMessage(), "error");
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
					"SQL Exception caught while executing getUNNumberInfo query");
		} finally {
			try {
				dBConnection.closeConnections();
			} catch (Exception e) {
				e.printStackTrace();
				dBConnection.displayLog(
						"Exception caught while closing the connection"
								+ " message: " + e.getMessage(), "error");
				throw new DGSMSNestableException(
						DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
						"Exception caught while closing the connection");
			}
		}
		return data;
	}

	/**
	 * Description : Returns weight index based on 'road weight group'
	 * 
	 * @param weightId
	 * @return String
	 */
	public String getWeightIndex(int weightId) {
		DBConnection dBConnection = new DBConnection(HomeActivity.getContext());
		String weightIndex = null;
		try {
			String selectTableSQL = "select w.name as weight_index from "
					+ DBConstants.TABLE_WEIGHT + " as w WHERE w.id = "
					+ weightId + "";

			dBConnection.resultSet = dBConnection.getResultSetForSelect(
					selectTableSQL, dbCodeRead);
			while (dBConnection.hasNextElement(dBConnection.resultSet)) {
				String weight_index = dBConnection.getString(
						dBConnection.resultSet, "weight_index");
				weightIndex = weight_index;
			}
		} catch (Exception se) {
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_AUTHENTICATION_ERROR,
					"Exception caught while executing getUnClassInfo query");
		} finally {
			try {
				dBConnection.closeConnections();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return weightIndex;
	}

	@Override
	public Object getInstance() {
		return null;
	}
}