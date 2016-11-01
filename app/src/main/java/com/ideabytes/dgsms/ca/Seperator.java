package com.ideabytes.dgsms.ca;

/**
 * Created by sairam on 15/9/16.
 */
public class Seperator {


    boolean flag;
   /*
   *flag= true -->generic
   *flag=false-->landstar
   */

    private static Seperator ourInstance = new Seperator();

    private Seperator() {
    }


    public static Seperator getInstance() {
        return ourInstance;
    }


    public  boolean changeTo(){
        flag= true;
        return flag ;
    }



}
