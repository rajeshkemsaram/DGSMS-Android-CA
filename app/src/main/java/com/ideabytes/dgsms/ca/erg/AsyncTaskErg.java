package com.ideabytes.dgsms.ca.erg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.com.ca.dgsms.ca.model.DBConstants;
import org.json.JSONArray;
import org.json.JSONObject;


import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/***********************************************************
 * Copy right @Ideabytes Software India Private Limited 
 * Web site : http://ideabytes.com
 * Name : AsyncTaskErg
 * author:  Suman
 * Description : This is to get ERG data from web server
 * Modified Date : 20-01-2016
 * Reason: To handle no erg case
 *********************************************************/
public class AsyncTaskErg extends AsyncTask<String, String, String> implements
		DBConstants {
	private String TAG = "AsyncTaskErg";
	private ProgressDialog progressDialog = null;
	private Context context = null;
	private String language = null;
	private String unNumber = null;
	private String[] materialTypes;
	private String material;

	public AsyncTaskErg(Context a) {
		context = a;
	}

	protected void onPostExecute(String result) {
		// execution of result of Long time consuming operation
		super.onPostExecute(result);
		// Log.v(TAG,"erg response 2"+result);
		if (progressDialog != null)
			progressDialog.cancel();
		try {
			Log.v(TAG, "erg response " + result);
			JSONObject jsnobject = new JSONObject(result);
			//Log.v(TAG, " erg jsnobject " + jsnobject);
			JSONObject results = jsnobject.getJSONObject("results");
			//Log.v(TAG, " results " + results);
			Log.v("results*",results.toString());
			if (result.length() > 2) {
				JSONObject data = results.getJSONObject("data");
				String popUp = data.getString("popup");
				// Log.v(TAG, " popUp " + popUp);
				JSONArray jArray = data.getJSONArray("unnumberArray");

				materialTypes = new String[jArray.length()];

				for (int i = 0; i < jArray.length(); i ++) {
					JSONObject jsonObject = jArray.getJSONObject(i);
					//  Log.v(TAG, " jsonObject " + jsonObject);
					material = jsonObject.getString("material");
					materialTypes[i] = material;

				}
//				showDialog(materialTypes,result);

				if (popUp.equalsIgnoreCase("true")) {
					if(jArray.length()==1){
						JSONObject obj12=jArray.getJSONObject(0);
						String somedata=obj12.getString("material");
						material=somedata;
						Log.v("Material1",material);
						AsyncTaskErgGuide asyncTaskErgGuide = new AsyncTaskErgGuide(context);
						asyncTaskErgGuide.execute(result,language,unNumber,material);

					} else{
//					//give pop up to Select Exact Material
					showDialog(materialTypes,result);}
				} else {
					//if no pop uo, show erg directly
					AsyncTaskErgGuide asyncTaskErgGuide = new AsyncTaskErgGuide(context);
					asyncTaskErgGuide.execute(result,language,unNumber,material);
					Log.v("Material2",material);

				}
			} else {
				Log.v(TAG, "Guide for the UN Number is not found");
				Toast.makeText(context, Utils.getResString(R.string.No_ERG_Guide), Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(context, "Getting ERG...",
				"Please Wait..");
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		unNumber = params[0];
		language = params[1];

		String ergUrl = SERVER_URL_ERG+unNumber+"/"+language+".json";
		Log.v(TAG,"erg url "+ergUrl);

		HttpURLConnection httpURLConnection = null;
		try  {
			URL url = new URL(ergUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			//read response
			StringBuilder sb = new StringBuilder();
			Log.v(TAG,"http ok msg"+HttpURLConnection.HTTP_OK);
			Log.v(TAG,"httpgetrsponse"+ httpURLConnection.getResponseCode());
			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.v(TAG,"http mesg"+HttpURLConnection.HTTP_OK);
				//read response
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				//  System.out.println("" + sb.toString());
				result = sb.toString();
				Log.v(TAG,result+"*******");

			} else {
				Log.e(TAG,"error in erg "+httpURLConnection.getResponseMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpURLConnection.disconnect();
		}
		return result;
	}

	/**
	 * To display different Material types
	 */
	private class ShowExactMaterialType extends BaseAdapter {
		private List<String> listItems = null;
		private Dialog dialog;
		private String result;
		public ShowExactMaterialType(List<String> items, Dialog dialog,String result) {
			this.listItems = items;
			this.dialog = dialog;
			this.result = result;
		}


		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return listItems.indexOf(position);
		}

		@Override
		public long getItemId(int position) {
			return listItems.lastIndexOf(position);
		}
		/* private view holder class */
		private class ViewHolder {
			RadioGroup radioGroup = null;
			RadioButton radioButton = null;
			TextView tv1 = null;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.custom_dialog_single_selection_center_text, null);
				holder = new ViewHolder();
				holder.radioGroup = (RadioGroup) convertView
						.findViewById(R.id.Dialoag_Single_Choice_Radio_Group);
				holder.radioButton = (RadioButton) convertView
						.findViewById(R.id.Dialoag_Single_Choice_Radio_Btn);
				holder.tv1 = (TextView) convertView
						.findViewById(R.id.Dialoag_Single_Choice_Title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.radioGroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group, int checkedId) {
							try {
								// Log.v(TAG,"material "+listItems.get(position));
								//if no pop uo, show erg directly
								AsyncTaskErgGuide asyncTaskErgGuide = new AsyncTaskErgGuide(context);
								asyncTaskErgGuide.execute(result,language,unNumber,material);
								Log.v("Material :",material);

								dialog.cancel();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

			holder.tv1.setText(listItems.get(position));
			return convertView;
		}
	}

	/**
	 * To show Select Exact Material alert for different material types
	 * @param list
	 * @param result
	 */
	public void showDialog(final String[] list, final String result) {
		List<String> languagesList = Arrays.asList(list);
		try {
			final Dialog dialogBulkPkg = new Dialog(context, R.style.PauseDialog);
			dialogBulkPkg.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogBulkPkg.setContentView(R.layout.select_exact_shipping_name);
			dialogBulkPkg.setCanceledOnTouchOutside(false);

			TextView title = (TextView) dialogBulkPkg
					.findViewById(R.id.Dialoag_Title);
//			title.setText(Localization.Select_Description);// localization
			title.setText("Material");
			ShowExactMaterialType myShowList = new ShowExactMaterialType(languagesList,dialogBulkPkg,result);
			ListView lv1 = (ListView) dialogBulkPkg
					.findViewById(R.id.Dialoag_ListView);

			lv1.setAdapter(myShowList);

			dialogBulkPkg.setOnKeyListener(new DialogInterface.OnKeyListener() {

				@Override
				public boolean onKey(DialogInterface arg0, int keyCode,
									 KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						Toast.makeText(context, Utils.getResString(R.string.Select_Description), Toast.LENGTH_SHORT)
								.show();
					} else if (keyCode == KeyEvent.KEYCODE_HOME) {
						Toast.makeText(context, Utils.getResString(R.string.Select_Description), Toast.LENGTH_SHORT)
								.show();
					}
					return true;
				}
			});

			dialogBulkPkg.show();

		} catch (Exception e) {
			e.printStackTrace();
			Utils.generateNoteOnSD(FOLDER_PATH_DEBUG,TEXT_FILE_NAME, Arrays.toString(e.getStackTrace()));
		}
	}


	@Override
	public Object getInstance() {
		return null;
	}
}