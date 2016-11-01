package com.ideabytes.dgsms.ca.adapters;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.HomeActivity;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogChooseLanguage;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
/******************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : CustomAdapterChooseLanguages
 * author:  Suman
 * Description : Customized dialog to change language
 *****************************************************/
public class CustomAdapterChooseLanguages extends BaseAdapter implements DBConstants {

	private List<String> listItems = null;//items to be displayed in the alert dialog
	private Activity adaperActivityChooseLang = null;//Activity reference
	private Dialog dialogChooseLanguage;

	public CustomAdapterChooseLanguages(List<String> items,
			Activity parentActivity,Dialog dialogChooseLanguage) {
		this.listItems = items;
		this.adaperActivityChooseLang = parentActivity;
		this.dialogChooseLanguage = dialogChooseLanguage;
	}

	@Override
	public Object getInstance() {
		return null;
	}

	/* private view holder class */
	private class ViewHolder {
		RadioGroup radioGroup = null;//radio group of radio buttons
		RadioButton radioButton = null;//radio button to select preferred language
		TextView tv1 = null;//text view to display 'languages'
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) adaperActivityChooseLang
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.custom_dialog_single_selection_center_text, null);
			holder = new ViewHolder();
			holder.radioGroup = (RadioGroup) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Radio_Group);
			holder.radioButton = (RadioButton) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Radio_Btn);
			holder.tv1 = (TextView) convertView
					.findViewById(R.id.Dialoag_Single_Choice_Title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.radioGroup
		.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				try {
					if (!listItems.get(position).equals(
							Utils.getResString(R.string.Menu_change_locale))) {
						// User selected value set to language
//						Localization localLanguage = new Localization(adaperActivityChooseLang);
						if (listItems.get(position).equals("English")) {
							Utils.language = "en";
							changeLocale("en","US");
//							localLanguage.getLanguageContent(1);
							Utils.language = "en";//store preferred language
							Intent intent = new Intent(adaperActivityChooseLang,
									HomeActivity.class);
							adaperActivityChooseLang.startActivity(intent);
							adaperActivityChooseLang.finish();
						} else if (listItems.get(position).equals(
								"Spanish")) {
							Utils.language = "es";
							changeLocale("es","ES");
//							localLanguage.getLanguageContent(2);
							Utils.language = "es";//store preferred language
							Intent intent = new Intent(adaperActivityChooseLang,
									HomeActivity.class);
							adaperActivityChooseLang.startActivity(intent);
							adaperActivityChooseLang.finish();
						} else if (listItems.get(position).equals(
								"French")) {
							Utils.language = "fr";
							changeLocale("fr","CA");
//							localLanguage.getLanguageContent(3);
							Utils.language = "fr";//store preferred language
							Intent intent = new Intent(adaperActivityChooseLang,
									HomeActivity.class);
							adaperActivityChooseLang.startActivity(intent);
							adaperActivityChooseLang.finish();
						}
						Toast.makeText(adaperActivityChooseLang,
								Utils.getResString(R.string.Toast_Selected_Language),
								Toast.LENGTH_LONG).show();
						dialogChooseLanguage.cancel();

					}
				} catch (Exception e) {
					new Exceptions(adaperActivityChooseLang.getApplicationContext(),CustomAdapterChooseLanguages.this
							.getClass().getName(), "Error::CustomAdapterChooseLanguages " +
							Arrays.toString(e.getStackTrace()));
					e.printStackTrace();
				}
			}
		});

		holder.tv1.setText(listItems.get(position));
		return convertView;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return listItems.indexOf(getItem(position));
	}


	public void changeLocale(final String language,final String country) {
		Locale locale = new Locale(language, country);
		Locale.setDefault(locale);
		Log.d("language","language  after "+Locale.getDefault());
		Configuration config = new Configuration();
		config.locale = locale;
		adaperActivityChooseLang.getApplicationContext().getResources().updateConfiguration(config, null);
	}
}
