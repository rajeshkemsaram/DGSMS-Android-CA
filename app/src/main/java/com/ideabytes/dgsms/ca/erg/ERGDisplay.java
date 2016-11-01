package com.ideabytes.dgsms.ca.erg;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class ERGDisplay extends Activity implements DBConstants {
    private String TAG = ERGDisplay.class.getSimpleName();
    TableLayout tablelayout;
    TextView Parameter, SI, IMP;
    String language = null;
    String guide, unnumber;
    String[] column_one_names = {Utils.getResString(R.string.small_Isolation),Utils.getResString(R.string.small_Daypad),
            Utils.getResString(R.string.small_Night_Pad),Utils.getResString(R.string.large_lsolation),
            Utils.getResString(R.string.large_Day_Low_Pad),Utils.getResString(R.string.large_Day_Moderate_Pad),
            Utils.getResString(R.string.large_Day_High_Pad),Utils.getResString(R.string.large_Night_Low_Pad),
            Utils.getResString(R.string.large_night_Moderate_Pad),Utils.getResString(R.string.large_Night_High_Pad)};
    String[] siValues = new String[10];
    String[] impValues = new String[10];
    String[] set6 = {Utils.getResString(R.string.large_Day_PAD),Utils.getResString(R.string.large_Night_Pad)};
    String[] set6siValues = new String[6];
    String[] set6impvalues = new String[6];
    int flag; //column number deciding flag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergdisplay);
        TextView spill_isolation_distance=(TextView)findViewById(R.id.spill_isolation_distance);
        spill_isolation_distance.setText(Utils.getResString(R.string.Spill_Isolation_Distance));
        tablelayout = (TableLayout) findViewById(R.id.erg_table);
        language = getIntent().getStringExtra("language");
        guide = getIntent().getStringExtra("guideid");
        unnumber = AddPlacardDialogActivity.eT_UnNumber.getText().toString();
        Log.v(TAG, language);
        buildData();
        //disabling default actionbar
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        //setting custom Actionbar
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_ergaction_bar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.cust_text);
        mTitleTextView.setText("UN:" + "" + unnumber + " Guide No: " + guide.substring(6));
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }


    /**
     * table heading formation
     */
    private void initialHeading() {
        LayoutInflater initialinflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headingview = initialinflator.inflate(R.layout.erg_initial_row, null);
        // horizontal row creation
        View vv2 = new View(this);
        vv2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        vv2.setBackgroundColor(Color.BLACK);
        tablelayout.addView(vv2);
        //column 1
        Parameter = (TextView) headingview.findViewById(R.id.column_text1);
        Parameter.setText(Utils.getResString(R.string.Parameter));
        Parameter.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //column 2
        SI = (TextView) headingview.findViewById(R.id.column_text2);
        SI.setText(Utils.getResString(R.string.S_I));
        SI.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //column 3
        IMP = (TextView) headingview.findViewById(R.id.column_text3);
        //if language is fr then don't display imp column
        if (!language.equalsIgnoreCase("fr")) {
            IMP.setText(Utils.getResString(R.string.Imperial));
            IMP.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else IMP.setVisibility(View.GONE);
        tablelayout.addView(headingview);
        //horizontal row creation
        View vv1 = new View(this);
        vv1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        vv1.setBackgroundColor(Color.BLACK);
        tablelayout.addView(vv1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //On back arrow clicked
            Intent intent = new Intent(this, FragActivity.class);
            intent.putExtra("value", AsyncTaskErgGuide.result1);
            intent.putExtra("isolationResponse", AsyncTaskErgGuide.isolationresponse1);
            intent.putExtra("waterResponse", AsyncTaskErgGuide.waterresponse1);
            intent.putExtra("language", AsyncTaskErgGuide.language1);
            intent.putExtra("zero", "1");
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * getting values from json
     */
    private void buildData() {
        String isolationresponse = getIntent().getStringExtra("isolationresponse");
        try {
            JSONObject json = new JSONObject(isolationresponse);
            JSONObject result = json.getJSONObject("results");
            Log.v(TAG, result.toString() + "*****");
            if (result.getString("status").equalsIgnoreCase("00")) {
//					Log.v(TAG,"in build after if ");
                JSONObject data = result.getJSONObject("data");
                String extendedErg = data.getString("extendedErg");
//					Log.v(TAG,extendedErg);
                if (data.getString("extendedErg").equalsIgnoreCase("0")) {
                    //setting initial Columns
                    initialHeading();
                    //reading and storing  String related to S.I units from json object
                    set6siValues[0] = data.getString("smallIsolation");
                    set6siValues[1] = data.getString("smallDayPAD");
                    set6siValues[2] = data.getString("smallNightPAD");
                    set6siValues[3] = data.getString("largeIsolation");
                    set6siValues[4] = data.getString("largeDayPAD");
                    set6siValues[5] = data.getString("largeNightPAD");
                    //reading and storing  String related to IMP units from json object
                    set6impvalues[0] = data.getString("smallIsolationIMP");
                    set6impvalues[1] = data.getString("smallDayPADIMP");
                    set6impvalues[2] = data.getString("smallNightPADIMP");
                    set6impvalues[3] = data.getString("largeIsolationIMP");
                    set6impvalues[4] = data.getString("largeDayPADIMP");
                    set6impvalues[5] = data.getString("largeNightPADIMP");
                    tableBody();
                } else {
                    flag = 1;
                    //setting initial Columns
                    initialHeading();
                    //reading and storing  String related to S.I units from json object
                    impValues[0] = data.getString("smallIsolationIMP");
                    impValues[1] = data.getString("smallDayPADIMP");
                    impValues[2] = data.getString("smallNightPADIMP");
                    impValues[3] = data.getString("largeIsolationIMP");
                    impValues[4] = data.getString("largeDayLowPADIMP");
                    impValues[5] = data.getString("largeDayModeratePADIMP");
                    impValues[6] = data.getString("largeDayHighPADIMP");
                    impValues[7] = data.getString("largeNightLowPADIMP");
                    impValues[8] = data.getString("largeNightModeratePADIMP");
                    impValues[9] = data.getString("largeNightHighPADIMP");
                    //reading and storing  String related to IMP units from json object
                    siValues[0] = data.getString("smallIsolation");
                    siValues[1] = data.getString("smallDayPAD");
                    siValues[2] = data.getString("smallNightPAD");
                    siValues[3] = data.getString("largeIsolation");
                    siValues[4] = data.getString("largeDayLowPAD");
                    siValues[5] = data.getString("largeDayModeratePAD");
                    siValues[6] = data.getString("largeDayHighPAD");
                    siValues[7] = data.getString("largeNightLowPAD");
                    siValues[8] = data.getString("largeNightModeratePAD");
                    siValues[9] = data.getString("largeNightHighPAD");
                    tableBody();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * formation of table data
     */
    public void tableBody() {
        int columnNumbers = 0;
        if (flag == 1) {
            columnNumbers= 10;
        } else {
            columnNumbers = 6;
        }
        for (int i = 0; i < columnNumbers; i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.erg_tablevalues, tablelayout, false);
            TextView bodytext_1 = (TextView) v.findViewById(R.id.column_one_value);
            bodytext_1.setText(column_one_names[i]);
            if (columnNumbers == 6 && i == 4) {
                bodytext_1.setText(set6[0]);
            }
            if (columnNumbers == 6 && i == 5) {
                bodytext_1.setText(set6[1]);
            }
            TextView bodytext_2 = (TextView) v.findViewById(R.id.column_second_value);
            bodytext_2.setText(siValues[i]);
            if (columnNumbers == 6) {
                bodytext_2.setText(set6siValues[i]);
            }
            TextView bodytext_3 = (TextView) v.findViewById(R.id.column_third_value);
            if (!language.equalsIgnoreCase("fr")) {
                bodytext_3.setText(impValues[i]);
                if (columnNumbers == 6) {
                    bodytext_3.setText(set6impvalues[i]);
                }
            } else bodytext_3.setVisibility(View.GONE);
            tablelayout.addView(v);
            //horizontal row creation
            View vv1 = new View(this);
            vv1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
            vv1.setBackgroundColor(Color.BLACK);
            tablelayout.addView(vv1);
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }

}

