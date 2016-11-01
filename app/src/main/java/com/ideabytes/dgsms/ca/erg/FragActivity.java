package com.ideabytes.dgsms.ca.erg;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogEmrCountryList;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
/****************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : FragActivity
 * author:  Suman
 * Description : This class is to fragments to display ERG data
 * Modified Date : 27-10-2015
 ***************************************************************/

public class FragActivity extends FragmentActivity implements DBConstants {
	private FragmentTabHost mTabHost;
	private JSONObject jsonObj;
	private JSONArray jArray1;
	private String myString = "myString";
	private String guide_id = "empty";
	private String description = "empty";
	private TextView tvTitle,tvDescription;
	private Button dsp;
	private String language=null;
	TextView spilltext;
	String TAG=getClass().getSimpleName().toString();



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.frag_activity);

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.fragactivity_custombar, null);
			spilltext=(TextView)mCustomView.findViewById(R.id.spilltext);
//		String htmlString="<u>SPILL ISOLATION DETAILS </u>";

//		String htmlString="<u>SPILL ISOLATION DETAILS </u>";
//		spilltext.setText(Html.fromHtml(htmlString));
		spilltext.setText(Utils.getResString(R.string.spill_isolation_details));
		spilltext.setPaintFlags(spilltext.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		Data();
	}


	public void	Data(){
	final	String isolationresponse=getIntent().getStringExtra("isolationResponse");
	final   String language=getIntent().getStringExtra("language");
	final   String waterresponse=getIntent().getStringExtra("waterResponse");
	JSONObject json = null;
	try {
		json = new JSONObject(isolationresponse);
		JSONObject result=json.getJSONObject("results");

		if(!result.getString("status").equalsIgnoreCase("00")) {
			spilltext.setVisibility(View.GONE);
		}
	} catch (JSONException e) {
		e.printStackTrace();
	}
	tvTitle = (TextView) findViewById(R.id.tvData);
	tvDescription = (TextView) findViewById(R.id.tvData1);

	TextView txt = (TextView) findViewById(R.id.tvEmergencyNumbers);
	txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

	txt.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			//list of countries
			String list[] = {"CANADA","UNITED STATES","MEXICO","ARGENTINA","BRAZIL","COLOMBIA"};
			AlertDialogEmrCountryList alert = new AlertDialogEmrCountryList(FragActivity.this);
			alert.showdialog(list);
		}
	});
	TextView waterText=(TextView)findViewById(R.id.watertext);
	waterText.setVisibility(View.GONE);
	JSONObject waterjson = null;
	try {
		waterjson = new JSONObject(waterresponse);
		JSONObject result=waterjson.getJSONObject("results");
		Log.v("watter",result.toString());

		if(result.getString("status").equalsIgnoreCase("00")) {
			String watertext=result.getString("data");
			waterText.setVisibility(View.VISIBLE);
			waterText.setText(watertext);
			Log.v("waterandtext",watertext);
		}
	} catch (JSONException e) {
		e.printStackTrace();
	}
	mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
	mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
		String i=getIntent().getStringExtra("zero");
		if(i.equalsIgnoreCase("1"))
		{
		myString=getIntent().getStringExtra("value");
		}else {
			myString = getIntent().getStringExtra("value");
		}
	mTabHost.addTab(
			mTabHost.newTabSpec("tab1").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab1)),
			FragmentOne.class, null);
	mTabHost.addTab(
			mTabHost.newTabSpec("tab2").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab2)),
			FragmentTwo.class, null);
	mTabHost.addTab(
			mTabHost.newTabSpec("tab3").setIndicator(getTabIndicator(mTabHost.getContext(), R.string.tab3)),
			FragmentThree.class, null);

	try {
		jsonObj = new JSONObject(myString);
		jArray1 = jsonObj.getJSONArray("guideArray");
		for(int j = 0; j <  jArray1.length(); j++) {
			JSONObject jsonObject1 = jArray1.getJSONObject(j);
			JSONObject guide_data = jsonObject1.getJSONObject("guide_data");
			description = guide_data.getString("DESCRIPTION");
			guide_id = guide_data.getString("id");
			Log.v(TAG,jArray1.toString());
		}

		spilltext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FragActivity.this, ERGDisplay.class);
				i.putExtra("isolationresponse",isolationresponse);
				i.putExtra("language",language);
				i.putExtra("guideid",guide_id);
				startActivityForResult(i,1);
			}
		});

		tvTitle.setText("UN:"+ AddPlacardDialogActivity.eT_UnNumber.getText().toString()
				+" Guide No :"+guide_id.substring(6));

		tvDescription.setText("Description ("+description+")");
	} catch(Exception e) {
		e.printStackTrace();
	}
}
	private View getTabIndicator(Context context, int title) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.textView);
		tv.setText(title);
		return view;
	}
	public String getMyData() {
		return myString;
	}

	@Override
	public Object getInstance() {
		return null;
	}


}