package com.doannganh.salesmobileassistant.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.doannganh.salesmobileassistant.R;

public class PermissionUtil {
    /**
     * Check network
     * @param context
     * @return
     */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void showToastNetworkError(Context context){
        Toast.makeText(context, R.string.checkconnect_error, Toast.LENGTH_SHORT).show();
    }

    // ----------------------------------------------------
    /**
     * Check permission
     */
    public static boolean checkPermission(Context context, String permission){
        if (ContextCompat.checkSelfPermission(context, permission)//Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public static void showDialogPermission(Activity activity, int requestCode, String[] permissions){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static void showToastNotPermission(Context context){
        Toast.makeText(context, R.string.checkpermission_error, Toast.LENGTH_LONG).show();
    }

    /**
     *  Check GPS
     */
    public static boolean checkGPSEnable(LocationManager manager){
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return false;
        }
        return true;
    }

    public static void showDialogGPS(Activity activity){
        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
}
