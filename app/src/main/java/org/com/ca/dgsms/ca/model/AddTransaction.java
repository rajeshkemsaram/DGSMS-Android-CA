package org.com.ca.dgsms.ca.model;

import android.database.Cursor;
import android.util.Log;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.utils.Utils;

import org.com.ca.ca.exceptions.DGSMSNestableException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : AddTransaction
 * author:  Suman
 * Description : this class is to save all transactions into table transaction_details to display placard
 * Created Date : 23-11-2015
 * Modified Date : 23-11-2015
 ****************************************************************/
public class AddTransaction implements DBConstants {

    @Override
    public Object getInstance() {
        return null;
    }
    //to get data need readable database object
    private static int dbCodeRead = 1;
    private static int dbCodeInsert = 0;
    class placarddetails {
        String placard;
        String type;

        public placarddetails(String placard, String type) {
            this.placard = placard;
            this.type = type;
        }
    }
    // "/api/web/AddTransaction/{maxPlacard}.json"
    public String PrintValues(final String User_id,final JSONObject jsnobject) {
        Boolean elevated = false;
        String action = "DISPLAYPLACARDWITHDG";
        String session_id = "";
        String module = "DISPLAYPLACARDWITHDG";
        String ip_address = "";
        String data = "";
        int utilized_time = 0;
        String statusForActivitylog = null;
        int userIdForLog = 0;
        int status = 0;
        boolean dgValidator = false;
        // initiate the timer
        final long startTime = System.currentTimeMillis();
        try {
            String finalvalue = null;
                String errmsg = null;
                String errcode = null;
                String group_name = null;
                String display_name = null;
                int placard_holders = 1;//TODO need to change this
                String transaction_id = User_id;
               // System.out.println("user input "+jsnobject);
                String tosp = jsnobject.toString();

                Object resultt = InsertIntoTransctn.processTransaction(tosp,
                        placard_holders);
                errcode = ((Values) resultt).error_code1;
                errmsg = ((Values) resultt).error_msg1;
                group_name = ((Values) resultt).group_name1;
                display_name = ((Values) resultt).display_name1;
                if (errcode.equals("99")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("error_code", errcode);
                    map.put("status", "00");
                    map.put("error_msg", errmsg);
                    map.put("placard_holder", "0");
//                    rep = PrepareStatusResponse.prepareSucessResponse(this,
//                            map, getAcceptMediaType());
//                    getResponse().setStatus(Status.SUCCESS_OK);
                    statusForActivitylog = "error_case";

                    return errcode+"::"+errmsg;
                } else {
                    finalvalue = new PlacardDisplayLogic().placardDisplayLogic(
                            User_id, transaction_id);
                    String finalValueArray[] = finalvalue.split("@@@");
                    int Trans_id = transId(User_id, transaction_id);
                    String Trans_id1 = Integer.toString(Trans_id);
                    String placard_holder[] = finalValueArray[2]
                            .split("-COUNT-");
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("error_code", errcode);
                    map.put("status", "00");
                    map.put("error_msg", errmsg);
                    if (elevated) {
                        map.put("elevated_status", "1");
                    } else {
                        map.put("elevated_status", "0");
                    }
                    map.put("Basic_Optimize", finalValueArray[0]);
                    map.put("Non_Optimize", finalValueArray[1]);
                    map.put("Optimize", placard_holder[0]);
                    map.put("placard_holder", placard_holder[1]);

                    return finalvalue;
                }


        } catch (DGSMSNestableException e) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, TEXT_FILE_NAME, "Error::AddTransaction::final value from dg logic" +
                    Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while getting final value from dg logic ");
        }
    }

    public int transId(String user_id1, String transaction_id) {
        DBConnection dbConnection = new DBConnection(HomeActivity.getContext());
        Cursor resultSet = null;
        int trans_id = 0;
        try {
            String selectQuery = "select max(id) as id from "
                    + DBConstants.TABLE_TRANSACTIONS + " where user_id = "
                    +"\""+ user_id1 +"\""+ " and transaction_id = "
                    +"\""+ transaction_id+"\"" ;
            resultSet = dbConnection.getResultSetForSelect(selectQuery,dbCodeRead);
            while (dbConnection.hasNextElement(resultSet)) {
                int maxID = dbConnection.getInt(resultSet,"id");
                trans_id = maxID;
            }
        } catch (Exception se) {
            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, TEXT_FILE_NAME, "Error::AddTransaction::transId()" +
                    Arrays.toString(se.getStackTrace()));
           se.printStackTrace();
            throw new DGSMSNestableException(
                    DGSMSNestableException.CODE_INTERNAL_SERVER_ERROR,
                    "Exception caught while getting max_id from database");
        } finally
        {
            if (resultSet != null) {
                resultSet.close();
            }
            dbConnection.closeConnections();
        }
        return trans_id;
    }
}

