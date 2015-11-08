package tracking.id11723222.com.trackingapplication.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;

import tracking.id11723222.com.trackingapplication.Constants;
import tracking.id11723222.com.trackingapplication.TimeUnit;

/**
 * Created by phealeyhang on 1/11/15.
 */

public class TrackingService extends IntentService {



    /**
     * Default constructor will set the name to the constant intent service name
     */

    public TrackingService() {
        super(Constants.INTENT_SERVICE_NAME);
    }

    /**
     *Constructor will set the name to a given name
     */

    public TrackingService(String name) {
        super(name);
    }

    private void setTimerValues(){

    }

    /**
     *Destroy service
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * This will retrieve the interval and duration from the given intent. It will use this to
     * calculate the maxValue, which is the amount of inverals within a duration. The seconds
     * will be the amount of time the thread will sleep for in millisecondsd * 1000. The do-while
     * loop used will first wait for the first interval before getting the current location and time
     * (in milliseconds) of the user and adding that into an intent and sending it as a broadcast.
     * It will do this until the maxValue(amount of intervals) reach 0.
     *@param intent
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int mDuration = Integer.parseInt(mSharedPreferences.getString(Constants.PREF_DURATION_SETTINGS, ""));
        int mInterval = Integer.parseInt(mSharedPreferences.getString(Constants.PREF_INTERVAL_SETTINGS,""));
        int mMaxValue = mDuration / mInterval;
        String timeUnit = mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS, Constants.FIRST_TIME);

        do{
            try {
                //handle seconds case
                if(timeUnit.equals(Constants.FIRST_TIME)){
                    Thread.sleep(Constants.MILLISECONDS_TO_SECONDS * mInterval);
                }
                //handle minutes case
                else if(timeUnit.equals(Constants.SECOND_TIME))
                {
                    Thread.sleep(Constants.MILLISECONDS_TO_MINUTES * mInterval);
                }
                updateUserLocation();
            } catch (InterruptedException e) {
            }
        }while((--mMaxValue) > 0);

    }

    private void updateUserLocation(){
        Intent newIntent = new Intent();
        LatLng location = getCurrentLocation();
        Time time = new Time(System.currentTimeMillis());
        newIntent.setAction(Constants.UPDATE_COMMAND);
        newIntent.putExtra(Constants.EXTRA_LOCATION, location);
        newIntent.putExtra(Constants.TIME, time);
        sendBroadcast(newIntent);

    }

    /**
     * This will get the current location from the LocationManaager. The Location manager is used
     * to call the getLastKnownLocation which gets the current location from the network provider
     * and it will later be converted into a LatLng object
     * @return LatLng
     */

    private LatLng getCurrentLocation(){
        Context mContext = getApplicationContext();
        LocationManager mManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
        Location currentLocation = mManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
    }




}
