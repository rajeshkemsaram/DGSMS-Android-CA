package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;

import org.com.ca.dgsms.ca.model.DBConstants;


import com.ideabytes.dgsms.ca.adapters.CustomAdapterChooseLanguages;
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
/**********************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : AlertDialogChooseLanguage
 * author:  Suman
 * Description : Customized dialog to show language list
 ********************************************************/
public class AlertDialogChooseLanguage extends Dialog implements DBConstants {
	
	private Activity activity;
	public AlertDialogChooseLanguage(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	/**
	 * This method pop ups a drop down list with maximum no of placards that to
	 * be placed on the Truck so that User can select
	 * 
	 * author Suman
	 * @param list list of languages
	 * @return User Selected Max Placard Value
	 * @since 5.0 
	 */
	public String showdialog(final String[] list) {
		List<String> languagesList = Arrays.asList(list);
		try {
            Dialog dialogChooseLanguage = new Dialog(activity,R.style.PauseDialog);
		dialogChooseLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogChooseLanguage.setContentView(R.layout.custom_dialog_title);
		dialogChooseLanguage.setCanceledOnTouchOutside(false);
		//dialogChooseLanguage.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView title = (TextView) dialogChooseLanguage
				.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
		title.setText(Utils.getResString(R.string.Menu_change_locale));// localization

		CustomAdapterChooseLanguages myShowList = new CustomAdapterChooseLanguages(
				languagesList, activity,dialogChooseLanguage);
		ListView lv1 = (ListView) dialogChooseLanguage
				.findViewById(R.id.Dialoag_ListView);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		lv1.setAdapter(myShowList);

		dialogChooseLanguage.show();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return selected language from the list
		return languagesList.get(0);
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
