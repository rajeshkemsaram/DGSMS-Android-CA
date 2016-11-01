package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;


import com.ideabytes.dgsms.ca.adapters.CustomAdapterOnPickupOrders;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
/********************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogOnPickupOrders
 * author:  Suman
 * Description : Customized dialog to show pick up orders from web
 *******************************************************************/
public class AlertDialogOnPickupOrders extends Dialog implements DBConstants {

	private Activity activity;
	public AlertDialogOnPickupOrders(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	/**
	 * Shows alert dialog with pickup orders from web along with radio
	 * buttons, to select any of them (handles pushed orders from web)
	 * 
	 * author Suman
	 * @param list pick up order value as json array
	 * @return User Selected pickup order from the list displayed
	 * @since 5.0 
	 */
	public void showDialog(final JSONArray list) {
		try {
			Dialog dialogOrders = new Dialog(activity,R.style.PauseDialog);
			dialogOrders.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogOrders.setContentView(R.layout.custom_dialog_title);
			dialogOrders.setCanceledOnTouchOutside(false);
			//dialogMaxPlacards.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView title = (TextView) dialogOrders
					.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
			title.setText(Utils.getResString(R.string.Dialog_Message_Select_Orders));

			CustomAdapterOnPickupOrders myShowList = new CustomAdapterOnPickupOrders(
					list, activity,dialogOrders);

			ListView lv1 = (ListView) dialogOrders
					.findViewById(R.id.Dialoag_ListView);

			getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			lv1.setAdapter(myShowList);

			dialogOrders.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Object getInstance() {
		return null;
	}
}
