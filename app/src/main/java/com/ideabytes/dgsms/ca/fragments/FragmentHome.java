package com.ideabytes.dgsms.ca.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ideabytes.dgsms.ca.AddPlacardDialogActivity;
import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.MyAppData;
import com.ideabytes.dgsms.ca.database.DeleteDBData;
import com.ideabytes.dgsms.ca.database.InsertDBData;
import com.ideabytes.dgsms.ca.showall.ShowAllFragAct;
import com.ideabytes.dgsms.ca.ShowAll;
import com.ideabytes.dgsms.ca.alertdialog.AlertDialogOnPickupOrders;
import com.ideabytes.dgsms.ca.database.GetDBData;

import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;
import org.com.ca.dgsms.ca.model.DeleteTransaction;
import org.com.ca.dgsms.ca.model.PlacardDisplayLogic;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/****************************************************************
 * Copy right @Ideabytes Software India Private Limited
 * Web site : http://ideabytes.com
 * Name : FragmentHome
 * author:  Suman
 * Description : To Show placards on main screen and banner data
 * Created Data : 15-10-2015
 * Modified Date : 04-12-2015
 * Reason: checking terms and conditions usinf text file content
 ****************************************************************/
public class FragmentHome extends Fragment implements DBConstants {
    private final String LOGTAG = "FragmentHome";
    private View rootView;
    private TextView tv;
    String TAG=getClass().getSimpleName().toString();
    private Utils utils = new Utils();
    private ArrayList<String> listOfIds = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            //by default load single placard as max placard value
            rootView = inflater.inflate(R.layout.main_fragment, container, false);
            tv = (TextView) rootView.findViewById(R.id.tvMain);
            //get placard count from text file that is updated when placard loaded

