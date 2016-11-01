package com.ideabytes.dgsms.ca.alertdialog;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.adapters.CustomAdapterSp84;
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
/*******************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogSp84
 * author:  Suman
 * Description : Customized dialog to show sp84 status
 *******************************************************/
public class AlertDialogSp84 extends Dialog implements DBConstants {

	private Activity activity;
	public AlertDialogSp84(Activity activity) {
		super(activity);
		this.activity =  activity ;
	}

	/**
	 * This method pop ups a drop down list with all Virus types if sp84 value
	 * of UN Numbers is 1
	 * 
	 * author Suman
	 * @param list list of sp84 values
	 * @since 5.0 
	 */
	public void showDialog(final String[] list) {
		List<String> sp84List = Arrays.asList(list);
		try {
			final Dialog dialogSp84 = new Dialog(activity, R.style.PauseDialog);
			dialogSp84.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogSp84.setContentView(R.layout.custom_dialog_title);
			dialogSp84.setCanceledOnTouchOutside(false);
			//dialogSp84.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

			TextView title = (TextView) dialogSp84
					.findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
			title.setText(Utils.getResString(R.string.Dialog_Sp84_Title));

			CustomAdapterSp84 myShowList = new CustomAdapterSp84(sp84List,
					activity,dialogSp84);
			ListView lv1 = (ListView) dialogSp84
					.findViewById(R.id.Dialoag_ListView);

			getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			lv1.setAdapter(myShowList);
			
			//back/home button lister, don't cancel dialog on back/home button press 
			dialogSp84.setOnKeyListener(new OnKeyListener() {

	            @Override
	            public boolean onKey(DialogInterface arg0, int keyCode,
	                    KeyEvent event) {
	                if (keyCode == KeyEvent.KEYCODE_BACK) {
	                    Toast.makeText(activity, "Select virus type", Toast.LENGTH_SHORT)
	                    .show();
	                } else if (keyCode == KeyEvent.KEYCODE_HOME) {
	                    Toast.makeText(activity, "Select virus type", Toast.LENGTH_SHORT)
	                    .show();
	                }
	                return true;
	            }
	        });

			dialogSp84.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public Object getInstance() {
		return null;
	}
}
