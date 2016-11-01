package com.ideabytes.dgsms.ca.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.model.DataCenter;
import com.ideabytes.dgsms.ca.utils.Utils;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogSpecialCase;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;

import com.ideabytes.dgsms.landstar.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/********************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : CustomAdapterSpecialCase
 * author:  Suman
 * Description : Customized dialog to show special case descriptions
 ********************************************************************/
public class CustomAdapterSpecialCase extends BaseAdapter implements DBConstants {

    private final String TAG = "CustomAdapterSpecial";
    private JSONArray listItems = null;
    private Activity adapterSplCaseActivity = null;//Activity reference
    public JSONObject jsonRsponse = null;//response to display list items for a un number
    private Dialog dialogSpecialCase;
    int d = 0;
    private ArrayList<String> alistItems = new ArrayList<>();

    public CustomAdapterSpecialCase(JSONArray items,
                                    Activity parentActivity, Dialog dialogSpecialCase) {
        this.listItems = items;
        this.adapterSplCaseActivity = parentActivity;
        this.dialogSpecialCase = dialogSpecialCase;
    }


    public CustomAdapterSpecialCase(ArrayList<String> alistItems, Activity parentActivity, Dialog dialogSpecialCase, int d) {
        this.alistItems = alistItems;
        this.adapterSplCaseActivity = parentActivity;
        this.dialogSpecialCase = dialogSpecialCase;
        this.d = d;
    }

    @Override
    public DataCenter getInstance() {
        return DataCenter.getInstance();
    }

    /* private view holder class */
    private class ViewHolder {
        RadioGroup radioGroup = null;//radio group of radio buttons
        RadioButton radioButton = null;//radio button to select preferred special case
        TextView tv1 = null;//text view to display 'special case description'
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) adapterSplCaseActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.custom_dialog_single_selection, null);
            holder = new ViewHolder();
            holder.radioGroup = (RadioGroup) convertView
                    .findViewById(R.id.Dialoag_Single_Choice_Radio_Group);
            holder.radioButton = (RadioButton) convertView
                    .findViewById(R.id.Dialoag_Single_Choice_Radio_Btn);
            holder.tv1 = (TextView) convertView
                    .findViewById(R.id.tv_virus_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.radioGroup
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        try {
                            String s = null;
                            // User restricted to select any of descriptions
                            // that are in the list
                            // set Description user selected into Look Up
                            // Log.v(TAG, "spn  " + listItems);
                            // System.out.println("jsonResponse "+jsonResponse);
                            //no need of displaying # desc
                            if (d == 1) {
                                Log.v(TAG, "inif");
                                for (int k = 0; k < alistItems.size(); k++) {
                                    String data[] = alistItems.get(k).split("#");
                                    Log.v(TAG + "Data :", data[0]);
                                    AddPlacardDialogActivity.tV_UnDesc.setText(data[0]);
                                    AddPlacardDialogActivity.tV_UnDesc.setTextColor(Color.BLACK);
                                    AddPlacardDialogActivity.displayButtonViewChange();
                                    if (data[0].contains("N.O.S")) {
                                        AddPlacardDialogActivity.nosrow.setVisibility(View.VISIBLE);
                                    }
                                    dialogSpecialCase.dismiss();
                                }
                            }
                            String italic[] = listItems.getString(position).split("#");
                            AddPlacardDialogActivity.tV_UnDesc.setText((italic[0]));
                            AddPlacardDialogActivity.tV_UnDesc.setTextColor(Color.BLACK);
                            if (italic[0].contains("N.O.S")) {
                                AddPlacardDialogActivity.nosrow.setVisibility(View.VISIBLE);
                            }
                            // Set Description which is storing into
                            // transaction table
                            AddPlacardDialogActivity.displayButtonViewChange();
                            // Dismiss dialog on proper description
                            // selection
                            dialogSpecialCase.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utils.generateNoteOnSD(FOLDER_PATH_DEBUG, TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
                        }
                    }
                });
        String s = "";
        String italic[] = new String[0];
        try {
            if (d == 1) {
                italic = alistItems.get(position).split("#");
            } else {
                italic = listItems.getString(position).split("#");
            }
            if (italic.length == 2) {
                // Log.v(TAG, "second  " + italic[1]);
                s = "<i>" + italic[1] + "</i>";
            }
            holder.tv1.setText(Html.fromHtml(italic[0] + s));


        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        //Log.v(TAG,"first "+italic[0]);
        //display # desc in italic
        return convertView;
    }

    @Override
    public int getCount() {
        if (d == 1) {
            return alistItems.size();
        }
        return listItems.length();
    }

    @Override
    public Object getItem(int position) {
        if (d == 1) {
            return alistItems;
        }
        return listItems;

    }

    @Override
    public long getItemId(int position) {
        if (d == 1) {
            return alistItems.size();
        }
        return listItems.length();
    }
}


//				try {
//					// User restricted to select any of descriptions
//					// that are in the list
//					if (!listItems.get(position).equals(Localization.Select_Description)) {
//						// set Description user selected into Look Up
//						jsonRsponse = listItems.get(position);
//						 //System.out.println("jsonRsponse "+jsonRsponse);
//						getInstance().setName(jsonRsponse.optString(COL_NAME));
//						String description=jsonRsponse.optString("description").toString();
//						String removehash[]=description.split("#");
//
////						AddPlacardDialogActivity.tV_UnDesc.setText(jsonRsponse.optString(
////								"description").toString());
//						AddPlacardDialogActivity.tV_UnDesc.setText(removehash[0]);
//						// Set Description which is storing into
//						// transaction table
//						new AddPlacardDialogActivity().displayButtonViewChange();
//						// Dismiss dialog on proper description
//						// selection
//						dialogSpecialCase.cancel();
//					} else {
//						// User selects "Select Description" display
//						// toast to select any of descriptions
//						Toast.makeText(adapterSplCaseActivity,
//								"You Must Select Any of Description",
//								Toast.LENGTH_SHORT).show();
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		jsonRsponse = listItems.get(position);
//
////		holder.tv1.setText(jsonRsponse.optString("description").toString());
//
//		String noat[]=jsonRsponse.optString("description").toString().toString().split("@");
//		if(noat.length>1) {
//			for (int i = 0; i < noat.length; i++) {
//				holder.tv1.setText(noat[i].toString());
//			}
//		}
////		String nohash=jsonRsponse.optString("description").toString().replace("#","");
////
//		holder.tv1.setText(noat.toString());
//		return convertView;


