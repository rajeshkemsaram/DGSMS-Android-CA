package com.ideabytes.dgsms.ca.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.ShowAll;
import com.ideabytes.dgsms.ca.database.DeleteDBData;
import com.ideabytes.dgsms.landstar.R;


import static android.content.Context.WINDOW_SERVICE;
import static com.ideabytes.dgsms.ca.HomeActivity.getContext;

/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : MyAdapter
 * author:  Suman
 * Description : This Adapter is to show placards in 3 types
 * Created Data : 12-01-2016
 * Modified Date : 19-04-2016
 * Reason: changed getView method , fixed issue in showing first value repeating in list view
 ****************************************************************/
public class MyAdapter extends BaseAdapter {
    private String TAG = MyAppData.class.getSimpleName();
    private Activity activity;
    private String[] placards = null;
    private String[] unNumbers = null;
    private String[] groupNames;

    public MyAdapter(Activity activity, String[] placards,String[] unNumbers,String[] groupNames) {
        this.activity = activity;
        this.placards = placards;
        this.unNumbers = unNumbers;
        this.groupNames = groupNames;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflator = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflator.inflate(R.layout.placards, parent, false);
        }
        try {
            TextView textView = (TextView) convertView.findViewById(R.id.tvUnNumber);

            textView.setText(unNumbers[position]);

            TextView tvGroupName = (TextView) convertView.findViewById(R.id.tvGroupName);
            String groupName = groupNames[position];
            ImageView flag = (ImageView) convertView.findViewById(R.id.ivPlacard);
            String placard = placards[position];
            Log.v(TAG,"placards "+placard);
            //  System.out.println("in frag add "+placard+" at position"+position+" disp name "+Utils.groupName);
            if (groupName.startsWith("1") && placard.equalsIgnoreCase("class1")) {
                tvGroupName.setText(groupName);
            } else if (groupName.startsWith("1") && placard.equalsIgnoreCase("class1_4")) {
                tvGroupName.setText(groupName.substring(3));
            } else if (groupName.startsWith("1") && placard.equalsIgnoreCase("class1_5")) {
                tvGroupName.setText(groupName.substring(3));
            } else if (groupName.startsWith("1") && placard.equalsIgnoreCase("class1_6")) {
                tvGroupName.setText(groupName.substring(3));
            } else {
                tvGroupName.setText("");
            }
            if (!placard.startsWith("classsempty")) {
                flag.setVisibility(View.VISIBLE);
                int ImResPrimary = activity.getResources().getIdentifier(placard,
                        "drawable", activity.getPackageName());
                Drawable drawable = activity.getResources().getDrawable(ImResPrimary);
                flag.setImageDrawable(drawable);
//                DisplayMetrics dm = new DisplayMetrics();
//                activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//                double x = Math.pow(dm.widthPixels/dm.xdpi,2);
//                double y = Math.pow(dm.heightPixels/dm.ydpi,2);
//                double screenInches = Math.sqrt(x+y);
//                screenInches=  (double)Math.round(screenInches * 10) / 10;
//                Log.d("debug","Screen inches : " + screenInches);
//                if(screenInches<=5){
//                    flag.getLayoutParams().height = 300;
//                    flag.getLayoutParams().width=300;
//                    flag.setImageDrawable(drawable);
//                }
//                if(screenInches<7&&screenInches>5){
//                    flag.getLayoutParams().height = 450;
//                    flag.getLayoutParams().width=450;
//                    flag.setImageDrawable(drawable);
//                }

            } else {
                flag.setImageResource(R.drawable.classsempty);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //any expcetion entry into db, delete latest recode that caused issue, so that even
            //crashed app works for next entries no need of regi/veri
           // new DeleteDBData(activity.getApplicationContext()).deleteFromTransLatestEntry();
        }
        return convertView;
    }
    @Override
    public int getCount() {
        return placards.length;
    }

    @Override
    public Object getItem(int position) {
        return placards[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}