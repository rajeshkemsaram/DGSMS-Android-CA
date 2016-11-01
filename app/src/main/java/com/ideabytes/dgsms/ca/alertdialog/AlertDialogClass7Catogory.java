package com.ideabytes.dgsms.ca.alertdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ideabytes.dgsms.ca.adapters.CustomAdapterClass7Catogory;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.Arrays;
import java.util.List;
/**********************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AlertDialogClass7Catogory
 * author:  Suman
 * Description : Customized dialog to show categories for class 7
 * Create Date : 19-10-2015
 ********************************************************/
public class AlertDialogClass7Catogory extends Dialog implements DBConstants {

    private Activity activity;//activity reference

    public AlertDialogClass7Catogory(Activity activity) {
        super(activity);
        this.activity =  activity ;
    }

    /**
     * Shows alert dialog with all packing groups along with radio buttons
     * to select any of them
     *
     * @param  packageList of categories for Class 7
     * author Suman
     * */
    public void showDialog(final List<String> packageList) {
        try {
            Dialog dialogClass7 = new Dialog(activity,R.style.PauseDialog);
            dialogClass7.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogClass7.setContentView(R.layout.custom_dialog_title);
            dialogClass7.setCanceledOnTouchOutside(false);
            //dialogClass7.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

            TextView title = (TextView) dialogClass7
                    .findViewById(R.id.Dialoag_Title);//text view to display alert dialog title
            title.setText(Utils.getResString(R.string.Dialog_Category));

            CustomAdapterClass7Catogory myShowList = new CustomAdapterClass7Catogory(
                    packageList, activity,dialogClass7);
            ListView lv1 = (ListView) dialogClass7
                    .findViewById(R.id.Dialoag_ListView);

            getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lv1.setAdapter(myShowList);

            //back/home button lister, don't cancel dialog on back/home button press 
            dialogClass7.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(activity, "You must select any of the categories ", Toast.LENGTH_SHORT)
                                .show();
                    } else if (keyCode == KeyEvent.KEYCODE_HOME) {
                        Toast.makeText(activity, "You must select any of the categories ", Toast.LENGTH_SHORT)
                                .show();
                    }
                    return true;
                }
            });

            dialogClass7.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Object getInstance() {
        return null;
    }
}
