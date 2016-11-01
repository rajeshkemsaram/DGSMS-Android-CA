package com.ideabytes.dgsms.ca.exceptions;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ideabytes.dgsms.ca.alertdialog.AlertDialogTechError;
import com.ideabytes.dgsms.ca.utils.Utils;
import com.ideabytes.dgsms.landstar.R;

import org.com.ca.dgsms.ca.model.DBConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by suman on 7/12/15.
 */
public class Exceptions extends Exception implements DBConstants {
    public Context context;
    private String className;
    private String body;
    public Exceptions() {

    }
    public Exceptions(Context context,final String className,final String body) {
        this.context = context;
        this.className = className;
        this.body = body;
        showErrorDialog(className,body);
    }
    public void showErrorDialog(final String className,final String body) {
        generateDebugFile(FOLDER_PATH_DEBUG, className, body);
        AlertDialogTechError alertDialogTechError = new AlertDialogTechError(context);
        alertDialogTechError.showDialog(Utils.getResString(R.string.technical_error));
        return;
    }
    /***
     * This method stores debug text in specified location if any exception
     * caught of File (Bug report)
     *
     * @author Suman
     * @param fileName
     * @param body
     * @since 5.2.0
     */
    public static void generateDebugFile(final String path, final String fileName, final String body) {
        try {
            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();
            }
            FileOutputStream fOut = new FileOutputStream(root+"/"+fileName+".txt");
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
           // System.out.println(body);
            myOutWriter.append(body);
            myOutWriter.close();
            fOut.close();
            // Toast.makeText(MainActivity.getContext(), "Bug Report generated",
            // Toast.LENGTH_SHORT).show();
            // if(new
            // ConnectionDetector(MainActivity.getContext()).isConnectingToInternet())
            // {
            // new SendBugReportAsyncTask(context).execute();
            // }
        } catch (Exception e) {
            Log.e("Exceptions",
                    "Error in writing debug note to folder path" + e.toString());
        }
    }// generateDebugFile()

    @Override
    public Object getInstance() {
        return null;
    }
}
