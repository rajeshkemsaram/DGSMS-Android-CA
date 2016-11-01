package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/*****************************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : AlertDialogEmergencyDetails
 * author:  Suman
 * Description : Customized dialog to show emergency details list
 ******************************************************************/
public class AlertDialogEmergencyDetails extends Dialog implements DBConstants {
	private Activity activity;
	public AlertDialogEmergencyDetails(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	/**
	 * This method shows emergency phone numbers details of a particular country
	 * based on section
	 * 
	 * author Suman
	 * @param titleDialog,details of emergency contacts
	 * @return User Selected country name
	 * @since 5.0 
	 */
	public void showdialog(final String titleDialog, final String details) {
		try {
			final Dialog dialogEmergencyDetails = new Dialog(activity, R.style.PauseDialog);
			dialogEmergencyDetails.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogEmergencyDetails.setContentView(R.layout.custom_dialog_emergency_details);
			dialogEmergencyDetails.setCanceledOnTouchOutside(false);
			//dialogChooseLanguage.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView title = (TextView) dialogEmergencyDetails
					.findViewById(R.id.Dialog_Em_Details_Title);//text view to display alert dialog title
			title.setText(titleDialog);// localization

			TextView message = (TextView) dialogEmergencyDetails
					.findViewById(R.id.Dialog_Em_Details_Data);//text view to display alert dialog message
			message.setTextColor(Color.BLACK);
			message.setText(Html.fromHtml(details));// localization

			ImageView close = (ImageView) dialogEmergencyDetails.findViewById(R.id.Dialog_Em_Details_Remove);
			close.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogEmergencyDetails.cancel();
				}
			});

			dialogEmergencyDetails.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}

