package com.ideabytes.dgsms.ca.showall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogNoEnoughPlacards;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowAllFragAct extends FragmentActivity implements DBConstants,View.OnTouchListener {
    private FragmentTabHost mTabHost;
    private String dgResult;
    private String unList[];
    private String[] placardNames;
    private String[] groupNames;
    private Activity activity;
    private Button b1,b2,b3;
    private LinearLayout linearLayout;
    private Utils utils = new Utils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.show_all_btns);
        linearLayout = (LinearLayout) findViewById(R.id.fragone1);
        linearLayout.setOnTouchListener(ShowAllFragAct.this);
        Intent intent = getIntent();
        dgResult = intent.getStringExtra(RESULT);
        //rajesh
        b1 = (Button) findViewById(R.id.b1);
//        b1.setText(Utils.getResString(R.string.tv_optimised));
        b1.setText(Utils.getResString(R.string.optimise));
        b1.setTextColor(Color.WHITE);
        tabSelection(b1);
        //rajesh
        b2 = (Button) findViewById(R.id.b2);
//        b2.setText(Utils.getResString(R.string.tv_semi_optimised));
        b2.setText(Utils.getResString(R.string.semi_optimise));
        b2.setTextColor(Color.WHITE);
        tabSelection(b2);
        //rajesh
        b3 = (Button) findViewById(R.id.b3);
//        b3.setText(Utils.getResString(R.string.tv_non_optimised));
        b3.setText(Utils.getResString(R.string.non_optimise));
        b3.setTextColor(Color.WHITE);
        tabSelection(b3);
        defaultSelection();


    }

    public String[] getGroupNames() {
        return groupNames;
    }
    public String[] getPlacardNames() {
        return placardNames;
    }
    public String[] getUnList() {
        return unList;
    }
    public String getDgResult() {
        return dgResult;
    }
    public int getPlacardCount() {
        return new Utils().optimize(dgResult).size();
    }


    private String[] get(final String dgResult,final String key,final int placardCount) {
        int count = 0;
        placardNames = new String[placardCount];
        unList = new String[placardCount];
        groupNames = new String[placardCount];
//        placardsList = new ArrayList<>(placardCount);
        HashMap<String, String> res;
        Utils utils = new Utils();
        if (key.startsWith("Semi")) {
            res = utils.semiOptimize(dgResult);
        } else if (key.startsWith("Basic")) {
            res = utils.basicOptimize(dgResult);
        } else {
            //for any string default is to show optimized
            res = utils.optimize(dgResult);
        }
        String msg = "";
        String placardImagePrimary = "classsempty";//to display empty placard
        for (Map.Entry<String, String> e : res.entrySet()) {
//            Placards placards = new Placards();
            String result[] = e.getValue().split(":");
            String className = result[0];
            // System.out.println("in frag className "+className);
            String unNumber = result[1];
//            placards.setUnNumber(unNumber);
            // System.out.println("in frag unNumber "+unNumber);
            // String groupName = result[2];
            // String groupName = result[2];
            ///if placard is empty then show "no placard required message"
            if (unNumber.equals("none")) {
                unNumber = "";
            }

//                    System.out.println(" debug key is " + e.getKey() + " class is " + className + " un number is " + unNumber
//                            + " group name is " + Utils.groupName);

            if (className.equals("sempty") || className.equals("1s")) {
                msg = Utils.getResString(R.string.No_Placards_Required_Message);
                className = "sempty";
            } else if (className.startsWith("Not Enough Placard Holders on the truck Do NOT accept")) {
                AlertDialogNoEnoughPlacards dialogDoNotLod = new AlertDialogNoEnoughPlacards(
                        activity);
                dialogDoNotLod.showDailog(
                        Utils.getResString(R.string.Dialog_Do_Not_Accept_Load), -1);
            } else {
                msg = Utils.getResString(R.string.Placards_On_Truck_Message);
            }

            placardImagePrimary = "class"
                    + className.replace('.', '_');
            //for class name 6_s, make un number empty because this placard is with printed
            //un number on it,ex case un 3373
            if (className.equals("6_s")) {
                unNumber = "";
            } else if (className.equals("1_1")||className.equals("1_2") ||className.equals("1_3")) {
                placardImagePrimary = "class1";
            }
            else if (className.equals("i_h")) {
                // inhalation placard for sp23 un 1163
                unNumber = "";
            } else if (className.equals("2_s")) {
                // case un 1005 with description placard on user
                placardImagePrimary = "s1005";
            }
            // Log.v(LOGTAG, "in frag placardImagePrimary " + placardImagePrimary + " count " + count);
            placardNames[count] = placardImagePrimary;
            unList[count] = unNumber;
            groupNames[count] = Utils.groupName;
            // Log.v(LOGTAG, "in frag placardNames[count] " + placardNames[count]);
            count++;
        }
        return placardNames;
    }

    public void tabSelection(final Button button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //button.setPressed(true);
                switch(v.getId()) {
                    case R.id.b1:
                        // handle button b1 click;
                        b1.setPressed(true);
                        b2.setPressed(false);
                        b3.setPressed(false);
                        try {
                            FragOptimised fragOptimised = new FragOptimised();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragOptimised).commit();
                            //call optimised
                            get(dgResult,OPTIMIZED,utils.optimize(dgResult).size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.b2:
                        // handle button b2 click;
                        b1.setPressed(false);
                        b2.setPressed(true);
                        b3.setPressed(false);
                        try {
                            FragSemiOptimised fragSemiOptimised = new FragSemiOptimised();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragSemiOptimised).commit();
                            //call semi optimised
                            get(dgResult,SEMI_OPTIMIZED,utils.semiOptimize(dgResult).size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.b3:
                        // handle button b3 click;
                        b1.setPressed(false);
                        b2.setPressed(false);
                        b3.setPressed(true);
                        try {
                            FragNonOptimised fragNonOptimised = new FragNonOptimised();
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragNonOptimised).commit();
                            //call non optimised
                            get(dgResult,NON_OPTIMIZED,utils.basicOptimize(dgResult).size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:

                }
                return true;
            }
        });
    }
    private void defaultSelection() {
        b1.setPressed(true);
        b2.setPressed(false);
        b3.setPressed(false);
        try {
            FragOptimised fragOptimised = new FragOptimised();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragOptimised).commit();
            //call optimised
            get(dgResult,OPTIMIZED,utils.optimize(dgResult).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getInstance() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        b1.setPressed(true);
        b2.setPressed(false);
        b3.setPressed(false);
        try {
            FragOptimised fragOptimised = new FragOptimised();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragOptimised).commit();
            //call optimised
            get(dgResult,OPTIMIZED,utils.optimize(dgResult).size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
