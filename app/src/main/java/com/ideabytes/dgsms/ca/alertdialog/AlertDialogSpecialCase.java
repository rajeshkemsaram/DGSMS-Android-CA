package com.ideabytes.dgsms.ca.alertdialog;

import java.util.ArrayList;
import java.util.Arrays;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.adapters.CustomAdapterSpecialCase;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/*******************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogSpecialCase
 * author:  Suman
 * Description : Customized dialog to show special case descriptions
 ********************************************************************/
public class AlertDialogSpecialCase extends Dialog implements DBConstants {

	private Activity activity;
	public AlertDialogSpecialCase(Activity activity) {
		super(activity);
		this.activity = activity ;
	}

	/**
	 * This method pop ups a drop down list with no of descriptions that are
	 * related to the UN Numbers if erap details response length is greater than
	 * 
	 * author Suman
	 * @param specialCaseList list special cases
	* @since 5.0
	 */
//	public void showDialog(final ArrayList<JSONObject> specialCaseList) {
	 public void showDialog(final JSONArray specialCaseList) {

		try {
			final Dialog dialogSpecialCase = new Dialog(activity);
			dialogSpecialCase.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogSpecialCase.setContentView(R.layout.custom_dialog_title);
			dialogSpecialCase.setCanceledOnTouchOutside(false);
			dialogSpecialCase.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView title = (TextView) dialogSpecialCase
					.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
			title.setText(Utils.getResString(R.string.Select_Description));

			CustomAdapterSpecialCase myShowList = new CustomAdapterSpecialCase(
					specialCaseList,activity,dialogSpecialCase);

			ListView lv1 = (ListView) dialogSpecialCase
					.findViewById(R.id.Dialoag_ListView);

			getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));

			lv1.setAdapter(myShowList);
			
			//back/home button lister, don't cancel dialog on back/home button press 
			dialogSpecialCase.setOnKeyListener(new OnKeyListener() {

	            @Override
	            public boolean onKey(DialogInterface arg0, int keyCode,
	                    KeyEvent event) {
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	                    Toast.makeText(activity, "Select description", Toast.LENGTH_SHORT)
	                    .show();
	                } else if (keyCode == KeyEvent.KEYCODE_HOME) {
	                    Toast.makeText(activity, "Select description", Toast.LENGTH_SHORT)
	                    .show();
	                }
	                return true;
	            }
	        });
			
			dialogSpecialCase.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void showDialog1(final ArrayList<String> specialCaseList) {
				int d=1;
		try {
			final Dialog dialogSpecialCase = new Dialog(activity);
			dialogSpecialCase.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogSpecialCase.setContentView(R.layout.custom_dialog_title);
			dialogSpecialCase.setCanceledOnTouchOutside(false);
			dialogSpecialCase.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView title = (TextView) dialogSpecialCase
					.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
			title.setText(Utils.getResString(R.string.Select_Description));

			CustomAdapterSpecialCase myShowList = new CustomAdapterSpecialCase(
					specialCaseList,activity,dialogSpecialCase, d);

			ListView lv1 = (ListView) dialogSpecialCase
					.findViewById(R.id.Dialoag_ListView);

			getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));

			lv1.setAdapter(myShowList);

			//back/home button lister, don't cancel dialog on back/home button press
			dialogSpecialCase.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int keyCode,
									 KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						Toast.makeText(activity, "Select description", Toast.LENGTH_SHORT)
								.show();
					} else if (keyCode == KeyEvent.KEYCODE_HOME) {
						Toast.makeText(activity, "Select description", Toast.LENGTH_SHORT)
								.show();
					}
					return true;
				}
			});

			dialogSpecialCase.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Object getInstance() {
		return null;
	}
}
