package com.ideabytes.dgsms.ca.textwatcher;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


import com.ideabytes.dgsms.ca.model.DataCenter;
import com.ideabytes.dgsms.ca.utils.UnNumberInfo;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import org.com.ca.dgsms.ca.model.DBConstants;

/**
 * Created by suman on 13/5/16.
 */
public class GenericTextWatcher implements TextWatcher,DBConstants {
    private final String LOGTAG = "GenericTextWatcher";
    private static View view;
    private static Activity activity;
    private boolean isTextChangedByUser = true;
    private static GenericTextWatcher genericTextWatcher = new GenericTextWatcher();
    public static GenericTextWatcher intit(Activity activity1, View view1) {
        activity = activity1;
        view = view1;
        return genericTextWatcher;
    }
//    public GenericTextWatcher(Activity activity,View view) {
//        this.view = view;
//        this.activity = activity;
//    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //Log.v(LOGTAG," beforeTextChanged");
    }
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       // Log.v(LOGTAG," onTextChanged");

    }

    public void afterTextChanged(Editable editable) {
        switch(view.getId()){
            case R.id.et_dgMassPkg:
                DataCenter.getInstance().geteTGrossWeightTotal().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
                Log.v(LOGTAG,"1 "+ editable.toString().length());
                if (DataCenter.getInstance().geteTNumberOfUnits().length() > 0 &&
                        DataCenter.getInstance().geteTDGGrossMassPkg().getText().toString().length() > 0 ) {
                    int units = Integer.parseInt(DataCenter.getInstance().geteTNumberOfUnits().getText().toString());
                    double dgWeight = Double.parseDouble(DataCenter.getInstance().geteTDGGrossMassPkg().getText().toString());
                    DataCenter.getInstance().geteTGrossWeightTotal().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
                    DataCenter.getInstance().geteTGrossWeightTotal().setText("" + dgWeight * units);
                    DataCenter.getInstance().geteTGrossWeightTotal().setSelection(DataCenter.getInstance().geteTGrossWeightTotal().getText().length());
                    DataCenter.getInstance().geteTGrossWeightTotal().addTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
                }
//                else {
//                    DataCenter.getInstance().geteTGrossWeightTotal().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
//                    DataCenter.getInstance().geteTGrossWeightTotal().setText("");
//                    DataCenter.getInstance().geteTGrossWeightTotal().addTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
//                }
                break;
            case R.id.enter_un_number:
                Log.v(LOGTAG,"2 "+editable. toString().length());
                UnNumberInfo unNumberInfo = new UnNumberInfo(activity.getApplicationContext());
                if (DataCenter.getInstance().geteTUnNumber().length() > 3) {
                    String unnumber = DataCenter.getInstance().geteTUnNumber().getText().toString();
                    // Log.v(LOGTAG, "unnumber " + unnumber);
                    String description =  unNumberInfo.getSingleValue(unNumberInfo.getUnNumberInfo(unnumber), COL_DESCRIPTION);
                    DataCenter.getInstance().gettVUnDesc().setText(description);//setting un description to text view
                    DataCenter.getInstance().gettVUnDesc().setTextColor(Color.BLACK);
                    DataCenter.getInstance().getBtnErg().setEnabled(true);
                    DataCenter.getInstance().getBtnErg().setTextColor(Color.parseColor("#FFFFFF"));
                    DataCenter.getInstance().getBtnErg().setBackgroundColor(Color.parseColor("#FF7F50"));
                } else {
                    DataCenter.getInstance().gettVUnDesc().setText("UN Description");//setting un description to text view
                    DataCenter.getInstance().geteTUnNumber()
                            .setError(Utils.getResString(R.string.UN_Number_4Ditgits_error));
                    DataCenter.getInstance().getBtnErg().setEnabled(false);
                    DataCenter.getInstance().getBtnErg().setTextColor(Color.parseColor("#FFFFFF"));
                    DataCenter.getInstance().getBtnErg().setBackgroundColor(Color.parseColor("#452300"));
                }
                break;
            case R.id.eTDgGrossTotal:
                DataCenter.getInstance().geteTDGGrossMassPkg().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
                Log.v(LOGTAG,"3 "+ editable.toString().length());
                if (DataCenter.getInstance().geteTNumberOfUnits().length() > 0 &&
                        DataCenter.getInstance().geteTGrossWeightTotal().getText().toString().length() > 0) {
                    int units = Integer.parseInt(DataCenter.getInstance().geteTNumberOfUnits().getText().toString());
                   // Log.v(LOGTAG,"units "+units);
                    double dgTotalWeight = Double.parseDouble(DataCenter.getInstance().geteTGrossWeightTotal().getText().toString());
                    //Log.v(LOGTAG, "dgTotalWeight " + dgTotalWeight);
                   // Log.v(LOGTAG, "dg pkg weight  " + String.valueOf(dgTotalWeight / units));
                    //remove change listener
                    DataCenter.getInstance().geteTDGGrossMassPkg().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
                    DataCenter.getInstance().geteTDGGrossMassPkg().setText("" + dgTotalWeight / units);
                    DataCenter.getInstance().geteTDGGrossMassPkg().setSelection(DataCenter.getInstance().geteTDGGrossMassPkg().getText().length());
                    key(DataCenter.getInstance().geteTDGGrossMassPkg());
                    DataCenter.getInstance().geteTDGGrossMassPkg().addTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
                }
//                else {
//                    DataCenter.getInstance().geteTDGGrossMassPkg().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
//                    DataCenter.getInstance().geteTDGGrossMassPkg().setText("");
//                    DataCenter.getInstance().geteTDGGrossMassPkg().addTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
//                }
                break;
            case R.id.enter_Units:
                Log.v(LOGTAG,"4 "+ editable.toString().length());
                if (DataCenter.getInstance().geteTNumberOfUnits().length() > 0 &&
                        DataCenter.getInstance().geteTDGGrossMassPkg().getText().toString().length() > 0) {
                    int units = Integer.parseInt(DataCenter.getInstance().geteTNumberOfUnits().getText().toString());
                    double dgWeight = Double.parseDouble(DataCenter.getInstance().geteTDGGrossMassPkg().getText().toString());
                    DataCenter.getInstance().geteTGrossWeightTotal().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
                    DataCenter.getInstance().geteTGrossWeightTotal().setText(""+dgWeight * units);
                    DataCenter.getInstance().geteTGrossWeightTotal().addTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTGrossWeightTotal()));
                } else if (DataCenter.getInstance().geteTNumberOfUnits().length() > 0 &&
                        DataCenter.getInstance().geteTGrossWeightTotal().getText().toString().length() > 0 ) {
                    int units = Integer.parseInt(DataCenter.getInstance().geteTNumberOfUnits().getText().toString());
                    double dgTotalWeight = Double.parseDouble(DataCenter.getInstance().geteTGrossWeightTotal().getText().toString());
                    DataCenter.getInstance().geteTDGGrossMassPkg().removeTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
                    DataCenter.getInstance().geteTDGGrossMassPkg().setText(""+dgTotalWeight / units);
                    DataCenter.getInstance().geteTDGGrossMassPkg().addTextChangedListener(GenericTextWatcher.intit(activity, DataCenter.getInstance().geteTDGGrossMassPkg()));
                }
                break;
        }
    }
    private void key(final EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    //this is for backspace
                    Log.v(LOGTAG,"clicked backalshs");
                    editText.removeTextChangedListener(GenericTextWatcher.intit(activity,editText));
                }
                return false;
            }
        });
    }

    @Override
    public Object getInstance() {
        return null;
    }
}
