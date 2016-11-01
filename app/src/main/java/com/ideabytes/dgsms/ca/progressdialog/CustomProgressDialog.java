package com.ideabytes.dgsms.ca.progressdialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideabytes.dgsms.landstar.R;


/**************************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : WebActivity
 * author:  Suman
 * Created on 10/10/15.
 * Modified Date : 27-10-2015
 * Description : This class is to register new user or verify existing user
 * (license creation pages from server will be handled here)
 ***************************************************************************/

public class CustomProgressDialog extends Dialog {

    private Context mContext;
    private TextView mTitle = null;
    private View v = null;
    private ProgressBar progressBar;

    public CustomProgressDialog(Context context) {
        super(context);
        mContext = context;
        /** 'Window.FEATURE_NO_TITLE' - Used to hide the mTitle */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /** Design the dialog in main.xml file */
        setContentView(R.layout.custom_progressdialog);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        mTitle = (TextView) findViewById(R.id.dialogTitle);
        progressBar = (ProgressBar) findViewById(R.id.progress);

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mTitle.setText(mContext.getResources().getString(titleId));
    }
}
