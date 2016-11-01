package org.com.ca.dgsms.ca.model;

import android.database.Cursor;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.com.ca.ca.exceptions.DGSMSNestableException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.utils.Utils;
/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : InsertIntoTransctn
 * author:  Suman
 * Description : this class is to save all transactions into table transaction_details to display placard
 * Created Date : 23-11-2015
 * Modified Date : 18-04-2016
 * Reason: inserting push order status as defualt 0, this will be used in pushind shipments to server
 ****************************************************************/
class Values {
	public String error_code1 = null;
	public String error_msg1 = null;
	public String group_name1 = null;
	public String display_name1 = null;

	public Values(String error_code1, String error_msg1, String group_name1,
				  String display_name1) {
		this.error_code1 = error_code1;
		this.error_msg1 = error_msg1;
		this.group_name1 = group_name1;
		this.display_name1 = display_name1;
	}
}

public class InsertIntoTransctn implements DBConstants {
    private static final String TAG = InsertIntoTransctn.class.getSimpleName();
    private static com.ideabytes.dgsms.ca.logs.Log log = new com.ideabytes.dgsms.ca.logs.Log();
	//to get data need readable database object
	private static int dbCodeRead = 1;
	private static int dbCodeInsert = 0;
	public static Values processTransaction(String array, int placard_holders) {
		DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
		String error_code1 = "00";
		String error_msg1 = "Success";
		String display_groupName = "";
		int length = 0;
		try {
			JSONObject jsnobject = new JSONObject(array);
			JSONArray dataArray = jsnobject.getJSONArray("Data");
			String bl = null;
			String un_number = null;
			String name = null;
			int un_class_id = 0;
			double dg_weight = 0;
			double package_weight = 0;
			double gross_weight = 0;
			int number_of_units = 0;
			int weight_type = 0;
			double weight_in_kgs = 0;
			String erap_no = null;
			int subsidary_exist = 0;
			int unnumber_display = 0;
			int dangerous_placard = 0;
			String pkg_group = null;
			String primary_placard = null;
			String secondary_placard = null;
			double weight_index = 0;
			String erap_index = null;
			String user_id = "";
			int optimise = 0;
			int consignee_danger = 0;
			String ibc_status = "";
			String ibc_residue_status = "";
			String transaction_id = "";
			String description = "";
			String untype = "";
			String nos = "";
			String date_time="";
			String group_name = "";
			int nonexempt = 0;
			int un_style = 0;
			int special_provision = 0;
			// String tralier_number = null;
			// String inserted_date_time = null;
			Log.v(" rja value : ", array);
			for (int j = 0; j < dataArray.length(); j++) {
				JSONObject jsonObjectinDataArray = dataArray.getJSONObject(j);
				bl = jsonObjectinDataArray.getString("bl");
				un_number = jsonObjectinDataArray.getString("un_number");
				un_class_id = jsonObjectinDataArray.getInt("un_class_id");
				dg_weight = jsonObjectinDataArray.getDouble("dg_weight");
				package_weight = jsonObjectinDataArray
						.getDouble("package_weight");
				special_provision = jsonObjectinDataArray
						.getInt("special_provision");
				gross_weight = jsonObjectinDataArray.getDouble("gross_weight");
				number_of_units = jsonObjectinDataArray
						.getInt("number_of_units");
				weight_type = jsonObjectinDataArray.getInt("weight_type");
				un_style = jsonObjectinDataArray.getInt("un_style");
				name = jsonObjectinDataArray.getString("name");
				weight_in_kgs = jsonObjectinDataArray
						.getDouble("weight_in_kgs");
				optimise = jsonObjectinDataArray.getInt("optimise");
				nonexempt = jsonObjectinDataArray.getInt("non_exempt");
				// New variable from font end to update the value of danger when
				// the weight greater than 1000 kgs and same conginor
				consignee_danger = jsonObjectinDataArray
						.getInt("consignee_danger");
				erap_no = jsonObjectinDataArray.getString("erap_no");
				subsidary_exist = jsonObjectinDataArray
						.getInt("subsidary_exist");
				unnumber_display = jsonObjectinDataArray
						.getInt("unnumber_display");
				ibc_status = jsonObjectinDataArray.getString("ibc_status");
				ibc_residue_status = jsonObjectinDataArray
						.getString("ibc_residue_status");
				untype = jsonObjectinDataArray.getString("untype");
				pkg_group = jsonObjectinDataArray.getString("pkg_group");
				dangerous_placard = jsonObjectinDataArray
						.getInt("dangerous_placard");
				// Object primary_placard1 =
				// jsonObjectinDataArray.get("primary_placard");
				// primary_placard = String.valueOf(primary_placard1);
				primary_placard = jsonObjectinDataArray
						.getString("primary_placard");
				//System.out.println("suman primary_placard" + primary_placard);
				// Object secondary_placard1 =
				// jsonObjectinDataArray.get("secondary_placard");
				secondary_placard = jsonObjectinDataArray
						.getString("secondary_placard");
				group_name = jsonObjectinDataArray.getString("group_name");
				//System.out.println("GROUP_NAME" + group_name);
				transaction_id = jsonObjectinDataArray
						.getString(COL_TRANSACTION_ID);
				description = jsonObjectinDataArray.getString("description");
				// inserted_date_time =
				// jsonObjectinDataArray.getString("inserted_date_time");
				erap_index = jsonObjectinDataArray.getString(COL_ERAP_INDEX);
				nos = jsonObjectinDataArray.getString(COL_NOS);
//				nos = "";
				date_time=jsonObjectinDataArray.getString(COL_INSERTED_DATE_TIME);
				String weight_indexString = jsonObjectinDataArray
						.getString("weight_index");
				if (weight_indexString.equals(""))
					weight_index = 0;
				else
					weight_index = Double.parseDouble(weight_indexString);
				if ((!erap_index.equals(""))) {
					double temp = Double.parseDouble(erap_index);
					if (weight_index > temp) {
						weight_index = temp - 0.1;
					}
				}
				user_id = jsonObjectinDataArray.getString(COL_USER_ID);
			}
			int record_count = 0;
			int dangerous_placard_local = 0;
			dangerous_placard_local = dangerous_placard;
			if (primary_placard.startsWith("1")) {
				// Change in query to disallow class 1 to be mixed with
				// class 7
				String query1 = "select count(*) from "
						+ DBConstants.TABLE_TRANSACTIONS
						+ " where primary_placard like '7%'"
						+ " and user_id = " + "\"" + user_id + "\""
						+ " and transaction_id = " + "\"" + transaction_id
						+ "\""+" and "+COL_TRANSACTION_STATUS+" = 0";
				resultSet = dbConnection.getResultSetForSelect(query1,dbCodeRead);
				while (dbConnection.hasNextElement(resultSet)) {
					record_count = dbConnection.getInt(resultSet,"count(*)");
				}

				if (record_count > 0) {
					resultSet.close();
					return new Values("99",
							"Class 1 and Class 7 cannot be mixed together.",
							null, null);
				}
			} else if (group_name.equals("S")) {

			} else if (primary_placard.startsWith("7")) {
				// Change in query to disallow class 7 to be mixed with
				// class 1
				String query2 = "select count(*) from "
						+ DBConstants.TABLE_TRANSACTIONS
						+ " where primary_placard like '1%'"
						+ " and user_id = " + "\"" + user_id + "\""
						+ " and transaction_id = " + "\"" + transaction_id
						+ "\""+" and "+COL_TRANSACTION_STATUS+" = 0";

				resultSet = dbConnection.getResultSetForSelect(query2,dbCodeRead);
				while (dbConnection.hasNextElement(resultSet)) {
					record_count = dbConnection.getInt(resultSet,"count(*)");
				}
				resultSet.close();
				if (record_count > 0) {
					resultSet.close();
					return new Values("99",
							"Class 1 and Class 7 cannot be mixed together.",
							null, null);
				}
			}
			// Code for mutual exclusion in class 1 based on groups
			String selectGroupQuery = "select group_name,un_number,name from "
					+ DBConstants.TABLE_TRANSACTIONS + " where user_id = "
					+ "\"" + user_id + "\"" + " and transaction_id = " + "\""
					+ transaction_id + "\""+" and "+COL_TRANSACTION_STATUS+" = 0";
			resultSet = dbConnection.getResultSetForSelect(selectGroupQuery,dbCodeRead);
			JSONArray groupNames = new JSONArray();
			String group_name_rs = null;
			JSONArray unnumberForGroupJArray = new JSONArray();
			String unnumberforgroup = null;
			JSONArray nameForSJArray = new JSONArray();
			String nameForS = null;
			while (dbConnection.hasNextElement(resultSet)) {
				group_name_rs = dbConnection.getString(resultSet,"group_name");
				unnumberforgroup = dbConnection.getString(resultSet,"un_number");
				nameForS = dbConnection.getString(resultSet,"name");
				unnumberForGroupJArray.put(unnumberforgroup);
				nameForSJArray.put(nameForS);
				groupNames.put(group_name_rs);
			}
			resultSet.close();
			String unnumberForGroupJArrayinString = unnumberForGroupJArray
					.toString();
			String groupNamesinString = groupNames.toString();
			String nameForSJArrayinString = nameForSJArray.toString();

			length = groupNamesinString.length();
            if (groupNamesinString.length() > 2) {
                if (un_number.equals("0333")
                        || un_number.equals("0334")
                        || un_number.equals("0335")
                        || un_number.equals("0336")) {
                    if (groupNamesinString.contains("A")) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    } else if ((groupNamesinString.contains("B"))
                            || (groupNamesinString.contains("C"))
                            || (groupNamesinString.contains("D"))
                            || (groupNamesinString.contains("E"))
                            || (groupNamesinString.contains("F"))
                            || (groupNamesinString.contains("H"))
                            || (groupNamesinString.contains("J"))
                            || (groupNamesinString.contains("K"))
                            || (groupNamesinString.contains("L") || (groupNamesinString
                            .contains("N")))) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    } else {
                        display_groupName = group_name;
                    }
                } else if (group_name.equals("C") || group_name.equals("D")
                        || group_name.equals("E") || group_name.equals("N")) {
                    log.debug(TAG,"For class C");
                    if (unnumberForGroupJArrayinString.contains("0333")
                            || unnumberForGroupJArrayinString
                            .contains("0334")
                            || unnumberForGroupJArrayinString
                            .contains("0335")
                            || unnumberForGroupJArrayinString
                            .contains("0336")) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    } else {
                        String result = displayGroupNameCalculation(
                                group_name, groupNamesinString.substring(1,
                                        groupNamesinString.length() - 1));
                        log.debug(TAG,"group name result ***** : " + result);
                        if (result.contains("X")) {
                            return new Values(
                                    "99",
                                    "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                    null, null);
                        } else {
                            display_groupName = group_name;
                        }
                    }
                }  else if(group_name.equals("L")) {
                    //added code for  compatibility group L shall only be carried on the same transport vehicle with an identical explosive.
                    if (!unnumberForGroupJArrayinString.contains(un_number)
                            && groupNamesinString.contains("L")) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    }
                    String result = displayGroupNameCalculation(group_name,
                            groupNamesinString.substring(1,
                                    groupNamesinString.length() - 1));
                    log.debug(TAG,"group name result ***** : " + result);
                    if (result.contains("X")) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    } else {
                        display_groupName = group_name;
                    }
                } else if (!group_name.equals("")) {

                    log.debug(TAG,"For class B");
                    String result = displayGroupNameCalculation(group_name,
                            groupNamesinString.substring(1,
                                    groupNamesinString.length() - 1));
                    log.debug(TAG,"group name result ***** : " + result);
                    if (result.contains("X")) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    } else {
                        display_groupName = group_name;
                    }
                }else{
                    if (!unnumberForGroupJArrayinString.contains(un_number)
                            && groupNamesinString.contains("L")) {
                        return new Values(
                                "99",
                                "Cannot Accept Load. Compatibility Group Letter Doesn't Match",
                                null, null);
                    }
                }
            }

            //by default push shipment status insert as 0
			String query3 = "INSERT INTO transaction_details ( bl,un_number,un_class_id,dg_weight,package_weight,pkg_group,gross_weight,number_of_units,weight_type,weight_in_kgs,erap_no,group_name,name,subsidary_exist,unnumber_display,dangerous_placard_original,dangerous_placard,primary_placard,secondary_placard,weight_index,untype,ibc_status,ibc_residue_status,user_id,transaction_id,description,max_placard,optimise,non_exempt,consignee_danger,un_style,special_provision,"+COL_TRANSACTION_STATUS+","+COL_PUSH_STATUS+","+COL_NOS+","+COL_INSERTED_DATE_TIME+") VALUES ("
					+ "\""
					+ bl
					+ "\""
					+ ","
					+ "\""
					+ un_number
					+ "\""
					+ ","
					+ un_class_id
					+ ","
					+ dg_weight
					+ ","
					+ package_weight
					+ ","
					+ "\""
					+ pkg_group
					+ "\""
					+ ","
					+ gross_weight
					+ ","
					+ number_of_units
					+ ","
					+ weight_type
					+ ","
					+ weight_in_kgs
					+ ","
					+ "\""
					+ erap_no
					+ "\""
					+ ","
					+ "\""
					+ group_name
					+ "\""
					+ ","
					+ "\""
					+ name
					+ "\""
					+ ","
					+ subsidary_exist
					+ ","
					+ unnumber_display
					+ ","
					+ dangerous_placard_local
					+ ","
					+ dangerous_placard
					+ ","
					+ "\""
					+ primary_placard
					+ "\""
					+ ","
					+ "\""
					+ secondary_placard
					+ "\""
					+ ","
					+ weight_index
					+ ","
					+ "\""
					+ untype
					+ "\""
					+ ","
					+ ibc_status
					+ ","
					+ ibc_residue_status
					+ ","
					+ "\""
					+ user_id
					+ "\""
					+ ","
					+ "\""
					+ transaction_id
					+ "\""
					+ ","
					+ "\""
					+ description
					+ "\""
					+ ","
					+ placard_holders
					+ ","
					+ optimise
					+ ","
					+ nonexempt
					+ ","
					+ consignee_danger
					+ ","
					+ un_style + "," + special_provision +","+"0"+",0"+ ","+ "\""+nos+ "\""+ ","+ "\""+date_time+ "\")";

			dbConnection.getResultSetForInsertOrUpdate(query3,dbCodeInsert);
			return new Values("00", "", null, null);
		}  catch (Exception je) {
			je.printStackTrace();
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_AUTHENTICATION_ERROR,
					"JSON exception caught while executing getPlayCard query");

		} finally {
			try {
				close(resultSet);
                if (dbConnection != null) {
                    dbConnection.closeConnections();
                }
			} catch (Exception e) {
				e.printStackTrace();
				throw new DGSMSNestableException(
						DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
						"Exception caught while closing the connection");
			}
		}
	}

	public void deleteFromTransaction(String array) {
		DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
		String primary_class = null;
		double totalWeight = 0;
		try {
			JSONObject jsnobject = new JSONObject(array);
			String user_id = jsnobject.getString("user_id");
			String col_id = jsnobject.getString("col_id");
			String transaction_id = jsnobject
					.getString("transaction_id");
			/*
			 * New logic written by prashanth to handle case for deleting the
			 * record whose consignee danger value is 0 and and value of same
			 * class is 1 by deleting the 0 column the total weight is less than
			 * 1001 then the value if consignee danger of existing should be
			 * updated to 0
			 */

			String getClassOfDeletingRecord = "select primary_placard from "
					+ DBConstants.TABLE_TRANSACTIONS + " where id =" + col_id;
            resultSet = dbConnection.getResultSetForSelect(getClassOfDeletingRecord,dbCodeRead);
			while (dbConnection.hasNextElement(resultSet)) {
				primary_class = dbConnection.getString(resultSet,"primary_placard");
			}
            close(resultSet);
			//03-12-2015, in web logic they are deleting, here update transaction_status = 1 ,
			//tp show history of transactions done by user also these transactions used in pushing transactions to mobile web portal
			String deleteRecordFromTransactionDetails = "update "
					+ DBConstants.TABLE_TRANSACTIONS
					+ " set transaction_status = 1 where transaction_status = 0 and id = " + col_id
					+ " and user_id = " + "\""+user_id+"\"";
            dbConnection.getResultSetForInsertOrUpdate(deleteRecordFromTransactionDetails,dbCodeInsert);

			String getTotalWeight = "select sum(weight_in_kgs) from "
					+ DBConstants.TABLE_TRANSACTIONS
					+ " where primary_placard = '" + primary_class
					+ "' and user_id = " +"\""+ user_id +"\""+ " and transaction_id = "
					+ "\""+transaction_id +"\""+" and "+COL_TRANSACTION_STATUS+" = 0";
            resultSet = dbConnection.getResultSetForSelect(getTotalWeight,dbCodeRead);
			while (dbConnection.hasNextElement(resultSet)) {
				totalWeight = dbConnection.getDouble(resultSet,"sum(weight_in_kgs)");
			}
			if (totalWeight < 1001) {
				String updateConsignee = "UPDATE  `transaction_details` SET  `consignee_danger` =  '0' WHERE primary_placard = '"
						+ primary_class
						+ "' and user_id = "
						+ "\""+user_id+"\""
						+ " and transaction_id = " + "\""+transaction_id +"\""+" and "+COL_TRANSACTION_STATUS+" = 0";
				dbConnection.getResultSetForInsertOrUpdate(updateConsignee,dbCodeInsert);
				dbConnection.closeConnections();
			}
			// new logic written by prashanth on 11/1/2015 for simplying placard
			// display logic on delete

		}  catch (Exception e) {
			e.printStackTrace();
			throw new DGSMSNestableException(
					DGSMSNestableException.CODE_AUTHENTICATION_ERROR,
					"Exception caught while executing deleteFromTransaction");

		} finally {
			try {
                close(resultSet);
				if (dbConnection!= null) {
					dbConnection.closeConnections();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new DGSMSNestableException(
						DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
						"Exception caught while closing the connection");
			}
		}
	}
    /***
     * display group name for based on input group name and already inserted in
     * the group names added or not in result contains X means not aceptable
     * otherwise acceptable
     *
     * @param inputGroupName
     * @param databaseGroupNames
     * @return
     */
    public static String displayGroupNameCalculation(String inputGroupName,
                                                     String databaseGroupNames) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        JSONArray unnumberForGroupJArray = new JSONArray();
        try {
            String selectQuery = "SELECT " + inputGroupName
                    + " FROM class1_group_compatibility where group1 IN ("
                    + databaseGroupNames.replace("\"","'") + ")";
            log.debug(TAG,"select query for group name : " + selectQuery);
            resultSet = dbConnection.getResultSetForSelect(selectQuery, dbCodeRead);
            while (dbConnection.hasNextElement(resultSet)) {
                unnumberForGroupJArray.put(dbConnection.getString(resultSet,inputGroupName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing displayGroupNameCalculation query");
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_AUTHENTICATION_ERROR,
                    "Exception caught while executing displayGroupNameCalculation query");
        } finally {
            try {
                close(resultSet);
                if (dbConnection != null) {
                    dbConnection.closeConnections();
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.error(TAG,"Exception caught while closing the connection"
                        + " message: " + e.getMessage());
                throw new DGSMSNestableException(
                        DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                        "Exception caught while closing the connection");
            }
        }
        return unnumberForGroupJArray.toString();
    }
    private static void close(final Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
	@Override
	public Object getInstance() {
		return null;
	}
}
