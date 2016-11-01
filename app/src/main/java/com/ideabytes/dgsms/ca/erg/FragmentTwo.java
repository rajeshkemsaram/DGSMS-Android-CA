package com.ideabytes.dgsms.ca.erg;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ideabytes.dgsms.ca.exceptions.Exceptions;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

/************************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : FragmentTwo
 * author:  Suman
 * Description : This fragment class is to display ERG data
 * Modified Date : 27-10-2015
 ************************************************************/
public class FragmentTwo extends Fragment implements OnTouchListener,DBConstants {
	private Button b1,b2,b3;
	private LinearLayout linearLayout;
	private JSONObject jsonObj;
	private JSONArray jArray1;
	private JSONObject PUBLIC_SAFETY;
	private String myString = "myString";
	//private Button btnBack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragActivity activity = (FragActivity) getActivity();
		myString = activity.getMyData();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_two, container, false);
		linearLayout = (LinearLayout) rootView.findViewById(R.id.fragone1);
		linearLayout.setOnTouchListener(this);
//		btnBack = (Button) rootView.findViewById(R.id.btnBack);
//		btnBack.setVisibility(View.VISIBLE);
//		btnBack.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				getActivity().onBackPressed();
//			}
//		});

		b1 = (Button) rootView.findViewById(R.id.b1);
		b1.setText(Utils.getResString(R.string.details));
		tabSelection(b1);

		b2 = (Button) rootView.findViewById(R.id.b2);
		b2.setText(Utils.getResString(R.string.protective_clothing));
		tabSelection(b2);

		b3 = (Button) rootView.findViewById(R.id.b3);
		b3.setText(Utils.getResString(R.string.evacuation));
		tabSelection(b3);

		try {
			jsonObj = new JSONObject(myString);
			jArray1 = jsonObj.getJSONArray("guideArray");
			for(int j = 0; j <  jArray1.length(); j++) {
				JSONObject jsonObject1 = jArray1.getJSONObject(j);	
				JSONObject guide_data = jsonObject1.getJSONObject("guide_data");
				PUBLIC_SAFETY = guide_data.getJSONObject("PUBLIC_SAFETY");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		defaultSelection();

		return rootView;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int eventaction = event.getAction();

		switch (eventaction) {
		case MotionEvent.ACTION_DOWN: 
			// finger touches the screen
			//btnBack.setVisibility(View.GONE);
			break;

		case MotionEvent.ACTION_MOVE:
			// finger moves on the screen
			//btnBack.setVisibility(View.GONE);
			break;

		case MotionEvent.ACTION_UP:   
			/// finger leaves the screen
			//btnBack.setVisibility(View.VISIBLE);
			break;
		case MotionEvent.ACTION_CANCEL:
			//btnBack.setVisibility(View.VISIBLE);
			break;
		}
		// tell the system that we handled the event and no further processing is required
		return true; 
	}

	public void tabSelection(final Button button) {
		button.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//button.setPressed(true);
				switch(v.getId())
				{
				case R.id.b1:
					// handle button b1 click;
					b1.setPressed(true);
					b2.setPressed(false);
					b3.setPressed(false);
					try {
						linearLayout.removeAllViews();
						//btnBack.setVisibility(View.VISIBLE);
						JSONArray jArray = PUBLIC_SAFETY.getJSONArray("Details");
						for (int i = 0; i < jArray.length(); i++) {
							String data = jArray.getString(i);
							if(data.contains(" CALL Emergency")) {
								data = "·CALL Emergency Response Telephone Number listed in " +
										"Emergency Response Telephone Number guide, you can find this list by clicking on " +
										"Emergency Response Telephone Number link above";
							}
							TextView tv = new TextView(getActivity());
							tv.setText("\n");
							tv.setLayoutParams(new Utils().setTextViewParams(10,0,0,0));
							tv.setTextColor(Color.BLACK);
							linearLayout.addView(tv);
							if(!data.contains("·")) {
								data = "<b>" + data + "</b>";
								Spanned conBold = Html.fromHtml(data);	
								tv.setText(conBold);
							} else {
								data = data.replace("·", "");
								tv.setText(data.trim());
							}
						}
					} catch (Exception e) {
						new Exceptions(getContext(),FragmentTwo.this
								.getClass().getName(), "Error::FragmentTwo " +
								Arrays.toString(e.getStackTrace()));
						e.printStackTrace();
					}
					break;
				case R.id.b2:
					// handle button b2 click;
					b1.setPressed(false);
					b2.setPressed(true);
					b3.setPressed(false);
					try {
						linearLayout.removeAllViews();
						//btnBack.setVisibility(View.VISIBLE);
						JSONArray jArray = PUBLIC_SAFETY.getJSONArray("PROTECTIVE_CLOTHING");
						for (int i = 0; i < jArray.length(); i++) {
							String data = jArray.getString(i);
							TextView tv = new TextView(getActivity());
							tv.setText("\n");
							tv.setLayoutParams(new Utils().setTextViewParams(10,0,0,0));
							tv.setTextColor(Color.BLACK);
							linearLayout.addView(tv);
							if(!data.contains("·")) {
								data = "<b>" + data + "</b>";
								Spanned conBold = Html.fromHtml(data);	
								tv.setText(conBold);
							} else {
								data = data.replace("·", "");
								tv.setText(data.trim());
							}
						}
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
						linearLayout.removeAllViews();
					//	btnBack.setVisibility(View.VISIBLE);
						JSONArray jArray = PUBLIC_SAFETY.getJSONArray("EVACUATION");
						for (int i = 0; i < jArray.length(); i++) {
							String data = jArray.getString(i);
							TextView tv = new TextView(getActivity());
							tv.setText("\n");
							tv.setLayoutParams(new Utils().setTextViewParams(10,0,0,0));
							tv.setTextColor(Color.BLACK);
							linearLayout.addView(tv);
							if(!data.contains("·")) {
								data = "<b>" + data + "</b>";
								Spanned conBold = Html.fromHtml(data);	
								tv.setText(conBold);
							} else {
								data = data.replace("·", "");
								tv.setText(data.trim());
							}
						}
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

	/**
	 * Bydefualt selects first tab value
	 */
	private void defaultSelection() {
		b1.setPressed(true);
		b2.setPressed(false);
		b3.setPressed(false);
		try {
			linearLayout.removeAllViews();
			//btnBack.setVisibility(View.VISIBLE);
			JSONArray jArray = PUBLIC_SAFETY.getJSONArray("Details");
			for (int i = 0; i < jArray.length(); i++) {
				String data = jArray.getString(i);
				if(data.contains(" CALL Emergency")) {
					data = "·CALL Emergency Response Telephone Number listed in " +
							"Emergency Response Telephone Number guide, you can find this list by clicking on " +
							"Emergency Response Telephone Number link above";
				}
				TextView tv = new TextView(getActivity());
				tv.setText("\n");
				tv.setLayoutParams(new Utils().setTextViewParams(10,0,0,0));
				tv.setTextColor(Color.BLACK);
				linearLayout.addView(tv);
				if(!data.contains("·")) {
					data = "<b>" + data + "</b>";
					Spanned conBold = Html.fromHtml(data);	
					tv.setText(conBold);
				} else {
					data = data.replace("·", "");
					tv.setText(data.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getInstance() {
		return null;
	}
}