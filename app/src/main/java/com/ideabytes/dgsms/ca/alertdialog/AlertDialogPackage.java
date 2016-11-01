package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;

import org.com.ca.dgsms.ca.model.DBConstants;


import com.ideabytes.dgsms.ca.adapters.CustomAdapterPackageGroup;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**********************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogPackage
 * author:  Suman
 * Description : Customized dialog to show package group
 ********************************************************/
public class AlertDialogPackage extends Dialog implements DBConstants {

	private Activity activity;
	public AlertDialogPackage(Activity activity) {
		super(activity);
		this.activity =  activity ;
	}

	/**
	 * Shows alert dialog with all packing groups along with radio buttons
	 * to select any of them
	 * 
	 * author Suman
	 * @param packageList package list of a un number
	 * */
	public void showDialog(final List<String> packageList) {
		try {
		final Dialog dialogPackage = new Dialog(activity,R.style.PauseDialog);
		dialogPackage.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogPackage.setContentView(R.layout.custom_dialog_title);
		dialogPackage.setCanceledOnTouchOutside(false);
		//dialogClass7.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView title = (TextView) dialogPackage
				.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
		title.setText(Utils.getResString(R.string.Dialog_Package_Group));

		CustomAdapterPackageGroup myShowList = new CustomAdapterPackageGroup(
				packageList, activity,dialogPackage);
		ListView lv1 = (ListView) dialogPackage
				.findViewById(R.id.Dialoag_ListView);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		lv1.setAdapter(myShowList);
		
		//back/home button lister, don't cancel dialog on back/home button press 
		dialogPackage.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(activity, "You must select any of the package group", Toast.LENGTH_SHORT)
                    .show();
                } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                    Toast.makeText(activity, "You must select any of the package group", Toast.LENGTH_SHORT)
                    .show();
                }
                return true;
            }
        });
		
		dialogPackage.show();
		
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
