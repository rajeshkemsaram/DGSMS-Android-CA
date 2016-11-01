package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import org.com.ca.dgsms.ca.model.DBConstants;
import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;

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
import android.widget.LinearLayout.LayoutParams;
/************************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : AlerDialogInValidUnNumber
 * author:  Suman
 * Description : Customized dialog to show Invalid un number
 *************************************************************/
public class AlerDialogInValidUnNumber extends Dialog implements DBConstants {

	private Activity activity;//Activity reference

	public AlerDialogInValidUnNumber(Activity activity) {
		super(activity);
		this.activity = activity ;
	}

	/** 
	 * This method displays dialog on Invalid UN Number
	 * 
	 * author suman
	 * @param message message to display on alert dialog
	 * @since 3.0 
	 */
	public void showDialg(final String message) {
		try {
		// populate dialog UN Number invalid case and clear focus in all fields
		AddPlacardDialogActivity.eT_UnNumber.getText().clear();
		AddPlacardDialogActivity.eT_BillOfLading.clearFocus();
		AddPlacardDialogActivity.eT_DGGrossMassPkg.clearFocus();
		final Dialog dialogInvalidUn = new Dialog(activity, R.style.PauseDialog);
		dialogInvalidUn.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogInvalidUn.setCanceledOnTouchOutside(false);
		dialogInvalidUn.setContentView(R.layout.alert_dialog);
		dialogInvalidUn.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//dialogInvalidUn.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView tvTitle = (TextView) dialogInvalidUn
				.findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
		tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));
		TextView tvMessage = (TextView) dialogInvalidUn
				.findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
		if(message.startsWith("Forbidden")) {
		tvMessage.setText(Utils.getResString(R.string.Dialog_Forbidden_Message1));
		} else {
			tvMessage.setText(Utils.getResString(R.string.Dialog_InvalidUn_Message));
		}

		Button inunClose = (Button) dialogInvalidUn
				.findViewById(R.id.Alert_Dialog_Btn_Ok);//button to dismiss

		inunClose.setText(Utils.getResString(R.string.btn_ok));
		inunClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogInvalidUn.dismiss();
				AddPlacardDialogActivity.eT_BillOfLading.clearFocus();
				AddPlacardDialogActivity.eT_UnNumber.requestFocus();
				// focus must be in UN Number
				// edit text field
				AddPlacardDialogActivity.eT_UnNumber.setText("");
			}
		});
		dialogInvalidUn.show();
		
		} catch (Exception e) {
			new Exceptions(activity.getApplicationContext(),AlerDialogInValidUnNumber.this
					.getClass().getName(), "Error::AlerDialogInValidUnNumber::showDialog " +
					Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
