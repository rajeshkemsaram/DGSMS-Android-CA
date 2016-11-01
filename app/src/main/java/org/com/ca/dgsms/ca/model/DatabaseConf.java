package org.com.ca.dgsms.ca.model;

import org.com.ca.ca.exceptions.DGSMSNestableException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.database.DatabaseDAO;

public class DatabaseConf extends DatabaseDAO implements DBConstants {
	//to get data need readable database object
	private static int dbCodeRead = 1;
	private static int dbCodeInsert = 0;
	private Context context = null;

	public DatabaseConf(Context context) {
		this.context = context;
	}

	public Object getErapDetailsForWeb(int unNumberDescId) {
		Object result = null;
		DBConnection dBConnection = new DBConnection(HomeActivity.getContext());
		Cursor resultSet = null;
		try {
			String selectTableSQLFromErap = "SELECT * FROM " + TABLE_ERAP_INFO
					+ " WHERE " + COL_UNNUMBERDESC_ID + " =" + unNumberDescId;

			// dBConnection.displayLog("debug::getErapDetailsForWeb::","selectTableSQLFromErap:"+
			// selectTableSQLFromErap);

			resultSet = dBConnection.getResultSetForSelect(
					selectTableSQLFromErap, dbCodeRead);
			JSONArray local1 = new JSONArray();
			while (dBConnection.hasNextElement(resultSet)) {
				JSONObject local = new JSONObject();
				// int unnumber = rs.getInt("unnumber");
				String pkg_group = resultSet
						.getString(resultSet
								.getColumnIndex(COL_PKG_GROUP));
				String erap_index = resultSet
						.getString(resultSet
								.getColumnIndex(COL_ERAP_INDEX));
				if (erap_index.equals("See SP84")) {
					String sp84value = getSP84ValueForWeb();
					local.put("sp84value", sp84value);
					local.put("sp84status", "1");
				} else {
					local.put("sp84status", "0");
				}
				String erap_status = resultSet
						.getString(resultSet
								.getColumnIndex(COL_ERAP_STATUS));
				// System.out.println("unnumber" + unnumber);
				// System.out.println("pkg_group" + pkg_group);
				// System.out.println("erap_index" + erap_index);
				// System.out.println("erap_status" + erap_status);
				// local.put("unnumber", unnumber);
				local.put("pkg_group", pkg_group);
				local.put("erap_index", erap_index);
				local.put("erap_status", erap_status);
				local1.put(local);
			}
			result = local1;

			// dBConnection.displayLog("debug","result:" + result);
		} catch (SQLException se) {
			se.printStackTrace();
			dBConnection.displayLog(
					"error",
					"SQL exception caught while getting erap info: "
							+ se.getMessage());
			// throw new DGSMSNestableException(
			// DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
			// "SQL exception caught while getting erap info");
		} catch (Exception se) {
			se.printStackTrace();
			dBConnection.displayLog(
					"JSON exception caught while getting erap info: "
							+ se.getMessage(), "error");
		} finally {
			try {
                close(resultSet);
                if (dBConnection != null) {
                    dBConnection.closeConnections();
                }
			} catch (Exception e) {
				e.printStackTrace();
				dBConnection.displayLog(
						"Exception caught while closing the connection"
								+ " message: " + e.getMessage(), "error");
			}
		}
		return result;
	}

