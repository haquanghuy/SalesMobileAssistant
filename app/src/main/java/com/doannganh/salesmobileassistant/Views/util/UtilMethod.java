package com.doannganh.salesmobileassistant.Views.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.doannganh.salesmobileassistant.Presenter.OrderPresenter;

import java.net.InetAddress;

public class UtilMethod {

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

    public static int convertStatusToId(String statusName){
        switch (statusName){
            case "Pending":
                return 1;
            case "Verifying":
                return 2;
            case "Completed":
                return 3;
        }
        return -1;
    }

    public static String formartDoubleToStringInt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
}
