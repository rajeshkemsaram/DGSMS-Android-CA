package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.adapters.CustomAdapterTransHistory;
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
/**********************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogTransHistory
 * author:  Suman
 * Description : Customized dialog to transaction history
 *********************************************************/
public class AlertDialogTransHistory extends Dialog implements DBConstants {
	
	private Activity parent;
	public AlertDialogTransHistory(Activity parent) {
		super(parent);
		this.parent = parent;
	}
	/**
	 * Displays no of clients list from web service while registering
	 * 
	 * author Suman
	 * @param history list of transaction history values
	 * @return User Selected company
	 * @since v.b.5.0 
	 */
	public String showDialog(final List<String> history) {
		try {
		final Dialog dialogHistory = new Dialog(parent,R.style.PauseDialog);
		dialogHistory.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogHistory.setContentView(R.layout.custom_dialog_spinner);
		dialogHistory.setCanceledOnTouchOutside(false);
		//dialogChooseLanguage.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView title = (TextView) dialogHistory
				.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
		title.setText(Utils.getResString(R.string.Title_Transaction_History));// localization

		CustomAdapterTransHistory myShowList = new CustomAdapterTransHistory(
				history, parent);
		ListView lv1 = (ListView) dialogHistory
				.findViewById(R.id.Dialoag_ListView);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		lv1.setAdapter(myShowList);
		
		ImageView btnClose = (ImageView) dialogHistory .findViewById(R.id.dialog2remove);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogHistory.cancel();
			}
		});

		dialogHistory.show();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return history.get(0);
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
