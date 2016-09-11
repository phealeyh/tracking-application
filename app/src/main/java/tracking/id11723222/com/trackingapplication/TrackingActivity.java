package tracking.id11723222.com.trackingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;

import tracking.id11723222.com.trackingapplication.model.LocationData;
import tracking.id11723222.com.trackingapplication.model.ReminderData;
import tracking.id11723222.com.trackingapplication.model.ReminderDatabaseHelper;
import tracking.id11723222.com.trackingapplication.services.TrackingService;

public class TrackingActivity extends AppCompatActivity {

    private Button mStartButton, mResetButton, mEmailButton;
    private Chronometer mIntervalChronomter;
    //view
    private ListView mLocationListView;
    //model
    private ArrayList<LocationData> locations;
    //controller
   // private ArrayAdapter<LocationData> locationAdapter;
    private LocationAdapter locationAdapter;
    private IntentFilter intentFilter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        setButtonListeners();
        setChronometer();
        setIntervalText();

        intent = new Intent(getApplicationContext(), TrackingService.class);
        locations = new ArrayList<LocationData>();
        mLocationListView = (ListView) findViewById(R.id.locations_list);
        //locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        mLocationListView.setAdapter(locationAdapter = new LocationAdapter(getApplicationContext(),
                                        getIntent().getStringExtra(Constants.EXTRA_LOCATION)));

        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_COMMAND);
        intentFilter.addAction(Constants.FINISH_COMMAND);

        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //populate fields
        setIntervalText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //registerReceiver(mBroadcastReceiver,intentFilter);
        setIntervalText();
    }



    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(mBroadcastReceiver);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

        return super.onOptionsItemSelected(item);
    }


    /**
     * This method will set each button listener to handle it's respective action events.
     * If the start button is clicked, the TrackingService Intent is started along which will
     * track the user's coordinate at each interval.
     * The Reset values button will reset all of the values inside the lists
     * The Email button will open up a new email intent and allow the user to email the locations
     * and times recorded.
     */

    private void setButtonListeners(){
        mStartButton = (Button)findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopWatch();
                //start service
                startService(intent);
                Toast.makeText(getApplicationContext(),Constants.STARTED,Toast.LENGTH_LONG).show();
            }
        });
        mResetButton = (Button)findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),Constants.CLEARED,Toast.LENGTH_LONG).show();
                clearLocations();


            }
        });
        mEmailButton = (Button) findViewById(R.id.emailButton);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpEmail();
                Toast.makeText(getApplicationContext(),Constants.EMAIL_SENT,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setIntervalText(){
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TextView textView = (TextView)findViewById(R.id.interval_text);
        String interval = "Target Interval: " +
                mSharedPreferences.getString(Constants.PREF_INTERVAL_SETTINGS, Integer.toString(Constants.FIRST_INTERVAL)) +
                " " +
                mSharedPreferences.getString(Constants.PREF_TIME_SETTINGS,Integer.toString(Constants.FIRST_DURATION));
        textView.setText(interval);
        textView.setTextColor(Color.RED);
    }


    private void clearLocations(){
        locations.clear();
        locationAdapter.notifyDataSetChanged();
    }

    private void setUpEmail(){
        Intent newIntent = new Intent(Intent.ACTION_SEND);
        newIntent.setData(Uri.parse(Constants.MAIL_TO));
        newIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.RECORDED_LOCATIONS);
        newIntent.putExtra(Intent.EXTRA_TEXT, locations.toString());
        newIntent.setType(Constants.EMAIL_FORMAT);
        startActivity(Intent.createChooser(newIntent, Constants.EMAIL_ON));

    }

    private void updateLocations(Intent intent){
        locations.add(new LocationData(0,
                intent.getExtras().get(Constants.EXTRA_LOCATION).toString(),
                intent.getExtras().get(Constants.TIME).toString()));
        locationAdapter.notifyDataSetChanged();
    }


    /**
     * Sets the chronometer to time the intervals given.
     */
    private void setChronometer(){
        mIntervalChronomter = (Chronometer) findViewById(R.id.interval_timer);
    }

    private class LocationAdapter extends CursorAdapter {
        private String mLocation;
        /**
         * Creates a ReminderAdapter providing a list of reminder potentially filtered by location.
         *
         * @param context
         * @param location the location to filter by, or null if all reminders should be included.
         */
        public LocationAdapter(Context context, String location) {
            super(context, ReminderDatabaseHelper.get(TrackingActivity.this).getLocationData(location), 0);
            mLocation = location;
        }

        /**
         * Inflates a new view based on the given results of
         * the database cursor based on the item_reminder_data template layout
         *@param context, cursor, parent
         *@return View
         */


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recorded_times, null);
        }

        /**
         *
         * Sets the occupying data from the cursor database query results and then
         * sets it's correlating columns to the correct text views
         *@param view
         *@param context
         *@param cursor
         */


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // This is made "final" so that it can be referenced from within the anonymous OnClickListener below.
            final LocationData locationData = ((ReminderDatabaseHelper.ReminderCursor) cursor).getLocationData();

            TextView locationTV = (TextView) view.findViewById(R.id.recorded_location);
            TextView timeTV = (TextView) view.findViewById(R.id.elapsed_time_text);
            locationTV.setText(locationData.getmLocation());

            timeTV.setText(locationData.getmTime());
        }


    }


    /**
     * Broadcast receiver. This handles all the message coming in.
     *
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.UPDATE_COMMAND)){
                locationAdapter.notifyDataSetChanged();
                //updateLocations(intent);
                Toast.makeText(getApplicationContext(), Constants.UPDATE_COMMAND, Toast.LENGTH_SHORT).show();
                Log.e("Received: ","User Location Received");

                startStopWatch();
            }
            else if(intent.getAction().equals(Constants.FINISH_COMMAND)){
                Toast.makeText(getApplicationContext(), Constants.FINISH_COMMAND, Toast.LENGTH_SHORT).show();
                Log.e("Received: ","User Location Finished Updating");
                setChronometerToZero();
            }
        }
    };



    private void startStopWatch(){
        mIntervalChronomter.setBase(SystemClock.elapsedRealtime());
        mIntervalChronomter.start();
    }

    private void setChronometerToZero(){
        mIntervalChronomter.setBase(SystemClock.elapsedRealtime());
        mIntervalChronomter.stop();
    }




}
