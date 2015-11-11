package tracking.id11723222.com.trackingapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;

import tracking.id11723222.com.trackingapplication.services.TrackingService;

public class TrackingActivity extends AppCompatActivity {

    private Button mStartButton, mResetButton, mEmailButton;
    private Chronometer mIntervalChronomter;
    private ListView mLocationListView, mTimeListView;
    private ArrayList<LatLng> locations;
    private ArrayList<Time> times;
    private ArrayAdapter<LatLng> locationAdapter;
    private ArrayAdapter<Time> timeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        setButtonListeners();
        //set for later
        setChronometer();
        locations = new ArrayList<LatLng>();
        times = new ArrayList<Time>();
        mTimeListView = (ListView) findViewById(R.id.locations_list);
        mLocationListView = (ListView) findViewById(R.id.times_list);
        timeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,times);
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        mTimeListView.setAdapter(timeAdapter);
        mLocationListView.setAdapter(locationAdapter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_COMMAND);
        registerReceiver(mBroadcastReceiver, intentFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
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
                startService(new Intent(getApplicationContext(),TrackingService.class));
                //start service
                Toast.makeText(getApplicationContext(),Constants.STARTED,Toast.LENGTH_LONG).show();
            }
        });
        mResetButton = (Button)findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),Constants.CLEARED,Toast.LENGTH_LONG).show();
                clearLocations();
                clearTimes();
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

    private void clearTimes(){
        times.clear();
        timeAdapter.notifyDataSetChanged();
    }

    private void clearLocations(){
        locations.clear();
        locationAdapter.notifyDataSetChanged();
    }

    private void setUpEmail(){
        Intent newIntent = new Intent(Intent.ACTION_SEND);
        newIntent.setData(Uri.parse(Constants.MAIL_TO));
        newIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.RECORDED_LOCATIONS);
        newIntent.putExtra(Intent.EXTRA_TEXT, locations.toString() + Constants.NEW_LINE + times.toString());
        newIntent.setType(Constants.EMAIL_FORMAT);
        startActivity(Intent.createChooser(newIntent, Constants.EMAIL_ON));

    }

    private void updateLocations(Intent intent){
        locations.add((LatLng) intent.getExtras().get(Constants.EXTRA_LOCATION));
        locationAdapter.notifyDataSetChanged();

    }

    private void updateTimes(Intent intent) {
        times.add((Time) intent.getExtras().get(Constants.TIME));
        timeAdapter.notifyDataSetChanged();
    }

    /**
     * Sets the chronometer to time the intervals given.
     */
    private void setChronometer(){
        mIntervalChronomter = (Chronometer) findViewById(R.id.interval_timer);
        //set it to the interval given
        mIntervalChronomter.setBase(5000L);
        mIntervalChronomter.start();
    }

    /**
     * Broadcast receiver. This handles all the message coming in.
     *
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.ENTRY_CREATED, Constants.UPDATE_COMMAND);
            updateLocations(intent);
            updateTimes(intent);
            Toast.makeText(getApplicationContext(), Constants.UPDATE_COMMAND, Toast.LENGTH_SHORT).show();
            mIntervalChronomter.setBase(5000L);
        }
    };



}
