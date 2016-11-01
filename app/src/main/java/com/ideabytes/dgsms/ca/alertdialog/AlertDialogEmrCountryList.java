package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.adapters.CustomAdapterEmrCountryList;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/******************************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : AlertDialogEmrCountryList
 * author:  Suman
 * Description : Customized dialog to show emergency country list
 ******************************************************************/
public class AlertDialogEmrCountryList extends Dialog implements DBConstants {
	
	private Activity activity;
	public AlertDialogEmrCountryList(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	/**
	 * This method shows countries list which user can select for the country 
	 * that user wants to know emergency contact details
	 * 
	 * author Suman
	 * @param list list of countries
	 * @return User Selected Max Placard Value
	 * @since 5.0 
	 */
	public String showdialog(final String[] list) {
		List<String> languagesList = Arrays.asList(list);
		try {
			final Dialog dialogEmergencyNumbersCountryList = new Dialog(activity,R.style.PauseDialog);
		dialogEmergencyNumbersCountryList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogEmergencyNumbersCountryList.setContentView(R.layout.custom_dialog_country_list);
		dialogEmergencyNumbersCountryList.setCanceledOnTouchOutside(false);
		//dialogChooseLanguage.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView title = (TextView) dialogEmergencyNumbersCountryList
				.findViewById(R.id.Dialoag_Country_Title);//text view to display alert dialog title
		title.setText("Emergency Response Telephone Numbers");// localization
		
		ImageView close = (ImageView) dialogEmergencyNumbersCountryList.findViewById(R.id.Dialoag_Emrgency_Remove);
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogEmergencyNumbersCountryList.cancel();
			}
		});

		CustomAdapterEmrCountryList myShowList = new CustomAdapterEmrCountryList(
				languagesList, activity,dialogEmergencyNumbersCountryList);
		ListView lv1 = (ListView) dialogEmergencyNumbersCountryList
				.findViewById(R.id.Dialoag_Country_ListView);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		lv1.setAdapter(myShowList);

		dialogEmergencyNumbersCountryList.show();
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
