package org.com.ca.dgsms.ca.model;

import com.ideabytes.dgsms.ca.HomeActivity;
import com.ideabytes.dgsms.ca.database.DatabaseDAO;
import com.ideabytes.dgsms.ca.database.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBConnection extends DatabaseDAO {

	public Cursor resultSet = null;
	public SQLiteDatabase connection = null;
	private Context context = null;


	public DBConnection(Context context) {
		this.context = context;
	}

	// public final String DB_USERNAME = "cmadmin_dev";
	// public final String DB_PASSWORD = "CmAdmin123";
	// public final String DB_SCHEMA = "cmadmin_dgms";

	public String getValue(Cursor c, String t) {

		c.getString(c.getColumnIndex(t));

		return "test";

	}

    public Cursor getResultSetForSelect(String query, final int code) {
        try {
            getDatabaseConnection(code);
            resultSet = db.rawQuery(query, null);

        } catch (NullPointerException npe) {
            System.out
                    .println("NullPointerException caught while executing query, error msg: 40"
                            + npe);
        } catch (CursorIndexOutOfBoundsException ciobe) {
            ciobe.printStackTrace();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            System.out
                    .println("Execption caught while executing query, error msg: "
                            + sqlex);
        }
        return resultSet;

    }
    public void getResultSetForInsertOrUpdate(final String query,
                                              final int code) {
        try {
            getDatabaseConnection(code);
            // resultSet = connection.rawQuery(query,null);
            db.execSQL(query);

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            System.out
                    .println("Execption caught while executing query, error msg: "
                            + sqlex);
        }
    }
    public SQLiteDatabase getConnection(Context context) {
        try {
            if (db == null) {
                System.out.println("Connection cannot be established");
            }

        } catch (SQLException sql) {
            sql.printStackTrace();
        } catch (Exception e) {
            System.out
                    .println("Execption caught while connecting to database, error msg: "
                            + e);
            e.printStackTrace();
        }
        return db;
    }

	public void closeConnections() {
		try {
			if (resultSet != null)
				resultSet.close();
			if (connection != null)
				connection.close();
			connection = null;

		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Execption caught while closing database, error msg: "
							+ e);
		}
	}

	public boolean hasNextElement(Cursor cursor) {
		if (cursor.moveToNext()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method displays Error message which is used in debugging
	 * 
	 * @author suman
	 * @param className
	 * @param message
	 */
	public void displayLog(String className, String message) {
		android.util.Log.v(className, message);
	}

	public String getString(Cursor c, String t) {
		String result = "";

		try {
			result = c.getString(c.getColumnIndex(t));
		} catch (CursorIndexOutOfBoundsException ciobe) {
			ciobe.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("Execption caught while getting value, error msg: "
							+ e);
		}
		return result;
	}

	public int getInt(Cursor c, String t) {
		int result = 0;
		try {
			result = c.getInt(c.getColumnIndex(t));
		} catch (CursorIndexOutOfBoundsException ciobe) {
			ciobe.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("Execption caught while getting value, error msg: "
							+ e);
		}
		return result;
	}

	public boolean getBoolean(Cursor c, String t) {
		boolean result = false;
		try {
			result = Boolean.parseBoolean(c.getString(c.getColumnIndex(t)));
		} catch (CursorIndexOutOfBoundsException ciobe) {
			ciobe.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("Execption caught while getting value, error msg: "
							+ e);
		}
		return result;
	}

	public double getDouble(Cursor c, String t) {
		double result = 0;
		try {
			result = c.getDouble(c.getColumnIndex(t));
		} catch (CursorIndexOutOfBoundsException ciobe) {
			ciobe.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("Execption caught while getting value, error msg: "
							+ e);
		}
		return result;
	}
}
