package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;

import org.com.ca.dgsms.ca.model.DBConstants;


import com.ideabytes.dgsms.ca.adapters.CustomAdapterIbcResidue;
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
 * Name : AlertDialogResidue
 * author:  Suman
 * Description : Customized dialog to show residue status
 **********************************************************/
public class AlertDialogResidue extends Dialog implements DBConstants {
	
	private Activity activity;
	public AlertDialogResidue(Activity activity) {
		super(activity);
		this.activity = activity;
	}
	
	/**
	 * Displays dialog with residue options yes/no for particular un number
	 * 
	 * author Suman
	 * @param list list of residue values
	 * @return User Selected Max Placard Value
	 * @since 5.0 
	 */
	public String showdialog(final String[] list) {
		List<String> languagesList = Arrays.asList(list);
		try {
		final Dialog dialogRecidue = new Dialog(activity, R.style.PauseDialog);
		dialogRecidue.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogRecidue.setContentView(R.layout.custom_dialog_spinner);
		dialogRecidue.setCanceledOnTouchOutside(false);
		//dialogChooseLanguage.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

		TextView title = (TextView) dialogRecidue
				.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
		title.setText(Utils.getResString(R.string.Dialog_Add_Item_Tv_Residue));// localization

		CustomAdapterIbcResidue myShowList = new CustomAdapterIbcResidue(
				languagesList, activity,dialogRecidue);
		ListView lv1 = (ListView) dialogRecidue
				.findViewById(R.id.Dialoag_ListView);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		lv1.setAdapter(myShowList);
		
		//back/home button lister, don't cancel dialog on back/home button press 
		dialogRecidue.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(activity, "Select residue status", Toast.LENGTH_SHORT)
                    .show();
                } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                    Toast.makeText(activity, "Select residue status", Toast.LENGTH_SHORT)
                    .show();
                }
                return true;
            }
        });

		dialogRecidue.show();
		
		} catch (Exception e) {
			Log.e(AlertDialogResidue.this.getClass().getSimpleName(), "Error showDialog method " + e.toString());
			e.printStackTrace();
		}

		return languagesList.get(0);
	}

	@Override
	public Object getInstance() {
		return null;
	}
}
