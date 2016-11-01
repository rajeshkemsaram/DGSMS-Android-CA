package com.ideabytes.dgsms.ca.josn;

import org.json.JSONArray;
import org.json.JSONObject;

/*******************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : SyncWithServer
 * Created Date : 03-11-2015
 * Modified on : 03-11-2015
 * author:  Suman
 * Description : This class to return json array based on url and json response from service
 ********************************************************************/
public class JSONParser {

    public JSONArray getJsonArray(final String response,final String jsonKey) {
        JSONArray jsonArray = null;
        try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsnobject2 = jsonObject1.getJSONObject("results");
                JSONObject jsnobject3 = jsnobject2.getJSONObject("result");
                JSONObject jsnobject4 = jsnobject3.getJSONObject("results");
                jsonArray = jsnobject4.getJSONArray(jsonKey);

                //System.out.println("json key = "+jsonKey+" json values = "+jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}

