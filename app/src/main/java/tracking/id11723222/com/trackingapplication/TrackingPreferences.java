package tracking.id11723222.com.trackingapplication;

import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.prefs.Preferences;

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
        addPreferencesFromResource(R.xml.timer_settings);
        setPreferences();

    }

    private void setPreferences(){
        mTimeFormatPreference = (ListPreference)findPreference(Constants.PREF_TIME_SETTINGS);
        mTimeFormatPreference.setValueIndex(Constants.SECOND_INDEX);
        mTimeFormatPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mTimeFormatPreference.setSummary(newValue.toString());
                return true;
            }
        });

        mDurationPreference = (ListPreference)findPreference(Constants.PREF_DURATION_SETTINGS);
        mDurationPreference.setValueIndex(Constants.FIRST_DURATION_INDEX);
        mDurationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mDurationPreference.setSummary(newValue.toString());
                return true;
            }
        });
        mIntervalPreference = (ListPreference)findPreference(Constants.PREF_INTERVAL_SETTINGS);
        mIntervalPreference.setValueIndex(Constants.FIRST_INTERVAL_INDEX);
        mIntervalPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mIntervalPreference.setSummary(newValue.toString());
                return true;
            }
        });
    }


}
