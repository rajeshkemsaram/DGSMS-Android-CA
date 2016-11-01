package org.com.ca.dgsms.ca.model;

import com.ideabytes.dgsms.ca.utils.Utils;

import org.json.JSONObject;

import java.util.Arrays;

public class DeleteTransaction implements DBConstants {
	static String finalvalue = null;

	public void deleteFromTransaction(String col_id, String userid,
			String maxplacard, String transactionid) {
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("user_id", userid);
			jObject.put("maxPlacard", maxplacard);
			jObject.put("col_id", col_id);
			jObject.put("transaction_id", userid);
			String tosp = jObject.toString();
			//new DatabaseConf(MainActivity.getContext()).updateTwoPointTwoToZeroFromDelete(colid,userid,transactionid);
			InsertIntoTransctn insertIntoTransctn = new InsertIntoTransctn();
			insertIntoTransctn.deleteFromTransaction(tosp);
			} catch (Exception e) {
			Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, TEXT_FILE_NAME, "Error::DeleteTransaction::deleteFromTransaction()" +
					Arrays.toString(e.getStackTrace()));
		}

	}

	@Override
	public Object getInstance() {
		return null;
	}
}