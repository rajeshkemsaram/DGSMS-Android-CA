package com.ideabytes.dgsms.ca.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;

/**
 * Created by sairam on 20/9/16.
 */
public class CustomPsnAdapter extends BaseAdapter {
   Context mcontext;
    String data;

    public CustomPsnAdapter(AddPlacardDialogActivity addPlacardDialogActivity, String description) {
    mcontext=addPlacardDialogActivity;
    data=description;

    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
