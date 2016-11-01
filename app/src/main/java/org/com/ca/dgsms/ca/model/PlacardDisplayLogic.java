package org.com.ca.dgsms.ca.model;

import android.database.Cursor;
import android.util.Log;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.database.GetDBData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.com.ca.ca.exceptions.DGSMSNestableException;
import org.json.JSONArray;
import org.json.JSONObject;

class placarddetails {
    String placard;
    String type;

    public placarddetails(String placard, String type) {
        this.placard = placard;
        this.type = type;
    }
}

public class PlacardDisplayLogic implements DBConstants {
    private final String TAG = PlacardDisplayLogic.class.getSimpleName();
    //to get data need readable database object
    private static int dbCodeRead = 1;
    private static int dbCodeInsert = 0;
    com.ideabytes.dgsms.ca.logs.Log log = new com.ideabytes.dgsms.ca.logs.Log();
    /**
     * arrayOfTransactions is list of transactions under given transaction id
     *
     * @datatype JSONArray
     */
    private JSONArray arrayOfTransactions = new JSONArray();
    /**
     * get class 1 group names data and stored in one json object
     */
    private static final JSONObject class1SegregationData = class1SegregationData();
    /**
     * get class 1 compatability display class name data and stored in json
     * object
     */
    private static final JSONObject classSegregationData = class1Compatibility();
    /**
     * transactionsBasedOnIds is list of transactions under given transaction id
     *
     * @datatype JSONObject
     */
    private JSONObject transactionsBasedOnIds = new JSONObject();