	public String getSP84ValueForWeb() {
		String result = null;
		DBConnection dBConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
		try {
			String SqlSelectQuery = "SELECT group_concat(value) as info FROM "
					+ TABLE_SP84_INFO;
			// dBConnection.displayLog( "debug","SqlSelectQuery:" +
			// SqlSelectQuery);
			resultSet = dBConnection.getResultSetForSelect(
					SqlSelectQuery, dbCodeRead);
			while (dBConnection.hasNextElement(resultSet)) {
				result = resultSet
						.getString(resultSet
								.getColumnIndex("info"));
			}
		} catch (Exception se) {
			se.printStackTrace();
			dBConnection.displayLog(
					"SQL exception caught while getting License data: "
							+ se.getMessage(), "error");
		} finally {
			try {
                close(resultSet);
                if (dBConnection != null) {
                    dBConnection.closeConnections();
                }
			} catch (Exception e) {
				e.printStackTrace();
				dBConnection.displayLog(
						"Exception caught while closing the connection"
								+ " message: " + e.getMessage(), "error");
			}
		}
		return result;
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

//	public int checkExcemptionForPlacarding(String User_id, String transaction_id,
//											int deleteOrAdd) {
//		int status = 0;
//		ResultSet rs = null;
//		ResultSet rs2 = null;
//		ResultSet rs3 = null;
//		Connection c = null;
//		Statement statement = null;
//		Statement statement2 = null;
//		Statement statement3 = null;
//		Statement statement4 = null;
//		String selectQuery = null;
//		String selectExcemptWeight = null;
//		String grossWeightQuery = null;
//		try {
//			c = DatabaseConnectionLocal.getConnection();
//			statement = c.createStatement();
//			statement2 = c.createStatement();
//			statement3 = c.createStatement();
//			statement4 = c.createStatement();
//			// int getShipAndName = getShipAndNameTwoOne(User_id,
//			// transaction_id);
//			// if (getShipAndName > 0) {
//			selectQuery = "select id,name, sum(weight_in_kgs) weight from transaction_details where user_id = "
//					+ User_id
//					+ " and transaction_id = '"
//					+ transaction_id
//					+ "' and exemptionValue !=1 group by name";
//			// } else {
//			// selectQuery =
//			// "select id,name, sum(weight_in_kgs) weight from transaction_details where user_id = "
//			// + User_id
//			// + " and transaction_id = '"
//			// + transaction_id
//			// +
//			// "' and name not in ('1.1','1.2','1.3','1.5','2.3','4.3','5.2','6.1','7.3') group by name";
//			// }
//			log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//					+ selectQuery);
//			rs = statement.executeQuery(selectQuery);
//			double weightOfSingleClass = 0;
//			double excemptWeight = 0;
//			double grossWeight = 0;
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				weightOfSingleClass = rs.getDouble("weight");
//				if (deleteOrAdd == 1) {
//					if (weightOfSingleClass < 1000) {
//						grossWeightQuery = "select sum(weight_in_kgs) weight from transaction_details where user_id = "
//								+ User_id
//								+ " and transaction_id = '"
//								+ transaction_id + "'";
//						log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//								+ grossWeightQuery);
//						rs3 = statement3.executeQuery(grossWeightQuery);
//						while (rs3.next()) {
//							grossWeight = rs3.getDouble("weight");
//						}
//						// if (getShipAndName > 0) {
//						selectExcemptWeight = "select sum(weight_in_kgs) weight from transaction_details where user_id = "
//								+ User_id
//								+ " and transaction_id = '"
//								+ transaction_id + "' and exemptionValue = 1 ";
//						// } else {
//						// selectExcemptWeight =
//						// "select sum(weight_in_kgs) weight from transaction_details where user_id = "
//						// + User_id
//						// + " and transaction_id = '"
//						// + transaction_id
//						// +
//						// "' and name in ('1.1','1.2','1.3','1.5','2.3','4.3','5.2','6.1','7.3')";
//						// }
//						log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//								+ selectExcemptWeight);
//						rs2 = statement2.executeQuery(selectExcemptWeight);
//						while (rs2.next()) {
//							excemptWeight = rs2.getDouble("weight");
//						}
//						log.debug("grossWeightgrossWeightgrossWeightgrossWeight"
//								+ grossWeight
//								+ "          excemptWeightexcemptWeightexcemptWeightexcemptWeight"
//								+ excemptWeight);
//						if ((grossWeight - excemptWeight) >= 500) {
//							String updateExcemptToOne = "update transaction_details set excempt = 1 where user_id = "
//									+ User_id
//									+ " and transaction_id = '"
//									+ transaction_id
//									+ "' and id = "
//									+ id
//									+ " and exemptionValue != 1";
//							log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//									+ updateExcemptToOne);
//							statement4.executeUpdate(updateExcemptToOne);
//						}
//					} else if (weightOfSingleClass > 1000) {
//
//					}
//				} else if (deleteOrAdd == 0) {
//					grossWeightQuery = "select sum(weight_in_kgs) weight from transaction_details where user_id = "
//							+ User_id
//							+ " and transaction_id = '"
//							+ transaction_id + "'";
//					log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//							+ grossWeightQuery);
//					rs3 = statement3.executeQuery(grossWeightQuery);
//					while (rs3.next()) {
//						grossWeight = rs3.getDouble("weight");
//					}
//					// if (getShipAndName > 0) {
//					selectExcemptWeight = "select sum(weight_in_kgs) weight from transaction_details where user_id = "
//							+ User_id
//							+ " and transaction_id = '"
//							+ transaction_id + "' and exemptionValue = 1";
//					// } else {
//					// selectExcemptWeight =
//					// "select sum(weight_in_kgs) weight from transaction_details where user_id = "
//					// + User_id
//					// + " and transaction_id = '"
//					// + transaction_id
//					// +
//					// "' and name in ('1.1','1.2','1.3','1.5','2.3','4.3','5.2','6.1','7.3')";
//					// }
//					log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//							+ selectExcemptWeight);
//					rs2 = statement2.executeQuery(selectExcemptWeight);
//					while (rs2.next()) {
//						excemptWeight = rs2.getDouble("weight");
//					}
//					if ((grossWeight - excemptWeight) < 500) {
//						String updateExcemptToOne = "update transaction_details set excempt = 0 where user_id = "
//								+ User_id
//								+ " and transaction_id = '"
//								+ transaction_id + "' and excempt = 1 ";
//						log.debug("grossWeightQuerygrossWeightQuerygrossWeightQuery"
//								+ updateExcemptToOne);
//						statement4.executeUpdate(updateExcemptToOne);
//					}
//				}
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			log.error("Exception caught while executing checkExcemptionForPlacarding "
//					+ " message: " + e.getMessage());
//			throw new DGSMSNestableException(
//					DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
//					"Exception caught while executing checkExcemptionForPlacarding");
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("Exception caught while executing checkExcemptionForPlacarding query");
//			throw new DGSMSNestableException(
//					DGSMSNestableException.CODE_AUTHENTICATION_ERROR,
//					"Exception caught while executing checkExcemptionForPlacarding query");
//		} finally {
//			try {
//				if (statement != null) {
//					statement.close();
//					statement = null;
//				}
//				if (statement2 != null) {
//					statement2.close();
//					statement2 = null;
//				}
//				if (statement3 != null) {
//					statement3.close();
//					statement3 = null;
//				}
//				if (statement4 != null) {
//					statement4.close();
//					statement4 = null;
//				}
//				if (c != null) {
//					c.close();
//					c = null;
//				}
//				if (rs != null) {
//					rs.close();
//					rs = null;
//				}
//				if (rs2 != null) {
//					rs2.close();
//					rs2 = null;
//				}
//				if (rs3 != null) {
//					rs3.close();
//					rs3 = null;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				log.error("Exception caught while closing the connection"
//						+ " message: " + e.getMessage());
//				throw new DGSMSNestableException(
//						DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
//						"Exception caught while closing the connection");
//			}
//		}
//		return status;
//	}
}