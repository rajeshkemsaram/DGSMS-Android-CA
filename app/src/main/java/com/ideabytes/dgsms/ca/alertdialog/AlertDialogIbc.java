package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;

import org.com.ca.dgsms.ca.model.DBConstants;


import com.ideabytes.dgsms.ca.adapters.CustomAdapterIbc;
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
/********************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : AlertDialogIbc
 * author:  Suman
 * Description : Customized dialog to show IBC status
 *******************************************************/
public class AlertDialogIbc extends Dialog implements DBConstants {
	
	private Activity activity;

	public AlertDialogIbc(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	/**
	 * displays IBC status for a un number 
	 * 
	 * author Suman
	 * @param list list of ibc status
	 * @return User Selected ibc status
	 * @since 5.0 
	 */
	public String showdialog(final String[] list) {
		List<String> languagesList = Arrays.asList(list);
		try {
		final Dialog dialogIbc = new Dialog(activity, R.style.PauseDialog);
		dialogIbc.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogIbc.setContentView(R.layout.custom_dialog_spinner);
		dialogIbc.setCanceledOnTouchOutside(false);
		//dialogChooseLanguage.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView title = (TextView) dialogIbc
				.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
		title.setText(Utils.getResString(R.string.Dialog_Add_Item_Tv_IBC));// localization

		CustomAdapterIbc myShowList = new CustomAdapterIbc(
				languagesList, activity,dialogIbc);
		ListView lv1 = (ListView) dialogIbc
				.findViewById(R.id.Dialoag_ListView);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		lv1.setAdapter(myShowList);
		//back/home button lister, don't cancel dialog on back/home button press 
		dialogIbc.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(activity, "Select IBC status", Toast.LENGTH_SHORT)
                    .show();
                } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                    Toast.makeText(activity, "Select IBC status", Toast.LENGTH_SHORT)
                    .show();
                }
                return true;
            }
        });
		
		dialogIbc.show();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return languagesList.get(0);
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
