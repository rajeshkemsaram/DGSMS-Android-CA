package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/**************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogMutualExclusion
 * author:  Suman
 * Description : Customized dialog to show Mutual exclusion among shipments
 ***************************************************************************/
public class AlertDialogMutualExclusion extends Dialog implements DBConstants {

	private Activity activity;
	public AlertDialogMutualExclusion(Activity activity) {
		super(activity);
		this.activity = activity ;
	}

	/**
	 * This method is used to show dialog on
	 * Mutual Exclusion means Class 1 related placards cannot be mixed with
	 * other class and vice versa
	 * 
	 * author Suman
	 * @param message message to display on alert dialog
	 * @param colId id
	 * @since 5.2 
	 */
	public void showDialog(String message,final int colId) {
		try {
			final Dialog dialogME = new Dialog(activity, R.style.PauseDialog);
			dialogME.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogME.setCanceledOnTouchOutside(false);
			dialogME.setContentView(R.layout.alert_dialog);
			//dialogDoNotLoad.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView tvTitle = (TextView) dialogME
					.findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
			tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));

			TextView tvMessage = (TextView) dialogME
					.findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
			tvMessage.setText(message);

			Button ok = (Button) dialogME
					.findViewById(R.id.Alert_Dialog_Btn_Ok);
			ok.setText(Utils.getResString(R.string.btn_ok));

			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				
					dialogME.dismiss();				
					updateOrders(colId);
				}
			});
			dialogME.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateOrders(int colId) {
		//once order is selected then change the status to 0 if it is in abnormal case , 
		//then it will display in notification 
		//for pickup orders	, this can be used once the previous placards delivered
		new UpdateDBData(activity.getApplicationContext()).updateOrders(0, colId);
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
