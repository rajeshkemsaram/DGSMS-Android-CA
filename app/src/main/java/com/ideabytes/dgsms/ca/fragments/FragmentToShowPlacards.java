package com.ideabytes.dgsms.ca.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;


import com.ideabytes.dgsms.ca.adapters.MyAdapter;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogNoEnoughPlacards;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.util.HashMap;
import java.util.Map;

/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : FragmentToShowPlacards
 * author:  Suman
 * Description : This Fragment is to show placards in 3 types
 * Created Data : 12-01-2016
 * Modified Date : 19-04-2016
 * Reason: changed global variables to local to avoid duplicate values in string array
 ****************************************************************/
public class FragmentToShowPlacards  extends Fragment implements DBConstants {
    private final static String LOGTAG = "FragmentToShowPlacards";
    private Activity activity;
    private View rootView;
    GridView gridView;
    private String unList[];
    private String[] placardNames;
    private String[] groupNames;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();//get activity of main class
        //by default load single placard as max placard value
        rootView = inflater.inflate(R.layout.main, container, false);
        Bundle bundle = this.getArguments();
        // System.out.println("in frag placardCount "+placardCount);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        String dgResult = bundle.getString(RESULT);
        // System.out.println("in frag result "+dgResult);
        String key = bundle.getString(KEY);
        // System.out.println("in frag key "+key);
        int placardCount = bundle.getInt(COUNT, 1);
       // Log.v(LOGTAG, "placardCount " + placardCount);
        gridView.setAdapter(new MyAdapter(activity, get(dgResult, key, placardCount), unList,groupNames));
        if (placardCount < 2) {
            gridView.setNumColumns(placardCount);
        }
        return rootView;
    }

    private String[] get(final String dgResult,final String key,final int placardCount) {
        int count = 0;
        String[] placardNames = new String[placardCount];
        unList = new String[placardCount];
        groupNames = new String[placardCount];
        HashMap<String, String> res;
        Utils utils = new Utils();
        if (key.startsWith(SEMI_OPTIMIZED)) {
            res = utils.semiOptimize(dgResult);
        } else if (key.startsWith(NON_OPTIMIZED)) {
            res = utils.basicOptimize(dgResult);
        } else {
            //for any string default is to show optimized
            res = utils.optimize(dgResult);
        }

        String msg = "";
        String placardImagePrimary = "classsempty";//to display empty placard
        for (Map.Entry<String, String> e : res.entrySet()) {
            String result[] = e.getValue().split(":");
            String className = result[0];
            // System.out.println("in frag className "+className);
            String unNumber = result[1];

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
                className = "sempty";
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
            Log.v(LOGTAG, "in frag placardImagePrimary " + placardImagePrimary + " count " + count);
            placardNames[count] = placardImagePrimary;
            unList[count] = unNumber;
            groupNames[count] = Utils.groupName;
            Log.v(LOGTAG, "in frag placardNames[count] " + placardNames[count]);
            count++;
        }
        Log.v(LOGTAG, "placardNames" + placardNames);
        return placardNames;
    }

    @Override
    public Object getInstance() {
        return null;
    }
}

