package tracking.id11723222.com.trackingapplication;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.prefs.Preferences;

import tracking.id11723222.com.trackingapplication.model.PreferenceArray;

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
        String[] timeArray = getResources().getStringArray(R.array.preference_time_settings);
        setPreferences();
        mSharedPreferences.edit().apply();
    }

    private void setPreferences(){
        mTimeFormatPreference = (ListPreference)findPreference(Constants.PREF_TIME_SETTINGS);
        mTimeFormatPreference.setValueIndex(getSettingIndex(Constants.PREF_TIME_SETTINGS));
        //mTimeFormatPreference.setSummary(mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS, ""));
        mTimeFormatPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mTimeFormatPreference.setSummary(newValue.toString());
                mSharedPreferences.edit().putString(Constants.PREF_TIME_SETTINGS, newValue.toString());
                return true;
            }
        });

        mDurationPreference = (ListPreference)findPreference(Constants.PREF_DURATION_SETTINGS);
        mDurationPreference.setValueIndex(getSettingIndex(Constants.PREF_DURATION_SETTINGS));
        //mDurationPreference.setSummary(mSharedPreferences.getString(Constants.PREF_DURATION_SETTINGS, ""));
        mDurationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDurationPreference.setSummary(newValue.toString());
                mSharedPreferences.edit().putString(Constants.PREF_DURATION_SETTINGS, newValue.toString());
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
                mSharedPreferences.edit().putString(Constants.PREF_INTERVAL_SETTINGS, newValue.toString());
                return true;
            }
        });
    }

    private int getSettingIndex(String prefType){
        String storedPreference;
        if(prefType.equals(Constants.PREF_TIME_SETTINGS)){
            //get string
            storedPreference = mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS,Constants.FIRST_TIME);
            return new PreferenceArray().getTimePreference(storedPreference);
        }
        else if(prefType.equals(Constants.PREF_DURATION_SETTINGS)){
            storedPreference = mSharedPreferences.getString(Constants.PREF_DURATION_SETTINGS,Integer.toString(Constants.FIRST_DURATION));
            return new PreferenceArray().getDurationPreference(storedPreference);

        }
        else if(prefType.equals(Constants.PREF_INTERVAL_SETTINGS)){
            storedPreference = mSharedPreferences.getString(Constants.PREF_INTERVAL_SETTINGS,Integer.toString(Constants.FIRST_INTERVAL));
            return new PreferenceArray().getIntervalPreference(storedPreference);

        }
        //return the default value
        return 0;
    }


}
