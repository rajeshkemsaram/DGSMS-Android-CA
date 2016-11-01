package com.ideabytes.dgsms.ca.adapters;

import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;

import com.ideabytes.dgsms.ca.model.DataCenter;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogClass7Catogory;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
/*********************************************************
 *Copy right @Ideabytes Software India Private Limited 
 *Web site : http://ideabytes.com
 *Name : CustomAdapterClass7Catogory
 * author:  Suman
 * Description : Customized dialog to show categories for class 7
 * @Create Date : 19-10-2015
 ********************************************************/
public class CustomAdapterClass7Catogory extends BaseAdapter implements DBConstants {

    //private final String TAG = "CustomAdapterClass7Catogory";
    private List<String> listItems = null;//items to be displayed in the alert dialog
    private Activity adapterPckGrpActivity = null;//Activity reference
    private Dialog dialogClass7;
    public CustomAdapterClass7Catogory(List<String> items, Activity parentActivity,Dialog dialogClass7) {
        this.listItems = items;
        this.adapterPckGrpActivity = parentActivity;
        this.dialogClass7 = dialogClass7;
    }

    @Override
    public DataCenter getInstance() {
        return DataCenter.getInstance();
    }

    /* private view holder class */
    private class ViewHolder {
        RadioGroup radioGroup = null;//radio group of radio buttons
        RadioButton radioButton = null;//radio button to select preferred package group
        TextView tv1 = null;//text view to display 'package group'
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) adapterPckGrpActivity
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
                            // User restricted to select any of pkg group that are
                            // in the list
                            if (!listItems.get(position).equals(
                                    Utils.getResString(R.string.Dialog_Category))) {
                                if(listItems.get(position).equalsIgnoreCase("I")) {
                                    getInstance().setPp("7.1");
                                    getInstance().setName("7.1");
                                } else  if(listItems.get(position).equalsIgnoreCase("II")) {
                                    getInstance().setPp("7.2");
                                    getInstance().setName("7.2");
                                } else  if(listItems.get(position).equalsIgnoreCase("III")) {
                                    getInstance().setPp("7.3");
                                    getInstance().setName("7.3");
                                }
                                dialogClass7.cancel();
                            }
                        } catch (Exception e) {
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
}
