package tracking.id11723222.com.trackingapplication.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.Locale;

import tracking.id11723222.com.trackingapplication.Constants;
import tracking.id11723222.com.trackingapplication.model.LocationData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;

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
        PowerManager pm = (PowerManager)getApplicationContext().getSystemService(
                            Context.POWER_SERVICE);
        PowerManager.WakeLock wk = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wakelock");
        wk.acquire();
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int mDuration = Integer.parseInt(mSharedPreferences.getString(Constants.PREF_DURATION_SETTINGS, Integer.toString(Constants.FIRST_DURATION)));
        int mInterval = Integer.parseInt(mSharedPreferences.getString(Constants.PREF_INTERVAL_SETTINGS,Integer.toString(Constants.FIRST_INTERVAL)));
        int mMaxValue = mDuration / mInterval;
        String timeUnit = mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS, Constants.FIRST_TIME);
        do{
            try {
                //handle seconds case
                if(timeUnit.equals(Constants.FIRST_TIME)){
                    Thread.sleep(Constants.MILLISECONDS_TO_SECONDS * mInterval);
                }
                //handle minutes case
                else if(timeUnit.equals(Constants.SECOND_TIME)) {
                    Thread.sleep(Constants.MILLISECONDS_TO_MINUTES * mInterval);
                }
                else{ //handle hours case
                    Thread.sleep(Constants.MILLISECONDS_TO_HOURS * mInterval);
                }
                addEntry();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while((--mMaxValue) > 0);
        wk.release();
        finishCommand();
    }

    private void addEntry(){
        LatLng location = getCurrentLocation();
        Time time = new Time(System.currentTimeMillis());
        try {
            Address add = new Geocoder(this, Locale.getDefault()).getFromLocation(location.latitude,location.longitude,1).get(0);
            String address = add.getFeatureName() +  " " + add.getThoroughfare() + ", "
                    + add.getLocality() + ", " + add.getAdminArea() + ", " + add.getCountryName();
            LocationData locationData = new LocationData(Constants.ZERO, address,
                    time.toString());
            ReminderDatabaseHelper.get(this).addLocation(locationData);

            Intent newIntent = new Intent();
            newIntent.setAction(Constants.UPDATE_COMMAND);
            newIntent.putExtra(Constants.EXTRA_LOCATION, locationData);
            sendBroadcast(newIntent);

        } catch (Exception ex) {
            Log.e(Constants.ERROR, Constants.ADD_CLICKED_EXCEPTION, ex);
        }

    }



    private void finishCommand(){
        Intent newIntent = new Intent();
        newIntent.setAction(Constants.FINISH_COMMAND);
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
