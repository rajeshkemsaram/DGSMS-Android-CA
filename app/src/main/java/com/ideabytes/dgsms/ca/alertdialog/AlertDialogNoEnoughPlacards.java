package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.HomeActivity;

import com.ideabytes.dgsms.ca.database.DeleteDBData;
import com.ideabytes.dgsms.ca.database.GetDBData;
import com.ideabytes.dgsms.ca.database.UpdateDBData;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.Arrays;
/***************************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogNoEnoughPlacards
 * author:  Suman
 * Description : Customized dialog to show placards holders when exceeded max
 * placard holders on the Truck,Using this user can change max placard holders on the Truck
 * Created Date : 06-10-2015
 ****************************************************************************/
public class AlertDialogNoEnoughPlacards extends Dialog implements DBConstants {

	private Activity activity;
	public AlertDialogNoEnoughPlacards(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	/**
	 * This method is used to show in abnormal cases when there is no place holder
	 * for new placard in the truck
	 * 
	 * author Suman
	 * @param message message to display on alert dialog
	 * @param colId id
	 * @since 5.0 
	 */
	public void showDailog(String message,final int colId) {
       try {
			final Dialog dialogContLoadIntoTruck = new Dialog(activity,R.style.PauseDialog);
			dialogContLoadIntoTruck.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogContLoadIntoTruck.setCanceledOnTouchOutside(false);
			dialogContLoadIntoTruck
			.setContentView(R.layout.custom_dialog_no_placard_holders);
			//dialogContLoadIntoTruck.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView tvTitle = (TextView) dialogContLoadIntoTruck
					.findViewById(R.id.Alert_Dialog_Title);//text view to display alert dialog title
			tvTitle.setText(Utils.getResString(R.string.Dialog_Alert_Title));

			TextView tvMessage = (TextView) dialogContLoadIntoTruck
					.findViewById(R.id.Alert_Dialog_Message);//text view to display alert dialog message
			tvMessage.setText(message);

			Button ok = (Button) dialogContLoadIntoTruck
					.findViewById(R.id.Alert_Dialog_Btn_Ok);
			ok.setText(Utils.getResString(R.string.btn_ok));

			ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					dialogContLoadIntoTruck.dismiss();
					//call main activity to set selected max value for placard holder
					Intent i = new Intent(activity,HomeActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);
					activity.startActivity(i);
					
					updateOrders(colId);
				}
			});

			Button btnCancel = (Button) dialogContLoadIntoTruck
					.findViewById(R.id.Alert_Dialog_Btn_Cancel);
			btnCancel.setText("Cancel");
			btnCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {	
					new DeleteDBData(activity.getApplicationContext()).deleteFromTransLatestEntry();//delete entry that cuase this msg
					//call main activity to refresh shipment
					Intent i = new Intent(activity,HomeActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_CLEAR_TASK);
					activity.startActivity(i);
					//get max placard holder value from db i.e previous selection will be added as
					//max placard holder value
					dialogContLoadIntoTruck.cancel();//dismiss dialog on "Cancel" btn click
				}
			});
			dialogContLoadIntoTruck.show();
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
