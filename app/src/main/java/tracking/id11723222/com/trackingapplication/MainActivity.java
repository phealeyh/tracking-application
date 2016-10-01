package tracking.id11723222.com.trackingapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import tracking.id11723222.com.trackingapplication.settings.LocationSettingsChecker;

public class MainActivity extends AppCompatActivity {
    private Context context;
    /**
     * This will inflate the layout to the activity_main layout
     *
     *@param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        PreferenceManager.setDefaultValues(this,R.xml.timer_settings,false);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    /**
     * Starts the ListTimetableActivity with a blank intent
     * once the ListTimetableActivity region is clicked
     *@param view
     */

    public void onShowTimetableClicked(View view) {
        startActivity(new Intent(this, ListTimetableActivity.class));
    }

    /**
     * Starts the ShowMapWithTimetable activity fragment with a blank intent
     * once the ShowMapTimetable region is clicked
     *@param view
     */


    public void onShowTimetableWithMap(View view) {

        if(LocationSettingsChecker.checkLocationSettings(context) && LocationSettingsChecker.checkGPSSettings(context)){
            startActivity(new Intent(this, ShowMapWithTimetable.class));
        }
        else if(!LocationSettingsChecker.checkLocationSettings(context)){
            Toast.makeText(getApplicationContext(), Constants.TURN_ON_LOCATION, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }
        else{ //change mobile data settings
            Toast.makeText(getApplicationContext(), Constants.TURN_ON_DATA, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.COM_SETTINGS,Constants.SUMMARY_ACTIVITY_USAGE));
            startActivity(intent);

        }
    }


    /**
     * Starts the TrackingActivity based on a blank intent
     * once the TrackingActivity region is clicked.
     *@param view
     */

    public void onStartTrackingClicked(View view) {
        if(LocationSettingsChecker.checkLocationSettings(context) && LocationSettingsChecker.checkGPSSettings(context)){
            startActivity(new Intent(this, TrackingActivity.class));

        }
        else if(!LocationSettingsChecker.checkLocationSettings(context)){
            Toast.makeText(getApplicationContext(), Constants.TURN_ON_LOCATION, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }
        else{ //change mobile data settings
            Toast.makeText(getApplicationContext(), Constants.TURN_ON_DATA, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.COM_SETTINGS,Constants.SUMMARY_ACTIVITY_USAGE));
            startActivity(intent);

        }

    }

    /**
     * Starts the ShowUserLocationActivity based on a blank intent
     * once the ShowUserLocationActivity is clicked
     *@param view
     */

    public void onUserLocationClicked(View view) {
        if(LocationSettingsChecker.checkLocationSettings(context) && LocationSettingsChecker.checkGPSSettings(context)){
            startActivity(new Intent(this, ShowUserLocationActivity.class));

        }
        else if(!LocationSettingsChecker.checkLocationSettings(context)){
            Toast.makeText(getApplicationContext(), Constants.TURN_ON_LOCATION, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }
        else{ //change mobile data settings
            Toast.makeText(getApplicationContext(), Constants.TURN_ON_DATA, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(Constants.COM_SETTINGS,Constants.SUMMARY_ACTIVITY_USAGE));
            startActivity(intent);

        }
    }

    /**
     * Will inflate the menu based on the menu_main menu
     *@param menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Sets the action_settings element in the menu to listen for clicks.
     * It will start the CreateTimetableActivity class used for creating new entries based
     * on a blank intent.
     *@param item
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_settings) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, CreateTimetableActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.tracking_preferences){
            startActivity(new Intent(this,TrackingPreferences.class));
            return true;
        }
        else if(id == R.id.about_page){
            startActivity(new Intent(this,AboutPageActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
