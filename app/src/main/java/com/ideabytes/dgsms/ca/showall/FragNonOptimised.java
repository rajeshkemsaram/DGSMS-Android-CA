package com.ideabytes.dgsms.ca.showall;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ideabytes.dgsms.ca.adapters.MyAdapter;
import com.ideabytes.dgsms.landstar.R;


public class FragNonOptimised extends Fragment {

    private String unList[];
    private String[] placardNames;
    private String[] groupNames;
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        ShowAllFragAct activity = (ShowAllFragAct) getActivity();
        int placardCount = activity.getPlacardCount();
        unList = activity.getUnList();
        placardNames = activity.getPlacardNames();
        groupNames = activity.getGroupNames();
        gridView.setAdapter(new MyAdapter(getActivity(),placardNames,unList,groupNames)); // uses the view to get the context instead of getActivity().
        if (placardCount < 2) {
            gridView.setNumColumns(placardCount);
        }
        return view;
    }
}