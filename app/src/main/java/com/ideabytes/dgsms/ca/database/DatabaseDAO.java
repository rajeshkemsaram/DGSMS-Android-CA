package com.ideabytes.dgsms.ca.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by suman on 27/7/16.
 */
public class DatabaseDAO {
    private Context context;
    public DatabaseDAO() {

    }
    public DatabaseDAO(Context context) {
        this.context = context;
    }
    public SQLiteDatabase db;
    public synchronized void getDatabaseConnection(final int code) {
        try {
            if (code == 0) {
                db = DatabaseHelper.getDatabaseInstance(context).getWritableDatabase();
            } else {
                db = DatabaseHelper.getDatabaseInstance(context).getReadableDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void closeDatabaseConnection() {
        try {
            if (db != null && db.isOpen()) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