            Button addButton = (Button) rootView.findViewById(R.id.BtnAddPlcard);
            //addButton.setText(Localization.Dialog_Load_Item_Title);
            addButton.setText(Utils.getResString(R.string.load_item));
            addButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getActivity(), AddPlacardDialogActivity.class);
                    getActivity().startActivity(myIntent);

                }
            });

            // To refresh banner data, this show update placard transactions on the main screen
            display(populateBannerData());

            //PlacardsString.getOptimized(populateBannerData());
            Button btnShowAll = (Button) rootView.findViewById(R.id.BtnShowAllTypes);
            setShowAllBtnVisibility(btnShowAll);
            btnShowAll.setText(Utils.getResString(R.string.show_placards));
            btnShowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowAllFragAct.class);
                    intent.putExtra(RESULT, populateBannerData());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            Button btnDeleteAll = (Button) rootView. findViewById(R.id.BtnDeleteAll);
            setShowAllBtnVisibility(btnDeleteAll);
            btnDeleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDeleteAll();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void showAlertDeleteAll() {
        //ALERT_DIALOG_THEME
        android.app.AlertDialog.Builder builder;
        builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(utils.getResString(getActivity().getApplicationContext(),R.string.Dialog_Alert_Title));
        builder.setMessage(utils.getResString(getActivity().getApplicationContext(),R.string.alert_msg_delete_all));
        builder.setCancelable(true);
        //when 404 show only one button,else two buttons means no force update
        builder.setPositiveButton(utils.getResString(getActivity().getApplicationContext(),R.string.btn_continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listOfIds.size() > 0) {
                    //for delete all , trans id must be "0"
                    savePlacardContent("delete all");
                    new DeleteDBData(getActivity().getApplicationContext()).deleteAllShipments(listOfIds);
                    listOfIds.clear();
                    Bundle bundle = new Bundle();
                    bundle.putString("showOrdersAction", "fromFrag");
                    FragmentHome fragmentHome = new FragmentHome();
                    fragmentHome.setArguments(bundle);
                    //after deleting call home fragment to refresh banner data and placards info
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragmentHome).commit();
                }
                dialog.cancel();
            }
        });
        builder.setNegativeButton(utils.getResString(getActivity().getApplicationContext(),R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
     }
    /**
     * This method gets data from transaction table lookup to populate banner
     * data in main screen
     *
     * author Suman
     *
     * @return void
     * @since 1.0
     */
    // To refresh banner data from database
    @SuppressLint("NewApi")
    public String populateBannerData() {
        String deviceId = new Utils().getDeviceId(getActivity().getApplicationContext());
        try {
            //on each launch check for pick up orders which are stored in local database table,
            //which are not yet loaded after received from web portal
//           if(!getArguments().getString("showOrdersAction").equalsIgnoreCase("fromAdapter")) {
            GetDBData get = new GetDBData(getActivity().getApplicationContext());
            if (get.getPickupOrders().length() > 0) {
                JSONArray orders = new Utils(getActivity().getApplicationContext())
                        .getOrdersFromDatabase();

                AlertDialogOnPickupOrders alert = new AlertDialogOnPickupOrders(
                        getActivity());
                alert.showDialog(orders);
            }
            //populate banner data with shipment details
//            final CustomAdapterShowListBannerData myShowList = new CustomAdapterShowListBannerData(
//                    new GetDBData(
//                            getActivity().getApplicationContext()).getBannerData()
//            );
//            final ListView listItemsForBanner = (ListView) rootView.findViewById(R.id.mainListView);
//            getActivity().getWindow().setBackgroundDrawable(
//                    new ColorDrawable(Color.TRANSPARENT));
//            listItemsForBanner.setAdapter(myShowList);



//            deleteall.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    final Dialog deleteEntry = new Dialog(getActivity(),R.style.PauseDialog);
//                    deleteEntry.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    deleteEntry.setCanceledOnTouchOutside(false);
//                    deleteEntry.setContentView(R.layout.custom_dialog_two_buttons);
//                    deleteEntry.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;
//                    TextView tvTitle = (TextView) deleteEntry
//                            .findViewById(R.id.Dialog_Two_Title);
//
//                    tvTitle.setText("Alert Message");
//                    TextView tvData1 = (TextView) deleteEntry
//                            .findViewById(R.id.Dialog_Two_Message);
//                    tvData1.setText("Do you want to delete all Shipments?");
//                    Button deleteEntryYes = (Button) deleteEntry
//                            .findViewById(R.id.Dialog_Two_Yes);
//                    deleteEntryYes.setText("Yes");
//                    deleteEntryYes.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ArrayList<HashMap<String,String>> data=new GetDBData(getActivity().getApplicationContext()).getBannerData();
//                            for(int i=0;i<data.size();i++){
//                                savePlacardContent("delete all");
//                                Log.v(TAG +"col id",data.get(i).get(COL_ID));
//                                new DeleteTransaction()
//                                        .deleteFromTransaction(data.get(i).get(COL_ID),
//                                                data.get(i).get(COL_USER_ID), data.get(i).get(COL_TRANSACTION_ID),
//                                                data.get(i).get(COL_MAX_PLACARD));
//                                deleteEntry.dismiss();
//                                FragmentManager fragmentManager = getFragmentManager();
//                                fragmentManager.beginTransaction()
//                                        .replace(R.id.frame_container, new FragmentHome()).commit();
//                            }
//                        }
//                    });
//
//                    Button deleteEntryNo = (Button) deleteEntry
//                            .findViewById(R.id.Dialog_Two_No);
//                    deleteEntryNo.setText("No");
//                    deleteEntryNo.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            deleteEntry.dismiss();
//                        }
//                    });
//
//                    deleteEntry.show();
//                }
//            });
//           }

        } catch (Exception e) {
            e.printStackTrace();
        }
    return new PlacardDisplayLogic().placardDisplayLogic(deviceId,deviceId);
    }// populateBannerData()

    @Override
    public Object getInstance() {
        return null;
    }

    /**
     * Class to display all shipment details as banner on main screen
     */
    private class CustomAdapterShowListBannerData extends BaseAdapter  {
        private ArrayList<HashMap<String,String>> listItems = null;//items to be displayed in the alert dialog
        private Activity adapterBannerDataActivity = null;
        private int lastPosition = -1;//animation position
        private LayoutInflater mInflater;
        public CustomAdapterShowListBannerData(ArrayList<HashMap<String,String>> items) {
            this.listItems = items;


        }

        /* private view holder class */
        private class ViewHolder {
            Button minus = null;//button to unload item
            TextView tv = null;//text view to display transaction data for a shipment
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.banner_data, null);
                holder = new ViewHolder();
                holder.minus = (Button) convertView.findViewById(R.id.btn_Unload);
                holder.tv = (TextView) convertView.findViewById(R.id.tv_banner_data);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // final String completeString = listItems.get(position);
            final String bl = listItems.get(position).get(COL_BL);
            final String unNumber = listItems.get(position).get(COL_UN_NUMBER);
            String cl = listItems.get(position).get(COL_PRIMARY_PLACARD);
            //for class 1 display complete class like 1.1A
            if (listItems.get(position).get(COL_PRIMARY_PLACARD).startsWith("1")) {
                cl = listItems.get(position).get(COL_NAME);
            }
            // Log.v(LOGLOGTAG,"name "+cl);
            //for class 1.4S, remove group name, if appends 14SS
            String group = listItems.get(position).get(COL_GROUP_NAME);
            if (cl.equalsIgnoreCase("1.4S")) {
                group = "";
            }
            final String netWt = listItems.get(position).get(COL_GROSS_WEIGHT);
            final String dgWt = listItems.get(position).get(COL_DG_WEIGHT);
            final String units = listItems.get(position).get(COL_NO_OF_UNITS);
            final String ibcStatus = listItems.get(position).get(COL_IBC_STATUS);
            final String weightType = listItems.get(position).get(COL_WEIGHT_TYPE);
            final String NOStp=listItems.get(position).get(COL_NOS_DETAILS);
            String completeString = "";
                    if(NOStp.equalsIgnoreCase("1")){
//                    completeString=unNumber+" CL:"+cl+group+" NetWt:"+netWt+weightType+" DGWt:"+dgWt+" "+weightType
//                    +" Units:"+units+" B/L:"+bl+" Bulk:"+ibcStatus ;
                        completeString=unNumber+" CL:"+cl+group+" NetWt:"+netWt+weightType+" DGWt:"+dgWt+" "+weightType
                                +" Units:"+units+" B/L:"+bl ;
                    }
            else {
//                        completeString=unNumber+" CL:"+cl+group+" NetWt:"+netWt+weightType+" DGWt:"+dgWt+" "+weightType
//                                +" Units:"+units+" B/L:"+bl+" Bulk:"+ibcStatus +" N.O.S:"+NOStp;
                        completeString=unNumber+" CL:"+cl+group+" NetWt:"+netWt+weightType+" DGWt:"+dgWt+" "+weightType
                                +" Units:"+units+" B/L:"+bl+" N.O.S:"+NOStp;
                    }
            final String finalCompleteString = completeString;
            holder.minus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog deleteEntry = new Dialog(getActivity(),R.style.PauseDialog);
                    deleteEntry.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    deleteEntry.setCanceledOnTouchOutside(false);
                    deleteEntry.setContentView(R.layout.custom_dialog_two_buttons);
                    deleteEntry.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_Left_Right;

                    TextView tvTitle = (TextView) deleteEntry
                            .findViewById(R.id.Dialog_Two_Title);
//                    Log.v(TAG +" unloadtext",Localization.Dialog_Remove_Entry);
                    tvTitle.setText(Utils.getResString(R.string.unload_title));
                    TextView tvData1 = (TextView) deleteEntry
                            .findViewById(R.id.Dialog_Two_Message);
                    tvData1.setText(finalCompleteString);
                    Button deleteEntryYes = (Button) deleteEntry
                            .findViewById(R.id.Dialog_Two_Yes);
                    deleteEntryYes.setText(Utils.getResString(R.string.Dialog_Btn_Delete_Yes));

                    Button deleteEntryNo = (Button) deleteEntry
                            .findViewById(R.id.Dialog_Two_No);
                    deleteEntryNo.setText(Utils.getResString(R.string.Dialog_Btn_Delete_No));

                    deleteEntryYes.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            try {
                                //here userid and transaction always same, that is y used only one ArrayList to set both
                                final String id = listItems.get(position).get(COL_ID);
                                final String userID = listItems.get(position).get(COL_USER_ID);
                                final String transID = listItems.get(position).get(COL_TRANSACTION_ID);
                                final String maxPlacard = listItems.get(position).get(COL_MAX_PLACARD);
                                listOfIds.add(id);
                                savePlacardContent("delete");
                                new DeleteTransaction()
                                        .deleteFromTransaction(id,
                                                userID, maxPlacard, transID);
                                deleteEntry.dismiss();
                               //after deleting call home fragment to refresh banner data and placards info
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame_container, new FragmentHome()).commit();
                            } catch (Exception e) {
                                e.printStackTrace();}
                        }
                    });

                    deleteEntryNo.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            deleteEntry.dismiss();
                        }
                    });
                    deleteEntry.show();

                }
            });
            try {
                //Log.v(LOGTAG,"banner data(shipment) "+mData.get(position));
                if (listItems.get(position).get(COL_ID) != null) {
                    listOfIds.add(listItems.get(position).get(COL_ID));
                }
                holder.tv.setText(completeString);
//                if(AddPlacardDialogActivity.text[0].toString().contains("N.O.S")){
//
//                    AddPlacardDialogActivity.nos= "N.O.S:"+" " +AddPlacardDialogActivity.nos_text.getText().
//                            toString().trim();
//                }


                Log.v(TAG, completeString +" "+ AddPlacardDialogActivity.nos);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Animation animation = AnimationUtils.loadAnimation(getActivity(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            convertView.startAnimation(animation);
            return convertView;
        }
        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return listItems.indexOf(getItem(position));
        }
    }

    /**
     * This method is to display placards in fragment
     */
    private void display(final String placardString) {
        FragmentManager fragmentManager = null;
        GetDBData getDBData = new GetDBData(getActivity().getApplicationContext());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            fragmentManager = getChildFragmentManager();
        }
        Bundle bundle = new Bundle();
        Utils utils = new Utils();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            String deviceId = new Utils().getDeviceId(getActivity().getApplicationContext());
           // Log.v(LOGTAG, "display():: dgResult " + dgResult);
            FragmentToShowPlacards fragmentSinglePlacardDefault = new FragmentToShowPlacards();
            HashMap<String,String> res = utils.optimize(placardString);
            bundle.putInt(COUNT, res.size());
            bundle.putString(RESULT, new PlacardDisplayLogic().placardDisplayLogic(deviceId,deviceId));
            bundle.putString(KEY, OPTIMIZED);
            for (Map.Entry<String, String> e : res.entrySet()) {
                String result[] = e.getValue().split(":");
                String className = result[0];
                //Log.v(LOGTAG, "className " + className);
                if (getDBData.getBannerData().size() > 0) {
                    if (className.equalsIgnoreCase("sempty")) {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(Utils.getResString(R.string.No_Placards_Required_Message));
                        tv.setTextColor(Color.BLACK);
                    }
                }
            }
            fragmentSinglePlacardDefault.setArguments(bundle);
            fragmentManager.popBackStack();//before adding , remove frags if any
