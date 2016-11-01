package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.HashMap;

import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.asynctask.AsyncTaskToVerifyLicense;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.networkcheck.DialogCheckNetConnectivity;
import com.ideabytes.dgsms.ca.reciever.NetworkUtil;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/********************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogVerifyLicense
 * author:  Suman
 * Description : Customized dialog to verify license for every 3 days
 * Modified Date : 27-10-2015
 *********************************************************************/
public class AlertDialogVerifyLicense extends Dialog implements DBConstants {

	private Activity parent;

	public AlertDialogVerifyLicense(Activity parent) {
		super(parent);
		this.parent = parent;
	}

    /**
     * Author : Suman
     * @param message message to display on alert dialog
     */
	public void showdialog(String message) {
		try {
			final Dialog dialogVerifyLicense = new Dialog(parent,R.style.PauseDialog);
			dialogVerifyLicense.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogVerifyLicense.setContentView(R.layout.alert_dialog);
			dialogVerifyLicense.setCanceledOnTouchOutside(false);
			TextView tvTitle = (TextView)dialogVerifyLicense
					.findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
			tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));
			TextView tvMessage = (TextView)dialogVerifyLicense
					.findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
			tvMessage.setText(Utils.getResString(R.string.Dialog_LICENSE_VERIFY));
			Button btnOk = (Button)dialogVerifyLicense
					.findViewById(R.id.Alert_Dialog_Btn_Ok);
			btnOk.setText(Utils.getResString(R.string.btn_ok));

			btnOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if(NetworkUtil.getConnectivityStatus(parent.getApplicationContext()) != 0) {
                           AsyncTaskToVerifyLicense asyncTaskToVerifyLicense = new AsyncTaskToVerifyLicense(parent,0);
                           asyncTaskToVerifyLicense.execute(SERVER_URL_VERIFY_LICENSE);
                            dialogVerifyLicense.dismiss();
						} else {
							DialogCheckNetConnectivity alertNoNet = new DialogCheckNetConnectivity(parent);
							alertNoNet.showDialog();
						}} catch (Exception e ) {
						e.printStackTrace();
						}

				}
			});
			//On back/home button press dismiss dialog as well as close the activity
			dialogVerifyLicense.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					new UpdateDBData(parent.getApplicationContext()).updateVerifyLicense("1");
					dialogVerifyLicense.cancel();
					parent.finish();
					return true;
				}
			});

			dialogVerifyLicense.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public Object getInstance() {
		return null;
	}
}
