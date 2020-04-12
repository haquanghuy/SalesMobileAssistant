package com.doannganh.salesmobileassistant.util;

import android.app.Activity;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.net.InetAddress;

public class UtilMethod {

    public static void hideKeyboardFrom(Activity context) {
        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public static String makeMyURLByAPI(String server, String entry, @Nullable String param){
        String re = "";

        if(server.substring(server.length()-1).equals("/"))
            re = server.substring(0, server.length()-1);

        re += "/api/";
        re += entry;
        if(param != null)
            re += "/" + param;

        return re;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    public static String convertIdToStatus(int id){
        switch (id){
            case 1:
                return "Pending";
            case 2:
                return "Verifying";
            case 3:
                return "Completed";
        }
        return "NaN";
    }

    public static String formartDoubleToStringInt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
}
