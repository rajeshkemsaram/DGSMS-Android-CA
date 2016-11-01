package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.ideabytes.dgsms.ca.database.DeleteDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.Arrays;

/*******************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogIbc
 * author:  Suman
 * Description : Customized dialog to show IBC status
 *****************************************************/
public class AlertDialogLicenseExpaired extends Dialog implements DBConstants {
	private Activity parent;

	public AlertDialogLicenseExpaired(Activity parent) {
		super(parent);
		this.parent = parent;
	}

	/**
	 *
	 * @param message message to display on alert dialog
	 * Author : Suman
	 */
	public void showdialog(final String message) {
		try {
			final Dialog dialogLicenseExpaired = new Dialog(parent,R.style.PauseDialog);
			dialogLicenseExpaired.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogLicenseExpaired.setContentView(R.layout.alert_dialog);
			dialogLicenseExpaired.setCanceledOnTouchOutside(false);
			TextView tvTitle = (TextView)dialogLicenseExpaired
					.findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
			tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));
			TextView tvMessage = (TextView)dialogLicenseExpaired
					.findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
			tvMessage.setText(message);
			Button btnOk = (Button)dialogLicenseExpaired
					.findViewById(R.id.Alert_Dialog_Btn_Ok);
			btnOk.setText(Utils.getResString(R.string.Dialog_Btn_Exit));

			btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) parent).finish();
                    //once expired message comes, delete all tables data from database
                    // so that process starts from first
                    new DeleteDBData(parent.getApplicationContext()).deleteRulesData();
					new DeleteDBData(parent.getApplicationContext()).deleteConfigDetails();
                    dialogLicenseExpaired.cancel();
                }
            });
			//On back/home button press dismiss dialog as well as close the activity
			dialogLicenseExpaired.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							((Activity) parent).finish();
							new DeleteDBData(parent.getApplicationContext()).deleteRulesData();
							dialog.dismiss();
						} else if ( keyCode == KeyEvent.KEYCODE_HOME) {
                            ((Activity) parent).finish();
                            new DeleteDBData(parent.getApplicationContext()).deleteRulesData();
                            dialog.dismiss();
                        }
					return true;
				}
			});

			dialogLicenseExpaired.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
