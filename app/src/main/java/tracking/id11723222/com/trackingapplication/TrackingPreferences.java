package tracking.id11723222.com.trackingapplication;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;

public class TrackingPreferences extends PreferenceActivity {

    private SharedPreferences mSharedPreferences;
    private ListPreference mTimeFormatPreference, mDurationPreference, mIntervalPreference;

    /**
     * Load xml layout with proper settings
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        addPreferencesFromResource(R.xml.timer_settings);
        setPreferences();
    }

    private void setPreferences(){
        mTimeFormatPreference = (ListPreference)findPreference(Constants.PREF_TIME_SETTINGS);
        mTimeFormatPreference.setSummary(mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS, ""));
        mTimeFormatPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mTimeFormatPreference.setSummary(newValue.toString());
                mSharedPreferences.edit().putString(Constants.PREF_TIME_SETTINGS, newValue.toString()).apply();
                return true;
            }
        });

        mDurationPreference = (ListPreference)findPreference(Constants.PREF_DURATION_SETTINGS);
        mDurationPreference.setSummary(mSharedPreferences.getString(Constants.PREF_DURATION_SETTINGS, ""));
        mDurationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDurationPreference.setSummary(newValue.toString());
                mSharedPreferences.edit().putString(Constants.PREF_DURATION_SETTINGS, newValue.toString()).apply();
                return true;
            }
        });
        mIntervalPreference = (ListPreference)findPreference(Constants.PREF_INTERVAL_SETTINGS);
        //mIntervalPreference.setValueIndex(getSettingIndex(Constants.PREF_INTERVAL_SETTINGS));
        mIntervalPreference.setSummary(mSharedPreferences.getString(Constants.PREF_INTERVAL_SETTINGS, ""));
        mIntervalPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIntervalPreference.setSummary(newValue.toString());
                mSharedPreferences.edit().putString(Constants.PREF_INTERVAL_SETTINGS, newValue.toString()).apply();
                return true;
            }
        });
    }

    private int getSettingIndex(String prefType){
        String storedPreference;
        if(prefType.equals(Constants.PREF_TIME_SETTINGS)){
            //get string
            storedPreference = mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS,Constants.FIRST_TIME);
            return getTimeIndex(storedPreference);
        }
        else if(prefType.equals(Constants.PREF_DURATION_SETTINGS)){
            storedPreference = mSharedPreferences.getString(Constants.PREF_DURATION_SETTINGS,Integer.toString(Constants.FIRST_DURATION));
            return getDurationIndex(storedPreference);

        }
        else if(prefType.equals(Constants.PREF_INTERVAL_SETTINGS)){
            storedPreference = mSharedPreferences.getString(Constants.PREF_INTERVAL_SETTINGS,Integer.toString(Constants.FIRST_INTERVAL));
            return getIntervalIndex(storedPreference);
        }
        return 0;
    }

    private int getTimeIndex(String preference){
        String[] timeArray = getResources().getStringArray(R.array.preference_time_settings);
        for(int i = 0; i < timeArray.length; i++){
            if(timeArray.equals(preference)) return i;
        }
        return 0;
    }

    private int getDurationIndex(String preference){
        String[] durationArray = getResources().getStringArray(R.array.preference_duration);
        for(int i = 0; i < durationArray.length; i++){
            if(durationArray.equals(preference)) return i;
        }
        return 0;
    }

    private int getIntervalIndex(String preference){
        String[] intervalArray = getResources().getStringArray(R.array.preference_interval);
        for(int i = 0; i < intervalArray.length; i++) {
            if(intervalArray.equals(preference)) return i;
        }
        return 0;
    }




}
