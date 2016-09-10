package tracking.id11723222.com.trackingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;

import tracking.id11723222.com.trackingapplication.model.Location;
import tracking.id11723222.com.trackingapplication.services.TrackingService;

public class TrackingActivity extends AppCompatActivity {

    private Button mStartButton, mResetButton, mEmailButton;
    private Chronometer mIntervalChronomter;
    private ListView mLocationListView;
    private ArrayList<Location> locations;
    private ArrayAdapter<Location> locationAdapter;
    private IntentFilter intentFilter;
    private Intent intent;
    private long chronoTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        setButtonListeners();
        setChronometer();
        setIntervalText();

        chronoTime = 1000;
        intent = new Intent(getApplicationContext(), TrackingService.class);
        locations = new ArrayList<Location>();
        mLocationListView = (ListView) findViewById(R.id.locations_list);
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        mLocationListView.setAdapter(locationAdapter);
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_COMMAND);
        intentFilter.addAction(Constants.FINISH_COMMAND);

        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(mBroadcastReceiver,intentFilter);
        setIntervalText();
    }



    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
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
        locations.add(new Location(
                (LatLng) intent.getExtras().get(Constants.EXTRA_LOCATION),
                (Time) intent.getExtras().get(Constants.TIME)));
        locationAdapter.notifyDataSetChanged();
    }


    /**
     * Sets the chronometer to time the intervals given.
     */
    private void setChronometer(){
        mIntervalChronomter = (Chronometer) findViewById(R.id.interval_timer);
    }

    /**
     * Broadcast receiver. This handles all the message coming in.
     *
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.UPDATE_COMMAND)){
                updateLocations(intent);
                Toast.makeText(getApplicationContext(), Constants.UPDATE_COMMAND, Toast.LENGTH_SHORT).show();
                startStopWatch();
            }
            else if(intent.getAction().equals(Constants.FINISH_COMMAND)){
                Toast.makeText(getApplicationContext(), Constants.FINISH_COMMAND, Toast.LENGTH_SHORT).show();
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
