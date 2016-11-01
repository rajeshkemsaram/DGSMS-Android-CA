package com.ideabytes.dgsms.ca.logs;

/**
 * Created by suman on 13/1/16.
 */
public class Log {
    public void debug(final String className,final String message) {
        android.util.Log.v(className, "::" + message);
    }
    public void error(final String className,final String message) {
        android.util.Log.e(className, "::" + message);
    }
}
