/********************************************************
 * Ideabytes Software India Pvt Ltd.                              
 * 50 Jayabheri Enclave, Gachibowli, HYD                          
 * Created Date : 01/01/15                                      
 * Created By : Prashanth B                       
 * Vision : IB Innovation                                         
 * Modified by : Haritha Rekapalli, Lokesh    Date : 12/01/15    Version : V1  
 * Description : To add transactions and display placards from/to DB.
 ********************************************************/

package org.com.ca.dgsms.ca.model;

import android.content.Context;

import com.ideabytes.dgsms.ca.HomeActivity;

import org.com.ca.ca.exceptions.DGSMSNestableException;
import org.json.JSONArray;
import org.json.JSONObject;

public class UnInfo implements DBConstants {

	private static final String TAG = "UnInfo";

	public static Context context = null;

	public UnInfo() {

	}

	public UnInfo(Context context) {
		UnInfo.context = context;
	}

	public static Context getDatabaseContext() {
		return HomeActivity.getContext();
	}

	private static final String UNCLASS_ID = "unclass_id";
	HandleValidateUnNumber handleValidateUnNumber = new HandleValidateUnNumber();

	public String validateUnNumber(String unNumber) {
		DBConnection dBConnection = new DBConnection(UnInfo.context);

		try {

			if (null != unNumber) {
				int excludedCount = (int) handleValidateUnNumber
						.getExcludedCountOfUnNumber(unNumber);

				// dBConnection.displayLog(TAG,"Excluded Count: ::validateUnNumber::"
				// + excludedCount);

				if (excludedCount > 0) {
					dBConnection.displayLog(TAG, "UN Number Not allowed");
					unNumber = "\"UN Number Not allowed\"";
					return unNumber;
				} else {
					String dataofuninfo = handleValidateUnNumber
							.getUNNumberClassId(unNumber);
					JSONObject jobject0 = new JSONObject(dataofuninfo);
					JSONArray jArray0 = jobject0.getJSONArray("Data");
					int unInfo = 0;
					for (int i = 0; i < jArray0.length(); i++) {
						JSONObject jsonObjectinDataArray = jArray0
								.getJSONObject(i);
						unInfo = jsonObjectinDataArray.getInt(UNCLASS_ID);
					}
					if (unInfo > 0) {
						if (unInfo == 1) {
							unNumber = "\"Forbidden UN Number\"";
							dBConnection.displayLog(TAG, "Forbidden UN Number");
							return unNumber;
						} else {
							String unnumber_detail = handleValidateUnNumber
									.getUnNumberLookupDetails(unNumber);
							return unnumber_detail;
						}
					} else {
						unNumber = "\"Invalid UN Number\"";
						return unNumber;
					}
				}
			} else {
				unNumber = "\"Invalid UN Number\"";
				return unNumber;
			}
		} catch (DGSMSNestableException e) {
			e.printStackTrace();
			dBConnection.displayLog(TAG,
					"Exception caught while getting unnumber information : "
							+ " message: " + e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			dBConnection.displayLog(TAG,
					"Exception caught while getting UN Number: " + " message: "
							+ e.getMessage());
		} finally {
			dBConnection.closeConnections();
		}
		return unNumber;
	}

	/**
	 * @author Suman
	 * @param User_id
	 * @param placard_holders
	 * @param transaction_id
	 * @return query
	 */
	public String buildQuery(String User_id, int placard_holders,
			String transaction_id) {
		String selectTableSQL = "SELECT * FROM (SELECT * FROM (SELECT "+COL_UN_NUMBER+",SUM("+COL_WEIGHT_IN_KGS+") as cw," 
				+ COL_ERAP_NO+","+COL_SUBSIDARY_EXIST+","+COL_UNNUMBER_DISPLAY+","+COL_DANGEROUS_PLACARD+","+COL_PRIMARY_PLACARD+","+COL_SECONDARY_PLACARD+","
				+ COL_WEIGHT_INDEX+" as wl,CASE WHEN((sum("+COL_WEIGHT_IN_KGS+") > "
				+ COL_WEIGHT_INDEX+") AND ("+COL_PRIMARY_PLACARD+" != \"\")) THEN 'placard required' ELSE 'placard not required' END as placard, id as iddd FROM "+TABLE_TRANSACTIONS+" WHERE "+COL_USER_ID+" = "
				+ "\""+User_id+ "\""
				+ " AND "+COL_TRANSACTION_STATUS+" = 0 AND "+COL_TRANSACTION_ID+" = "
				+ "\""+transaction_id+ "\""
				+ " GROUP BY "+COL_PRIMARY_PLACARD+" ) ibc_sc WHERE iddd not in (SELECT "+COL_UN_NUMBER+" FROM "+TABLE_TRANSACTIONS+" WHERE "+COL_USER_ID+" = "
				+ "\""+User_id+ "\""
				+ " AND "+COL_TRANSACTION_STATUS+" = 0 AND "+COL_TRANSACTION_ID+" = "
				+ "\""+transaction_id+ "\""
				+ " AND "+COL_IBC_STATUS+" = 1) union SELECT "+COL_UN_NUMBER+",SUM("+COL_WEIGHT_IN_KGS+") as cw,"+COL_ERAP_NO+","+COL_SUBSIDARY_EXIST+","+COL_UNNUMBER_DISPLAY+","
				+ COL_DANGEROUS_PLACARD+","+COL_PRIMARY_PLACARD+","+COL_SECONDARY_PLACARD+","+COL_WEIGHT_INDEX+" as wl,CASE WHEN((sum("+COL_WEIGHT_IN_KGS+") > "+COL_WEIGHT_INDEX+") AND ("
				+ COL_PRIMARY_PLACARD+" != \"\")) THEN 'placard required' ELSE 'placard not required'  END as placard, id as iddd FROM "+TABLE_TRANSACTIONS+" WHERE "+COL_USER_ID+" = "
				+ "\""+User_id+ "\""
				+ " AND "+COL_TRANSACTION_STATUS+" = 0 AND "+COL_TRANSACTION_ID+" = "
				+ "\""+transaction_id+ "\""
				+ " AND "+COL_IBC_STATUS+" = 1 GROUP BY "+COL_PRIMARY_PLACARD+" ,"+COL_WEIGHT_INDEX+") tr WHERE "+COL_UN_NUMBER+" != ''";
		return selectTableSQL;
	}// buildQuery()

	/**
	 * @author Suman
	 * @param primary_placard
	 * @param User_id
	 * @param transaction_id
	 * @return query
	 */
	public String query__pp_level(String primary_placard,
			String User_id, String transaction_id) {
		String query__pp_level = "select * from " + TABLE_TRANSACTIONS
				+ " where " + COL_TRANSACTION_STATUS + " = 0 and "
				+ COL_PRIMARY_PLACARD + " =  \'" + primary_placard + "\' and "
				+ COL_USER_ID + " =" + "\""+User_id+ "\""+ " and " + COL_TRANSACTION_ID
				+ "=" + "\""+transaction_id+ "\"";
		return query__pp_level;
	}// query__pp_level()

	/**
	 * @author Suman
	 * @param PP1
	 * @param User_id
	 * @param un_number_local
	 * @param transaction_id
	 * @return query
	 */
	public String selectquery_e_l(String PP1, String User_id,
			String un_number_local, String transaction_id) {
		String selectquery_e_l = "select "
				+ IF_CONDITION
				+ " count(*) > 0 then 'true' else 'false' end as erap_count from "
				+ TABLE_TRANSACTIONS + " where " + COL_PRIMARY_PLACARD
				+ " = \"" + PP1 + "\"" + " and " + COL_USER_ID + " = \""
				+ User_id + "\"" + "and  " + COL_UN_NUMBER + " = "
				+"\""+ un_number_local+ "\""+ " and " + COL_TRANSACTION_ID + " = "
				+ "\""+transaction_id+ "\"" + " and " + COL_TRANSACTION_STATUS + " = 0 and "+COL_UN_CLASS_ID+" != 61 and (" + COL_ERAP_NO + " != '' or "
				+ COL_UNNUMBER_DISPLAY + " = 1)";

		return selectquery_e_l;

	}// selectquery_e_l()

	/**
	 * @author Suman
	 * @param user_id
	 * @param primary_placard
	 * @param un_number
	 * @param transaction_id
	 * @return query
	 */
	public String insertTransactionSqlSelect(String user_id,
			String primary_placard, String un_number, String transaction_id) {
		String sqlSelect = "select case when (sum("+COL_WEIGHT_IN_KGS+") / sum("+COL_NO_OF_UNITS+")) > 450 then 'true' else 'false' end as lflag from "+TABLE_TRANSACTIONS+" where "+COL_TRANSACTION_STATUS+" = 0 and " +
				COL_USER_ID+" = "
				+ "\""+user_id+ "\""
				+ " and "+COL_PRIMARY_PLACARD+" = "
				+ "\""
				+ primary_placard
				+ "\""
				+ " and "+COL_UN_NUMBER+" = "
				+ "\""+un_number+"\""
				+ " and "+COL_TRANSACTION_ID+" = " + "\"" + transaction_id + "\"";

		return sqlSelect;
	}// insertTransactionSqlSelect()

	/**
	 * @author Suman
	 * @param User_id
	 * @param PP1
	 * @param transaction_id
	 * @param un_number_local
	 * @return query
	 */
	public String sqlSelect(String User_id, String PP1,
			String transaction_id, String un_number_local) {
		String sqlSelect = "select " + IF_CONDITION + "(sum("
				+ COL_WEIGHT_IN_KGS + ") / sum(" + COL_NO_OF_UNITS
				+ ")) > 450 then 'true' else 'false' end as lflag from "
				+ TABLE_TRANSACTIONS + " where " + COL_TRANSACTION_STATUS
				+ " = 0 and " + COL_USER_ID + " = " +"\"" + User_id +"\""+ " and "
				+ COL_PRIMARY_PLACARD + " = " + "\"" + PP1 + "\"" + " and  "
				+ COL_TRANSACTION_ID + " = " + "\"" + transaction_id + "\""
				+ " and " + COL_UN_NUMBER + " in (" +"'"+ un_number_local+"'" +")";

		return sqlSelect;
	}// sqlSelect()

	/**
	 * This method is for 2.2 and 2.4, when un numbers matches '1072',  '1073',  '3156',  '3157' and bulk is 
	 * "yes: then display 2.4, if bulk is "no" then display 2.2
	 * 
	 * @author suman
	 * @param User_id
	 * @param transaction_id
	 * @return select query
	 */
	public String checkGreenPlacard(String User_id,String transaction_id) {
		String checkGreenPlacard = "select * from "+TABLE_TRANSACTIONS+"  WHERE  "+COL_UN_NUMBER+" IN ('1072',  '1073',  '3156',  '3157') AND " +
				""+COL_IBC_STATUS+" = 1 AND  "+COL_TRANSACTION_STATUS+" = 0 AND  "+COL_TRANSACTION_ID+" =  "

			+ "\""+ transaction_id + "\""+ " AND "+COL_USER_ID+" = "+ "\"" + User_id+"\"";

		return checkGreenPlacard;
	}

	@Override
	public Object getInstance() {
		return null;
	}
}