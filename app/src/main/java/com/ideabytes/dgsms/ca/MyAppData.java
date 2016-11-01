package com.ideabytes.dgsms.ca;

import android.app.Application;

import com.ideabytes.dgsms.ca.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by suman on 16/8/16.
 */
public class MyAppData extends Application {


    private static MyAppData singleton = new MyAppData( );
    private MyAppData() { }
    public static MyAppData getInstance( ) {
        return singleton;
    }


    private String today="", yesterday="";

    public String getToday() {
        if (today.equalsIgnoreCase("")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(c.getTime());

        }
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getYesterday() {
        if (yesterday.equalsIgnoreCase("")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(c.getTime());
        }
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    private String userName = "rajesh123";
    private boolean isLicenseExpired = false;
    public void setLicenseExpired(boolean licenseExpired) {
        isLicenseExpired = licenseExpired;
    }

    public boolean isLicenseExpired() {
        return isLicenseExpired;
    }
     public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    private boolean verifyLicense = false;
    public void setVerifyLicense(boolean verifyLicense) {
        this.verifyLicense = verifyLicense;
    }
    public boolean isVerifyLicense() {
        return verifyLicense;
    }
    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    private String fcmToken = "";
    public void setUpdatePriority(int updatePriority) {
        this.updatePriority = updatePriority;
    }

    public int getUpdatePriority() {
        return updatePriority;
    }

    public int updatePriority = 0;

    public boolean isApkUpdated() {
        return isApkUpdated;
    }

    public void setApkUpdated(boolean apkUpdated) {
        isApkUpdated = apkUpdated;
    }

    public boolean isApkUpdated = false;
   public void setDetonatorType(String detonatorType) {
        this.detonatorType = detonatorType;
    }

    public void setHrcq(boolean hrcq) {
        isHrcq = hrcq;
    }

    public boolean isHrcq() {
        return isHrcq;
    }

    public boolean isHrcq = false;
    public void setHrcq(String hrcq) {
        this.hrcq = hrcq;
    }

    public String getHrcq() {
        return hrcq;
    }

    private String hrcq = "";
    public String getDetonatorType() {
        return detonatorType;
    }

    public String detonatorType = "Not sure";
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
