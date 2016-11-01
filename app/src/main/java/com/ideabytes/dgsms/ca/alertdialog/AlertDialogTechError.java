package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;

import org.com.ca.dgsms.ca.model.DBConstants;


import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/*******************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogTechError
 * author:  Suman
 * Description : Customized dialog to display "authorization message"
 *********************************************************************/
public class AlertDialogTechError extends Dialog implements View.OnClickListener,DBConstants {
	private Dialog dialogTechEroor = null;
	private Context context;

	public AlertDialogTechError(Context context) {
		super(context);
		this.context = context;
	}
	/**
	 * This Class alerts Error/abnormal messages
	 * 
	 * author Suman
	 * @param message message to display on alert dialog
	 * @since 5.0 
	 */
	public void showDialog(String message) {
		try {
			dialogTechEroor = new Dialog(context, R.style.PauseDialog);
			dialogTechEroor.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogTechEroor.setContentView(R.layout.alert_dialog);
			dialogTechEroor.setCanceledOnTouchOutside(false);

			TextView title = (TextView) dialogTechEroor
					.findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
			title.setText("Alert Message");

			TextView msg = (TextView) dialogTechEroor
					.findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
			msg.setText(message);

			Button ok = (Button) dialogTechEroor.findViewById(R.id.Alert_Dialog_Btn_Ok);
			ok.setText("OK");
			ok.setOnClickListener(this);

			//On back/home button press dismiss dialog as well as close the activity
			dialogTechEroor.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						//delete config details so that it executes from starting 
						((Activity) context).finish();
						dialog.dismiss();
					} else if (keyCode == KeyEvent.KEYCODE_HOME) {
						((Activity) context).finish();
					}
					return true;
				}
			});

			dialogTechEroor.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//On ok button press dismiss dialog as well as close the activity
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.Alert_Dialog_Btn_Ok:
			dialogTechEroor.cancel();
			((Activity) context).finish();
			break;
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
