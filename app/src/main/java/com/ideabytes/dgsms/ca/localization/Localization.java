//package com.ideabytes.dgsms.ca.localization;
//
//import org.com.ca.dgsms.ca.model.DBConstants;
//
//import com.ideabytes.dgsms.ca.database.DatabaseDAO;
//import com.ideabytes.dgsms.ca.database.DatabaseHelper;
//import com.ideabytes.dgsms.ca.database.InsertDBData;
//import com.ideabytes.dgsms.ca.database.GetDBData;
//import com.ideabytes.dgsms.ca.exceptions.Exceptions;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import java.util.Arrays;
//
///****************************************************************************************
// * Copy right @Ideabytes Software India Private Limited
// * Web site : http://ideabytes.com
// * Name : Localization
// * Modfified on : 27-10-2015
// * author:  Suman
// * Description : This class is to store and retrieve all localization data to application
// *****************************************************************************************/
//public class Localization extends DatabaseDAO implements DBConstants {
//
//	private Context context;
//	//private static String TAG = "Localization";
//    private int dbCode = 0;
//	public Localization(Context context) {
//		super(context);
//        this.context = context;
//	}
//
//	public static String app_name = null;
//	public static String Dialog_Btn_Delete_No = "No";
//	public static String Dialog_Btn_Delete_Yes = "Yes";
//	public static String Dialog_Btn_Ok = "OK";
//    public static String Dialog_Btn_Save = "Save";
//	public static String Dialog_Btn_Exit = "Exit";
//	public static String Dialog_Btn_Wifi = "Wifi";
//	public static String Dialog_Btn_Mobile = "Mobile";
//	public static String Dialog_Alert_Title = "Alert Message";
//	public static String Dialog_InvalidUn_Message = "Invalid UN Number";
//	public static String Dialog_Forbidden_Message = "Forbidden UN Number";
//	public static String Dialog_Load_Item_Title = "Load Item";
//	public static String Dialog_Add_Item_Title = "Add Item";
//	public static String Dialog_Add_Item_Et_BL = "Bill of Lading";
//
//	public static String Dialog_Add_Item_Btn_Scan = "Scan";
//	public static String Dialog_Add_Item_Et_GRWeight = "Enter Weight";
//	public static String Dialog_Add_Item_Et_Unumber = "UN Number";
//	public static String Dialog_Add_Item_Et_Units = "Enter No Of Units";
//	public static String Dialog_Add_Item_Btn_Display = "Display Placard";
//	public static String Dialog_Add_Item_Btn_RbPounds = "Lbs";
//	public static String Dialog_Add_Item_Btn_RbKgs = "Kgs";
//	public static String Dialog_Add_Item_Et_Erap = "Enter ERAP";
//	public static String Dialog_Add_Item_Tv_IBC = "Is packing type an IBC ?";
//	public static String Dialog_Add_Item_Tv_Residue = "Is there residue in IBC ?";
//
//	public static String Please_Enter_Gross_Weight_error = "Enter DG Gross Mass";
//	public static String UN_Number_4Ditgits_error = "UN Number must be 4 digits ";
//	public static String Please_Enter_Bill_OF_lad_error = "Enter Bill of Lading";
//	public static String Rear_Camera_Unavailable = "Rear Facing Camera Unavailable";
//	public static String Sorry_Try_Again_Message = "Sorry Try Again";
//	public static String Mutual_Exclusion_Message = "Class 1 cannot be mixed with Class 7";
//
//	// menu items
//	public static String Menu_change_locale = "Change Language";
//	public static String Menu_Buy_Renew_License = "Buy/Renew License";
//    public static String Menu_Upgrade_License = "Upgrade License";
//	public static String Menu_Show_Orders = "Show Orders";
//	public static String Menu_Max_Placards = "Max Placards";
//	public static String Menu_History = "History";
//    public static String Menu_Items_Quit = "Quit";
//	public static String Dialog_Max_Plcard_Message = "Select Max Placards on the Truck";
//	public static String Select_Description = "Select Description";
//	public static String Dialog_Sp84_Title = "Select Type of Virus";
//	public static String Dialog_Package_Group = "Select Packing Group";
//    public static String Dialog_Category = "Select Category";
//	public static String Dialog_Cont_Be_Loaded = "Can't Be Loaded into Truck";
//	public static String TV_UN_Description = "UN Description";
//	public static String Et_DG_Mass_Pkg = "DG mass/pkg";
//	public static String Et_No_Of_Pkg = "No. of Pkgs";
//	public static String Et_ERAP =  "ERAP Number";
//	public static String No_Placards_Required_Message = "Placard Not Required";
//	public static String Placards_On_Truck_Message = "These Placard(s) Must be on the Truck";
//	public static String Dialog_Remove_Entry = "Do You Want to Unload Item ?";
//	public static String Toast_Max_Placards = "Max Placard holders set to ";
//	public static String Toast_Selected_Language = "Language set to English ";
//	public static String Toast_No_Network = "Please Connect To Working Internet Connection";
//	public static String Toast_No_Fhash_Light = "Your Device Don't Have Flash Light";
//	public static String TV_DG_Gross_Mass = "DG Gross Mass ";
//	public static String Dialog_Do_Not_Accept_Load = "Not Enough Placard Holders on the truck Do NOT accept load";
//	public static String Dialog_License_Expaired = "Your License Expired, Please Contact Admin";
//	public static String Dialog_LICENSE_VERIFY = "Please Verify Your License";
//	public static String Toast_Message_No_Orders = "Sorry, you Don't have any orders to pickup";
//	public static String Dialog_Message_Select_Orders = "Select Order";
//	public static String Dialog_Message_No_change	 = "Cannot change No of Placards";
//	public static String Title_Transaction_History = "Transaction History";
//    //Danger placard optimization options
//    public static String Enable = "Enable";
//    public static String Disable = "Disable";
//    public static String Btn_Show_All = "Show Placarding Options";
//    public static String Btn_Optimized = "Optimized";
//    public static String Btn_SemiOptimized = "Semi Optimized";
//    public static String Btn_Non_Optimized = "Non Optimized";
//    public static String All_Scenarios = "Placards in all three scenarios";
//    public static String No_ERG_Guide = "Guide for the UN Number is not found";
//
//
//	private InsertDBData insertdb = null;
//
//	public void insertLanguageContent() {
//        Cursor c = null;
//        try {
//            getDatabaseConnection(dbCode);
//            insertdb = new InsertDBData(context);
//            String query = "select * from " + TABLE_LANGUAGE;
//            c = db.rawQuery(query, null);
//           //  Log.v("Localization", "COUNT OF TABLE LANGUAGE==" + c.getCount());
//
//            if (c.getCount() == 0) {
//                // Log.v("Localization", "IN THE LOOP COUNT =0");
//                insertdb.insertTestData();
//                getLanguageContent(1);
//            }
//
//		/*
//		 * CASE :Database is present i.e When you close(kill/stop) the app and
//		 * restart it (recreate/oncreate)
//		 */
//            else {
//                getLanguageContent(lastHighlighted());
//            }
//        } catch (Exception e) {
//            new Exceptions(context,Localization.this
//                    .getClass().getName(),Arrays.toString(e.getStackTrace()));
//        } finally {
//            //closes database connection
//            closeDatabaseConnection();
//            if(c != null) {
//                c.close();
//            }
//        }
//	}
//
//	public int lastHighlighted() {
//        Cursor c = null;
//        int highlighted_language_id = 0;
//        try {
//            //gets connection for SQLite database
//            getDatabaseConnection(dbCode);
//            String query = "select " + COL_LANGUAGE_ID + " from " + TABLE_LANGUAGE
//                    + " where " + COL_SELECTED_STATE + "=1";
//
//            c = db.rawQuery(query, null);
//
//            while (c.moveToNext()) {
//
//                highlighted_language_id = c.getInt(c
//                        .getColumnIndex(COL_LANGUAGE_ID));
//            }
//        } catch (Exception e) {
//            new Exceptions(context,Localization.this
//                    .getClass().getName(), Arrays.toString(e.getStackTrace()));
//        } finally {
//            //closes database connection
//            closeDatabaseConnection();
//            if(c != null) {
//                c.close();
//            }
//        }
//		return highlighted_language_id;
//	}
//
//	public void getLanguageContent(int language_id) {
//        Cursor c = null;
//        try {
//            //System.out.println("get language content "+language_id);
//            //gets connection for SQLite database
//            getDatabaseConnection(dbCode);
//            String Attribute_Name = null;
//            String Text = null;
//
//            setSelectedStateInLanguageZero(db);
//
//            String query = "select " + COL_LANGUAGE_CONTENT_ID + ", "
//                    + COL_MSG_TEXT + " from " + TABLE_LANGUAGE_CONTENT + " where "
//                    + COL_LANGUAGE_ID + "='" + language_id + "'";
//
//            c = db.rawQuery(query, null);
//
//            // Log.v("Localization","no ofcolumns in language table ==" + c.getColumnCount());
//            // Log.v("Localization", "sizes of Special Cases==" + c.getCount());
//
//            while (c.moveToNext()) {
//
//                Attribute_Name = c.getString(c
//                        .getColumnIndex(COL_LANGUAGE_CONTENT_ID));
//                Text = c.getString(c.getColumnIndex(COL_MSG_TEXT));
//
//                if (Attribute_Name.equals("app_name")) {
//                    app_name = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Btn_Delete_No")) {
//                    Dialog_Btn_Delete_No = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Btn_Delete_Yes")) {
//                    Dialog_Btn_Delete_Yes = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Btn_Ok")) {
//                    Dialog_Btn_Ok = Text;
//                } if (Attribute_Name.equals("Dialog_Btn_Save")) {
//                    Dialog_Btn_Save = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Btn_Exit")) {
//                    Dialog_Btn_Exit = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Alert_Title")) {
//                    Dialog_Alert_Title = Text;
//                }
//                if (Attribute_Name.equals("Dialog_InvalidUn_Message")) {
//                    Dialog_InvalidUn_Message = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Forbidden_Message")) {
//                    Dialog_Forbidden_Message = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Load_Item_Title")) {
//                    Dialog_Load_Item_Title = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Title")) {
//                    Dialog_Add_Item_Title = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Et_BL")) {
//                    Dialog_Add_Item_Et_BL = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Btn_Scan")) {
//                    Dialog_Add_Item_Btn_Scan = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Et_GRWeight")) {
//                    Dialog_Add_Item_Et_GRWeight = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Et_Unumber")) {
//                    Dialog_Add_Item_Et_Unumber = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Et_Units")) {
//                    Dialog_Add_Item_Et_Units = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Btn_Display")) {
//                    Dialog_Add_Item_Btn_Display = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Btn_RbPounds")) {
//                    Dialog_Add_Item_Btn_RbPounds = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Btn_RbKgs")) {
//
//                    Dialog_Add_Item_Btn_RbKgs = Text;
//                }
//                if (Attribute_Name.equals("Please_Enter_Gross_Weight_error")) {
//                    Please_Enter_Gross_Weight_error = Text;
//                }
//                if (Attribute_Name.equals("Please_Enter_Bill_OF_lad_error")) {
//                    Please_Enter_Bill_OF_lad_error = Text;
//                }
//                if (Attribute_Name.equals("Rear_Camera_Unavailable")) {
//                    Rear_Camera_Unavailable = Text;
//                }
//                if (Attribute_Name.equals("Sorry_Try_Again_Message")) {
//                    Sorry_Try_Again_Message = Text;
//                }
//                if (Attribute_Name.equals("Mutual_Exclusion_Message")) {
//                    Mutual_Exclusion_Message = Text;
//                }
//                if (Attribute_Name.equals("Menu_Items_Quit")) {
//                    Menu_Items_Quit = Text;
//                }
//                if (Attribute_Name.equals("Menu_change_locale")) {
//                    Menu_change_locale = Text;
//                }
//                if (Attribute_Name.equals("Menu_Show_Orders")) {
//                    Menu_Show_Orders = Text;
//                }
//                if (Attribute_Name.equals("Menu_Buy_Renew_License")) {
//                    Menu_Buy_Renew_License = Text;
//                } if (Attribute_Name.equals("Menu_Upgrade_License")) {
//                    Menu_Upgrade_License = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Max_Plcard_Message")) {
//                    Dialog_Max_Plcard_Message = Text;
//                }
//                if (Attribute_Name.equals("Select_Description")) {
//                    Select_Description = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Sp84_Title")) {
//                    Dialog_Sp84_Title = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Package_Group")) {
//                    Dialog_Package_Group = Text;
//                } if (Attribute_Name.equals("Dialog_Category")) {
//                    Dialog_Category = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Cont_Be_Loaded")) {
//                    Dialog_Cont_Be_Loaded = Text;
//                }
//                if (Attribute_Name.equals("TV_UN_Description")) {
//                    TV_UN_Description = Text;
//                }
//                if (Attribute_Name.equals("Et_DG_Mass_Pkg")) {
//                    Et_DG_Mass_Pkg = Text;
//                }
//                if (Attribute_Name.equals("Et_No_Of_Pkg")) {
//                    Et_No_Of_Pkg = Text;
//                }
//                if (Attribute_Name.equals("Et_ERAP")) {
//                    Et_ERAP = Text;
//                }
//                if (Attribute_Name.equals("Placards_On_Truck_Message")) {
//                    Placards_On_Truck_Message = Text;
//                }
//                if (Attribute_Name.equals("No_Placards_Required_Message")) {
//                    No_Placards_Required_Message = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Remove_Entry")) {
//                    Dialog_Remove_Entry = Text;
//                }
//                if (Attribute_Name.equals("Menu_Max_Placards")) {
//                    Menu_Max_Placards = Text;
//                }
//                if (Attribute_Name.equals("Menu_History")) {
//                    Menu_History = Text;
//                }
//                if (Attribute_Name.equals("Toast_Max_Placards")) {
//                    Toast_Max_Placards = Text;
//                }
//                if (Attribute_Name.equals("Toast_Selected_Language")) {
//                    Toast_Selected_Language = Text;
//                }
//                if (Attribute_Name.equals("Toast_No_Network")) {
//                    Toast_No_Network = Text;
//                }
//                if (Attribute_Name.equals("Toast_No_Fhash_Light")) {
//                    Toast_No_Fhash_Light = Text;
//                }
//                if (Attribute_Name.equals("TV_DG_Gross_Mass")) {
//                    TV_DG_Gross_Mass = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Et_Erap")) {
//                    Dialog_Add_Item_Et_Erap = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Do_Not_Accept_Load")) {
//                    Dialog_Do_Not_Accept_Load = Text;
//                }
//                if (Attribute_Name.equals("UN_Number_4Ditgits_error")) {
//                    UN_Number_4Ditgits_error = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Tv_IBC")) {
//                    Dialog_Add_Item_Tv_IBC = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Add_Item_Tv_Residue")) {
//                    Dialog_Add_Item_Tv_Residue = Text;
//                }
//                if (Attribute_Name.equals("Dialog_License_Expaired")) {
//                    Dialog_License_Expaired = Text;
//                }
//                if (Attribute_Name.equals("Dialog_LICENSE_VERIFY")) {
//                    Dialog_LICENSE_VERIFY = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Btn_Wifi")) {
//                    Dialog_Btn_Wifi = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Btn_Mobile")) {
//                    Dialog_Btn_Mobile = Text;
//                }
//                if (Attribute_Name.equals("Toast_Message_No_Orders")) {
//                    Toast_Message_No_Orders = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Message_Select_Orders")) {
//                    Dialog_Message_Select_Orders = Text;
//                }
//                if (Attribute_Name.equals("Dialog_Message_No_change")) {
//                    Dialog_Message_No_change = Text;
//                }
//                if (Attribute_Name.equals("Title_Transaction_History")) {
//                    Title_Transaction_History = Text;
//                }  if (Attribute_Name.equals("Enable")) {
//                    Enable = Text;
//                }  if (Attribute_Name.equals("Disable")) {
//                    Disable = Text;
//                }  if (Attribute_Name.equals("Btn_Show_All")) {
//                    Btn_Show_All = Text;
//                } if (Attribute_Name.equals("Btn_Optimized")) {
//                    Btn_Optimized = Text;
//                }
//                if (Attribute_Name.equals("Btn_SemiOptimized")) {
//                    Btn_SemiOptimized = Text;
//                }
//                if (Attribute_Name.equals("Btn_Non_Optimized")) {
//                    Btn_Non_Optimized = Text;
//                }  if (Attribute_Name.equals("All_Scenarios")) {
//                    All_Scenarios = Text;
//                }  if (Attribute_Name.equals("No_ERG_Guide")) {
//                    No_ERG_Guide = Text;
//                }
//
//                // Log.v(TAG, "####### Language details table details #######");
//                // Log.v(TAG," NAME  " + Attribute_Name);
//
//                // Log.v(TAG," TEXT  " + Text);
//
//            }
//            updateSelectedStateLanguageTable(language_id);
//            GetDBData gdb = new GetDBData(context);
//            gdb.getLanguage();
//        } catch (Exception e) {
//            new Exceptions(context,Localization.this
//                    .getClass().getName(), Arrays.toString(e.getStackTrace()));
//        } finally {
//            //closes database connection
//            closeDatabaseConnection();
//            if(c != null) {
//                c.close();
//            }
//        }
//	}
//
//	public int updateSelectedStateLanguageTable(int language_id) {
//        int rows = 0;
//        try {
//            //gets connection for SQLite database
//            getDatabaseConnection(dbCode);
//            ContentValues values = new ContentValues();
//            values.put(COL_SELECTED_STATE, 1);
//
//            rows = db.update(TABLE_LANGUAGE, values, COL_LANGUAGE_ID
//                    + " = ?", new String[]{Integer.toString(language_id)});
//        }
//        catch (Exception e) {
//            new Exceptions(context,Localization.this
//                    .getClass().getName(), Arrays.toString(e.getStackTrace()));
//        } finally {
//            //closes database connection
//            closeDatabaseConnection();
//
//        }
//		return rows;
//
//	}
//
//	// String
//	// querysetallZero="update "+TABLE_LANGUAGE+" set "+COL_SELECTED_STATE+
//	// "= 0";
//	// db.rawQuery( querysetallZero, null);
//
//	public int setSelectedStateInLanguageZero(SQLiteDatabase connection) {
//
//		ContentValues values = new ContentValues();
//		values.put(COL_SELECTED_STATE, 0);
//
//		int rows = connection.update(TABLE_LANGUAGE, values, null, null);
//		return rows;
//
//	}
//
//    @Override
//    public Object getInstance() {
//        return null;
//    }
//}