//            fragmentTransaction.replace(R.id.frame_container, fragmentSinglePlacardDefault).commit();
//            fragmentTransaction
//                    .add(R.id.frame_container, fragmentSinglePlacardDefault).commit();

            ArrayList<HashMap<String,String>> bannerData = new GetDBData(
                    getActivity().getApplicationContext()).getBannerData();
            // Log.v("Suman","size "+bannerData.size());
            if (bannerData.size() <= 0) {
                fragmentTransaction.replace(R.id.frame_container, fragmentSinglePlacardDefault).commit();
            }
            CustomAdapterShowListBannerData myShowList = new CustomAdapterShowListBannerData(
                    new GetDBData(
                            getActivity().getApplicationContext()).getBannerData()
            );
            final ListView lv1 = (ListView)rootView. findViewById(R.id.mainListView);
            getActivity().getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
            lv1.setAdapter(myShowList);
        }  catch (Exception e) {
            e.getMessage();
        }
    }
    /**
     * Based on records in the database enable "Show All" button ,
     * when nothing added no need to show, "Show All" button
     *
     * @param button
     */
    private void setShowAllBtnVisibility(final Button button) {
        GetDBData getDBData = new GetDBData(getActivity().getApplicationContext());
        if (getDBData.getBannerData().size() > 0) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }


    public void savePlacardContent(final String action) {
        String deviceId = new Utils().getDeviceId(getActivity().getApplicationContext());
        PlacardDisplayLogic logic = new PlacardDisplayLogic();
        String finalString = logic.placardDisplayLogic(deviceId, deviceId);
        //save placarding type in db
        if (action.equalsIgnoreCase("delete all")) {
            //for delete all , trans id must be "0"
            new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, action,
                    "0");
        } else if (action.equalsIgnoreCase("delete")) {
            new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, action,
                    new GetDBData(getActivity().getApplicationContext()).getMaxTransId());
        } else {
            new InsertDBData(HomeActivity.getContext()).savePlacardingType(finalString, action,
                    new GetDBData(getActivity().getApplicationContext()).getMaxTransId());
        }
    }









}