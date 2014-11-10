package com.dss886.utils.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dss886 on 14/10/21.
 */
public class LogUtils {

    private static boolean enableLog = true;

    public static void v(String tag, String msg){
        print("v",tag,msg);
    }

    public static void d(String tag, String msg){
        print("d",tag,msg);
    }

    public static void w(String tag, String msg){
        print("w",tag,msg);
    }

    public static void e(String tag, String msg){
        printE("e",tag,msg);
    }

    private static void print(String type, String tag, String msg){
        if(!enableLog){
            return;
        }
        System.out.print(getTime());
        System.out.print(" ");
        System.out.print(type);
        System.out.print("/");
        System.out.print(getClassName());
        System.out.print(" ");
        System.out.print(tag);
        System.out.print(": ");
        System.out.println(msg);
    }

    private static void printE(String type, String tag, String msg){
        if(!enableLog){
            return;
        }
        System.err.print(getTime());
        System.err.print(" ");
        System.err.print(type);
        System.err.print("/");
        System.err.print(getClassName());
        System.err.print(" ");
        System.err.print(tag);
        System.err.print(": ");
        System.err.println(msg);
    }

    private static String getTime(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }

    private static String getClassName(){
        String fullName = Thread.currentThread().getStackTrace()[4].getClassName();
        String[] fullNames = fullName.split("\\.");
        return fullNames[fullNames.length - 1];
    }

}
