package tracking.id11723222.com.trackingapplication;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;

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
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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



}