    public String placardDisplayLogic(String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        try {
            // Code to insert records with above input user_id and
            // transaction_id to temptransaction
            String selectRecordsFromTransDetails = "select * from transaction_details where "+COL_TRANSACTION_STATUS+" = 0";
            resultSet = dbConnection.getResultSetForSelect(selectRecordsFromTransDetails,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet)) {
                int non_exempt = DBConstants.ZERO;
                int disp_pla_mandatory = DBConstants.ZERO;
                int swap_for_danger = dbConnection
                        .getInt(resultSet,"dangerous_placard_original");
                int unnumber_display = dbConnection.getInt(resultSet,COL_UNNUMBER_DISPLAY);
//                log.debug(LOGTAG, "unnumber_display"
//                        + unnumber_display);
				/*
				 * check the erap_no if erap_no is not null then update the
				 * mandatory coloumn to one to say the class is mandatory update
				 * display_placard_mandatory = 1(erap) non_exempt = 1 and
				 * swap_for_danger = 0 based on id(Primary key)
				 */
                if (!dbConnection.getString(resultSet,"erap_no").equals("")
                        || dbConnection.getInt(resultSet,"ibc_status") != DBConstants.ZERO) {
                    non_exempt = DBConstants.ONE;
                    swap_for_danger = DBConstants.ZERO;
                    disp_pla_mandatory = DBConstants.ONE;
                    log.debug(TAG,"in erap");
                }
				/*
				 * check the UnNumber is display on the Placard or not
				 */
                log.debug(TAG," wieght index : "
                        + dbConnection.getString(resultSet,"weight_index"));
                log.debug(TAG," weight_in_kgs : "
                        + dbConnection.getString(resultSet,"weight_in_kgs"));
                if ((dbConnection.getDouble(resultSet,"weight_in_kgs"))
                        / (dbConnection.getInt(resultSet,"number_of_units")) > 450
                        && (!dbConnection.getString(resultSet,"weight_index")
                        .equals("1e+10"))) {
                    log.debug(TAG," wieght index *******: "
                            + dbConnection
                            .getString(resultSet,"weight_index"));
                    unnumber_display = DBConstants.ONE;
                    non_exempt = 1;
                    disp_pla_mandatory = 1;
                    swap_for_danger = DBConstants.ZERO;
                }
				/*
				 * check the consignee_danger if consignee_danger is not null
				 * then update the mandatory coloumn to one to say the class is
				 * mandatory update display_placard_mandatory = 1(erap)
				 * non_exempt = 1 and swap_for_danger = 0 based on id(Primary
				 * key)
				 */
                if (dbConnection.getInt(resultSet,"consignee_danger") > DBConstants.ZERO) {
                    checkThousandRule(user_id, transaction_id,
                            dbConnection.getString(resultSet,"primary_placard"));
                    non_exempt = DBConstants.ONE;
                    disp_pla_mandatory = DBConstants.ONE;
                    swap_for_danger = DBConstants.ZERO;
                    log.debug(TAG,"consignee danger");
                }
				/*
				 * check the special_provision if special_provision is not null
				 * then update the mandatory coloumn to one to say the class is
				 * mandatory update display_placard_mandatory = 1(erap)
				 * non_exempt = 1 and swap_for_danger = 0 based on id(Primary
				 * key)
				 */
                if (dbConnection.getInt(resultSet,"special_provision") == DBConstants.SP_TWNTY_THREE) {
                    non_exempt = DBConstants.ONE;
                    swap_for_danger = DBConstants.ZERO;
                    disp_pla_mandatory = DBConstants.ONE;
                    log.debug(TAG,"special provision");
                }
                if (dbConnection.getString(resultSet,"secondary_placard")
                        .contains("e.t")
                        && (dbConnection.getDouble(resultSet,"weight_in_kgs") > dbConnection
                        .getDouble(resultSet,"weight_index"))) {
                    swap_for_danger = DBConstants.ZERO;
                    log.debug(TAG,"secondary placard et and weight greater than weight index");
                }

                String insertIntoTempTransaction = "INSERT INTO `temptransaction` ( `user_id`, `transaction_id`, `un_number`, `un_class_id`, `dg_weight`, `total_weight`, `number_of_units`, `pkg_group`, `untype`, `unnumber_display`, `name`, `group_name`, `ibc_status`, `consignor_1000kg`, `erap_number`, `primary_placard`, `secondary_placard`, `weight_index`, `special_provision`, `un_style`, `display_primary_placard`, `display_secondary_placard`, `display_placard_mandatory`, `non_exempt_default` , `non_exempt`, `nonoptimise_display_mandatory` ,`non_optimise`, `swap_for_danger`,"+COL_TRANSACTION_STATUS+") VALUES ("
                        +"\""+ dbConnection.getString(resultSet, COL_USER_ID)+"\""
                        + ", "
                        +"\""+ dbConnection.getString(resultSet,COL_TRANSACTION_ID)+"\""
                        + ", '"
                        + dbConnection.getString(resultSet,COL_UN_NUMBER)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_UN_CLASS_ID)
                        + "', '"
                        + dbConnection.getDouble(resultSet,COL_DG_WEIGHT)
                        + "', '"
                        + dbConnection.getDouble(resultSet,COL_WEIGHT_IN_KGS)
                        + "', '"
                        + dbConnection.getInt(resultSet,COL_NO_OF_UNITS)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_PKG_GROUP)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_UNTYPE)
                        + "', '"
                        + unnumber_display
                        + "', '"
                        + dbConnection.getString(resultSet,COL_NAME)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_GROUP_NAME)
                        + "', '"
                        + dbConnection.getInt(resultSet,COL_IBC_STATUS)
                        + "', '"
                        + dbConnection.getInt(resultSet,COL_CONSIGNEE_DANGER) // value
                        // will
                        // be
                        // 1
                        // if
                        // the
                        // weight
                        // of
                        // single
                        // class
                        // is
                        // greater
                        // than
                        // 1000
                        // kgs
                        + "', '"
                        + dbConnection.getString(resultSet,COL_ERAP_NO)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_PRIMARY_PLACARD)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_SECONDARY_PLACARD)
                        + "', '"
                        + dbConnection.getString(resultSet,COL_WEIGHT_INDEX)
                        + "', '"
                        + dbConnection.getInt(resultSet,COL_SPECIAL_PROVISION)
                        + "', '"
                        + dbConnection.getInt(resultSet,COL_UN_SYLE)
                        + "', '"
                        + DBConstants.DSPLY_PRMY_PLCRD
                        + "', '"
                        + DBConstants.DSPLY_SCNDRY_PLCRD
                        + "', '"
                        + disp_pla_mandatory
                        + "', '"
                        + dbConnection.getInt(resultSet,COL_NONEXCEMPT)
                        + "','"
                        + non_exempt
                        + "', '"
                        + disp_pla_mandatory
                        + "','"
                        + non_exempt + "','" + swap_for_danger + "',"+" 0 "+")";
                dbConnection.getResultSetForInsertOrUpdate(insertIntoTempTransaction, dbCodeInsert);
                //  Log.v(LOGTAG,"insertIntoTempTransaction "+insertIntoTempTransaction);
            }
            return iterateBasicOptimize(user_id, transaction_id)
                    + "@@@"
                    + itratFrOptmzeOrNonOptmze(user_id, transaction_id,
                    DBConstants.ZERO)
                    + "@@@"
                    + itratFrOptmzeOrNonOptmze(user_id, transaction_id,
                    DBConstants.ONE);
        }catch (Exception e) {

            e.printStackTrace();
            log.error(TAG,"Exception caught while executing placardDisplayLogic"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing placardDisplayLogic");
        } finally {
            // Flush temptransaction Details Based on UserId and TransactionId
            // after giving full String(optimize,semioptimize,nonoptmize).
            GetDBData getDBData = new GetDBData(HomeActivity.getContext());
           // log.debug(TAG,"temp table data "+getDBData.getTempTransactionDetails());
            truncateTempTransaction(user_id, transaction_id);
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
    }

    /**
     * iterate the basic optimize
     *
     * @param user_id
     * @param transaction_id
     * @return
     */
    public String iterateBasicOptimize(String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        String primaryPlacard = null;
        String secondaryPlacard = null;
        String pkgGroup = null;
        String unNumber = null;
        JSONArray ppArrayForGroupnameDisplayName = new JSONArray();
        ArrayList<placarddetails> placard_holder_array_basic = new ArrayList<placarddetails>();
        int placardCount = 0;
        String imgString = "";
        String finalValue = "";
        String display_name = null;
        String group_name = "";
        String erap_number = "";
        try {
          /*
			 * check the class 1 scenarios based on userid and transactionid
			 * update appropriate class 1 in temptransaction table.
			 */
            checkExplosiveForBasic(user_id, transaction_id);
            String selectRecords = "select distinct primary_placard,pkg_group,un_number,secondary_placard , erap_number from temptransaction";
            resultSet = dbConnection.getResultSetForSelect(selectRecords, dbCodeRead);
            log.debug(TAG,"Basic Optimize" + selectRecords);
            while (dbConnection.hasNextElement(resultSet)) {
                primaryPlacard = dbConnection
                        .getString(resultSet, "primary_placard");
                pkgGroup = dbConnection.getString(resultSet, "pkg_group");
                unNumber = dbConnection.getString(resultSet,"un_number");
                ppArrayForGroupnameDisplayName.put(primaryPlacard);
                secondaryPlacard = dbConnection
                        .getString(resultSet,"secondary_placard");
                erap_number = dbConnection.getString(resultSet,"erap_number");
                log.debug("raja primary placvard : ","ETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
                        + primaryPlacard);
                if (!primaryPlacard.equals("")  && !primaryPlacard.equals("1s")) {
                    boolean isPrimary = false;
                    log.debug("raja primary placvard : ","ETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
                            + primaryPlacard);
                    for (int s = 0; s < placard_holder_array_basic.size(); s++) {
                        if (placard_holder_array_basic.get(s).placard
                                .equals(primaryPlacard)) {
                            isPrimary = true;
                        }
                    }
                    if (!isPrimary) {
                        placard_holder_array_basic.add(new placarddetails(
                                primaryPlacard, "pp"));
                        placardCount++;
                    }
                }
                if (!secondaryPlacard.equals("")) {
                    String secondaryPlacardArray[] = secondaryPlacard
                            .split("#");
                    for (int i = 0; i < secondaryPlacardArray.length; i++) {
                        secondaryPlacard = secondaryPlacardArray[i];
                        log.debug(TAG,"secondaryDisplayPlacardsecondaryDisplayPlacardsecondaryDisplayPlacard"
                                + secondaryPlacard);
                        if ((secondaryPlacard.equals("1") && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("4.3") && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("6.1")
                                && pkgGroup.equals("I") && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("8")
                                && (unNumber.equals("2977") || unNumber
                                .equals("2978")) && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("e.t"))
                                || (secondaryPlacard.equals("i.h"))
                                || (secondaryPlacard.equals("fr"))
                                || (secondaryPlacard.equals("es"))
                                && !secondaryPlacard.equals("")) {
                            if (secondaryPlacard.equals("e.t")) {
                                boolean isScndry = false;
                                log.debug(TAG,"ETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
                                        + secondaryPlacard);
                                for (int s = 0; s < placard_holder_array_basic
                                        .size(); s++) {
                                    if (placard_holder_array_basic.get(s).placard
                                            .equals(secondaryPlacard)) {
                                        isScndry = true;
                                    }
                                }
                                if (!isScndry) {
                                    placard_holder_array_basic
                                            .add(new placarddetails(
                                                    secondaryPlacard, "et"));
                                    placardCount++;
                                }
                            } else if (secondaryPlacard.equals("i.h")) {
                                boolean isScndry = false;
                                for (int s = 0; s < placard_holder_array_basic
                                        .size(); s++) {
                                    if (placard_holder_array_basic.get(s).placard
                                            .equals(secondaryPlacard)) {
                                        isScndry = true;
                                    }
                                }
                                if (!isScndry) {
                                    log.debug(TAG,"IHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"
                                            + secondaryPlacard);
                                    placard_holder_array_basic
                                            .add(new placarddetails(
                                                    secondaryPlacard, "ih"));
                                    placardCount++;
                                }
                            } else {
                                boolean isScndry = false;
                                log.debug(TAG,"SPNOTNULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
                                        + secondaryPlacard);
                                for (int s = 0; s < placard_holder_array_basic
                                        .size(); s++) {
                                    if (placard_holder_array_basic.get(s).placard
                                            .equals(secondaryPlacard)) {
                                        isScndry = true;
                                    }
                                }
                                if (!isScndry) {
                                    int isPporSp = checkUnDisplay(
                                            secondaryPlacard, user_id,
                                            transaction_id);
                                    if (isPporSp == 0) {
                                        placard_holder_array_basic
                                                .add(new placarddetails(
                                                        secondaryPlacard, "sp"));
                                        placardCount++;
                                    } else if (isPporSp == 1) {
                                        placard_holder_array_basic
                                                .add(new placarddetails(
                                                        secondaryPlacard, "pp"));
                                        placardCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            log.debug(TAG,"secondaryDisplayPlacard*************" + secondaryPlacard);

            imgString = cnstrctRspnStrng(placard_holder_array_basic, user_id,
                    transaction_id);
            Log.v("basic : ",imgString);
            int placardShow = 0;
            if (imgString.equals("")) {
                imgString = "No placard Required";
                placardShow = 0;
            } else {
                placardShow = 1;
            }
            // Method to call get the group name based on inputs
            String result = displayGroupName1(
                    ppArrayForGroupnameDisplayName.toString(), user_id,
                    transaction_id);
            if (result.length() > 4) {
                String resultArray[] = result.split("@@@");
                finalValue = placardShow + "::" + imgString.replace(".", "_")
                        + "-GROUP-" + resultArray[0] + "-DISPLAY-"
                        + resultArray[1];
            } else {
                finalValue = placardShow + "::" + imgString.replace(".", "_")
                        + "-GROUP-" + group_name + "-DISPLAY-" + display_name;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing iterateBasicOptimize"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing iterateBasicOptimize");
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
        return finalValue;
    }

    /**
     * Method to flush Records in temptransaction based on user_id and
     * transaction_id
     *
     * @param user_id
     * @param transaction_id
     */
    public void truncateTempTransaction(String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        try {
            String deleteQuery = "DELETE FROM `temptransaction`";
            dbConnection.getResultSetForInsertOrUpdate(deleteQuery, dbCodeInsert);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing truncateTempTransaction"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing truncateTempTransaction");
        } finally {
            try {
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
    }

    /**
     * Iterate through the entire transaction and update nonexempt and danger
     * based on weightIndex
     *
     * @param user_id
     * @param transaction_id
     */
    /**
     * Iterate through the entire transaction and update nonexempt and danger
     * based on weightIndex
     *
     * @param user_id
     * @param transaction_id
     */
    public String itratFrOptmzeOrNonOptmze(String user_id, String transaction_id,
                                           int optimiseOrNonOptimise) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet1 = null;
        Cursor resultSet2  = null;
        Cursor resultSet3  = null;
        String selectPrimaryPlacard = null;
        String resultForOptimize = "";
        String primaryPlacard = "";
        double totalWeightOfRecords = 0;
        double totalWeightOfNEW = 0;
        String mandatory_placard = null;
        String mandatory_exempt = null;
        try {
            String getTotalWeight = "select sum(total_weight) from temptransaction where weight_index != '1e+10'";
            log.debug(TAG,"getTotalWeight***" + getTotalWeight);
            resultSet1 = dbConnection.getResultSetForSelect(getTotalWeight,dbCodeRead);

            while (dbConnection.hasNextElement(resultSet1)) {
                totalWeightOfRecords = dbConnection.getDouble(resultSet1 ,"sum(total_weight)");
            }
            String getNonExecmptWeight = "select sum(total_weight) from temptransaction where non_exempt_default = '1'";
            log.debug(TAG,"getNonExecmptWeight***" + getNonExecmptWeight);
            resultSet2 = dbConnection.getResultSetForSelect(getNonExecmptWeight,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet2)) {
                totalWeightOfNEW = dbConnection.getDouble(resultSet2,"sum(total_weight)");
            }
            if (optimiseOrNonOptimise == DBConstants.ONE) {
                mandatory_placard = "display_placard_mandatory";
                mandatory_exempt = "non_exempt";
            } else if (optimiseOrNonOptimise == DBConstants.ZERO) {
                mandatory_placard = "nonoptimise_display_mandatory";
                mandatory_exempt = "non_optimise";
            }
            // rajasekhar added extra varibale (weight_index) in query for
            // caculating weigth index
            // update the exempt and optimize values in a query
            selectPrimaryPlacard = "select un_number,sum(total_weight), weight_index, primary_placard, secondary_placard from temptransaction where `" + mandatory_placard
                    // start3
                    // 01-008-2016rajasekhar added code for resolve issue in
                    // 1005 and 2.3
                    // change name to primary_placard in group by query
                    // start
                    + "` = '0' group by primary_placard,weight_index";
            // end
            log.debug(TAG,"selectPrimaryPlacard***" + selectPrimaryPlacard);
            resultSet3 = dbConnection.getResultSetForSelect(selectPrimaryPlacard,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet3)) {
                primaryPlacard = dbConnection.getString(resultSet3,"primary_placard");
                double totalWeight = dbConnection
                        .getDouble(resultSet3,"sum(total_weight)");
                double weightIndex = dbConnection.getDouble(resultSet3,"weight_index");
                String un_number = dbConnection.getString(resultSet3,"un_number");

                log.debug(TAG,"suman un_number "+un_number);
                log.debug(TAG,"Weight greator than weght index ( totalWeight - weightIndex )"
                        + (totalWeight - weightIndex)
                        + " totalWeight : "
                        + totalWeight + " weightIndex : " + weightIndex);
                log.debug(TAG," totalWeightOfNEW ******: " + totalWeightOfNEW);
                log.debug(TAG," *****totalWeightOfRecords : "
                        + totalWeightOfRecords);
                if ((totalWeight > weightIndex)
                        || (totalWeightOfRecords - totalWeightOfNEW > 500)
                        && (weightIndex != 1.0E10)) {
                    String updateNonOptimise = null;
                    String updateOneFour = null;
                    updateNonOptimise = "UPDATE `temptransaction` SET `"
                            + mandatory_placard + "` ='1', `"
                            + mandatory_exempt
                            + "`='1' WHERE primary_placard = '"
                            + primaryPlacard + "'";
                    dbConnection.getResultSetForInsertOrUpdate(updateNonOptimise,dbCodeInsert);
                    log.debug(TAG,"updateNonOptimiseupdateNonOptimiseupdateNonOptimise"
                            + updateNonOptimise);
                    if (primaryPlacard.equals("1.4") && totalWeight <= 1000
                            && !un_number.equalsIgnoreCase("0301")) {
                        updateOneFour = "UPDATE `temptransaction` SET `"
                                + mandatory_placard + "`='0', `"
                                + mandatory_exempt
                                + "`='0' WHERE primary_placard = '"
                                + primaryPlacard + "'";
                        log.debug(TAG,"updateOneFourupdateOneFourupdateOneFourupdateOneFour"
                                + updateOneFour);
                        dbConnection.getResultSetForInsertOrUpdate(updateOneFour, dbCodeInsert);
                    }
                }
            }
			/*
			 * update the display primary placard and display secondary placard
			 */
            updateDisplayPlacards(user_id, transaction_id);
			/*
			 * check the class 1 scenarios based on userid and transactionid
			 * update appropriate class 1 in temptransaction table.
			 */
            checkExplosive(user_id, transaction_id);
			/*
			 * Method to construct the response with what placards are needed to
			 * be displayed and returing the finalString to the calling method
			 */
            resultForOptimize = constructResponse(user_id, transaction_id,
                    optimiseOrNonOptimise);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing iterateForOptimize"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing iterateForOptimize");
        } finally {
            try {
                close(resultSet1);
                close(resultSet2);
                close(resultSet3);
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
        return resultForOptimize;
    }

    /**
     * method to update display_primary_placard, display_secondary_placard if
     * they are mandatory for displaying
     *
     * @param user_id
     * @param transaction_id
     */
    public void updateDisplayPlacards(String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        String updatePlacardQuery = null;
        try {
            String primaryPlacard = null;
            String secondaryPlacard = null;
            String pkgGroup = "";
            String erap_number = "";
            String unNumber = null;
            String getTransactionDetails = "select * from temptransaction where (non_exempt = 1 or non_optimise = 1)";
            log.debug(TAG,"getTransactionDetails" + getTransactionDetails);
            resultSet = dbConnection.getResultSetForSelect(getTransactionDetails, dbCodeRead);
            log.debug(TAG,"updatePlacardQuery size " + resultSet.getCount());
            while (dbConnection.hasNextElement(resultSet)) {
                String secondaryToQuery = "";
                int id = dbConnection.getInt(resultSet,"id");
                primaryPlacard = dbConnection
                        .getString(resultSet,"primary_placard");
                secondaryPlacard = dbConnection
                        .getString(resultSet,"secondary_placard");
                pkgGroup = dbConnection.getString(resultSet,"pkg_group");
                erap_number = dbConnection.getString(resultSet,"erap_number");
                unNumber = dbConnection.getString(resultSet,"un_number");
                if (!secondaryPlacard.equals("")) {
                    String secondaryPlacardArray[] = secondaryPlacard
                            .split("#");
                    for (int i = 0; i < secondaryPlacardArray.length; i++) {
                        secondaryPlacard = secondaryPlacardArray[i];
                        if ((secondaryPlacard.equals("1") && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("4.3") && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("6.1")
                                && pkgGroup.equals("I") && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("8")
                                && (unNumber.equals("2977") || unNumber
                                .equals("2978")) && !erap_number
                                .equals(""))
                                || (secondaryPlacard.equals("e.t"))
                                || (secondaryPlacard.equals("fr"))
                                || (secondaryPlacard.equals("es"))
                                || (secondaryPlacard.equals("i.h"))) {
                            secondaryToQuery += secondaryPlacard + "#";
                            updatePlacardQuery = "UPDATE `temptransaction` SET `display_primary_placard`='"
                                    + primaryPlacard
                                    + "', `display_secondary_placard`='"
                                    + secondaryToQuery
                                    + "' where id = '"
                                    + id
                                    + "'";
                        } else {
                            updatePlacardQuery = "UPDATE `temptransaction` SET `display_primary_placard`='"
                                    + primaryPlacard
                                    + "' where id = '"
                                    + id
                                    + "'";
                        }
                    }
                }
                if (secondaryPlacard.equals("")) {
                    updatePlacardQuery = "UPDATE `temptransaction` SET `display_primary_placard`='"
                            + primaryPlacard + "' where id = '" + id + "'";
                }
                log.debug(TAG,"updatePlacardQuery" + updatePlacardQuery);
                dbConnection.getResultSetForInsertOrUpdate(updatePlacardQuery, dbCodeInsert);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing updateDisplayPlacards"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing updateDisplayPlacards");
        } finally {
            try {
                if (dbConnection != null) {
                    close(resultSet);
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

    }

    /**
     * Method to construct response for mandatory placards
     *
     * @param user_id
     * @param transaction_id
     * @return finalValue
     */
    public String constructResponse(String user_id, String transaction_id,
                                    int optimizeValue) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet1 = null;
        Cursor resultSet2 = null;
        Cursor resultSet3 = null;
        String imgString = "";
        String primaryDisplayPlacard = null;
        String secondaryDisplayPlacard = null;
        String unNumber = null;
        String pkgGroup = "";
        int placardCount = 0;
        int countOfExemptDanger = 0;
        String display_name = null;
        String group_name = "";
        JSONArray ppArrayForGroupnameDisplayName = new JSONArray();
        String finalValue = "";
        boolean isDanger = false;
        try {
            if (optimizeValue == DBConstants.ONE) {
                ArrayList<placarddetails> placard_holder_array_optimise = new ArrayList<placarddetails>();
                String getNonExcemptPlacards = "select distinct display_primary_placard,pkg_group,un_number,display_secondary_placard from temptransaction where non_exempt = 1 and swap_for_danger = 0";
                log.debug(TAG,"getNonExcemptPlacards***" + getNonExcemptPlacards);
                resultSet1 = dbConnection.getResultSetForSelect(getNonExcemptPlacards, dbCodeRead);
                while (dbConnection.hasNextElement(resultSet1)) {
                    primaryDisplayPlacard = dbConnection
                            .getString(resultSet1,"display_primary_placard");
                    pkgGroup = dbConnection.getString(resultSet1,"pkg_group");
                    unNumber = dbConnection.getString(resultSet1,"un_number");
                    ppArrayForGroupnameDisplayName.put(primaryDisplayPlacard);
                    secondaryDisplayPlacard = dbConnection
                            .getString(resultSet1,"display_secondary_placard");
                    log.debug(TAG,"primaryDisplayPlacardprimaryDisplayPlacard"
                            + primaryDisplayPlacard);
                    if (!primaryDisplayPlacard.equals("0")
                            && !primaryDisplayPlacard.equals("1s")) {
                        boolean isPrimary = false;
                        for (int s = 0; s < placard_holder_array_optimise
                                .size(); s++) {
                            if (placard_holder_array_optimise.get(s).placard
                                    .equals(primaryDisplayPlacard)) {
                                isPrimary = true;
                            }
                        }
                        log.debug(TAG,"In adding primary placard"
                                + primaryDisplayPlacard);
                        if (!isPrimary) {
                            placard_holder_array_optimise
                                    .add(new placarddetails(
                                            primaryDisplayPlacard, "pp"));
                            placardCount++;
                        }

                    }
                    log.debug(TAG,"secondaryDisplayPlacardsecondaryDisplayPlacardsecondaryDisplayPlacard"
                            + secondaryDisplayPlacard);
                    if (!secondaryDisplayPlacard.equals("0")) {
                        String secondaryPlacardArray[] = secondaryDisplayPlacard
                                .split("#");
                        for (int i = 0; i < secondaryPlacardArray.length; i++) {
                            secondaryDisplayPlacard = secondaryPlacardArray[i];
                            log.debug(TAG,"secondaryDisplayPlacardsecondaryDisplayPlacardsecondaryDisplayPlacard"
                                    + secondaryDisplayPlacard);
                            if (secondaryDisplayPlacard.equals("1")
                                    || secondaryDisplayPlacard.equals("4.3")
                                    || (secondaryDisplayPlacard.equals("6.1") && pkgGroup
                                    .equals("I"))
                                    || (secondaryDisplayPlacard.equals("8") && (unNumber
                                    .equals("2977") || unNumber
                                    .equals("2978")))
                                    || (secondaryDisplayPlacard.equals("e.t"))
                                    || (secondaryDisplayPlacard.equals("i.h"))
                                    || (secondaryDisplayPlacard.equals("fr"))
                                    || (secondaryDisplayPlacard.equals("es"))
                                    && !secondaryDisplayPlacard.equals("")) {
                                if (secondaryDisplayPlacard.equals("e.t")) {
                                    boolean isScndry = false;
                                    log.debug(TAG,"ETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
                                            + secondaryDisplayPlacard);
                                    for (int s = 0; s < placard_holder_array_optimise
                                            .size(); s++) {
                                        if (placard_holder_array_optimise
                                                .get(s).placard
                                                .equals(secondaryDisplayPlacard)) {
                                            isScndry = true;
                                        }
                                    }
                                    if (!isScndry) {
                                        placard_holder_array_optimise
                                                .add(new placarddetails(
                                                        secondaryDisplayPlacard,
                                                        "et"));
                                        placardCount++;
                                    }
                                } else if (secondaryDisplayPlacard
                                        .equals("i.h")) {
                                    boolean isScndry = false;
                                    for (int s = 0; s < placard_holder_array_optimise
                                            .size(); s++) {
                                        if (placard_holder_array_optimise
                                                .get(s).placard
                                                .equals(secondaryDisplayPlacard)) {
                                            isScndry = true;
                                        }
                                    }
                                    if (!isScndry) {
                                        log.debug(TAG,"IHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"
                                                + secondaryDisplayPlacard);
                                        placard_holder_array_optimise
                                                .add(new placarddetails(
                                                        secondaryDisplayPlacard,
                                                        "ih"));
                                        placardCount++;
                                    }
                                } else {
                                    boolean isScndry = false;
                                    log.debug(TAG,"SPNOTNULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
                                            + secondaryDisplayPlacard);
                                    for (int s = 0; s < placard_holder_array_optimise
                                            .size(); s++) {
                                        if (placard_holder_array_optimise
                                                .get(s).placard
                                                .equals(secondaryDisplayPlacard)) {
                                            isScndry = true;
                                        }
                                    }
									/*
									 * int isBulk = checkSpForBulk(user_id,
									 * transaction_id, secondaryDisplayPlacard);
									 * if (isBulk == 1 && !isScndry) {
									 * placard_holder_array_optimise .add(new
									 * placarddetails( secondaryDisplayPlacard,
									 * "pp")); placardCount++; } else
									 */if (!isScndry) {
                                        int isPporSp = checkUnDisplay(
                                                secondaryDisplayPlacard,
                                                user_id, transaction_id);
                                        if (isPporSp == 0) {
                                            placard_holder_array_optimise
                                                    .add(new placarddetails(
                                                            secondaryDisplayPlacard,
                                                            "sp"));
                                            placardCount++;
                                        } else if (isPporSp == 1) {
                                            placard_holder_array_optimise
                                                    .add(new placarddetails(
                                                            secondaryDisplayPlacard,
                                                            "pp"));
                                            placardCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Method to update non_exempt and non_optimise values to zero
                // so that they are not considered futher
                updateDuplicatePlacards(user_id, transaction_id,
                        placard_holder_array_optimise);
                String getExcemptPlacardsWithDangerReplacable = "select display_primary_placard from temptransaction where non_exempt = 1 and swap_for_danger = 1 group by primary_placard";
                log.debug(TAG,"getExcemptPlacardsWithDangerReplacable");
                resultSet2 = dbConnection.getResultSetForSelect(getExcemptPlacardsWithDangerReplacable, dbCodeRead);
                String onePrimaryPlacard = null;
                while (dbConnection.hasNextElement(resultSet2)) {
                    onePrimaryPlacard = dbConnection
                            .getString(resultSet2,"display_primary_placard");
                    countOfExemptDanger++;
                }
                if (countOfExemptDanger == 1) {
                    if (!onePrimaryPlacard.equals("0")
                            && !onePrimaryPlacard.equals("1s")) {
                        boolean isPrimary = false;
                        for (int s = 0; s < placard_holder_array_optimise
                                .size(); s++) {
                            if (placard_holder_array_optimise.get(s).placard
                                    .equals(onePrimaryPlacard)) {
                                isPrimary = true;
                            }
                        }
                        log.debug(TAG,"In adding primary placard"
                                + primaryDisplayPlacard);
                        if (!isPrimary) {
                            placard_holder_array_optimise
                                    .add(new placarddetails(onePrimaryPlacard,
                                            "pp"));
                            placardCount++;
                        }

                    }
                } else if (countOfExemptDanger > 1) {
                    log.debug(TAG,"PLacard holder array in size"
                            + placard_holder_array_optimise.size());
                    for (int i = 0; i < placard_holder_array_optimise.size(); i++) {
                        log.debug(TAG,"PLacard holder array in ");
                        if (placard_holder_array_optimise.get(i).placard
                                .equals("danger")) {
                            isDanger = true;
                        }
                    }
                    log.debug(TAG,"Before danger value======================= ");
                    if (!isDanger) {
                        log.debug(TAG,"In adding danger value======================= ");
                        placard_holder_array_optimise.add(new placarddetails(
                                "danger", ""));
                    }
                    if (placardCount == 2) {
                        placardCount = 1;
                    } else if (placardCount > 2) {
                        placardCount--;
                    } else if (placardCount == 0) {
                        placardCount++;
                    }
                }

                log.debug(TAG,"placard_holder_array_optimise.size()**********"
                        + placard_holder_array_optimise.size());
                // method to create the placard String based on the optimize
                // array, user id and transaction id
                imgString = cnstrctRspnStrng(placard_holder_array_optimise,
                        user_id, transaction_id);

                log.debug(TAG,"imgStringimgStringimgStringimgStringimgStringimgStringimgString"
                        + imgString);
                // Method to call get the group name based on inputs
                String resultWithDisplayGroupName = displayGroupName1(
                        ppArrayForGroupnameDisplayName.toString(), user_id,
                        transaction_id);
                log.debug(TAG,"resultWithDisplayGroupName"
                        + resultWithDisplayGroupName);
                int placardShow = 0;
                System.out
                        .println("********************************************imgString"
                                + imgString);
                if (imgString.equals("")) {
                    imgString = "No placard Required";
                    placardShow = 0;
                } else {
                    placardShow = 1;
                }
                if (resultWithDisplayGroupName.length() > 4) {
                    String resultWithDisplayGroupNameArray[] = resultWithDisplayGroupName
                            .split("@@@");
                    finalValue = placardShow + "::"
                            + imgString.replace(".", "_") + "-GROUP-"
                            + resultWithDisplayGroupNameArray[0] + "-DISPLAY-"
                            + resultWithDisplayGroupNameArray[1] + "-COUNT-"
                            + placardCount;
                } else {
                    finalValue = placardShow + "::"
                            + imgString.replace(".", "_") + "-GROUP-"
                            + group_name + "-DISPLAY-" + display_name
                            + "-COUNT-" + placardCount;
                }
            } else if (optimizeValue == DBConstants.ZERO) {
                ArrayList<placarddetails> placard_holder_array_non_optimise = new ArrayList<placarddetails>();
                String getNonExcemptPlacards = "select distinct display_primary_placard,pkg_group,un_number,display_secondary_placard from temptransaction where non_optimise = 1";
                log.debug(TAG,"getNonExcemptPlacards" + getNonExcemptPlacards);
                resultSet3 = dbConnection.getResultSetForSelect(getNonExcemptPlacards, dbCodeRead);
                while (dbConnection.hasNextElement(resultSet3)) {
                    primaryDisplayPlacard = dbConnection
                            .getString(resultSet3,"display_primary_placard");
                    pkgGroup = dbConnection.getString(resultSet3,"pkg_group");
                    unNumber = dbConnection.getString(resultSet3,"un_number");
                    ppArrayForGroupnameDisplayName.put(primaryDisplayPlacard);
                    secondaryDisplayPlacard = dbConnection
                            .getString(resultSet3,"display_secondary_placard");
                    if (!primaryDisplayPlacard.equals("")
                            || !primaryDisplayPlacard.equals("0")) {
                        boolean isPrimary = false;
                        for (int s = 0; s < placard_holder_array_non_optimise
                                .size(); s++) {
                            if (placard_holder_array_non_optimise.get(s).placard
                                    .equals(primaryDisplayPlacard)) {
                                isPrimary = true;
                            }
                        }
                        if (!isPrimary) {
                            placard_holder_array_non_optimise
                                    .add(new placarddetails(
                                            primaryDisplayPlacard, "pp"));
                            placardCount++;
                        }
                    }
                    if (!secondaryDisplayPlacard.equals("0")) {
                        String secondaryPlacardArray[] = secondaryDisplayPlacard
                                .split("#");
                        for (int i = 0; i < secondaryPlacardArray.length; i++) {
                            secondaryDisplayPlacard = secondaryPlacardArray[i];
                            log.debug(TAG,"secondaryDisplayPlacardsecondaryDisplayPlacardsecondaryDisplayPlacard"
                                    + secondaryDisplayPlacard);
                            if (secondaryDisplayPlacard.equals("1")
                                    || secondaryDisplayPlacard.equals("4.3")
                                    || (secondaryDisplayPlacard.equals("6.1") && pkgGroup
                                    .equals("I"))
                                    || (secondaryDisplayPlacard.equals("8") && (unNumber
                                    .equals("2977") || unNumber
                                    .equals("2978")))
                                    || (secondaryDisplayPlacard.equals("e.t"))
                                    || (secondaryDisplayPlacard.equals("i.h"))
                                    || (secondaryDisplayPlacard.equals("fr"))
                                    || (secondaryDisplayPlacard.equals("es"))
                                    && !secondaryDisplayPlacard.equals("")) {
                                if (secondaryDisplayPlacard.equals("e.t")) {
                                    boolean isScndry = false;
                                    log.debug(TAG,"ETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT"
                                            + secondaryDisplayPlacard);
                                    for (int s = 0; s < placard_holder_array_non_optimise
                                            .size(); s++) {
                                        if (placard_holder_array_non_optimise
                                                .get(s).placard
                                                .equals(secondaryDisplayPlacard)) {
                                            isScndry = true;
                                        }
                                    }
                                    if (!isScndry) {
                                        placard_holder_array_non_optimise
                                                .add(new placarddetails(
                                                        secondaryDisplayPlacard,
                                                        "et"));
                                        placardCount++;
                                    }
                                } else if (secondaryDisplayPlacard
                                        .equals("i.h")) {
                                    boolean isScndry = false;
                                    for (int s = 0; s < placard_holder_array_non_optimise
                                            .size(); s++) {
                                        if (placard_holder_array_non_optimise
                                                .get(s).placard
                                                .equals(secondaryDisplayPlacard)) {
                                            isScndry = true;
                                        }
                                    }
                                    if (!isScndry) {
                                        log.debug(TAG,"IHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH"
                                                + secondaryDisplayPlacard);
                                        placard_holder_array_non_optimise
                                                .add(new placarddetails(
                                                        secondaryDisplayPlacard,
                                                        "ih"));
                                        placardCount++;
                                    }
                                } else {
                                    boolean isScndry = false;
                                    log.debug(TAG,"SPNOTNULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"
                                            + secondaryDisplayPlacard);
                                    for (int s = 0; s < placard_holder_array_non_optimise
                                            .size(); s++) {
                                        if (placard_holder_array_non_optimise
                                                .get(s).placard
                                                .equals(secondaryDisplayPlacard)) {
                                            isScndry = true;
                                        }
                                    }
                                    if (!isScndry) {
                                        int isPporSp = checkUnDisplay(
                                                secondaryDisplayPlacard,
                                                user_id, transaction_id);
                                        if (isPporSp == 0) {
                                            placard_holder_array_non_optimise
                                                    .add(new placarddetails(
                                                            secondaryDisplayPlacard,
                                                            "sp"));
                                            placardCount++;
                                        } else if (isPporSp == 1) {
                                            placard_holder_array_non_optimise
                                                    .add(new placarddetails(
                                                            secondaryDisplayPlacard,
                                                            "pp"));
                                            placardCount++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // method to create the placard String based on the optimize
                // array, user id and transaction id
                imgString = cnstrctRspnStrng(placard_holder_array_non_optimise,
                        user_id, transaction_id);
                // Method to call get the group name based on inputs
                String resultWithDisplayGroupName = displayGroupName1(
                        ppArrayForGroupnameDisplayName.toString(), user_id,
                        transaction_id);
                int placardShow = 0;
                if (imgString.equals("")) {
                    imgString = "No placard Required";
                    placardShow = 0;
                } else {
                    placardShow = 1;
                }
                if (resultWithDisplayGroupName.length() > 4) {
                    String resultWithDisplayGroupNameArray[] = resultWithDisplayGroupName
                            .split("@@@");

                    finalValue = placardShow + "::"
                            + imgString.replace(".", "_") + "-GROUP-"
                            + resultWithDisplayGroupNameArray[0] + "-DISPLAY-"
                            + resultWithDisplayGroupNameArray[1];
                } else {
                    finalValue = placardShow + "::"
                            + imgString.replace(".", "_") + "-GROUP-"
                            + group_name + "-DISPLAY-" + display_name;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing constructResponse: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing constructResponse");
        } finally {
            try {
                close(resultSet1);
                close(resultSet2);
                close(resultSet3);
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
        return finalValue;
    }

    /**
     * Method to check if primaryPlacard with input secondaryPlacard is there or
     * not if yes return 1 so that add the secondaryPlacard as pp to
     * placard_holder_array
     *
     * @param secondaryDisplayPlacard
     * @param user_id
     * @param transaction_id
     *
     * @return 1 or 0
     */
    public int checkUnDisplay(String secondaryDisplayPlacard, String user_id,
                              String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        int count = DBConstants.ZERO;// variable to return the value of sql
        // record
        try {
            String updateDuplctPlcrds = "select count(id) from temptransaction where primary_placard = '"
                    + secondaryDisplayPlacard + "' and unnumber_display = 1";
            log.debug(TAG,"updateDuplctPlcrds" + updateDuplctPlcrds);
            dbConnection.resultSet = dbConnection.getResultSetForSelect(updateDuplctPlcrds, dbCodeInsert);
            while (dbConnection.hasNextElement(dbConnection.resultSet)) {
                count = dbConnection.getInt(dbConnection.resultSet,"count(id)");
            }
            if (count > DBConstants.ZERO) {
                return count;
            }
        }  catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing checkSpForBulk: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing checkSpForBulk");
        } finally {
            try {
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
        return count;
    }

    /**
     * Method to update non_exempt and non_optimise values to 0 to avoid them to
     * be considered further in processing
     *
     * @param user_id
     * @param transaction_id
     *
     * @return No return value the method updates the non_exempt and
     *         non_optimise values
     */
    public void updateDuplicatePlacards(String user_id, String transaction_id,
                                        ArrayList<placarddetails> placard_holder_array_optimise) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        try {
            for (int i = DBConstants.ZERO; i < placard_holder_array_optimise
                    .size(); i++) {
                String placard = placard_holder_array_optimise.get(i).placard;
                String updateDuplctPlcrds = "UPDATE  `temptransaction` SET  `non_exempt` = 0, `non_optimise` = 0"
                        + " WHERE primary_placard = '"
                        + placard
                        + "' and user_id = "
                        +"\""+ user_id+"\""
                        + " and transaction_id = "
                        +"\""+ transaction_id+"\""
                        + " and swap_for_danger = 1";
                dbConnection.getResultSetForInsertOrUpdate(updateDuplctPlcrds, dbCodeInsert);
            }
        }  catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing updateDuplicatePlacards: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing updateDuplicatePlacards");
        } finally {
            try {
                if (dbConnection!= null) {
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
    }

    /***
     * construct placard String based on userid and transactionid
     *
     * @param placard_holder_array_optimise
     * @param user_id
     * @param transaction_id
     * @return
     */
    public String cnstrctRspnStrng(
            ArrayList<placarddetails> placard_holder_array_optimise,
            String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet1 = null;
        Cursor resultSet2 = null;
        Cursor resultSet3 = null;
        String imgString = "";
        int unStyle = 0;
        try {
            for (int placards = 0; placards < placard_holder_array_optimise
                    .size(); placards++) {

                placarddetails pd = placard_holder_array_optimise.get(placards);
                String PP1 = pd.placard;
                String type = pd.type;
                String unNumberForDisplay = null;
                String pPlacard = PP1.replace(".", "_");
                if (type.equals("pp")) {
                    String selectqueryForDstnctNmbr = "select distinct un_number,untype,un_style from temptransaction where primary_placard ="
                            + "\""
                            + PP1
                            + "\""
                            + " and un_class_id != 61 and un_class_id != 92 and primary_placard not like '7%' and primary_placard not like '1%' and (erap_number != '' or unnumber_display = 1 or (untype = 'l' or untype = 'g'))";
                    System.out.println("selectTableSQLForERAP,UNUMBER,UNTYPE:"
                            + selectqueryForDstnctNmbr);
                    log.debug(TAG,"selectTableSQLForERAP,UNUMBER,UNTYPE:"
                            + selectqueryForDstnctNmbr);
                    resultSet1 = dbConnection.getResultSetForSelect(selectqueryForDstnctNmbr, dbCodeInsert);
                    while (dbConnection.hasNextElement(resultSet1)) {
                        boolean isunnumberRequired = false;
                        String un_number_local =dbConnection.getString(resultSet1,"un_number");
                        unStyle = dbConnection.getInt(resultSet1, "un_style");
                        String selectquery_e_l = "select case when count(*) > 0 then 'true' else 'false' end as erap_count from temptransaction where primary_placard = \""
                                + PP1
                                + "\""
                                + " and  un_number = "
                                + un_number_local
                                + "  and un_class_id != 61 and un_class_id != 92 and primary_placard not like '7%' and primary_placard not like '1%' and (erap_number != '' or unnumber_display = 1)";
                        log.debug(TAG,"selectTableSQLForERAP,UNUMBER_DISPLAY"
                                + selectquery_e_l);
                        resultSet2 = dbConnection.getResultSetForSelect(selectquery_e_l,dbCodeRead);
                        while (dbConnection.hasNextElement(resultSet2)) {
                            if (dbConnection.getBoolean(resultSet2,"erap_count")) {
                                isunnumberRequired = true;
                            }
                        }
                        if (isunnumberRequired) {
                            if (unNumberForDisplay == null) {
                                unNumberForDisplay = un_number_local;
                            } else {
                                unNumberForDisplay = unNumberForDisplay + "##"
                                        + un_number_local;
                            }
                            continue;
                        }
                        String sqlSelect = "select case when(sum(total_weight) / sum(number_of_units)) > 450 then 'true' else 'false' end as lflag from temptransaction where primary_placard = "
                                + "\""
                                + PP1
                                + "\""
                                + " and un_class_id != 61 and un_class_id != 92 and primary_placard not like '7%' and primary_placard not like '1%' and un_number in ("
                                + un_number_local + ")";

                        log.debug(TAG,"IFAG 662" + sqlSelect);

                        resultSet3 = dbConnection.getResultSetForSelect(sqlSelect,dbCodeRead);
                        while (dbConnection.hasNextElement(resultSet3)) {
                            isunnumberRequired = dbConnection.getBoolean(resultSet3,"lflag");
                        }

                        if (isunnumberRequired) {
                            if (unNumberForDisplay == null) {
                                unNumberForDisplay = un_number_local;
                            } else {
                                unNumberForDisplay = unNumberForDisplay + "##"
                                        + un_number_local;
                            }
                            continue;
                        }
                    }
                }

                if (unNumberForDisplay == null) {
                    log.debug(TAG,"in unnumber is null" + pPlacard + "typeeeeee "
                            + type);
                    imgString += pPlacard + "~~" + type + ",";
                    log.debug(TAG,"imgStringimgStringimgStringimgStringimgStringimgStringimgString"
                            + imgString);
                } else {
                    log.debug(TAG,"in unnumber is not null" + pPlacard
                            + "typeeeeee " + type);
                    if (unStyle == 2) {
                        String[] unNmbrAry = unNumberForDisplay.split("##");
                        String responseToUI = "";
                        for (int i = 0; i < unNmbrAry.length; i++) {
                            responseToUI += pPlacard + "~~" + type + "~~"
                                    + unNmbrAry[i] + ",";
                        }
                        imgString += responseToUI;
                        System.out.println("imgString" + imgString);
                    } else if (unStyle == 3) {

                        imgString += pPlacard + "~~" + type + "~~"
                                + unNumberForDisplay + ",";
                    }
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing constructResponse: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing constructResponse");
        } finally {
            try {
                close(resultSet1);
                close(resultSet2);
                close(resultSet3);
                if (dbConnection != null) {
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(TAG,"Exception caught while closing the connection"
                        + " message: " + e.getMessage());
            }
        }
        return imgString;
    }

    /**
     * update display_placard_mandatory, non_exempt, swap_for_danger based on
     * user_id and transaction_id
     *
     * @param user_id
     * @param transaction_id
     * @return mandatory value in Binary Format
     */
    public void checkExplosive(String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet1 = null;
        ArrayList<String> classOneArray = new ArrayList<String>();
        String updatePP = "";
        try {
            String getExplosiveValue = "select id,primary_placard from temptransaction where primary_placard like '1%'";
            log.debug(TAG,"getExplosiveValuegetExplosiveValuegetExplosiveValuegetExplosiveValue"
                    + getExplosiveValue);
            resultSet1 = dbConnection.getResultSetForSelect(getExplosiveValue,dbCodeRead);
            arrayOfTransactions = convertResultSetToJSONArray(resultSet1);
            log.debug(TAG,"arrayOfTransactions : " + arrayOfTransactions);
            log.debug(TAG,"arrayOfTransactions length  : "
                    + arrayOfTransactions.length());
            transactionsBasedOnIds = getIdbasedData(arrayOfTransactions);
            log.debug(TAG,"transactionsBasedOnIds : " + transactionsBasedOnIds);
            if (arrayOfTransactions.length() == 1) {
                JSONObject jsonObjectinDataArray = arrayOfTransactions
                        .getJSONObject(0);
                updatePP = jsonObjectinDataArray.getString("primary_placard");
            } else if (arrayOfTransactions.length() == 2) {
                JSONObject idandPrimary = getPrimaryPlacardAndIds();
                JSONArray primaryPlcardList = idandPrimary
                        .getJSONArray("ppArray");
                String firstPP = primaryPlcardList.getString(0);
                String secondPP = primaryPlcardList.getString(1);
                if (firstPP.equalsIgnoreCase("1")) {
                    firstPP = "1.1";
                }
                if (secondPP.equalsIgnoreCase("1")) {
                    secondPP = "1.1";
                }
                if (firstPP.equalsIgnoreCase("1s")) {
                    firstPP = "1.4";
                }
                if (secondPP.equalsIgnoreCase("1s")) {
                    secondPP = "1.4";
                }
                log.debug(TAG,"firstPP : " + firstPP + " secondPP :" + secondPP);
                log.debug(TAG,classSegregationData.getJSONObject(firstPP)
                        .getString(secondPP));
                if ((firstPP.equals("1.4") && secondPP.equals("1.6"))
                        || (firstPP.equals("1.6") && secondPP.equals("1.4"))) {
                    updatePP = displayOneFourAndOneSixLogic(1, user_id,
                            transaction_id);
                } else {
                    updatePP = classSegregationData.getJSONObject(firstPP)
                            .getString(secondPP);
                    if (updatePP.equals("1.1")) {
                        updatePP = "1";
                    }
                }
                log.debug(TAG,"result :*** " + idandPrimary);
            } else if (arrayOfTransactions.length() > 2) {
                JSONObject idandPrimary = getPrimaryPlacardAndIds();
                JSONArray primaryPlacardList = idandPrimary
                        .getJSONArray("ppArray");
                String pp = "";
                for (int i = 0; i < primaryPlacardList.length(); i++) {
                    pp = primaryPlacardList.getString(i);
                    if (pp.equalsIgnoreCase("1")) {
                        pp = "1.1";
                    }
                    if (pp.equalsIgnoreCase("1s")) {
                        pp = "1.4";
                    }
                    classOneArray.add(pp);
                }
                Collections.sort(classOneArray);
                if (classOneArray.contains("1.5")) {
                    classOneArray.remove("1.5");
                    classOneArray.add(classOneArray.size(), "1.5");
                }
                if ((classOneArray.get(0).toString().equals("1.4") && classOneArray
                        .get(classOneArray.size() - 1).toString().equals("1.6"))
                        || (classOneArray.get(0).toString().equals("1.6") && classOneArray
                        .get(classOneArray.size() - 1).toString()
                        .equals("1.4"))) {
                    updatePP = displayOneFourAndOneSixLogic(0, user_id,
                            transaction_id);
                } else {
                    updatePP = classSegregationData.getJSONObject(
                            classOneArray.get(0).toString()).getString(
                            classOneArray.get(classOneArray.size() - 1)
                                    .toString());
                    if (updatePP.equals("1.1")) {
                        updatePP = "1";
                    }
                }
                log.debug(TAG,"array of transaction : " + classOneArray);
            }
            log.debug(TAG,"TAG,update pp : ****** " + updatePP);
            String updateClassOnePlacard = "UPDATE  `temptransaction` SET  `primary_placard` =  '"
                    + updatePP
                    + "' WHERE user_id = "
                    +"\""+ user_id+"\""
                    + " and transaction_id = "
                    + "\""+transaction_id+"\""
                    + " and primary_placard like '1%' ";
            log.debug(TAG,"class one update query" + updateClassOnePlacard);
            dbConnection.getResultSetForInsertOrUpdate(updateClassOnePlacard,dbCodeInsert);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing checkExplosive: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing checkExplosive");
        } finally {
            try {
                if (dbConnection != null) {
                    close(resultSet1);
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * update display_placard_mandatory, non_exempt, swap_for_danger based on
     * user_id and transaction_id only the input class has weight greater than
     * 1000 kgs
     *
     * @param user_id
     * @param user_id
     * @param transaction_id
     * @return mandatory value in Binary Format
     */
    public void checkThousandRule(String user_id, String transaction_id,
                                  String primary_placard) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        try {
            String update1000kgAndMandatoryPlacard = "UPDATE `temptransaction` SET `display_placard_mandatory`='1', `non_exempt`='"
                    + DBConstants.ONE
                    + "', `non_optimise`='"
                    + DBConstants.ONE
                    + "', `swap_for_danger`='"
                    + DBConstants.ZERO
                    + "', consignor_1000kg = '"
                    + DBConstants.ONE
                    + "' WHERE `primary_placard`= '"
                    + primary_placard
                    + "'";
            dbConnection.getResultSetForInsertOrUpdate(update1000kgAndMandatoryPlacard, dbCodeInsert);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing checkThousandRule: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing checkThousandRule");
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(TAG,"Exception caught while closing the connection"
                        + " message: " + e.getMessage());
            }
        }
    }


    /**
     * rajasekhar added this method this method write for resolving the two
     * placards to one palcard update display_placard_mandatory, non_exempt,
     * swap_for_danger based on user_id and transaction_id
     *
     * @param user_id
     * @param transaction_id
     * @return mandatory value in Binary Format
     */
    public void checkExplosiveForBasic(String user_id, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet1 = null;
        ArrayList<String> classOneArray = new ArrayList<String>();
        String updatePP = "";
        try {
            String getExplosiveValue = "select id,primary_placard from temptransaction where primary_placard like '1%'";
            log.debug(TAG,"getExplosiveValuegetExplosiveValuegetExplosiveValuegetExplosiveValue"
                    + getExplosiveValue);
            resultSet1 = dbConnection.getResultSetForSelect(getExplosiveValue,dbCodeRead);
            arrayOfTransactions = convertResultSetToJSONArray(resultSet1);
            transactionsBasedOnIds = getIdbasedData(arrayOfTransactions);
            if (arrayOfTransactions.length() == 1) {
                JSONObject jsonObjectinDataArray = arrayOfTransactions
                        .getJSONObject(0);
                updatePP = jsonObjectinDataArray.getString("primary_placard");
            } else if (arrayOfTransactions.length() == 2) {
                JSONObject idandPrimary = getPrimaryPlacardAndIds();
                JSONArray primaryPlcardList = idandPrimary
                        .getJSONArray("ppArray");
                String firstPP = primaryPlcardList.getString(0);
                String secondPP = primaryPlcardList.getString(1);
                if (firstPP.equalsIgnoreCase("1")) {
                    firstPP = "1.1";
                }
                if (secondPP.equalsIgnoreCase("1")) {
                    secondPP = "1.1";
                }
                if (firstPP.equalsIgnoreCase("1s")) {
                    firstPP = "1.4";
                }
                if (secondPP.equalsIgnoreCase("1s")) {
                    secondPP = "1.4";
                }
                log.debug(TAG,"firstPP : " + firstPP + " secondPP :" + secondPP);
//				log.debug(classSegregationData.getJSONObject(firstPP)
//						.getString(secondPP));
                if ((firstPP.equals("1.4") && secondPP.equals("1.6"))
                        || (firstPP.equals("1.6") && secondPP.equals("1.4"))) {
                    updatePP = displayOneFourAndOneSixLogic(0, user_id,
                            transaction_id);
                } else {
                    updatePP = classSegregationData.getJSONObject(firstPP)
                            .getString(secondPP);
                    if (updatePP.equals("1.1")) {
                        updatePP = "1";
                    }
                }
                log.debug(TAG,"result :*** " + idandPrimary);
            } else if (arrayOfTransactions.length() > 2) {
                JSONObject idandPrimary = getPrimaryPlacardAndIds();
                JSONArray primaryPlacardList = idandPrimary
                        .getJSONArray("ppArray");
                String pp = "";
                for (int i = 0; i < primaryPlacardList.length(); i++) {
                    pp = primaryPlacardList.getString(i);
                    if (pp.equalsIgnoreCase("1")) {
                        pp = "1.1";
                    }
                    if (pp.equalsIgnoreCase("1s")) {
                        pp = "1.4";
                    }
                    classOneArray.add(pp);
                }
                Collections.sort(classOneArray);
                if (classOneArray.contains("1.5")) {
                    classOneArray.remove("1.5");
                    classOneArray.add(classOneArray.size(), "1.5");
                }
                if ((classOneArray.get(0).toString().equals("1.4") && classOneArray
                        .get(classOneArray.size() - 1).toString().equals("1.6"))
                        || (classOneArray.get(0).toString().equals("1.6") && classOneArray
                        .get(classOneArray.size() - 1).toString()
                        .equals("1.4"))) {
                    updatePP = displayOneFourAndOneSixLogic(0, user_id,
                            transaction_id);
                } else {
                    updatePP = classSegregationData.getJSONObject(
                            classOneArray.get(0).toString()).getString(
                            classOneArray.get(classOneArray.size() - 1)
                                    .toString());
                    if (updatePP.equals("1.1")) {
                        updatePP = "1";
                    }
                }
                log.debug(TAG,"array of transaction : " + classOneArray);
            }
            log.debug(TAG,"update pp : ****** " + updatePP);
            String updateClassOnePlacard = "UPDATE  `temptransaction` SET  `primary_placard` =  '"
                    + updatePP
                    + "' WHERE user_id = "
                    +"\""+ user_id+"\""
                    + " and transaction_id = "
                    + "\""+transaction_id+"\""
                    + " and primary_placard like '1%' ";
            dbConnection.getResultSetForInsertOrUpdate(updateClassOnePlacard,dbCodeInsert);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing checkExplosive: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing checkExplosive");
        } finally {
            try {
                if (dbConnection != null) {
                    close(resultSet1);
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * Gets the class 1 segg table data
     *
     * @return JSONObject
     */
    private static final JSONObject class1SegregationData() {
        JSONObject outerData = new JSONObject();
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        try {
            String selectclass1segQuery = "SELECT  `group1` as class_group, `A`, `B`, `C`, `D`, `E`, `F`, `G`, `H`, `J`, `K`, `L`, `N`, `S` FROM `class1_group_compatibility`";
            resultSet = dbConnection.getResultSetForSelect(selectclass1segQuery,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet)) {
                JSONObject innerData = new JSONObject();
                innerData.put("A", dbConnection.getString(resultSet,"A"));
                innerData.put("B", dbConnection.getString(resultSet,"B"));
                innerData.put("C", dbConnection.getString(resultSet,"C"));
                innerData.put("D", dbConnection.getString(resultSet,"D"));
                innerData.put("E", dbConnection.getString(resultSet,"E"));
                innerData.put("F", dbConnection.getString(resultSet,"F"));
                innerData.put("G", dbConnection.getString(resultSet,"G"));
                innerData.put("H", dbConnection.getString(resultSet,"H"));
                innerData.put("J", dbConnection.getString(resultSet,"J"));
                innerData.put("K", dbConnection.getString(resultSet,"K"));
                innerData.put("L", dbConnection.getString(resultSet,"L"));
                innerData.put("N", dbConnection.getString(resultSet,"N"));
                innerData.put("S", dbConnection.getString(resultSet,"S"));
                outerData.put(dbConnection.getString(resultSet,"class_group"), innerData);

            }

        }  catch (Exception e) {
            e.printStackTrace();
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing class1SegregationData");
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outerData;
    }

    /****
     * Last changed 29-June-2016 by Rajasekhar store class1 total information in
     * json object
     *
     * @return
     */
    private static final JSONObject class1Compatibility() {
        JSONObject outerData = new JSONObject();
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        try {
            String selectclass1segQuery = "SELECT  `class1` as classname, `C1_1` as C1_1, `C1_2` as C1_2, `C1_3` as C1_3, `C1_4` as C1_4, `C1_5` as C1_5, `C1_6` as C1_6  FROM `class1_compatibility`";
            resultSet = dbConnection.getResultSetForSelect(selectclass1segQuery,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet)) {
                JSONObject innerData = new JSONObject();
                innerData.put("1.1", dbConnection.getString(resultSet,"C1_1"));
                innerData.put("1.2", dbConnection.getString(resultSet,"C1_2"));
                innerData.put("1.3", dbConnection.getString(resultSet,"C1_3"));
                innerData.put("1.4", dbConnection.getString(resultSet,"C1_4"));
                innerData.put("1.5", dbConnection.getString(resultSet,"C1_5"));
                innerData.put("1.6", dbConnection.getString(resultSet,"C1_6"));
                outerData.put(dbConnection.getString(resultSet,"classname"), innerData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing class1Compatibility");
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {

            }
        }
        return outerData;
    }

    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * Converts the given result set to JSON array
     *
     * @param rs
     * @return
     * @throws Exception
     */
    public JSONArray convertResultSetToJSONArray(Cursor rs) {
        JSONArray json = new JSONArray();
        try {
            while (rs.moveToNext()) {
                JSONObject obj = new JSONObject();
                String[] columnNames = rs.getColumnNames();
                for (int i = 0; i < rs.getColumnCount(); i++) {
                    String column_name = columnNames[i];
                    if (rs.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                        obj.put(column_name, rs.getInt(i));
                    } else if (rs.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                        obj.put(column_name, rs.getBlob(i));
                    } else if (rs.getType(i) == Cursor.FIELD_TYPE_FLOAT) {
                        obj.put(column_name, rs.getFloat(i));
                    } else {
                        obj.put(column_name, rs.getString(i));
                    }
                }
                json.put(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * Creates a JSONObject getIdbasedData
     *
     * @param arrayOfTransactions
     * @return JSONObject
     */
    private static final JSONObject getIdbasedData(JSONArray arrayOfTransactions) {
        JSONObject outerData = new JSONObject();
        try {
            for (int i = 0; i < arrayOfTransactions.length(); i++) {
                outerData.put(arrayOfTransactions.getJSONObject(i).get("id")
                        .toString(), arrayOfTransactions.getJSONObject(i));
            }
        } catch (Exception se) {
            se.printStackTrace();
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "SQL Exception caught while converting getIdbasedData");
        }
        return outerData;
    }

    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * It gives getPrimaryPlacardAndIds
     *
     * @return JSONObject
     */
    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * It gives getPrimaryPlacardAndIds
     *
     * @return JSONObject
     */
    public JSONObject getPrimaryPlacardAndIds() {
        JSONObject primaryPlacardObject = new JSONObject();
        JSONArray ppArray = new JSONArray(new HashSet<String>());
        try {
            for (int i = 0; i < arrayOfTransactions.length(); i++) { // iterate

                ppArray.put(arrayOfTransactions.getJSONObject(i).getString(
                        "primary_placard"));
            }
            primaryPlacardObject.put("ppArray", ppArray);
            log.debug(TAG,"idAndBill test " + primaryPlacardObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing getPrimaryPlacardAndIds"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing getPrimaryPlacardAndIds");
        }
        return primaryPlacardObject;
    }

    /**
     * Last changed 28-June-2016 by Rajasekhar
     *
     * Remove Duplicates values from given list
     *
     * @param strArr
     *            String array with duplicate values
     * @return strArr String array without duplicate values
     */
    public List<Integer> removeDuplicates(List<Integer> strArr) {
        Set<Integer> hs = new HashSet<>();
        hs.addAll(strArr);
        strArr.clear();
        strArr.addAll(hs);
        return strArr;
    }

    /**
     *
     * @param array
     * @param user_id
     * @param transaction_id
     * @return
     */
    public String displayGroupName1(String array, String user_id,
                                    String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet1 = null;
        Cursor resultSet2 = null;
        String resultWithGroupDisplayName = null;
        try {
            String gNameToDisplay = "";
            String display_name = "";
            // String group_name = "";
            if ((array.toString().contains("2.1")
                    || array.toString().contains("2")
                    || array.toString().contains("3")
                    || array.toString().contains("4.1")
                    || array.toString().contains("4")
                    || array.toString().contains("5.1")
                    || array.toString().contains("5")
                    || array.toString().contains("6.1")
                    || array.toString().contains("6")
                    || array.toString().contains("\"1\"") || array.toString()
                    .contains("1."))
                    && array.toString().contains("\"1\"")
                    || array.toString().contains("1.")) {
                // JSONArray gName = new JSONArray();
                String gNameInString = null;
                String slectQueryForGnandDn = "select id,group_name from temptransaction where primary_placard like '1%' GROUP BY group_name";
                resultSet1 = dbConnection.getResultSetForSelect(slectQueryForGnandDn,dbCodeRead);
                log.debug(TAG,"query" + slectQueryForGnandDn+" count "+resultSet1.getCount());
                arrayOfTransactions = convertResultSetToJSONArray(resultSet1);
                transactionsBasedOnIds = getIdbasedData(arrayOfTransactions);
                if (arrayOfTransactions.length() == 1) {
                    JSONObject jsonObjectinDataArray = arrayOfTransactions
                            .getJSONObject(0);
                    gNameToDisplay = jsonObjectinDataArray
                            .getString("group_name");
                    //log.debug(TAG,"hii " + gNameToDisplay);
                } else if (arrayOfTransactions.length() == 2) {
                    JSONObject idandGroupName = getGroupNameAndIds();
                    JSONArray groupNameList = idandGroupName
                            .getJSONArray("groupNamearray");
                    String firstGroupName = groupNameList.getString(0);
                    String secondGroupName = groupNameList.getString(1);
                    gNameToDisplay = class1SegregationData.getJSONObject(
                            firstGroupName).getString(secondGroupName);
                } else if (arrayOfTransactions.length() > 2) {
                    JSONObject idandGroupName = getGroupNameAndIds();
                    JSONArray groupNameList = idandGroupName
                            .getJSONArray("groupNamearray");
                    gNameInString = groupNameList.toString();
                    if ((gNameInString.contains("C")
                            || gNameInString.contains("D") || gNameInString
                            .contains("E")) && gNameInString.contains("N")) {
                        gNameToDisplay = "D";
                    } else if (gNameInString.contains("C")
                            || gNameInString.contains("D")
                            || gNameInString.contains("E")) {
                        if (gNameInString.length() == 9) {
                            char abc[] = gNameInString.toCharArray();
                            if (abc[2] == abc[6]) {
                                gNameToDisplay = Character.toString(abc[2]);
                            } else {
                                gNameToDisplay = "E";
                            }
                        } else {
                            gNameToDisplay = "E";
                        }
                        log.debug(TAG,"gNameToDisplay7" + gNameToDisplay);
                    }
                }
                ArrayList<String> classOneArray = new ArrayList<>();
                String selectQueryForClass = "select id,name from temptransaction where primary_placard like '1%' order by name ";
                resultSet2 = dbConnection.getResultSetForSelect(selectQueryForClass,dbCodeRead);
                arrayOfTransactions = convertResultSetToJSONArray(resultSet2);
                transactionsBasedOnIds = getIdbasedData(arrayOfTransactions);
                if (arrayOfTransactions.length() == 1) {
                    JSONObject jsonObjectinDataArray = arrayOfTransactions
                            .getJSONObject(0);
                    display_name = jsonObjectinDataArray.getString("name");
                    // log.debug(TAG,"display_name final " + display_name);
                } else if (arrayOfTransactions.length() == 2) {
                    JSONObject idAndDisplayName = getDisplayNameAndIds();
                    JSONArray displayNameListList = idAndDisplayName
                            .getJSONArray("displayNameArray");
                    String displayNameFirst = displayNameListList.getString(0);
                    String displayNameSecond = displayNameListList.getString(1);
                    if (displayNameFirst.equalsIgnoreCase("1.4S")) {
                        displayNameFirst = "1.4";
                    }
                    if (displayNameSecond.equalsIgnoreCase("1.4S")) {
                        displayNameSecond = "1.4";
                    }
                    display_name = classSegregationData.getJSONObject(
                            displayNameFirst).getString(displayNameSecond);
                } else if (arrayOfTransactions.length() > 2) {
                    JSONObject idAndDisplayName = getDisplayNameAndIds();
                    JSONArray displayNameListList = idAndDisplayName
                            .getJSONArray("displayNameArray");
                    String nameInList = "";
                    for (int i = 0; i < displayNameListList.length(); i++) {
                        nameInList = displayNameListList.getString(i);
                        if (nameInList.equalsIgnoreCase("1.4S")) {
                            nameInList = "1.4";
                        }
                        classOneArray.add(nameInList);
                    }
                    Collections.sort(classOneArray);
                    if (classOneArray.contains("1.5")) {
                        classOneArray.remove("1.5");
                        classOneArray.add(classOneArray.size(), "1.5");
                    }
                    display_name = classSegregationData.getJSONObject(
                            classOneArray.get(0).toString()).getString(
                            classOneArray.get(classOneArray.size() - 1)
                                    .toString());
                }
            }
            log.debug(TAG,"gNameToDisplay final " + gNameToDisplay);
            resultWithGroupDisplayName = gNameToDisplay + "@@@" + display_name;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing displayGroupName: "
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing displayGroupName");
        } finally {
            try {
                if (dbConnection != null) {
                    close(resultSet1);
                    close(resultSet2);
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultWithGroupDisplayName;
    }

    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * It gives getGroupNameAndIds
     *
     * @return JSONObject
     */
    public JSONObject getGroupNameAndIds() {
        JSONObject groupNameObject = new JSONObject();
        JSONArray groupNameArray = new JSONArray(
                new HashSet<String>());
        try {
            for (int i = 0; i < arrayOfTransactions.length(); i++) { // iterate

                groupNameArray.put(arrayOfTransactions.getJSONObject(i)
                        .getString("group_name"));
            }
            groupNameObject.put("groupNamearray", groupNameArray);
            log.debug(TAG,"getGroupNameAndIds " + groupNameObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing getGroupNameAndIds"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing getGroupNameAndIds");
        }
        return groupNameObject;
    }

    /**
     * Last changed 29-June-2016 by Rajasekhar
     *
     * It gives list of getDisplayNameAndIds
     *
     * @return JSONObject
     */
    public JSONObject getDisplayNameAndIds() {
        JSONObject displayNameObject = new JSONObject();
        JSONArray displayNameArray = new JSONArray(
                new HashSet<String>());
        try {
            for (int i = 0; i < arrayOfTransactions.length(); i++) { // iterate

                displayNameArray.put(arrayOfTransactions.getJSONObject(i)
                        .getString("name"));
            }
            displayNameObject.put("displayNameArray", displayNameArray);
            log.debug(TAG,"displayNameArray " + displayNameObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(TAG,"Exception caught while executing getDisplayNameAndIds"
                    + " message: " + e.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while executing getDisplayNameAndIds");
        }
        return displayNameObject;
    }

    private static void close(final Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
    /**
     *
     * @param user_id
     * @return
     */
    public String displayOneFourAndOneSixLogic(int type, String user_id,
                                               String transactionId) {
        String updatPP = "";
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        double totalWightForOnePointFour = 0.0;
        double totalWightForOnePointSix = 0.0;
        String primaryPlcard = "";
        try {
            String displayOneFourAndOneSixLogicQuery = "";
            // type 0 means basic optimize
            if (type == 0) {
                displayOneFourAndOneSixLogicQuery = "select primary_placard,total_weight from temptransaction where (primary_placard = '1.4' or primary_placard = '1.6')";
            } else {
                displayOneFourAndOneSixLogicQuery = "select primary_placard,total_weight from temptransaction where (primary_placard = '1.4' or primary_placard = '1.6') and (non_exempt = 1 or non_optimise = 1)";
            }

            log.debug(TAG,"In displayOneFourAndOneSixLogic :"
                    + displayOneFourAndOneSixLogicQuery);
            resultSet = dbConnection.getResultSetForSelect(displayOneFourAndOneSixLogicQuery,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet)) {
                primaryPlcard = dbConnection
                        .getString(resultSet,"primary_placard");
                if (primaryPlcard.equals("1.4")) {
                    totalWightForOnePointFour += dbConnection
                            .getDouble(resultSet,"total_weight");
                }
                if (primaryPlcard.equals("1.6")) {
                    totalWightForOnePointSix += dbConnection
                            .getDouble(resultSet,"total_weight");
                }
            }
            log.debug(TAG,"In totalWightForOnePointFour :"
                    + totalWightForOnePointFour);
            log.debug(TAG,"In totalWightForOnePointSix :"
                    + totalWightForOnePointSix);
            if (totalWightForOnePointFour >= 1001
                    && totalWightForOnePointSix >= 10) {
                updatPP = "1.4";
            } else if (totalWightForOnePointFour >= 1001
                    && totalWightForOnePointSix < 10) {
                updatPP = "1.4";
            } else if (totalWightForOnePointFour < 1001
                    && totalWightForOnePointSix > 10) {
                updatPP = "1.6";
            } else if (totalWightForOnePointFour < 1001
                    && totalWightForOnePointSix < 10) {
                updatPP = "1.4";
            }

        } catch (Exception se) {
            se.printStackTrace();
            log.error(TAG,"SQL Exception caught while getting displayOneFourAndOneSixLogic from database: "
                    + " message: " + se.getMessage());
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "SQL Exception caught while getting displayOneFourAndOneSixLogic from database");
        } finally {
            try {
                if (dbConnection != null) {
                    close(resultSet);
                    dbConnection.closeConnections();
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error(TAG,"Exception caught while closing the connection"
                        + " message: " + e.getMessage());
            }
        }
        return updatPP;
    }

    @Override
    public Object getInstance() {
        return null;
    }
}