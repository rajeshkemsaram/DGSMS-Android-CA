package com.ideabytes.dgsms.ca.model;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by suman on 13/5/16.
 */
public class DataCenter {
    private static DataCenter dataCenter = new DataCenter();

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public String getFinalValue() {
        return finalValue;
    }

    private String finalValue = "No Placard Added";
    // Edit texts
    private EditText eTBillOfLading;//edit text for 'bill of lading'
    private EditText eTUnNumber;//edit text for 'un number'
    private EditText eTDGGrossMassPkg;//edit text for 'weight per package'
    private EditText eTNumberOfUnits;//edit text for 'no of units'
    private EditText eTErap;//edit text for 'ERAP'
    private EditText eTGrossWeightTotal;//edit text for 'gross weight'
    // TextViews
    private TextView tVUnDesc;//text view to display 'un description'
    private TextView tvIbc;//text view to display 'ibc select'
    private TextView tvResidue;//text view to display 'residue select'
    // button
    private Button btnIBC;//button for 'ibc'
    private Button btnResidue;//button for 'residue'
    private Button btnDisplayPlacard;////button for 'Display Placard'
    private Button btnErg;//button for 'ERG'

    public String getNonExempt() {
        return nonExempt;
    }

    public void setNonExempt(String nonExempt) {
        this.nonExempt = nonExempt;
    }

    private String nonExempt;

    public String getUnType() {
        return unType;
    }

    public void setUnType(String unType) {
        this.unType = unType;
    }

    private String unType;

    public String getName() {
        return name;
    }

    public String getPp() {
        return pp;
    }

    public String getUn_class_id() {
        return un_class_id;
    }

    public int getOptimise() {
        return optimise;
    }

    public int getExemption() {
        return exemption;
    }

    public int getShip() {
        return ship;
    }

    public String getDanger_placard() {
        return danger_placard;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public void setUn_class_id(String un_class_id) {
        this.un_class_id = un_class_id;
    }

    public void setOptimise(int optimise) {
        this.optimise = optimise;
    }

    public void setExemption(int exemption) {
        this.exemption = exemption;
    }

    public void setShip(int ship) {
        this.ship = ship;
    }

    public void setDanger_placard(String danger_placard) {
        this.danger_placard = danger_placard;
    }

    private String name;//name of the placard for dg logic
    private String pp;//primary placard for dg logic
    private String un_class_id;//un_class_id  for dg logic
    private int optimise = 1;//optimise for dg logic,BY DEFAULT 1(DISABLE)
    private int exemption = 0;//exemption for dg logic,BY DEFAULT 0
    private int ship = 0;//ship for dg logic,BY DEFAULT 0
    private String danger_placard = "0";//danger_placard for dg logic,BY DEFAULT 0
    public void seteTBillOfLading(EditText eTBillOfLading) {
        this.eTBillOfLading = eTBillOfLading;
    }

    public void seteTUnNumber(EditText eTUnNumber) {
        this.eTUnNumber = eTUnNumber;
    }

    public void seteTDGGrossMassPkg(EditText eTDGGrossMassPkg) {
        this.eTDGGrossMassPkg = eTDGGrossMassPkg;
    }

    public void seteTNumberOfUnits(EditText eTNumberOfUnits) {
        this.eTNumberOfUnits = eTNumberOfUnits;
    }

    public void seteTErap(EditText eTErap) {
        this.eTErap = eTErap;
    }

    public void seteTGrossWeightTotal(EditText eTGrossWeightTotal) {
        this.eTGrossWeightTotal = eTGrossWeightTotal;
    }

    public void settVUnDesc(TextView tVUnDesc) {
        this.tVUnDesc = tVUnDesc;
    }

    public void setTvIbc(TextView tvIbc) {
        this.tvIbc = tvIbc;
    }

    public void setTvResidue(TextView tvResidue) {
        this.tvResidue = tvResidue;
    }

    public void setBtnIBC(Button btnIBC) {
        this.btnIBC = btnIBC;
    }

    public void setBtnResidue(Button btnResidue) {
        this.btnResidue = btnResidue;
    }

    public void setBtnDisplayPlacard(Button btnDisplayPlacard) {
        this.btnDisplayPlacard = btnDisplayPlacard;
    }

    public void setBtnErg(Button btnErg) {
        this.btnErg = btnErg;
    }

    public EditText geteTBillOfLading() {
        return eTBillOfLading;
    }

    public EditText geteTUnNumber() {
        return eTUnNumber;
    }

    public EditText geteTDGGrossMassPkg() {
        return eTDGGrossMassPkg;
    }

    public EditText geteTNumberOfUnits() {
        return eTNumberOfUnits;
    }

    public EditText geteTErap() {
        return eTErap;
    }

    public EditText geteTGrossWeightTotal() {
        return eTGrossWeightTotal;
    }

    public TextView gettVUnDesc() {
        return tVUnDesc;
    }

    public TextView getTvIbc() {
        return tvIbc;
    }

    public TextView getTvResidue() {
        return tvResidue;
    }

    public Button getBtnIBC() {
        return btnIBC;
    }

    public Button getBtnResidue() {
        return btnResidue;
    }

    public Button getBtnDisplayPlacard() {
        return btnDisplayPlacard;
    }

    public Button getBtnErg() {
        return btnErg;
    }

    public static synchronized DataCenter getInstance() {
        return dataCenter;
    }
}
