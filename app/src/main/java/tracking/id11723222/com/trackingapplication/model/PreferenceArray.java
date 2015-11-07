package tracking.id11723222.com.trackingapplication.model;

import android.app.Activity;
import android.os.Bundle;

import tracking.id11723222.com.trackingapplication.R;

/**
 * Created by phealeyhang on 7/11/15.
 */
public class PreferenceArray extends Activity{

    private String preference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public int getTimePreference(String prefTime){
        String[] timeArray = getResources().getStringArray(R.array.preference_time_settings);
        for(int i = 0; i < timeArray.length; i++){
            if(timeArray.equals(prefTime)) return i;
        }

        return 0;
    }

    public int getDurationPreference(String prefDuration){
        String[] durationArray = getResources().getStringArray(R.array.preference_duration);
        for(int i = 0; i < durationArray.length; i++){
            if(durationArray.equals(prefDuration)) return i;
        }
        return 0;
    }

    public int getIntervalPreference(String prefInterval){
        String[] intervalArray = getResources().getStringArray(R.array.preference_interval);
        for(int i = 0; i < intervalArray.length; i++){
            if(intervalArray.equals(prefInterval)) return i;
        }
        return 0;
    }


}
