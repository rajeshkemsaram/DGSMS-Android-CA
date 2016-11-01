package com.ideabytes.dgsms.ca;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ideabytes.dgsms.ca.erg.FragmentOne;
import com.ideabytes.dgsms.ca.erg.FragmentThree;
import com.ideabytes.dgsms.ca.erg.FragmentTwo;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.fragments.FragmentToShowPlacards;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.Arrays;

/**
 * Created by suman on 13/1/16.
 */
public class ShowAll extends FragmentActivity implements DBConstants {
    private static final String LOGTAG = "ShowAll";
    private String dgResult;
    private final int VAR_OPTIMIZED = 1;
    private final int VAR_NON_OPTIMIZED = 2;
    private final int VAR_SEMI_OPTIMIZED = 3;
    private final int DEFAULT = 4;

    private FragmentTabHost mTabHost;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.settings_page);
//          setContentView(R.layout.settingtestone);
//                godata();
        // calling menu items,on clicking this navigation drawer will open
        Button btnOptions = (Button) findViewById(R.id.btnBack);
        btnOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ShowAll.this,HomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        dgResult = intent.getStringExtra(RESULT);
        //Log.v(LOGTAG, "dgResult " + dgResult);
        displayView(DEFAULT);
        TextView tvTitle = (TextView) findViewById(R.id.tvThreeTypes);
//        tvTitle.setText(Localization.All_Scenarios);
        TextView tvOptmized = (TextView) findViewById(R.id.tv1);
//        tvOptmized.setText(Localization.Btn_Optimized);
        TextView tvSemiOptmized = (TextView) findViewById(R.id.tv2);
//        tvSemiOptmized.setText(Localization.Btn_SemiOptimized);
        TextView tvNOnOptmized = (TextView) findViewById(R.id.tv3);
//        tvNOnOptmized.setText(Localization.Btn_Non_Optimized);
        displayView(1);
        displayView(2);
        displayView(3);
        Toast.makeText(getApplicationContext(),"Please Scroll if placards exceeded count 3",Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(final int type) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentToShowPlacards fragmentToShowPlacards = new FragmentToShowPlacards();
        Bundle bundle = new Bundle();
        Utils utils = new Utils();
        // update the main content by replacing fragments
        switch (type) {
            case 1:
               // Log.v(LOGTAG, "btnOptimized");
                bundle.putString(KEY, OPTIMIZED);
                bundle.putString(RESULT, dgResult);
                bundle.putInt(COUNT,utils.optimize(dgResult).size());
                fragmentToShowPlacards.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_container1, fragmentToShowPlacards).commit();
                break;
            case 2:
               // Log.v(LOGTAG, "btnSemiOptimized");
                bundle.putString(KEY, SEMI_OPTIMIZED);
                bundle.putString(RESULT, dgResult);
                bundle.putInt(COUNT, utils.semiOptimize(dgResult).size());
                fragmentToShowPlacards.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_container2, fragmentToShowPlacards).commit();
                break;
            case 3:
              //  Log.v(LOGTAG, "btnNonOptimized");
                bundle.putString(KEY, NON_OPTIMIZED);
                bundle.putString(RESULT, dgResult);
                bundle.putInt(COUNT, utils.basicOptimize(dgResult).size());
                fragmentToShowPlacards.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_container3, fragmentToShowPlacards).commit();
                break;
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }



//    public void fragmentset(){
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.)
//
//    }
//
//
//
//   public void godata()
//   {
//
//
//       mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//       mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//       mTabHost.addTab(
//               mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), "optimised")),
//               Optimised.class, null);
//       mTabHost.addTab(
//               mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), "semi")),
//               FragmentTwo.class, null);
//       mTabHost.addTab(
//               mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), "non")),
//               FragmentThree.class, null);
//   }



    private View getTabIndicator(Context context, String title) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }




}