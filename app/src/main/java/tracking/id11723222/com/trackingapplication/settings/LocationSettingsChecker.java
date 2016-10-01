package tracking.id11723222.com.trackingapplication.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

import java.lang.reflect.Method;

import tracking.id11723222.com.trackingapplication.Constants;

/**
 * Created by tranc on 1/10/2016.
 */

public class LocationSettingsChecker {
    public static boolean checkLocationSettings(Context context){

        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = false;


        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return network_enabled;
    }

    public static boolean checkGPSSettings(Context context){

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod(Constants.MOBILE_DATA_ENABLED);
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }
        return mobileDataEnabled;

    }





}
